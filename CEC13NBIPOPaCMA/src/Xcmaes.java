import CMAESold.Cmaes;

import java.util.ArrayList;

public class Xcmaes {
    State cur_state;
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
    double [] resultsBigPOP;
    double [] resultsSmallPOP;
    double SmallPOPdivBigPOPbudget;
    boolean stop;
    double [] cur_coeff;
    double [][] xnew;
    double [] fnew;
    int npt;
    int pop_size;
    Cmaes cmaes;
 //   int[] def_coeff = new int[4];


    public Xcmaes(){
        N = Configuration.DIM;
        pop_size = Configuration.popSize;
    }

    public void execute(){
        boolean bipop_criterion = false; //TODO ver que es bipop_criterion e inicializarlo

        while (cur_state.iRun <= nrestarts || bipop_criterion){
            //TODO cargar modelos de datos

            algo.nevals = 0;
            algo.realfunc = 1;
            algo.iSZ = 0;
            algo.aSZ = 0;
            algo.Fmin = 1e30;

            optSVM.init = false;
            optSVM.def_coeff = new int[4];
            optSVM.def_coeff[0] = (int)(40 + Math.floor(4 *(Math.pow(N, 1.7))));
            optSVM.def_coeff[1] = 6;
            optSVM.def_coeff[2] = 3;
            optSVM.def_coeff[3] = 1;
            optSVM.coeff = Util.copyArray(optSVM.def_coeff);

            optSVM.xmin = new double[4];
            optSVM.xmin[0] = 4 * N;
            optSVM.xmin[1] = 0;
            optSVM.xmin[2] = 0;
            optSVM.xmin[3] = 0.5;

            optSVM.xmax = new double[4];
            optSVM.xmax[0] = 2 * (40 + Math.floor(4* (Math.pow(N, 1.7))));
            optSVM.xmax[1] = 10;
            optSVM.xmax[2] = 6;
            optSVM.xmax[3] = 2;

            algo.CMAactive = Configuration.CMAactive;
            algo.withDisp = Configuration.withDisp;
            algo.withSurr = Configuration.withSurr;
            algo.withModelOptimization = Configuration.withModelOptimization;
            algo.iterstart = Configuration.iterstart;



            if(!Configuration.withModelOptimization){
                algo.maxArchSize = (int) Math.floor( optSVM.def_coeff[0]);
            }else{
                algo.maxArchSize = (int) Math.floor( optSVM.xmax[0]);
            }
            algo.ARX = new double[algo.maxArchSize][N];
            algo.ARF = new double[algo.maxArchSize];
            algo.ARneval = new double[algo.maxArchSize];
            Util.assignMatrix(algo.ARX, 0);
            Util.assignArray(algo.ARF, 0);
            Util.assignArray(algo.ARneval, 0);


            cur_state.iRun++;
            if(Configuration.newRestartRules){
                if((cur_state.iRun > 3) || (Configuration.iGlobalRun > 1)){
                    algo.withSurr = false;
                    algo.withModelOptimization = false;
                }
            }

            cur_state = cmaes.cmaes_initializeRun(cur_state); //TODO crear el metodo
            if (Configuration.BIPOP){
                lambda0 = (int)Math.floor(Configuration.popSize *(Math.pow(Configuration.IncPopSize, cur_state.irun - nrunswithsmallpopsize)));
                popsize = lambda0;
                cur_state.lambda = lambda0;
                cur_state.popsize = popsize;
            }
            cur_state.stopflag = false;
            algo.err = 0.5;
            algo.iter = 0;
            iSTEP = 1;
            WeDontBelieveInModel_STEP = 0;

            isIPOPRun = true;
            if(Configuration.BIPOP){
                double insigmafac = 1;
                if(cur_state.iRun > 2 && Util.summatory(Util.listToArray(budget.smallpopsi)) < SmallPOPdivBigPOPbudget*Util.summatory(Util.listToArray(budget.largepopsi))){
                    nrestarts++;
                    nrunswithsmallpopsize++;
                    budget.irunwithsmallpopsize = cur_state.iRun;
                    insigmafac = Math.pow(0.01, Configuration.rand.getFloat());

                    lambda0 = Configuration.popSize;
                    if(!Configuration.newRestartRules){
                        lambda0 = (int)Math.floor(lambda0 * Math.pow(cur_state.lambda / Configuration.IncPopSize /lambda0, Math.pow(Configuration.rand.getFloat(),2)));
                    }else{
                        lambda0 = Configuration.popSize;
                    }
                    popsize = lambda0;
                    lambda = lambda0;
                    budget.maxiter = 0.5 * Util.summatory(Util.listToArray(budget.largepopsi)) / lambda0;
                    cur_state.sigma = insigmafac * Util.max(cur_state.insigma);
                    cur_state.lambda = lambda;
                    cur_state.popsize = popsize;
                    isIPOPRun = false;

                }else{
                    budget.maxiter = Util.inf;
                }
                cur_state.stopMaxIter = Math.min(Configuration.MaxIter, (int)budget.maxiter);
            }

            if((Configuration.newRestartRules) && (isIPOPRun)){
                if(Configuration.BIPOP == false){
                    nrunswithsmallpopsize = 1;
                }
                ibigrun = cur_state.irun - nrunswithsmallpopsize +1;
                insigmafac = Math.pow(1.6, -(ibigrun-1));
                cur_state.sigma = insigmafac*Util.max(cur_state.insigma);
            }
            stop = false;
            loop();

            //TODO si ha llegado al parametro de fin terminar cuando el error era muy bajo creo recordar

            if(Configuration.newRestartRules == true){
                if((isIPOPRun) || (cur_state.irun < 3)){
                    resultsBigPOP[n_resultsBigPOP] = algo.Fmin;
                    n_resultsBigPOP++;
                }
                if((!isIPOPRun) || (cur_state.irun < 3)){
                    resultsSmallPOP[n_resultsSmallPOP] = algo.Fmin;
                    n_resultsSmallPOP++;
                }
                if(Util.min(resultsBigPOP) < Util.min (resultsSmallPOP)){
                    SmallPOPdivBigPOPbudget = 0.5;
                }else{
                    SmallPOPdivBigPOPbudget = 2.0;
                }
            }

            if (Configuration.BIPOP == true){
                if (cur_state.irun == 1) {
                    budget.smallpopsi = new ArrayList<>();
                    budget.smallpopsi.add(cur_state.counteval);
                    budget.largepopsi = new ArrayList<>();
                    budget.counteval0 = 0;
                    budget.irunwithsmallpopsize = 1;
                }
                if (budget.irunwithsmallpopsize == cur_state.irun) {
                    budget.smallpopsi.add(cur_state.counteval - budget.counteval0);
                }else if (cur_state.irun > 1) {
                    budget.largepopsi.add(cur_state.counteval - budget.counteval0);
                }
                budget.counteval0 = cur_state.counteval;

                bipop_criterion = Util.summatory(Util.listToArray(budget.smallpopsi)) < SmallPOPdivBigPOPbudget*Util.summatory(Util.listToArray(budget.largepopsi));
            }
        }
        //TODO mejor resultado en cur_state.xmean
    }

