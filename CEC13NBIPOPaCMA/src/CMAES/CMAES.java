package CMAES;

import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

/** The very well-known Rosenbrock objective function to be minimized.
 */
/*class fitfun implements IObjectiveFunction { // meaning implements methods valueOf and isFeasible
	public int DIM;
	public int FUN;


	public double valueOf (double[] x) {
		benchmark benchmark = new benchmark();
		test_func test_func = benchmark.testFunctionFactory(FUN, DIM);
		return test_func.f(x);
	}

	public boolean isFeasible(double[] x) {return true; } // entire R^n is feasible
}*/

 /*
 * @see CMAEvolutionStrategy
 * 
 * @author Nikolaus Hansen, released into public domain. 
 */
public class CMAES {
	public int irun;
	public int lambda;
	public double []  insigma;
	public  int stopMaxIter;
	public  boolean stop;
	public  double fmin;
	public  int counteval;
	public  double [] xmin;
	public int IncPopSize;
	public double [] xstart;
	public double sigma;
	public Options opts;

	private int DIM;
	private AllBenchmarks benchmarks;
	public int pop_size;
	private CMAEvolutionStrategy cma;
	double[] fitnessCmaes;
	public int MaxIter;


	public CMAES(int DIM, AllBenchmarks benchmarks) {
		this.DIM = DIM;
		this.benchmarks = benchmarks;
		this.xmin = null;
		this.fmin = Double.POSITIVE_INFINITY;
	}

	public void initialize(double [] xstart, double sigma, Options opts){
		this.opts = opts;
		this.sigma = sigma;
		this.xstart = xstart.clone();
		this.pop_size = (4 + (int)Math.floor(3*Math.log(DIM)));
		this.IncPopSize = 2;
		this.MaxIter = (int) Math.round( 1e3*Math.pow(DIM+5,2)/Math.sqrt(pop_size));
	}

	public void initializeRun(){
		this.cma = new CMAEvolutionStrategy();
		cma.parameters.setPopulationSize(pop_size);
		cma.parameters.setLambda(lambda);
		cma.setDimension(DIM); // overwrite some loaded properties
		cma.setInitialX(xstart); // in each dimension, also setTypicalX can be used

		if(sigma > 0){
			cma.setInitialStandardDeviation(sigma); // also a mandatory setting
		}
		if(opts.max_iter) {
			this.stopMaxIter = (int) Math.round (100 + 50 * Math.pow(DIM+3, 2) / Math.sqrt(pop_size));
			cma.options.stopMaxIter = this.stopMaxIter;
		}else{
			cma.options.stopMaxIter = MaxIter;
		}
		cma.options.stopMaxFunEvals = opts.max_evals;
		cma.options.stopFitness = benchmarks.bias() + 10e-8;

		if (opts.TolFun) {
			cma.options.stopTolFun = 1e-12;
		}
		if(opts.TolHistFun) {
			cma.options.stopTolFunHist = 1e-12;
		}
		if(opts.TolX) {
			cma.options.stopTolX = 1e-12 * sigma;
		}
		if(opts.TolUpX) {
			cma.options.stopTolUpXFactor = Double.POSITIVE_INFINITY;
		}




		insigma = new double[DIM];
		for (int i = 0; i < DIM; i++) {
			insigma[i] = sigma;
		}


		fitnessCmaes = new double[pop_size];
		//cma.parameters.setLambda(lambda);
		//pop_size = lambda;
		//cma.sigma = sigma;
		fitnessCmaes = cma.init();
	}

	public int iteration(){
		int evals = 0;
		while(cma.stopConditions.getNumber() == 0){ //&& (bestValue - bias.getBias(F) > cma.options.stopFitness) && cma.getCountEval() < max_values) {

			// --- core iteration step ---
			double[][] pop = cma.samplePopulation(); // get a new population of solutions
			for(int i = 0; i < pop.length; ++i) {    // for each candidate solution i
				fitnessCmaes[i] = benchmarks.f(pop[i]); //fitfun.valueOf(pop[i]);  // fitfun.valueOf() is to be minimized
				evals++;
				while(Double.isNaN(fitnessCmaes[i])){
					pop[i] = cma.resampleSingle(i);
					fitnessCmaes[i] = benchmarks.f(pop[i]);
					evals++;
				}
		//		System.out.println(fitnessCmaes[i]);
			}



			cma.updateDistribution(fitnessCmaes);         // pass fitness array to update search distribution
		}
		cma.setFitnessOfMeanX(benchmarks.f(cma.getMeanX())); // updates the best ever solution

		if(fmin > cma.bestever_fit) {
			xmin = cma.bestever_x.clone();
			fmin = cma.bestever_fit;
		}
		return evals;
	}

	public boolean finish(){
		if (cma.bestever_fit <= cma.options.stopFitness){
			return true;
		}

		if(cma.counteval >= cma.options.stopMaxFunEvals){
			return true;
		}

		counteval = (int)cma.counteval;

		return false;
	}


} // class
