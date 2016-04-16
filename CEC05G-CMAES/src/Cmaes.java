/**
 * Created by framg on 16/04/2016.
 */
public class Cmaes {
    private int F;
    private int DIM;
    private long seed;
    private int pop_size;
    private Records records;

    public Cmaes(int pop_size) {
        this.pop_size = pop_size;
        this.seed = Configuration.seed;
        this.DIM = Configuration.DIM;
        this.F = Configuration.F;
        this.records = Configuration.records;
    }

    public double execute(){
        IObjectiveFunction fitfun = new FunctionCEC05(F, DIM);

        // new a CMA-ES and set some initial values
        CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
        cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
        cma.setDimension(DIM); // overwrite some loaded properties
        cma.setInitialX(0.05); // in each dimension, also setTypicalX can be used
        cma.setSeed(seed);
        cma.setInitialStandardDeviation(0.5*(Bounds.getUpperBound(F) - Bounds.getLowerBound(F))); // also a mandatory setting
        cma.options.stopFitness = Bounds.Ter_Err;       // optional setting
        cma.options.stopMaxFunEvals = 10000 * DIM;
        cma.setPopulationSize(pop_size);
        cma.setBounds(Bounds.getUpperBound(F), Bounds.getLowerBound(F));
        // initialize cma and get fitness array to fill in later
        double[] fitness = cma.init();  // new double[cma.parameters.getPopulationSize()];

        // initial output to files
       // cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files

        double bestValue = 999999999;
        // iteration loop
        while ((cma.getCountEval() < cma.options.stopMaxFunEvals) && (bestValue - bias.getBias(F) > cma.options.stopFitness)){

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
                fitness[i] = fitfun.valueOf(pop[i]); // fitfun.valueOf() is to be minimized
                System.out.println("VALUE: " + fitness[i] + " at: " + cma.getCountEval());
            }
            cma.updateDistribution(fitness);         // pass fitness array to update search distribution
            bestValue = cma.getBestFunctionValue();
            records.newRecord(cma.getBestFunctionValue() - bias.getBias(F), (int)cma.getCountEval());
            // --- end core iteration step ---

            // output to files and console
          /*  cma.writeToDefaultFiles();
            int outmod = 150;
            if (cma.getCountIter() % (15*outmod) == 1)
                cma.printlnAnnotation(); // might write file as well
            if (cma.getCountIter() % outmod == 1)
                cma.println();*/
        }
        // evaluate mean value as it is the best estimator for the optimum
        cma.setFitnessOfMeanX(fitfun.valueOf(cma.getMeanX())); // updates the best ever solution
        records.newRecord(cma.getBestFunctionValue() - bias.getBias(F));

        // final output
       /* cma.writeToDefaultFiles(1);
        cma.println();
        cma.println("Terminated due to");
        for (String s : cma.stopConditions.getMessages())
            cma.println("  " + s);
        cma.println("best function value " + cma.getBestFunctionValue()
                + " at evaluation " + cma.getBestEvaluationNumber());
*/
        // we might return cma.getBestSolution() or cma.getBestX()
        return cma.getBestFunctionValue();
    }


}