    private void loop(){

        while(!stop) {
            if (algo.withSurr == false) {
                cur_state = cmaes.cmaes_iteration(cur_state);
                stop = cur_state.stop;
            } else { //TODO esta parte del algoritmo en ninguno de los test que hay propuestos se llama, porque implementarla?
              /*  double err = 0;
                algo.iter++;
                if (algo.iter < algo.iterstart) {
                    cur_state = cmaes.cmaes_iteration(cur_state);
                    algo.sav_fitness = cur_state.fitness;
                    algo.sav_out = cur_state.out;
                    algo.sav_counteval = cur_state.counteval;
                } else {
                    algo.realfunc = 0;

                    for (int i = 0; i<optSVM.coeff.length  ; i++) {
                        cur_coeff[i] = (optSVM.coeff[i] - optSVM.xmin[i]) / (optSVM.xmax[i] - optSVM.xmin[i]);
                    }
                    algo.coeffs[algo.iter-1] = Util.copyArray(cur_coeff);

                    for(int i=0; i<algo.iter - algo.iterstart; i++) {
                        algo.coeffs_mean[i] = Util.mean(algo.coeffs[i+algo.iterstart]);
                    }
                    model = xacmes_buildModel_RANKSVM(cur_state, optSVM.coeff); //TODO implementar funcion
                    RSVM  = new Model(model);

                    initial_state = new State(cur_state);
                    cur_state.savemodulo = false;
                    for (int i = 0; i < iSTEP; i++) {
                        algo.realfunc = 0;
                        cur_state = cmaes.cmaes_iteration(cur_state); //TODO implementar metodo
                    }
                    algo_without_new_points = new Algo(algo);

                    algo.realfunc = 1;
                    cur_state.savemodulo = true;
                    cur_state.fitness = algo.sav_fitness;
                    cur_state.out = algo.sav_out;
                    cur_state.counteval = algo.sav_counteval;

                    cur_state = cmaes.cmaes_iteration(cur_state); //TODO

                    algo.sav_fitness = cur_state.fitness;
                    algo.sav_out = cur_state.out;
                    algo.sav_counteval = cur_state.counteval;

                    stop = cur_state.stop;
                    algo_without_new_points = new Algo(algo);

                    for(int i=0; i<cur_state.Xnew_sorted.length; i++){
                        xnew[i] = Util.copyArray(cur_state.Xnew_sorted[i]);
                    }

                    fnew = Util.copyArray(cur_state.arfitness);
                    npt = xnew.length; //TODO check size (x,2)

                    if (model.nCrossValidation == 0) {
                        for(int i=0; i<model.CrossValidX.length; i++){
                            model.CrossValidX[i] = Util.copyArray(xnew[i]);
                        }
                        model.CrossValidF = Util.copyArray(fnew);
                    } else {
                        for (int i = 0; i < npt; i++) {
                            model.CrossValidX[model.nCrossValidation + i] = Util.copyArray(xnew[i]);
                            model.CrossValidF[model.nCrossValidation + i] = fnew[i];

                        }
                    }
                    model.nCrossValidation += npt;

                    err = xcmaes_estimateModelError(model); //TODO implementar metodo
                    algo.model_err[algo.iter] = err;


                    if ((iSTEP < Configuration.iSTEPminForHyperOptimization) && (WeDontBelieveInModel_STEP < Configuration.hyper_lambda)) {
                        WeDontBelieveInModel = true;
                        WeDontBelieveInModel_STEP++;
                    } else {
                        WeDontBelieveInModel = false;
                        WeDontBelieveInModel_STEP = 0;
                    }

                    if ((algo.withModelOptimization == true) && (WeDontBelieveInModel == false)) {
                        algo = new Algo(algo_without_new_points);
                        optSVM.nCrossValidation = npt;
                        for(int i=0; i<xnew.length; i++) {
                            optSVM.CrossValidX[i] = Util.copyArray(xnew[i]);
                        }
                        optSVM.CrossValidF = Util.copyArray(fnew);
                        optSVM.nx = cur_state.lambda;
                        optSVM.algo_initial_state = initial_state;
                        algo.realfunc = 2;
                        if (optSVM.init == false) {
                            int hyper_N = optSVM.coeff.length;
                            double hyper_X_a = 0.2;
                            double hyper_X_b = 0.8;
                            double hyper_sigma = 0.6;
                            int hyper_lambda = Configuration.hyper_lambda;

                            hyper_opts = cmaes_initialize('defaults'); //TODO implementar este modulo
                            hyper_opts.MaxFunEvals = 1e+10;
                            hyper_opts.StopFitness = -1;
                            hyper_opts.DispModulo = -1;
                            hyper_opts.DispFinal = "off";
                            hyper_opts.CMAactive = algo.CMAactive;
                            hyper_opts.LogModulo = 0;
                            hyper_opts.LogTime = 0;
                            hyper_opts.SaveVariables = "off";

                            hyper_opts.PopSize = hyper_lambda;
                            //TODO hyper_xstart raro de la ostia
                            optSVM.cur_state = cmaes_initialize(wrapperfct, hyper_xstart, hyper_sigma, hyper_opts);
                            optSVM.cur_state.irun = optSVM.cur_state.irun + 1;
                            optSVM.cur_state = cmaes_initializeRun(optSVM.cur_state);
                            optSVM.cur_state.stopflag = {}; //TODO check
                            optSVM.init = true;
                            optSVM.coeff = optSVM.def_coeff;
                        } else {
                            optSVM.cur_state = cmaes_iteration(optSVM.cur_state);

                            boolean hyper_stop = cur_state.stop;

                            xmean = optSVM.cur_state.xmean; //TODO check si es array copiarlo en dicho caso
                            ncoeff = size(optSVM.coeff, 2);
                            boolean correct = true;
                            for (int j = 0; j < ncoeff; j++) {
                                if ((xmean(j) < 0) || (xmean(j) > 1)) {
                                    correct = false;
                                }
                                coeff(j) = optSVM.xmin(j) + xmean(j) * (optSVM.xmax(j) - optSVM.xmin(j));
                            }
                            if (correct == true)
                                optSVM.coeff = coeff;
                            else
                                optSVM.coeff = optSVM.def_coeff;


                            //TODO display
                        }
                        algo = new Algo(algo_without_new_points);
                        algo.realfunc = 1;
                    }
                }
                if (algo.iter < algo.iterstart) {
                    algo.model_avrerr[algo.iter] = algo.err;
                }
                if (algo.iter >= algo.iterstart) {
                    double est_err = err;
                    algo.err = algo.err * (1 - Configuration.alpha) + Configuration.alpha * est_err;
                    algo.model_avrerr[algo.iter] = algo.err;
                    double pos = (Configuration.maxerr - algo.err) / Configuration.maxerr;
                    iSTEP = (int) Math.floor(pos * Configuration.maxStepts) - 1;
                    if (iSTEP < 0) {
                        iSTEP = 0;
                    }
                    if (iSTEP > Configuration.maxStepts) {
                        iSTEP = Configuration.maxStepts;
                    }
                    szcoeffs = size(algo.coeffs, 2);
                }*/
            }
              /*  if (mystat.stop == 1)
                    stop = true;


                if (algo.Fmin < Configuration.Ter_err) {
                    stop = true;
                    return;
                }*/

        }
        //TODO display resultados

    }

