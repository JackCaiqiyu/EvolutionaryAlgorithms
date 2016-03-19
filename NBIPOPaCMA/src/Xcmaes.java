import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
import com.sun.jndi.cosnaming.CNCtx;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

public class Xcmaes {
    State cur_state;
    State initial_state;
    Algo algo;
    Algo algo_without_new_points;
    OptSVM optSVM;
    Hyper_opts hyper_opts;
    Model model;
    Opts opts;
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
    double [] resultsBigPOP;
    double [] resultsSmallPOP;
    double SmallPOPdivBigPOPbudget;
    boolean stop;
    double [] cur_coeff;
    double [][] xnew;
    double [] fnew;
    int npt;
 //   int[] def_coeff = new int[4];


    public Xcmaes(){

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

            optSVM.def_coeff[0] = (int)(40 + Math.floor(4 *(Math.pow(N, 1.7)))); //TODO que es N????? DIM???
            optSVM.def_coeff[1] = 6;
            optSVM.def_coeff[2] = 3;
            optSVM.def_coeff[3] = 1;
            optSVM.coeff = copyArray(optSVM.def_coeff);

            optSVM.xmin[0] = 4 * N;
            optSVM.xmin[1] = 0;
            optSVM.xmin[2] = 0;
            optSVM.xmin[3] = 0.5;

            optSVM.xmax[0] = 2 * (40 + Math.floor(4* (Math.pow(N, 1.7))));
            optSVM.xmax[1] = 10;
            optSVM.xmax[2] = 6;
            optSVM.xmax[3] = 2;

            algo.CMAactive = Configuration.CMAactive;
            algo.withDisp = Configuration.withDisp;
            algo.withSurr = Configuration.withSurr;
            algo.withModelOptimization = Configuration.withModelOptimization;
            algo.iterstart = Configuration.iterstart;
            algo.withFileDisp = Configuration.withFileDisp;



            if(!Configuration.withModelOptimization){
                algo.maxArchSize = (int) Math.floor( optSVM.def_coeff[0]);
            }else{
                algo.maxArchSize = (int) Math.floor( optSVM.xmax[1]);
            }
            zeros(algo.ARX, N, algo.maxArchSize);
            zeros(algo.ARF, 1, algo.maxArchSize);
            zeros(algo.ARneval, 1, algo.maxArchSize);

            cur_state.iRun++;
            if(Configuration.newRestartRules){
                if((cur_state.iRun > 3) || (Configuration.iGlobalRun > 1)){
                    algo.withSurr = false;
                    algo.withModelOptimization = false;
                }
            }

            cur_state = cmaes_initializeRun(); //TODO crear el metodo
            if (Configuration.BIPOP){
                lambda0 = (int)Math.floor(opts.PopSize *(Math.pow(opts.IncPopSize, cur_state.irun - nrunswithsmallpopsize)));
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
                if(cur_state.iRun > 2 && sum(budget.smallpopsi) < SmallPOPdivBigPOPbudget*sum(budget.largepopsi)){
                    nrestarts++;
                    nrunswithsmallpopsize++;
                    budget.irunwithsmallpopsize = cur_state.iRun;
                    insigmafac = Math.pow(0.01, Configuration.rand.getFloat());

                    lambda0 = opts.PopSize;
                    if(!Configuration.newRestartRules){
                        lambda0 = (int)Math.floor(lambda0 * Math.pow(cur_state.lambda / opts.IncPopSize /lambda0, Math.pow(Configuration.rand.getFloat(),2)));
                    }else{
                        lambda0 = opts.PopSize;
                    }
                    popsize = lambda0;
                    lambda = lambda0;
                    budget.maxiter = 0.5 * sum(budget.largepopsi) / lambda0;
                    cur_state.sigma = insigmafac * max(cur_state.insigma); //TODO implementar funcion max
                    cur_state.lambda = lambda;
                    cur_state.popsize = popsize;
                    isIPOPRun = false;

                }else{
                    budget.maxiter = infinito; //TODO crear infinito
                }
                cur_state.stopMaxIter = min(opts.MaxIter, budget.maxiter); //TODO implementar funcion min
            }

            if((Configuration.newRestartRules) && (isIPOPRun)){
                if(Configuration.BIPOP == false){
                    nrunswithsmallpopsize = 1;
                }
                ibigrun = cur_state.irun - nrunswithsmallpopsize +1;
                insigmafac = Math.pow(1.6, -(ibigrun-1));
                cur_state.sigma = insigmafac*max(cur_state.insigma); //TODO implementar
            }
            stop = false;
            loop();

            cur_state = cmaes_finalize(cur_state); // TODO implementar metodo
            //TODO si ha llegado al parametro de fin terminar cuando el error era muy bajo creo recordar

            if(Configuration.newRestartRules == true){
                if((isIPOPRun) || (cur_state.irun < 3){
                    n_resultsBigPOP++;
                    resultsSmallPOP[n_resultsBigPOP-1] = algo.Fmin; //TODO crear array resultsSmallPOP
                }
                if(min(resultsBigPOP) < min (resultsSmallPOP)){
                    SmallPOPdivBigPOPbudget = 0.5;
                }else{
                    SmallPOPdivBigPOPbudget = 2.0;
                }
            }

            if (Configuration.BIPOP == true){
                if (cur_state.irun == 1) {
                    budget.smallpopsi = cur_state.counteval; //TODO porque array = int?
                    budget.largepopsi = null; //TODO controlar este nulo, en matlab vacia el vector aqui? uhm
                    budget.counteval0 = 0;
                    budget.irunwithsmallpopsize = 1;
                }
                if (budget.irunwithsmallpopsize == cur_state.irun) {
                    budget.smallpopsi = incrementarArray1(budget.smallpopsi); //TODO he incrementado el array en 1 controlar que no muera en algun lado
                    budget.smallpopsi[budget.smallpopsi.length-1] = cur_state.counteval - budget.counteval0;
                }else if (cur_state.irun > 1) {
                    budget.largepopsi = incrementarArray1(budget.largepopsi);
                    budget.largepopsi[budget.largepopsi.length-1] = cur_state.counteval - budget.counteval0;
                }
                budget.counteval0 = cur_state.counteval;

                bipop_criterion = sum(budget.smallpopsi) < SmallPOPdivBigPOPbudget*sum(budget.largepopsi);
            }
        }
        //TODO mejor resultado en cur_state.xmean
    }

