import CMAES.CMAES;
import CMAES.Options;
import com.sun.org.apache.bcel.internal.generic.BIPUSH;

import java.util.ArrayList;

public class Xcmaes {
    //  State cur_state;
    State initial_state;
    Algo algo;
    Algo algo_without_new_points;
    OptSVM optSVM;
    Hyper_opts hyper_opts;
    Model model;
    Budget budget;
    Model RSVM;
    int lambda0;
    int nrunswithsmallpopsize;
    int popsize;
    int iSTEP;
    int WeDontBelieveInModel_STEP;
    boolean WeDontBelieveInModel;
    boolean isIPOPRun;
    int nrestarts;
    int lambda;
    int ibigrun;
    double insigmafac;
    int N;
    int n_resultsBigPOP;
    int n_resultsSmallPOP;
    double[] resultsBigPOP;
    double[] resultsSmallPOP;
    double SmallPOPdivBigPOPbudget;
    boolean stop;

    double[] cur_coeff;
    double[][] xnew;
    double[] fnew;
    int npt;
   // int pop_size;
    int MAX_EVALS;

    boolean bipop_criterion;
    //   int[] def_coeff = new int[4];
    MyStat mystat;

    CMAES cmaes;



    public Xcmaes(int max_evals) {
        N = Configuration.DIM;
    //    pop_size = cmaes.popsize;
        cmaes = new CMAES(N, Configuration.benchmark);
        MAX_EVALS = max_evals;
        algo = new Algo();
        budget = new Budget();
        initialize();
    }

    public void initialize() {
        Options options = new Options();
        options.max_evals = MAX_EVALS;

        if (Configuration.BIPOP) {
            if (Configuration.noisy) {
                options.max_iter = true;
            }
            options.TolFun = true;
            options.TolHistFun = true;
            options.TolX = true;
            options.TolUpX = true;
            Options.ccov1 = true;
        //    Options.ccovmu = true;
        //    Options.ccum = true;
        }

        if (N < 10) {
            //TODO opcional
        }
        if (N >= 10) {
            //TODO opcional
        }

        double sigma0 = 2.0;
        if (Configuration.noisy) {
            //TODO opcional
        }

        sigma0 = 200 * 0.6;
        double[] xstart = new double[N];
        for (int i = 0; i < 10; i++) {
            xstart[i] = -100 + 200 * Configuration.rand.getDouble();
        }
        cmaes.initialize(xstart, sigma0, options);

        if (Configuration.BIPOP) {
            nrunswithsmallpopsize = 1;
            budget.smallpopsi = new ArrayList<>();
            budget.largepopsi = new ArrayList<>();
        }
        SmallPOPdivBigPOPbudget = 1.0;
        nrestarts = options.restarts;
        bipop_criterion = false;

        if (Configuration.newRestartRules) {
            SmallPOPdivBigPOPbudget = 1.0;
            n_resultsBigPOP = 0;
            n_resultsSmallPOP = 0;
            resultsBigPOP = new double[1];
            resultsBigPOP[0] = 0;
            resultsSmallPOP = new double[1];
            resultsSmallPOP[0] = 0;
        }

    }

