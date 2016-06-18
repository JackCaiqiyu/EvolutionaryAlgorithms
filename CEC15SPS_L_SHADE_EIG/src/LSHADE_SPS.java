import java.util.ArrayList;
import java.util.List;



public class LSHADE_SPS {
    int counteval, maxfunevals, NP, usefunevals, D, H, countstagnation, NPinit, NPmin, iChy, countiter, iM, nA, Q, Asize, iSP, TolStagnationIteration;
    boolean interpolation, EarlyStopOnFitness, AutoEarlyStop, EarlyStopOnTolFun, noiseHandling;
    double ftarget, ub, lb,  p, TolFun;

    double erw, crw, Ar, CRmin, CRmax;
    double [] fx, MCR, F, MF, Chy, FC,  CR, S_CR, S_F, S_df, fSP;
    double [][] X, V, XA, SP, SPA, U, A;
    double [] ER, MER, S_ER; //EIG version
    double [][] C;//EIG version
    double cw, cwinit; //EIG version
    int [] pbest, sortidxfSP;
    //test_func test_func;

    public LSHADE_SPS(){
        maxfunevals = Configuration.maxfunevals;
        ub = Configuration.benchmark.ubound();
        lb = Configuration.benchmark.lbound();

        NP = Configuration.NP;
        D = Configuration.D;
        H = Configuration.H;
        erw=Configuration.erw;
        crw = Configuration.crw;
        cw = Configuration.cw;
        cwinit = Configuration.cwinit;
        Ar = Configuration.Ar;
        p = Configuration.p;
        NPmin = Configuration.NPmin;
        Q = Configuration.Q;
        CRmin = Configuration.CRmin;
        CRmax = Configuration.CRmax;
        ftarget = Configuration.benchmark.bias();
        noiseHandling = Configuration.noise;
//        NP = Configuration.NP;
//        D = Configuration.D;
//        H = Configuration.H;
//        erw = Configuration.erw;
//        crw = Configuration.crw;
//        cw = Configuration.cw;
//        cwinit = Configuration.cwinit;

        SP = new double[NP][D];
        fSP = new double[NP];
        V = new double[NP][D];
        U = new double[NP][D];
        pbest = new int[NP];
        fx = new double[NP];
        MCR = new double[H];
        F = new double[NP];
        MF = new double[H];
        FC = new double[NP];
        CR = new double[NP];
        X = new double[NP][D];
        S_CR = new double[NP];
        S_F = new double[NP];
        S_df = new double[NP];

        S_ER = new double[NP];
        C = new double[NP][];
        ER = new double[NP];
        MER = new double[H];
        initialize();
    }