   /* private int[] incrementarArray1(int [] array){
        int [] newArray = new int [array.length +1];
        for(int i=0; i<array.length; i++){
            newArray [i] = array[i];
        }
        return array;
    }*/

  /*  private void xacmes_buildModel_RANKSVM(State cur_state, int [] coeff){
        int aSZ = algo.aSZ;



        double [] ARneval = Util.copyArray(Util.range(algo.ARneval, 0, aSZ)[0]); //algo.ARneval(1,1:aSZ);
        int [] arindex = Util.sort(ARneval);
        double [][] ARX = Util.sortByIndexs(algo.ARX, arindex);
        double [] ARF = Util.copyArray(Util.sortByIndexs(algo.ARF, arindex)[0]);

        double invsqrtC = cur_state.invsqrtC;
        double [] Xmean_model = cur_state.xmean;

        int nTrainMax = aSZ;
        int npts = (int)Math.floor(coeff[0]);
        if (nTrainMax > npts)
            nTrainMax = npts;

        int nTrain = 0;
        double [][]xtrain = null;
        double [] ftrain = null;


        nTrain = aSZ;
        if (nTrain > nTrainMax) nTrain = nTrainMax;

        int [] indx1 = new int[aSZ];
        for(int i=0; i<aSZ; i++)
            indx1[i] = aSZ - nTrain + i;
        xtrain = Util.sortByIndexs(ARX, indx1);
        ftrain = Util.sortByIndexs(ARF,indx1);
        arindex = Util.sort(ftrain);

        double [][] xtrainnew = new double[N][nTrain];
        arindex = Util.cutArray(arindex, nTrain);
        xtrainnew = Util.sortByIndexs(xtrain, arindex);
        xtrain =Util.copyMatrix( xtrainnew);




        int nCrossValidation = 0;
        double [][] CrossValidX = new double[nCrossValidation][N];
        Util.assignMatrix(CrossValidX, 0);
        double [] CrossValidF = new double[N];
        Util.assignArray(CrossValidF, 0);
        for (int i=0; i < nCrossValidation; i++) {
            int index = 1 + (int)Math.floor(Configuration.rand.getFloat() * nTrain);
            nTrain = nTrain - 1;
            CrossValidX[i] = Util.copyArray(xtrain[index]);
            CrossValidF[i] = ftrain[index];
            Util.eraseMember(xtrain, index);
            Util.eraseMember(ftrain, index);
        }

        int niter = (int)Math.floor( 1000*nTrain );
        double epsilon = 1;

        double Cval = 10e6;
        Cval = Math.pow(10,coeff[1]);
        int nAlpha = nTrain - 1;
        double [] Ci = new double[nAlpha];
        Util.assignArray(Ci, 0);
        int [] z = new int[nAlpha];
        for(int i=0; i<nAlpha; i++){
            z[i] = i;
        }
        for(int i=0; i<z.length; i++) {
            Ci[i] = Cval * (Math.pow(nAlpha- i, 3));
            Ci[i] = Cval * (Math.pow((nAlpha - i), coeff[2]));
        }
        double sigmaA = coeff[3];
        double sigmaPow = 1.0;

        double doEncoding = 1.0;
        double [][] x_tr = Util.copyMatrix(xtrain);

        double kernel = 0;

        RankSVMLearn(x_tr, N, nTrain, niter, epsilon, Ci, kernel, invsqrtC, sigmaA, sigmaPow, Xmean_model, doEncoding);
        //[xtrainEncoded, alphas, TwoSigmaPow2] = RankSVMLearn(x_tr, N, nTrain, niter, epsilon, Ci, kernel, invsqrtC, sigmaA, sigmaPow, Xmean_model, doEncoding, verbose); //TODO implementar

        model.nTrain = nTrain;
        model.Xmean_model = Xmean_model;
        model.invsqrtC = invsqrtC;
        model.doEncoding = doEncoding;
        model.xtrainEncoded = xtrainEncoded;
        model.alphas = alphas;
        model.TwoSigmaPow2 = TwoSigmaPow2;

        model.nCrossValidation = nCrossValidation;
        model.CrossValidX = CrossValidX;
        model.CrossValidF = CrossValidF;
    }


    private void RankSVMLearn(double [] x_tr,int N,int nTrain,int niter,double epsilon,double [] Ci,int kernel,double invsqrtC,double sigmaA,double sigmaPow,double [] Xmean_model,double doEncoding){

    }


    private void xacmes_selectTrainingPoints(double [][] arrX,double [] arrF,int aSZ,int xdim,int nTrainMax) {



    }*/

}
