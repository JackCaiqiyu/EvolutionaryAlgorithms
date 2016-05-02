package CMAESold;

/**
 * Created by framg on 16/04/2016.
 */
public class Cmaes {
    private int F;
    private int DIM;
    private long seed;
    private double [] xstart;
    private double sigma0;


    private int pop_size;
    public static double cs;
    public static double ccum;
    public static double ccovmu;
    public static double ccov;

    public Cmaes(double [] xstart, double sigma0, double ccov1, double ccovmu, double ccum) {
        this.F = Configuration.F;
        this.DIM = Configuration.DIM;
        this.seed = Configuration.seed;

        this.xstart = Util.copyArray(xstart);
        this.sigma0 = sigma0;
        this.ccov = ccov1;
        this.ccum = ccum;
        this.ccovmu = ccovmu;

    }

    public double execute(){
      //  IObjectiveFunction fitfun = new FunctionCEC05(F, DIM);

        // new a CMA-ES and set some initial values
        CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
        cma.readProperties(); // read options, see file CMAEvolutionStrategy.properties
        cma.setDimension(DIM); // overwrite some loaded properties
        cma.setInitialX(xstart); // in each dimension, also setTypicalX can be used
        cma.setSeed(seed);
        cma.setInitialStandardDeviation(sigma0); // also a mandatory setting
        cma.options.stopFitness = Configuration.Ter_err;       // optional setting
        cma.options.stopMaxFunEvals = Configuration.MaxIter;
        cma.setPopulationSize(pop_size);
        cma.getPar
      //  cma.setBounds(Bounds.getUpperBound(F), Bounds.getLowerBound(F));
        // initialize cma and get fitness array to fill in later

        Boundary_transformation.cmaes_boundary_transformation_init(Bounds.getLowerBound(Configuration.F), Bounds.getUpperBound(Configuration.F), 1);
        double [] x_in_bounds = new double[Configuration.DIM];



        double[] fitness = cma.init();  // new double[cma.parameters.getPopulationSize()];
        // initial output to files
       // cma.writeToDefaultFilesHeaders(0); // 0 == overwrites old files

        double bestValue = 999999999;
        // iteration loop
        while ((cma.getCountEval() < cma.options.stopMaxFunEvals) && (bestValue - bias.getBias(F) > cma.options.stopFitness)){

            // --- core iteration step ---
            double[][] pop = cma.samplePopulation(); // get a new population of solutions
            for(int i = 0; i < pop.length; ++i) {    // for each candidate solution i
                Boundary_transformation.cmaes_boundary_transformation(pop[i], x_in_bounds, Configuration.DIM);
                // a simple way to handle constraints that define a convex feasible domain
                // (like box constraints, i.e. variable boundaries) via "blind re-sampling"
                // assumes that the feasible domain is convex, the optimum is
                while (!inbound(x_in_bounds)) {    //   not located on (or very close to) the domain boundary,
                    pop[i] = cma.resampleSingle(i);    //   initialX is feasible and initialStandardDeviations are
                    Boundary_transformation.cmaes_boundary_transformation(pop[i], x_in_bounds, Configuration.DIM);
                }
                //   sufficiently small to prevent quasi-infinite looping here
                // compute fitness/objective value
                fitness[i] = Configuration.benchmark.f(x_in_bounds);  //fitfun.valueOf(pop[i]); // fitfun.valueOf() is to be minimized

            }
            cma.updateDistribution(fitness);         // pass fitness array to update search distribution
            bestValue = cma.getBestFunctionValue();
        }
        // evaluate mean value as it is the best estimator for the optimum
        cma.setFitnessOfMeanX(Configuration.benchmark.f(cma.getMeanX())); // updates the best ever solution



        return cma.getBestFunctionValue();
    }

    public boolean inbound(double[] ind) {
        int i;
        for (i = 0; i < Configuration.DIM; i++) {
            if (ind[i] > Bounds.getUpperBound(Configuration.F)) {
                //ind[i] = max;
                return false;
            } else if (ind[i] < Bounds.getLowerBound(Configuration.F)) {
                //  ind[i] = min;
                return false;
            }
        }
        return true;
    }

}