    private void initialize() {
//        Util.assignArray(MER, Configuration.ER);
//        Util.assignArray(S_ER, 0);
//        Util.assignArray(CR, Configuration.CR);
//        Util.assignArray(F, 0.5);
//
//        Ar = 2.6f;
//        p = 0.11;
//        NPmin = 4;
//        Q = 64;
//
//        ftarget = Util.ninf;
//        TolFun = 0;
//        TolStagnationIteration = Util.iinf;
//        usefunevals = Util.iinf;
//
//        NPinit = NP;
//
//        if (Configuration.ConstraintHandling.equals("Interpolation")) {
//            interpolation = true;
//        } else {
//            interpolation = false;
//        }
//
//
//        if (Configuration.EarlyStop.equals("fitness")) {
//            EarlyStopOnFitness = true;
//            AutoEarlyStop = false;
//            EarlyStopOnTolFun = false;
//        }else if (Configuration.EarlyStop.equals("'auto'")) {
//            EarlyStopOnFitness = false;
//            AutoEarlyStop = true;
//            EarlyStopOnTolFun = false;
//        }else if (Configuration.EarlyStop.equals("TolFun")) {
//            EarlyStopOnFitness = false;
//            AutoEarlyStop = false;
//            EarlyStopOnTolFun = true;
//        }else {
//            EarlyStopOnFitness = false;
//            AutoEarlyStop = false;
//            EarlyStopOnTolFun = false;
//        }
//
//        counteval = 0;
//        countiter = 1;
//        countstagnation = 0;
//
//
//
//        for(int i=0; i<NP; i++) {
//            for (int j = 0; j < D; j++) {
//                X[i][j]=lb + (ub - lb) * Configuration.rand.getDouble();
//            }
//        }
//
//
//        for(int i=0; i<NP; i++){
//            fx[i] = Configuration.benchmark.f(X[i]);
//        }
//        counteval = counteval + NP;
//
//
//        Asize = Math.round(Ar * NP);
//        A = new double[Asize][D];
//        for(int i=0; i<Asize; i++){
//            for (int j = 0 ; j< D; j++) {
//                A[i][j]=lb + (ub - lb) * Configuration.rand.getDouble();
//            }
//        }
//
//        nA = 0;
//
//        //SORT POPULATION
//        for(int i=0; i < NP; i++){
//            for(int j=i+1; j<NP; j++){
//                if(fx[i] > fx[j]){
//                    double tmp = fx[i];
//                    fx[i] = fx[j];
//                    fx[j] = tmp;
//
//                    double [] tmpa = Util.copyArray(X[i]);
//                    X[j] = Util.copyArray(X[j]);
//                    X[i] = Util.copyArray(tmpa);
//
//                }
//            }
//        }
//        // Configuration.records.newRecord(fx[0] - bias.getBias(Configuration.nF), counteval);
//        Configuration.records.newRecord(Math.abs(Configuration.benchmark.bias() - fx[0]), counteval);
//        Util.assignArray(MF, F);
//        Util.assignArray(MCR, CR);
//        iM = 0;
//        Util.assignArray(FC, 0);
//        SP = Util.copyMatrix(X);
//        fSP = Util.copyArray(fx);
//
//
//        iSP = 0;
//
//        V = Util.copyMatrix(X);
//        U = Util.copyMatrix(X);
//        Util.assignArray(S_CR, 0);
//        Util.assignArray(S_F, 0);
//        Util.assignArray(S_df, 0);
//
//        Chy = Configuration.rand.cauchyrnd(NP+10, 0, 0.1);
//        iChy = 0;
//        sortidxfSP = Util.sortOnlyIndexs(fSP);
//
//        C = Matlab.cov(X);
        TolStagnationIteration = Util.iinf;
        usefunevals = Util.iinf;
        NPinit = NP;

        if (Configuration.ConstraintHandling.equals("Interpolation")) {
            interpolation = true;
        } else {
            interpolation = false;
        }

        if (Configuration.EarlyStop.equals("fitness")) {
            EarlyStopOnFitness = true;
            AutoEarlyStop = false;
        }else if (Configuration.EarlyStop.equals("auto")) {
            EarlyStopOnFitness = false;
            AutoEarlyStop = true;
        }else {
            EarlyStopOnFitness = false;
            AutoEarlyStop = false;
        }

        int startX = 0;
        if(Configuration.Xinitial != null){
            X = new double[Configuration.Xinitial.length][D];
            for(int i=0; i<Configuration.Xinitial.length; i++){
                X[i] = Util.copyArray(Configuration.Xinitial[i]);
            }
            startX = Configuration.Xinitial.length;
            NP = startX;
        }



        counteval = 0;
        countiter = 1;
        countstagnation = 0;

        if(Configuration.Xinitial == null) {
            X = new double[NP][D];
            for (int i = startX; i < NP; i++) {
                for (int j = 0; j < D; j++) {
                    X[i][j] = lb + (ub - lb) * Configuration.rand.getDouble();
                }
            }
        }


        for(int i=0; i<NP; i++){
            fx[i] = Configuration.benchmark.f(X[i]);
        }
        counteval = counteval + NP;


        Asize = (int)Math.round(Ar * (double)NP);
        A = new double[Asize][D];
        for(int i=0; i<Asize; i++){
            for (int j = 0 ; j< D; j++) {
                A[i][j]=lb + (ub - lb) * Configuration.rand.getDouble();
            }
        }

        nA = 0;

        //SORT POPULATION
        /*for(int i=0; i < NP; i++){
            for(int j=i+1; j<NP; j++){
                if(fx[i] > fx[j]){
                    double tmp = fx[i];
                    fx[i] = fx[j];
                    fx[j] = tmp;

                    double [] tmpa = Util.copyArray(X[i]);
                    X[j] = Util.copyArray(X[j]);
                    X[i] = Util.copyArray(tmpa);

                }
            }
        }*/

        int []fidx = Util.sort(fx);
        X = Util.sortByIndexs(X, fidx);
        if(Configuration.isRecordsActive)
            Configuration.records.newRecord(Math.abs(Configuration.benchmark.bias() - fx[0]), counteval);

        Util.assignArray(MF, Configuration.F);
        Util.assignArray(MCR, Configuration.CR);
        Util.assignArray(MER, Configuration.ER);
        iM = 0;
        Util.assignArray(FC, 0);


        Util.assignArray(CR, 0);
        Util.assignArray(F, 0);

        Util.assignArray(S_ER, 0);

        C = Matlab.cov(X);



        SP = Util.copyMatrix(X);
        fSP = Util.copyArray(fx);


        iSP = 0;

        V = Util.copyMatrix(X);
        U = Util.copyMatrix(X);
        Util.assignArray(S_CR, 0);
        Util.assignArray(S_F, 0);
        Util.assignArray(S_df, 0);
        Util.assignArray(S_ER, 0);

        Chy = Configuration.rand.cauchyrnd((NP+10) * (NP+10), 0, Configuration.fw);
        iChy = 0;
        sortidxfSP = Util.sortOnlyIndexs(fSP);


    }