    public void execute() {

        while (cmaes.irun <= nrestarts || bipop_criterion) {

            algo.nevals = 0;
            algo.realfunc = 1;
            algo.iSZ = 0;
            algo.aSZ = 0;
            algo.Fmin = 1e30;


            // modelType = Configuration.modelType;
            //  optModel = InitModelParameters(modelType, N, Configuration.withModelOptimization);

            algo.CMAactive = Configuration.CMAactive;
            algo.withDisp = Configuration.withDisp;
            algo.withSurr = Configuration.withSurr;
            algo.withModelOptimization = Configuration.withModelOptimization;
            algo.iterstart = Configuration.iterstart;


//            if(!Configuration.withModelOptimization){
//                algo.maxArchSize = (int) Math.floor( optSVM.def_coeff[0]);
//            }else{
//                algo.maxArchSize = (int) Math.floor( optSVM.xmax[0]);
//            }
            algo.maxArchSize = Configuration.MaxTrainingPoints;
            algo.ARX = new double[algo.maxArchSize][N];
            algo.ARF = new double[algo.maxArchSize];
            algo.ARneval = new double[algo.maxArchSize];
            Util.assignMatrix(algo.ARX, 0);
            Util.assignArray(algo.ARF, 0);
            Util.assignArray(algo.ARneval, 0);


            cmaes.irun++;
            if (Configuration.newRestartRules) {
                if ((cmaes.irun > 3) || (Configuration.iGlobalRun > 1)) {
                    algo.withSurr = false;
                    algo.withModelOptimization = false;
                }
            }

            cmaes.initializeRun();
//            cmaes.initializeRun();
            if (Configuration.BIPOP) {
                lambda0 = (int) Math.floor(cmaes.pop_size * (Math.pow(cmaes.IncPopSize, cmaes.irun - nrunswithsmallpopsize)));
                popsize = lambda0;
                cmaes.lambda = lambda0;
                cmaes.pop_size = popsize;
            }
            algo.err = 0.5;
            algo.iter = 0;
            iSTEP = 0;
            WeDontBelieveInModel_STEP = 0;

            isIPOPRun = true;
            if (Configuration.BIPOP) {
                double insigmafac = 1;
                if (cmaes.irun > 2 && Util.summatory(Util.listToArray(budget.smallpopsi)) < SmallPOPdivBigPOPbudget * Util.summatory(Util.listToArray(budget.largepopsi))) {
                    nrestarts++;
                    nrunswithsmallpopsize++;
                    budget.irunwithsmallpopsize = cmaes.irun;
                    insigmafac = Math.pow(0.01, Configuration.rand.getDouble());

                    lambda0 = cmaes.pop_size;
                    if (!Configuration.newRestartRules) {
                        lambda0 = (int) Math.floor(lambda0 * Math.pow(cmaes.lambda / cmaes.IncPopSize / lambda0, Math.pow(Configuration.rand.getDouble(), 2)));
                    } else {
                        lambda0 = cmaes.pop_size;
                    }
                    popsize = lambda0;
                    lambda = lambda0;
                    budget.maxiter = 0.5 * Util.summatory(Util.listToArray(budget.largepopsi)) / lambda0;
                    cmaes.sigma = insigmafac * Util.max(cmaes.insigma);
                    cmaes.lambda = lambda;
                    cmaes.pop_size = popsize;
                    isIPOPRun = false;

                } else {
                    budget.maxiter = Integer.MAX_VALUE;
                }
                cmaes.stopMaxIter = Math.min(cmaes.MaxIter, (int) budget.maxiter);
            }

            if ((Configuration.newRestartRules) && (isIPOPRun)) {
                if (Configuration.BIPOP == false) {
                    nrunswithsmallpopsize = 1;
                }
                ibigrun = cmaes.irun - nrunswithsmallpopsize + 1;
                insigmafac = Math.pow(1.6, -(ibigrun - 1));
                cmaes.sigma = insigmafac * Util.max(cmaes.insigma);
            }




            loop();
            boolean break_result = cmaes.finish();

            if (break_result) {
                break;
            }


            if (Configuration.newRestartRules == true) {
                if ((isIPOPRun) || (cmaes.irun < 3)) {
                    resultsBigPOP[n_resultsBigPOP] = algo.Fmin;
                    n_resultsBigPOP++;
                }
                if ((!isIPOPRun) || (cmaes.irun < 3)) {
                    resultsSmallPOP[n_resultsSmallPOP] = algo.Fmin;
                    n_resultsSmallPOP++;
                }
                if (Util.min(resultsBigPOP) < Util.min(resultsSmallPOP)) {
                    SmallPOPdivBigPOPbudget = 0.5;
                } else {
                    SmallPOPdivBigPOPbudget = 2.0;
                }
            }

            if (Configuration.BIPOP == true) {
                if (cmaes.irun == 1) {
                    budget.smallpopsi = new ArrayList<>();
                    budget.smallpopsi.add(cmaes.counteval);
                    budget.largepopsi = new ArrayList<>();
                    budget.counteval0 = 0;
                    budget.irunwithsmallpopsize = 1;
                }
                if (budget.irunwithsmallpopsize == cmaes.irun) {
                    budget.smallpopsi.add(cmaes.counteval - budget.counteval0);
                } else if (cmaes.irun > 1) {
                    budget.largepopsi.add(cmaes.counteval - budget.counteval0);
                }
                budget.counteval0 = cmaes.counteval;

                bipop_criterion = Util.summatory(Util.listToArray(budget.smallpopsi)) < SmallPOPdivBigPOPbudget * Util.summatory(Util.listToArray(budget.largepopsi));
            }
        }
        //MyStat myStat = new MyStat();
        if(Configuration.myStat.fmin > cmaes.fmin) {
            Configuration.myStat.bestx = Util.copyArray(cmaes.xmin);
            Configuration.myStat.fmin = cmaes.fmin;
        }
      //  return myStat;
    }


    public void loop() {
        stop = false;
        int evals  = cmaes.iteration();
        Configuration.myStat.nevals += evals;
    }


    public static class MyStat{
        public int nevals;
        public double fmin;
        public double [] bestx;
    }

}
