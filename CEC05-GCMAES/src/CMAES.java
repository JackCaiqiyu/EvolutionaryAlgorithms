/** The very well-known Rosenbrock objective function to be minimized.
 */
class fitfun implements IObjectiveFunction { // meaning implements methods valueOf and isFeasible
	public int DIM;
	public int FUN;


	public double valueOf (double[] x) {
		benchmark benchmark = new benchmark();
		test_func test_func = benchmark.testFunctionFactory(FUN, DIM);
		return test_func.f(x);
	}

	public boolean isFeasible(double[] x) {return true; } // entire R^n is feasible
}

 /*
 * @see CMAEvolutionStrategy
 * 
 * @author Nikolaus Hansen, released into public domain. 
 */
public class CMAES {
	public static int last_eval;
	public static double last_fitness;
	public static int max_values;
	 public static Records records;

	public static void main(String[] args) {
		int DIM = 10;
		int F = 8;
		double stop_fitness = 10e-8;
		double best_fmin = Util.inf;
		records = new Records();
		for(int run=0; run<25; run++) {
			int pop_size = 4 + (int) Math.floor(3 * Math.log(DIM));
			double fmin = Util.inf;
			max_values = 10000 * DIM;
			while ((max_values > pop_size) && (fmin - bias.getBias(F) > stop_fitness)) {
				CMAES(DIM, F, pop_size);
				max_values -= last_eval;
				fmin = last_fitness;
				pop_size *= 2;
				if (fmin < best_fmin) {
					best_fmin = fmin;
				}
			}
			records.newRecord(fmin - bias.getBias(F), last_eval, max_values);
		}
		records.write(DIM, F, "G-CMAES", false);
		System.out.println("BEST FITNESS: " + (best_fmin - bias.getBias(F)));

	}


	public static void CMAES(int DIM, int F, int pop_size){
		fitfun fitfun2 = new fitfun();
		fitfun2.DIM = DIM;
		fitfun2.FUN =F;
		IObjectiveFunction fitfun = fitfun2;
		Rand rand = new Rand(seeds.getSeed(F));
		// new a CMA-ES and set some initial values
		CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
		//cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
		//cma.setPopulation(10);
		cma.parameters.setPopulationSize(pop_size);
		cma.setDimension(DIM); // overwrite some loaded properties
		double [] xstart = new double[DIM];
		for(int i=0; i<DIM; i++)
			xstart[i] = Bounds.getLowerBound(F)*rand.getFloat()+(Bounds.getUpperBound(F)-Bounds.getLowerBound(F))* rand.getFloat();
		cma.setInitialX(xstart); // in each dimension, also setTypicalX can be used
		cma.setInitialStandardDeviation(0.5*(Bounds.getUpperBound(F) - Bounds.getLowerBound(F))); // also a mandatory setting

		//cma.options.stopFitness = 10e-8;       // optional setting
		cma.options.stopMaxFunEvals = max_values;
		//cma.options.stopTolFun = 1e-12;
		cma.options.stopFitness = bias.getBias(F) + 10e-8;

		// initialize cma and get fitness array to fill in later
		double[] fitness = cma.init();  // new double[cma.parameters.getPopulationSize()];

		// initial output to files
		//cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files
		// iteration loop
		while(cma.stopConditions.getNumber() == 0){ //&& (bestValue - bias.getBias(F) > cma.options.stopFitness) && cma.getCountEval() < max_values) {

			// --- core iteration step ---
			double[][] pop = cma.samplePopulation(); // get a new population of solutions
			for(int i = 0; i < pop.length; ++i) {    // for each candidate solution i
				// a simple way to handle constraints that define a convex feasible domain
				// (like box constraints, i.e. variable boundaries) via "blind re-sampling"
				// assumes that the feasible domain is convex, the optimum is
				while (!fitfun.isFeasible(pop[i]))     //   not located on (or very close to) the domain boundary,
					pop[i] = cma.resampleSingle(i);    //   initialX is feasible and initialStandardDeviations are
				//   sufficiently small to prevent quasi-infinite looping here
				// compute fitness/objective value
				fitness[i] = fitfun.valueOf(pop[i]);  // fitfun.valueOf() is to be minimized
			}
			cma.updateDistribution(fitness);         // pass fitness array to update search distribution
			//bestValue = cma.getBestFunctionValue();
			System.out.println("VALUE: " + (cma.bestever_fit - bias.getBias(F)) + " at: " + cma.getCountEval());
			records.newRecord((cma.bestever_fit - bias.getBias(F)), (int)cma.getCountEval());
			// --- end core iteration step ---

			// output to files and console
			/*cma.writeToDefaultFiles();
			int outmod = 150;
			if (cma.getCountIter() % (15*outmod) == 1)
				cma.printlnAnnotation(); // might write file as well
			if (cma.getCountIter() % outmod == 1)
				cma.println();*/
		}
		// evaluate mean value as it is the best estimator for the optimum
		cma.setFitnessOfMeanX(fitfun.valueOf(cma.getMeanX())); // updates the best ever solution

		// final output
		cma.writeToDefaultFiles(1);
		cma.println();
		cma.println("Terminated due to");
		for (String s : cma.stopConditions.getMessages())
			cma.println("  " + s);
		cma.println("best function value " + cma.getBestFunctionValue()
				+ " at evaluation " + cma.getBestEvaluationNumber());

		System.out.println(Bounds.getLowerBound(F)+ ", " +Bounds.getUpperBound(F));
		System.out.println(Util.arrayToList(cma.bestever_x));
		System.out.println(fitfun.valueOf(cma.bestever_x) - bias.getBias(F));
		// we might return cma.getBestSolution() or cma.getBestX()

		last_eval = (int)cma.getCountEval();
		last_fitness = fitfun.valueOf(cma.bestever_x);
	}

} // class
