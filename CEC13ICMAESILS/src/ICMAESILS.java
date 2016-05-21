import java.util.*;

public class ICMAESILS {
    public boolean improve = true;
    public int relaynum;
    public double[] mtsls1solfitness;
    public double[] icmaessolfitness;
    public double[] icmaes_train_reward;
    public double[] mtsls1_train_reward;
    public double f;
    public double[] x;
    public double [][] matrix;
    public double initevalscout = 0;
    public double initbestvalue = Configuration.FLT_MAX;
    public double[] initkeepbestx;


    public ICMAESILS() {
        x = new double[Configuration.DIM];
    }

    public void execute() {
        int dim = Configuration.DIM;
        mtsls1solfitness = new double[dim + 2];
        icmaessolfitness = new double[dim + 2];
        icmaes_train_reward = new double[dim + 2];
        mtsls1_train_reward = new double[dim + 2];

        initialization(); // initialization
        for (int i = 0; i < dim; i++) {
            mtsls1solfitness[i] = initkeepbestx[i];
        }

        // competition phase

        for (relaynum = 0; relaynum < 1; relaynum++) {
            icmaes(10000 * dim * Configuration.getlearn_perbudget, mtsls1solfitness);
        }
        icmaes_train_reward = Util.copyArray(icmaessolfitness);
        for (int i = 0; i < dim; i++) {
            icmaessolfitness[i] = mtsls1solfitness[i];
        }
        ILSmtsls1(10000 * dim * Configuration.getlearn_perbudget, Configuration.getmtsls1per_ratedim * dim, icmaessolfitness);
        mtsls1_train_reward = Util.copyArray(mtsls1solfitness);

        // deployment phase
        if (icmaes_train_reward[dim] > mtsls1_train_reward[dim]) {
            ILSmtsls1(10000 * dim * (1 - Configuration.getlearn_perbudget * 2), Configuration.getmtsls1per_ratedim * dim, mtsls1_train_reward);
        } else {
            icmaes_train_reward[dim + 1] = mtsls1_train_reward[dim + 1];
            icmaes(10000 * dim * (1 - Configuration.getlearn_perbudget * 2), icmaes_train_reward);
        }


    }