    public double execute(){
        while(true) {
            boolean outofmaxfunevals = counteval > maxfunevals - NP;
            boolean outofusefunevals = counteval > usefunevals - NP;

            if (!EarlyStopOnFitness && !AutoEarlyStop && !EarlyStopOnTolFun) {
                if (outofmaxfunevals || outofusefunevals)
                    break;
            } else if (AutoEarlyStop) {
                boolean solutionconvergence = true;
                boolean functionvalueconvergence;
                boolean reachftarget = Util.min(fx) <= ftarget;
                for(int i=0; i<NP; i++) {
                    if(Util.std(X[i]) > 10 * Math.ulp(Util.mean(X[i]))){
                        solutionconvergence = false;
                        break;
                    }
                }

                functionvalueconvergence = Util.std(fx) <= 10 * Math.ulp(Util.mean(fx));
                boolean stagnation = countstagnation >= TolStagnationIteration;

                if (outofmaxfunevals || outofusefunevals || reachftarget || solutionconvergence || functionvalueconvergence || stagnation) {
                    break;
                }
            } else if (EarlyStopOnFitness) {
                boolean reachftarget = Util.min(fx) <= ftarget;

                if (outofmaxfunevals || outofusefunevals || reachftarget)
                    break;
            } else if (EarlyStopOnTolFun) {
                boolean functionvalueconvergence = Util.std(fx) <= TolFun;
                if (outofmaxfunevals || outofusefunevals || functionvalueconvergence)
                    break;
            }
//if(countiter == 16){
//    System.out.println("Debug");
//}

            countiter++;
            int [] r = new int[NP];
            for(int i=0; i<NP; i++)
                r[i] = (int) Math.floor((H-1) * Configuration.rand.getDouble());

            ER = new double[NP];
            for(int i=0; i<NP; i++){
                ER[i] = MER[r[i]] + erw * Configuration.rand.gaussian();
                if(ER[i] < 0){
                    ER[i] = 0;
                }
                if(ER[i] >1){
                    ER[i] = 1;
                }
            }

            F = new double[NP];
            for (int i = 0; i < NP; i++) {
                do {
                    F[i] = MF[r[i]] + Chy[iChy];
                    iChy = (iChy + 1) % Chy.length;
                }while (F[i] <= 0);
                if (F[i] > 1) {
                    F[i] = 1;
                }
            }

            CR = new double[NP];
            for (int i = 0; i < NP; i++) {
                CR[i] = MCR[r[i]] + (crw * Configuration.rand.gaussian());
                if (CR[i] < CRmin)
                    CR[i] = CRmin;
                if (CR[i] > CRmax)
                    CR[i] = CRmax;

            }

            pbest = new int[NP];
            for (int i = 0; i < NP; i++)
                pbest[i] = (int) Math.floor(Math.max(2, Math.round(p * (double)NP)) * Configuration.rand.getDouble());

            XA = Util.add(X, A);
            SPA = Util.add(SP, A);
            int [] r1 = new int[NP];
            int [] r2 = new int[NP];


            for (int i = 0; i < NP; i++) {
                r1[i] = i;
                while (i == r1[i])
                    r1[i] = (int) Math.floor((NP - 1) * Configuration.rand.getDouble());

                r2[i] = i;
                while (i == r2[i] || r1[i] == r2[i]) {
                    r2[i] = (int) Math.floor((NP + nA - 1) * Configuration.rand.getDouble());
                }
            }

            for (int i = 0; i < NP; i++) {
                if (FC[i] <= Q) {
                    for (int j = 0; j < D; j++)
                        V[i][j] = X[i][j] + F[i] * (X[pbest[i]][j] - X[i][j]) + F[i] * (X[r1[i]][j] - XA[r2[i]][j]);
                } else {
                    for (int j = 0; j < D; j++)
                        V[i][j] = SP[i][j] + F[i] * (SP[sortidxfSP[pbest[i]]][j] - SP[i][j]) + F[i] * (SP[r1[i]][j] - SPA[r2[i]][j]);
                }
            }


            C = Matrix.add(Matrix.multiplies(C, 1 -cw), Matrix.multiplies(Matlab.cov(X), cw));
            double [][] B = Matlab.eigV(C);
//            double [][] B = new double[D][D];
//            for(int i=0; i<D; i++){
//                for(int j=0; j<D; j++){
//                    B[i][j] = C[i][j] / 2;
//                }
//            }
            double [][] XT = Util.copyMatrix(X);
            double [][] VT = Util.copyMatrix(V);
            double [][] UT = Util.copyMatrix(U);
            double [][] SPT = Util.copyMatrix(SP);



//            for (int i = 0; i < NP; i++) {
//                int jrand = (int) Math.floor((D - 1) * Configuration.rand.getDouble());
//
//                if (FC[i] <= Q) {
//                    for (int j = 0; j < D; j++) {
//                        if (Configuration.rand.getDouble() < CR[i] || j == jrand) {
//                            U[i][j] = V[i][j];
//                        } else {
//                            U[i][j] = X[i][j];
//                        }
//                    }
//                } else {
//
//                    for (int j = 0; j > D; j++) {
//                        if (Configuration.rand.getDouble() < CR[i] || j == jrand) {
//                            U[i][j] = V[i][j];
//                        } else {
//                            U[i][j] = SP[i][j];
//                        }
//                    }
//                }
//            }
            for (int i = 0; i < NP; i++) {
                int jrand = (int) Math.floor((D - 1) * Configuration.rand.getDouble());

//                if(i == 71){
//                    System.out.print("DEBUG");
//                }

                if (FC[i] <= Q) {




                    if(Configuration.rand.getDouble() < ER[i]) {
                        XT[i] = Util.copyArray(Matrix.multiplies(B, X[i]));
                        VT[i] = Util.copyArray(Matrix.multiplies(B, V[i]));
                        for (int j = 0; j < D; j++) {
                            if (Configuration.rand.getDouble() < CR[i] || j == jrand) {
                                UT[i][j] = VT[i][j];
                            } else {
                                UT[i][j] = XT[i][j];
                            }
                        }
                        U[i] = Matrix.multiplies(B, UT[i]);
                    }else{
                        for(int j=0; j<D; j++){
                            if(Configuration.rand.getDouble() < CR[i] || j==jrand){
                                U[i][j] = V[i][j];
                            }else{
                                U[i][j] = X[i][j];
                            }
                        }
                    }








                } else {


                    if(Configuration.rand.getDouble() < ER[i]){
                        XT[i] = Util.copyArray(Matrix.multiplies(B, X[i]));
                        VT[i] = Util.copyArray(Matrix.multiplies(B, V[i]));
                        SPT[i] = Util.copyArray(Matrix.multiplies(B, SP[i]));
                        for(int j=0; j<D; j++){
                            if(Configuration.rand.getDouble() < CR[i] || j == jrand){
                                UT[i][j] = VT[i][j];
                            }else{
                                UT[i][j] = SPT[i][j];
                            }
                        }
                        U[i] = Util.copyArray(Matrix.multiplies(B, UT[i]));
                    }else {
                        for (int j = 0; j > D; j++) {
                            if (Configuration.rand.getDouble() < CR[i] || j == jrand) {
                                U[i][j] = V[i][j];
                            } else {
                                U[i][j] = SP[i][j];
                            }
                        }
                    }
                }
            }



//            for (int i = 0; i < NP; i++) {
//                int jrand = (int) Math.floor((D - 1) * Configuration.rand.getDouble());
//
//                if (FC[i] <= Q) {
//                    for (int j = 0; j < D; j++) {
//                        if (Configuration.rand.getDouble() < CR[i] || j == jrand) {
//                            U[i][j] = V[i][j];
//                        } else {
//                            U[i][j] = X[i][j];
//                        }
//                    }
//                } else {
//
//                    for (int j = 0; j > D; j++) {
//                        if (Configuration.rand.getDouble() < CR[i] || j == jrand) {
//                            U[i][j] = V[i][j];
//                        } else {
//                            U[i][j] = SP[i][j];
//                        }
//                    }
//                }
//            }

//            if (interpolation) {
//                for (int i = 0; i < NP; i++) {
//                    for (int j = 0; j < D; j++) {
//                        if (V[i][j] < lb) {
//                            if (FC[i] <= Q) {
//                                V[i][j] = 0.5 * (lb + X[i][j]);
//                            } else {
//                                V[i][j] = 0.5 * (lb + SP[i][j]);
//                            }
//                        } else if (V[i][j] > ub) {
//                            if (FC[i] <= Q) {
//                                V[i][j] = 0.5 * (ub + X[i][j]);
//                            } else {
//                                V[i][j] = 0.5 * (ub + SP[i][j]);
//                            }
//                        }
//                    }
//                }
//            }
            if (interpolation) {
                for (int i = 0; i < NP; i++) {
                    if (FC[i] <= Q) {
                        for (int j = 0; j < D; j++) {
                            if( U[i][j] < lb) {
                                U[i][j] = 0.5 * (lb + X[i][j]);
                            }else if (U[i][j] > ub) {
                                U[i][j] = 0.5 * (ub + X[i][j]);
                            }
                        }
                    }else{
                        for (int j = 0; j < D; j++) {
                            if( U[i][j] < lb) {
                                U[i][j] = 0.5 * (lb + SP[i][j]);
                            }else if (U[i][j] > ub) {
                                U[i][j] = 0.5 * (ub + SP[i][j]);
                            }
                        }
                    }
                }
            }


            double[] fu = new double[NP];
            for(int i=0; i<NP; i++){
                fu[i] = Configuration.benchmark.f(U[i]);
            }
            counteval+= NP;


            boolean FailedIteration = true;
            int nS = 0;
            for (int i = 0; i < NP; i++) {
                if (fu[i] < fx[i]) {
                    S_CR[nS] = CR[i];
                    S_F[nS] = F[i];
                    S_ER[nS] = ER[i];
                    S_df[nS] = Math.abs(fu[i] - fx[i]);
                    nS = nS + 1;
                    for (int j = 0; j < D; j++) {
                        X[i][j] = U[i][j];
                    }
                    fx[i] = fu[i];

                    if (nA < Asize) {
                        for (int j = 0; j < D; j++) {
                            A[nA][j] = X[i][j];
                        }
                        nA = nA + 1;
                    } else {
                        int ri = (int) Math.floor((Asize-1) * Configuration.rand.getDouble());
                        for (int j = 0; j < A[0].length; j++) {
                            A[ri][j] = X[i][j];
                        }
                    }

                    FailedIteration = false;
                    FC[i] = 0;
                    SP[iSP] = Util.copyArray(U[i]);
                    fSP[iSP] = fu[i];
                    iSP = (iSP + 1) % NP;

                } else {
                    FC[i] = FC[i] + 1;
                }
            }

            if (nS > 0) {
                double[] w = Util.divides(Util.range(S_df, 0, nS), Util.summatory(Util.range(S_df, 0, nS)));
                MER[iM] = Util.summatory(Matrix.multipliesElementByElement(w, Util.range(S_ER, 0, nS)));
                MCR[iM] = Util.summatory(Matrix.multipliesElementByElement(w, Util.range(S_CR, 0, nS)));
                MF[iM] = Util.summatory(Matrix.multipliesElementByElement(w, Matrix.power2(Util.range(S_F, 0, nS)))) / Util.summatory(Matrix.multipliesElementByElement(w, Util.range(S_F, 0, nS))) ;
                iM = (iM + 1) % H;
            }
            double NPinitAux = NPinit;
            double NPminAux = NPmin;
            double countevalAux = counteval;
            double maxfunevalsAux = maxfunevals;

            cw = (1 - countevalAux / maxfunevalsAux) * cwinit;
            //SORTS
            int [] indexs = Util.sort(fx);
            X = Util.sortByIndexs(X, indexs);
            FC = Util.sortByIndexs(FC, indexs);
            //Configuration.records.newRecord(fx[0] - bias.getBias(Configuration.nF), counteval);
            if(Configuration.isRecordsActive)
                Configuration.records.newRecord(Math.abs(Configuration.benchmark.bias() - fx[0]), counteval);
            //   System.out.println(fx[0]);
           // System.out.println("Eval: " + (Math.abs(Configuration.benchmark.bias() - fx[0])) + " at: " + counteval);


            NP = (int)Math.round(NPinitAux - (NPinitAux - NPminAux) * countevalAux / maxfunevalsAux);
            fx = Util.range(fx, 0, NP);
            X = Util.range(X, 0, NP);
            U = Util.range(U, 0, NP);

            Asize = (int) Math.round(Ar * (double)NP);
            if (nA > Asize) {
                nA = Asize;
                A = Util.range(A, 0, Asize);
            }

            FC = Util.range(FC, 0, NP);
            sortidxfSP = Util.sortOnlyIndexs(fSP);


            List<double[]> SPtmp = new ArrayList<>();
            List<Double> fSPtmp = new ArrayList<>();
            List<Integer> sortidxfSPtmp = new ArrayList<>();
            for (int i = 0; i < sortidxfSP.length; i++) {
                if (sortidxfSP[i] <= NP) {
                    SPtmp.add(SP[i]);
                    fSPtmp.add(fSP[i]);
                    sortidxfSPtmp.add(sortidxfSP[i]);
                }
            }
            SP = Util.listVectorToArray(SPtmp);
            fSP = Util.listToArrayDouble(fSPtmp);
            sortidxfSP = Util.listToArray(sortidxfSPtmp);

           iSP = ((iSP - 1) % NP) + 1;
           // if(iSP < 0) iSP = NP - 1;

            if (FailedIteration)
                countstagnation = countstagnation + 1;
            else
                countstagnation = 0;

        }
       // System.out.println("Eval: " + (Math.abs(Configuration.benchmark.bias() - fx[0])) + " at: " + counteval);
        // Configuration.records.newRecord(fx[0] - bias.getBias(Configuration.nF));
        // Configuration.records.endRun(fx[0] - bias.getBias(Configuration.nF), counteval, Configuration.maxfunevals);
        if(Configuration.isRecordsActive)
            Configuration.records.endRun(Math.abs(Configuration.benchmark.bias() - fx[0]), counteval, Configuration.maxfunevals);
        return Math.abs(Configuration.benchmark.bias() - fx[0]);
    }


//    private boolean all(double [] array){
//        for(int i=0; i<array.length; i++){
//            if(array[i] != 0.0){
//                return false;
//            }
//        }
//        return true;
//    }

}