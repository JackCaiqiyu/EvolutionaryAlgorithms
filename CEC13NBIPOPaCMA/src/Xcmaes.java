import CMAESold.Cmaes;
import com.benchmark.AllBenchmarks;

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
    boolean bipop_criterion;
 //   int[] def_coeff = new int[4];
    MyStat mystat;

    public Xcmaes(){
        N = Configuration.DIM;
        pop_size = Configuration.popSize;
    }

    public void execute(){

        while (cur_state.iRun <= nrestarts || bipop_criterion){

            algo.nevals = 0;
            algo.realfunc = 1;
            algo.iSZ = 0;
            algo.aSZ = 0;
            algo.Fmin = 1e30;

//            optSVM.init = false;
//            optSVM.def_coeff = new int[4];
//            optSVM.def_coeff[0] = (int)(40 + Math.floor(4 *(Math.pow(N, 1.7))));
//            optSVM.def_coeff[1] = 6;
//            optSVM.def_coeff[2] = 3;
//            optSVM.def_coeff[3] = 1;
//            optSVM.coeff = Util.copyArray(optSVM.def_coeff);
//
//            optSVM.xmin = new double[4];
//            optSVM.xmin[0] = 4 * N;
//            optSVM.xmin[1] = 0;
//            optSVM.xmin[2] = 0;
//            optSVM.xmin[3] = 0.5;
//
//            optSVM.xmax = new double[4];
//            optSVM.xmax[0] = 2 * (40 + Math.floor(4* (Math.pow(N, 1.7))));
//            optSVM.xmax[1] = 10;
//            optSVM.xmax[2] = 6;
//            optSVM.xmax[3] = 2;

            modelType = Configuration.modelType;
            optModel = InitModelParameters(modelType, N, Configuration.withModelOptimization);

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
            algo.maxArchSize = optModel.MaxTrainingPoints;
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
            iSTEP = 0;
            WeDontBelieveInModel_STEP = 0;

            isIPOPRun = true;
            if(Configuration.BIPOP){
                double insigmafac = 1;
                if(cur_state.iRun > 2 && Util.summatory(Util.listToArray(budget.smallpopsi)) < SmallPOPdivBigPOPbudget*Util.summatory(Util.listToArray(budget.largepopsi))){
                    nrestarts++;
                    nrunswithsmallpopsize++;
                    budget.irunwithsmallpopsize = cur_state.iRun;
                    insigmafac = Math.pow(0.01, Configuration.rand.getDouble());

                    lambda0 = Configuration.popSize;
                    if(!Configuration.newRestartRules){
                        lambda0 = (int)Math.floor(lambda0 * Math.pow(cur_state.lambda / Configuration.IncPopSize /lambda0, Math.pow(Configuration.rand.getDouble(),2)));
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
            if(break_result){
                break;
            }

            //TODO CMAES_FINALIZE

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

            if (mystat.nevals > Configuration.MaxEvalsWithSurrogate){
                algo.withSurr = false;
            }

            if(mystat.nevals > Configuration.max_evals){
                stop = true;
            }

            if (algo.withSurr == false) {
                cur_state = cmaes.cmaes_iteration(cur_state);
                stop = cur_state.stop;
            } else {

            }
            if (mystat.stop == true)
                stop = true;

            if (algo.Fmin < AllBenchmarks.objective()) {
                    stop = true;
                    return;
                }

        }

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