    public final double[] icmaes(double maxfevalsforicmaes, double[] mtsls1feedback) {

        // Cmaes cmaes ;
        //cmaes_t evo = new cmaes_t();
        double[][] pop;
        double[] fitvals;
        double fbestever = 0;
        double[] xbestever = null;
        double fmean;
        int i;
        int irun;
        int lambda = 0;
        int countevals = 0;
        int maxevals;
        String stop;
        double long_fitness;
        int dim = Configuration.DIM;
        double[] xinit = new double[dim];
        double[] stddev = new double[dim];
        maxevals = (int) maxfevalsforicmaes;
        int countmaxevalsforicmaes;
        int first = 1;
        double[] icmaeskeepbestxk = new double[dim];
        double liaobestvalue;
        double liaoevalscount;




        if (relaynum == 0) {
            liaobestvalue = initbestvalue;
            liaoevalscount = initevalscout;
            countmaxevalsforicmaes = maxevals;
        } else {
            liaobestvalue = mtsls1feedback[dim];
            liaoevalscount = mtsls1feedback[dim + 1];
            countmaxevalsforicmaes = (int) (maxevals + mtsls1feedback[dim + 1]);
        }

        double liaofirstforxinit;
        for (irun = 0; liaoevalscount < countmaxevalsforicmaes; ++irun) {
            if (irun == 0) {
                for (int j = 0; j < dim; j++) {
                    xinit[j] = mtsls1feedback[j];
                    icmaeskeepbestxk[j] = xinit[j];
                }
            } else {
                for (int j = 0; j < dim; j++) {
                    xinit[j] = (Bounds.getUpperBound(Configuration.F) - Bounds.getLowerBound(Configuration.F)) * Configuration.rand.getFloat() + Bounds.getLowerBound(Configuration.F);
                }
            }
            for (int j = 0; j < dim; j++) {
                stddev[j] = Configuration.tunec * ((Bounds.getUpperBound(Configuration.F)) - Bounds.getLowerBound(Configuration.F));
            }
            for (int n = 0; n < dim; n++) {
                x[n] = xinit[n];
            }
            //
            //com.benchmark.cec.cec05.test_func(x, f, dim,1,Configuration.getProblemID());
            f = Configuration.benchmark.f(x);
            liaofirstforxinit = f;
            liaoevalscount++;
            if (liaobestvalue - liaofirstforxinit > 1e-30) {
                if (liaoevalscount <= countmaxevalsforicmaes) {
                    liaobestvalue = liaofirstforxinit;
                   /* if (liaobestvalue <= 1e-8) {
                        System.out.printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                    } else {
                        System.out.printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                    }*/
                    for (int j = 0; j < dim; j++) {
                        icmaeskeepbestxk[j] = xinit[j];
                    }
                }
            }

            CMAEvolutionStrategy cma = new CMAEvolutionStrategy();
            cma.setDimension(dim);
            cma.setInitialX(xinit);
            cma.setSeed(Configuration.seed);
            cma.setInitialStandardDeviation(stddev[0]); // also a mandatory setting
            cma.options.stopFitness = Bounds.Ter_Err;       // optional setting
            cma.options.stopMaxFunEvals = 10000 * dim;
            cma.setPopulationSize(lambda);
            //cma.setBounds(com.benchmark.cec.cec05.Bounds.getUpperBound(Configuration.F), com.benchmark.cec.cec05.Bounds.getLowerBound(Configuration.F));
            double [] x_in_bounds = new double[Configuration.DIM];

            Boundary_transformation.cmaes_boundary_transformation_init(Bounds.getLowerBound(Configuration.F), Bounds.getUpperBound(Configuration.F), 1);
            //String filename = "initials.par";
            //fitvals = cmaes_init(evo, Configuration.getProblemDimension(), xinit, stddev, 0, lambda, filename);
            fitvals = cma.init();
            lambda = cma.getPopulationSize();
            double bestValue = 999999999;

            while ((cma.getCountEval() < cma.options.stopMaxFunEvals) && (bestValue - bias.getBias(Configuration.F) > cma.options.stopFitness)){
            //while ((stop = cmaes_TestForTermination(evo)) == 0) {
                //pop = cmaes_SamplePopulation(evo);
                pop = cma.samplePopulation();
                for (i = 0; i < pop.length; ++i) {
                    Boundary_transformation.cmaes_boundary_transformation(pop[i],x_in_bounds, Configuration.DIM );

                    //pop[i] = inbound(com.benchmark.cec.cec05.Bounds.getLowerBound(Configuration.F), com.benchmark.cec.cec05.Bounds.getUpperBound(Configuration.F), pop[i], dim);
                    while (!inbound(Bounds.getLowerBound(Configuration.F), Bounds.getUpperBound(Configuration.F),x_in_bounds, Configuration.DIM)) {     //   not located on (or very close to) the domain boundary,
                        pop[i] = cma.resampleSingle(i);
                        Boundary_transformation.cmaes_boundary_transformation(pop[i], x_in_bounds, Configuration.DIM);
                    }
                    /*for (int n = 0; n < dim; n++) {
                        x[n] = pop[i][n];
                    }*/
                    //com.benchmark.cec.cec05.test_func(x, f, dim, 1, Configuration.getProblemID());
                    f = Configuration.benchmark.f(x_in_bounds);
                    long_fitness = f;
                    liaoevalscount++;

                    if (long_fitness <= Configuration.FLT_MAX) {
                        fitvals[i] = long_fitness;
                    } else {
                        fitvals[i] = Configuration.FLT_MAX;
                    }

                    if (liaobestvalue - fitvals[i] > 1e-30) {
                        if (liaoevalscount <= countmaxevalsforicmaes) {
                            liaobestvalue = fitvals[i];
                            /*if (liaobestvalue <= 1e-8) {
                                System.out.printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                            } else {
                                System.out.printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                            }*/
                            for (int j = 0; j < dim; j++) {
                                icmaeskeepbestxk[j] = pop[i][j];
                            }
                        }
                    }

                }
                cma.updateDistribution(fitvals);
                bestValue = cma.getBestFunctionValue();
                Configuration.records.newRecord(bestValue - bias.getBias(Configuration.F), (int)cma.getCountEval());
              // System.out.println("Value: " + bestValue + " at: " + cma.getCountEval() );
                //cmaes_UpdateDistribution(evo, fitvals);
                //fflush(stdout);
            }

            lambda = (int)Configuration.tuned * cma.getPopulationSize();
            if (lambda > 200) {
                lambda = 200;
            }
           // cmaes_exit(evo);
        }
        for (int j = 0; j < dim; j++) {
            icmaessolfitness[j] = icmaeskeepbestxk[j];
        }
        icmaessolfitness[dim] = liaobestvalue;
        icmaessolfitness[dim + 1] = countmaxevalsforicmaes;

        Configuration.records.newRecord(liaobestvalue - bias.getBias(Configuration.F));

        return Util.copyArray(icmaessolfitness);
    }