    private void loop(){

        while(!stop) {
            if (algo.withSurr == false) {
                cur_state = cmaes_iteration(); // TODO implementar
                stop = cur_state.stop;
            } else {
                double err = 0;
                algo.iter++;
                if (algo.iter < algo.iterstart) {
                    cur_state = cmaes_iteration(cur_state); //TODO implementar
                    algo.sav_fitness = cur_state.fitness;
                    algo.sav_out = cur_state.out;
                    algo.sav_counteval = cur_state.counteval;
                } else {
                    algo.realfunc = 0;

                    for (int i = 0; i<optSVM.coeff.length  ; i++) {
                        cur_coeff[i] = (optSVM.coeff[i] - optSVM.xmin[i]) / (optSVM.xmax[i] - optSVM.xmin[i]);
                    }
                    algo.coeffs[algo.iter-1] = copyArray(cur_coeff);
                    algo.coeffs_mean = mean(); //TODO meter el array para la media

                    model = xacmes_buildModel_RANKSVM(cur_state, optSVM.coeff); //TODO implementar funcion
                    RSVM  = new Model(model);

                    initial_state = new State(cur_state);
                    cur_state.savemodulo = false;
                    for (int i = 0; i < iSTEP; i++) {
                        algo.realfunc = 0;
                        cur_state = cmaes_iteration(cur_state); //TODO implementar metodo
                    }
                    algo_without_new_points = new Algo(algo);

                    algo.realfunc = 1;
                    cur_state.savemodulo = true;
                    cur_state.fitness = algo.sav_fitness;
                    cur_state.out = algo.sav_out;
                    cur_state.counteval = algo.sav_counteval;

                    cur_state = cmaes_iteration(cur_state); //TODO

                    algo.sav_fitness = cur_state.fitness;
                    algo.sav_out = cur_state.out;
                    algo.sav_counteval = cur_state.counteval;

                    stop = cur_state.stop;
                    algo_without_new_points = new Algo(algo);

                    for(int i=0; i<cur_state.Xnew_sorted.length; i++){
                        xnew[i] = copyArray(cur_state.Xnew_sorted[i]);
                    }

                    fnew = copyArray(cur_state.arfitness);
                    npt = xnew.length; //TODO check size (x,2)

                    if (model.nCrossValidation == 0) {
                        for(int i=0; i<model.CrossValidX.length; i++){
                            model.CrossValidX[i] = copyArray(xnew[i]);
                        }
                        model.CrossValidF = copyArray(fnew);
                    } else {
                        for (int i = 0; i < npt; i++) {
                          //  model.CrossValidX = copyColumn(xnew,)


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
                            optSVM.CrossValidX[i] = copyArray(xnew[i]);
                        }
                        optSVM.CrossValidF = copyArray(fnew);
                        optSVM.nx = cur_state.lambda;
                        optSVM.algo_initial_state = initial_state;
                        algo.realfunc = 2;
                        if (optSVM.init == false) {
                            int hyper_N = size(optSVM.coeff, 2); //TODO check
                            double hyper_X_a = 0.2;
                            double hyper_X_b = 0.8;
                            double hyper_sigma = 0.6;
                            int hyper_lambda = Configuration.hyper_lambda;

                            hyper_opts = cmaes_initialize('defaults'); //TODO implementar este modulo
                            hyper_opts.MaxFunEvals = 1e+10;
                            hyper_opts.StopFitness = -1;
                            hyper_opts.DispModulo = -1;
                            hyper_opts.DispFinal = "off";
                            hyper_opts.CMA.active = algo.CMAactive; //TODO que es CMA ??????
                            hyper_opts.LogModulo = 0;
                            hyper_opts.LogTime = 0;
                            hyper_opts.SaveVariables = "off";

                            hyper_opts.PopSize = hyper_lambda;

                            hyper_xstart =[num2str(hyper_X_a) ' + ' num2str(hyper_X_b - hyper_X_a) '*rand('
                            num2str(hyper_N) ',1)'];

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
                    szcoeffs = size(algo.coeffs, 2); // TODO
                    //TODO display resultados
                }
            }
        }
        //TODO display resultados

    }

    private double[] copyArray(double [] aOld){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }

    private int[] copyArray(int [] aOld){
        int [] aNew = new int [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }

    private void zeros (double [][] array, int N, int M){
        array = new double[N][M];
        for(int i=0; i<N; i++){
            for(int j=0; j<M; j++){
                array[i][j] = 0;
            }
        }
    }

    private int sum(int [] array){
        int s = 0;
        for(int i=0; i<array.length; i++){
            s += array[i];
        }
        return s;
    }

    private double mean(double [] array){
        int sum = 0;
        for(int i=0; i<array.length; i++){
            sum += array[i];
        }
        return sum/array.length;
    }

    private int[] incrementarArray1(int [] array){
        int [] newArray = new int [array.length +1];
        for(int i=0; i<array.length; i++){
            newArray [i] = array[i];
        }
        return array;
    }

    private double[] selectRangeArray(double[] array, int start, int end){ //TODO check funcion funciona
        double [] newArray = new double[end-start+1];
        for(int i=start; i<=end; i++){
            newArray[i] = array[i];
        }
        return newArray;
    }

    private double [][] copyColumn(double [][] matrix, int col){
        double [][] newMatrix = new double[matrix.length][matrix[0].length]; //TODO check esto funciona
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++){
                newMatrix[i][j] = matrix[i][j];
            }
        }

        for(int i=0; i<matrix[0].length; i++){
            newMatrix[col][i] = matrix[col][i];
        }

        return newMatrix;
    }




}