    public final double[] ILSmtsls1(double maxmtsls1fevals, double maxiter, double[] icmaesfeedback) {
        int dim = Configuration.DIM;
        //cmaes_t evols = new cmaes_t();
        double liaobestmtsls1value;
        double liaoevalsmtsls1count;

        liaobestmtsls1value = icmaesfeedback[dim];
        liaoevalsmtsls1count = icmaesfeedback[dim + 1];
        maxmtsls1fevals = (int) (maxmtsls1fevals + icmaesfeedback[dim + 1]);

        double lsmin = Bounds.getLowerBound(Configuration.F);
        double lsmax = Bounds.getUpperBound(Configuration.F);
        double[] xk = new double[dim];
        double[] mtsls1keepbestxk = new double[dim];

        for (int i = 0; i < dim; i++) {

            xk[i] = icmaesfeedback[i];
            mtsls1keepbestxk[i] = xk[i];
        }
        double liaofirstforxinitmtsls1;

        for (int n = 0; n < dim; n++) {

            x[n] = xk[n];
        }
        //com.benchmark.cec.cec05.test_func(x, f, dim, 1, Configuration.getProblemID());
        f = Configuration.benchmark.f(x);
        liaofirstforxinitmtsls1 = f;
        liaoevalsmtsls1count++;
        if (liaobestmtsls1value - liaofirstforxinitmtsls1 > 1e-30) {
            liaobestmtsls1value = liaofirstforxinitmtsls1;
           // System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaofirstforxinitmtsls1);
        }

        double s;
        s = Configuration.getmtsls1_initstep_rate * (lsmax - lsmin);

        do {

            int iterun = 0;
            List<Double> conbef = new ArrayList<>();
            boolean convergence = false;
            double acceptbefore = liaobestmtsls1value;
            do {
                if (liaoevalsmtsls1count < maxmtsls1fevals) {
                    if (improve == false) {

                        s = s / (double) (2);

                        if (s < 1e-20) {

                            conbef.add(liaobestmtsls1value);
                            if (conbef.size() >= 2) {
                                if (Math.abs(conbef.get(conbef.size() - 1) - conbef.get(conbef.size() - 1)) < 1e-20)
                                {
                                    convergence = true;
                                }
                            }
                            s = ((0.6 - 0.3) * Configuration.rand.getFloat() + 0.3) * (lsmax - lsmin);
                        }
                    }
                    improve = false;
                    for (int i = 0; i < dim; i++) {
                        double before1;
                        for (int n = 0; n < dim; n++) {
                            x[n] = xk[n];
                        }
                        //com.benchmark.cec.cec05.test_func(x, f, dim, 1, Configuration.getProblemID());
                        f = Configuration.benchmark.f(x);
                        before1 = f + addPenalty(lsmin, lsmax, xk, dim, liaoevalsmtsls1count);

                        liaoevalsmtsls1count++;
                        if (liaobestmtsls1value - before1 > 1e-30) {
                            if (liaoevalsmtsls1count <= maxmtsls1fevals) {
                                liaobestmtsls1value = before1;
                               /* if (liaobestmtsls1value <= 1e-8) {
                                    System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                } else {
                                    System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                }*/

                                for (int j = 0; j < dim; j++) {
                                    mtsls1keepbestxk[j] = xk[j];
                                }
                            }
                        }

                        xk[i] = xk[i] - s;
                        double after1;
                        for (int n = 0; n < dim; n++) {
                            x[n] = xk[n];
                        }
                        //com.benchmark.cec.cec05.test_func(x, f, dim, 1, Configuration.getProblemID());
                        f = Configuration.benchmark.f(x);
                        after1 = f + addPenalty(lsmin, lsmax, xk, dim, liaoevalsmtsls1count);

                        liaoevalsmtsls1count++;
                        if (liaobestmtsls1value - after1 > 1e-30) {
                            if (liaoevalsmtsls1count <= maxmtsls1fevals) {
                                liaobestmtsls1value = after1;

                                /*if (liaobestmtsls1value <= 1e-8) {
                                    System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                } else {
                                    System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                }*/
                                for (int j = 0; j < dim; j++) {
                                    mtsls1keepbestxk[j] = xk[j];
                                }
                            }
                        }

                        if (Math.abs(after1 - before1) <= 1e-20) {
                            xk[i] = xk[i] + s;

                        } else {
                            if (after1 - before1 > 1e-20) {
                                xk[i] = xk[i] + s;
                                double before2 = before1;
                                xk[i] = xk[i] + 0.5 * s;
                                double after2;

                                for (int n = 0; n < dim; n++) {

                                    x[n] = xk[n];
                                }
                                //com.benchmark.cec.cec05.test_func(x, f, dim, 1, Configuration.getProblemID());
                                f = Configuration.benchmark.f(x);
                                after2 = f + addPenalty(lsmin, lsmax, xk, dim, liaoevalsmtsls1count);

                                liaoevalsmtsls1count++;
                                if (liaobestmtsls1value - after2 > 1e-30) {
                                    if (liaoevalsmtsls1count <= maxmtsls1fevals) {
                                        liaobestmtsls1value = after2;
                                        /*if (liaobestmtsls1value <= 1e-8) {
                                            System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                        } else {
                                            System.out.printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                        }*/
                                        for (int j = 0; j < dim; j++) {
                                            mtsls1keepbestxk[j] = xk[j];
                                        }
                                    }
                                }
                                if (after2 >= before2 || ((0 < (before2 - after2)) && ((before2 - after2) <= 1e-20))) {
                                    xk[i] = xk[i] - 0.5 * s;

                                } else {

                                    improve = true;
                                }
                            } else {

                                improve = true;
                            }

                        }
                    }
                    iterun++;
                } else {
                    break;
                }
            } while (iterun < maxiter && convergence == false);
            if (liaoevalsmtsls1count < maxmtsls1fevals) {
                if (acceptbefore - liaobestmtsls1value < 1e-20) {
                    for (int i = 0; i < dim; i++) {
                        double srand = (lsmax - lsmin) * Configuration.rand.getFloat() + lsmin;
                        xk[i] = srand + ((1 - Configuration.getmtsls1_iterbias_choice) * Configuration.rand.getFloat() + Configuration.getmtsls1_iterbias_choice) * (mtsls1keepbestxk[i] - srand);
                    }
                }

                s = Configuration.getmtsls1_initstep_rate * (lsmax - lsmin);

            } else {
                break;
            }

        } while (liaoevalsmtsls1count < maxmtsls1fevals);
        for (int i = 0; i < dim; i++) {
            mtsls1solfitness[i] =  mtsls1keepbestxk[i];
        }
        mtsls1solfitness[dim] = liaobestmtsls1value;
        mtsls1solfitness[dim + 1] = maxmtsls1fevals;
        return mtsls1solfitness;
    }

    public final double addPenalty(double minr, double maxr, double[] psol, int dim, double getfunevals) {
        int i;
        double penalty = 0.0;
        for (i = 0; i < dim; i++) {
            if (psol[i] < minr) {

                penalty += Math.abs(psol[i] - minr) * Math.abs(psol[i] - minr);
            }
            if (psol[i] > maxr) {

                penalty += Math.abs(psol[i] - maxr) * Math.abs(psol[i] - maxr);
            }
        }

        return penalty * getfunevals;
    }

    public boolean inbound(double min, double max, double[] ind, int num) {
        boolean check = true;
        int i;
        for (i = 0; i < num; i++) {
            if (ind[i] > max) {
                //ind[i] = max;
                return false;
            } else if (ind[i] < min) {
              //  ind[i] = min;
                return false;
            }
        }
        return check;
    }


   // private printcosttime_timeval tp = new printcosttime_timeval();

    /*public final double printcosttime() {
        double etime;
        gettimeofday(tp, null);
        etime = (double) tp.tv_sec + (double) tp.tv_usec / 1000000.0;
        return etime - Configuration.getStartTime();
    }*/

    public final void initialization() {
        initkeepbestx = new double[Configuration.DIM];
        int intipopsize = 1 * Configuration.DIM;
        matrix = new double[intipopsize][Configuration.DIM + 1];
        for (int i = 0; i < intipopsize; i++) {
            for (int a = 0; a < Configuration.DIM; a++) {
                matrix[i][a]= minmaxrand();
                x[a] = matrix[i][a];
            }
           // com.benchmark.cec.cec05.test_func(x, f, Configuration.getProblemDimension(), 1, Configuration.getProblemID());
            f = Configuration.benchmark.f(x);
            initevalscout++;
            matrix[i][Configuration.DIM] = f;
            if (initbestvalue - matrix[i][Configuration.DIM]>1e-30)
            {
                initbestvalue = matrix[i][Configuration.DIM];
                //System.out.printf("%.0f %le\n", initevalscout, initbestvalue);
                for (int j = 0; j < Configuration.DIM; j++) {
                    initkeepbestx[j] = matrix[i][j];
                }
            }
        }
    }

    public final double minmaxrand() {
        return (double) (Bounds.getLowerBound(Configuration.F) + (Bounds.getUpperBound(Configuration.F) - Bounds.getLowerBound(Configuration.F)) * Configuration.rand.getFloat());
    }


}