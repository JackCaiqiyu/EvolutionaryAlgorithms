import java.util.ArrayList;
import java.util.List;

public class LSHADE_SPS {
    int counteval, maxfunevals, NP, usefunevals, D, H, countstagnation, NPinit, NPmin, iChy, countiter, iM, nA, Q, Asize, iSP, TolStagnationIteration;
    boolean interpolation, EarlyStopOnFitness, AutoEarlyStop, EarlyStopOnTolFun;
    double ftarget, ub, lb,  p, TolFun;

    float Ar;

    double [] fx, MCR, F, MF, Chy, FC,  CR, S_CR, S_F, S_df, fSP;
    double [][] X, V, XA, SP, SPA, U, A;
    int [] pbest, sortidxfSP;
    //test_func test_func;

    public LSHADE_SPS(){
        NP = Configuration.NP;
        D = Configuration.D;
        H = 6;

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

        initialize();
    }



    private void initialize() {
        maxfunevals = Configuration.maxfunevals;
        ub = Bounds.getUpperBound(Configuration.nF);
        lb = Bounds.getLowerBound(Configuration.nF);


        Util.assignArray(CR, Configuration.CR);
        Util.assignArray(F, 0.5);

        Ar = 2.6f;
        p = 0.11;
        NPmin = 4;
        Q = 64;

        ftarget = Util.ninf;
        TolFun = 0;
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
            EarlyStopOnTolFun = false;
        }else if (Configuration.EarlyStop.equals("'auto'")) {
            EarlyStopOnFitness = false;
            AutoEarlyStop = true;
            EarlyStopOnTolFun = false;
        }else if (Configuration.EarlyStop.equals("TolFun")) {
            EarlyStopOnFitness = false;
            AutoEarlyStop = false;
            EarlyStopOnTolFun = true;
        }else {
            EarlyStopOnFitness = false;
            AutoEarlyStop = false;
            EarlyStopOnTolFun = false;
        }

        counteval = 0;
        countiter = 1;
        countstagnation = 0;



        for(int i=0; i<NP; i++) {
            for (int j = 0; j < D; j++) {
                X[i][j]=lb + (ub - lb) * Configuration.rand.getFloat();
            }
        }


        for(int i=0; i<NP; i++){
            fx[i] = Configuration.benchmark.f(X[i]);
        }
        counteval = counteval + NP;


        Asize = Math.round(Ar * NP);
        A = new double[Asize][D];
        for(int i=0; i<Asize; i++){
            for (int j = 0 ; j< D; j++) {
                A[i][j]=lb + (ub - lb) * Configuration.rand.getFloat();
            }
        }

        nA = 0;

        //SORT POPULATION
        for(int i=0; i < NP; i++){
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
        }
        Configuration.records.newRecord(fx[0] - bias.getBias(Configuration.nF), counteval);

        Util.assignArray(MF, F);
        Util.assignArray(MCR, CR);
        iM = 0;
        Util.assignArray(FC, 0);
        SP = Util.copyMatrix(X);
        fSP = Util.copyArray(fx);


        iSP = 0;

        V = Util.copyMatrix(X);
        U = Util.copyMatrix(X);
        Util.assignArray(S_CR, 0);
        Util.assignArray(S_F, 0);
        Util.assignArray(S_df, 0);

        Chy = Configuration.rand.cauchyrnd(NP+10, 0, 0.1);
        iChy = 0;
        sortidxfSP = Util.sortOnlyIndexs(fSP);


    }



    public void execute(){
        while(counteval < Configuration.maxfunevals && fx[0] - bias.getBias(Configuration.nF) > Bounds.Ter_Err) {
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

            countiter = countiter + 1;
            int [] r = new int[NP];
            for(int i=0; i<NP; i++)
                r[i] = (int) Math.floor((H-1) * Configuration.rand.getFloat());

            for (int i = 0; i < NP; i++)
                CR[i] = MCR[r[i]] + (0.1 * Configuration.rand.uniform());

            for (int i = 0; i < NP; i++)
                if (CR[i] < 0 || MCR[r[i]] == -1)
                    CR[i] = 0;

            for (int i = 0; i < NP; i++)
                if (CR[i] > 1)
                    CR[i] = 1;


            Util.assignArray(F, 0);
            for (int i = 0; i < NP; i++) {
                while (F[i] <= 0) {
                    F[i] = MF[r[i]] + Chy[iChy];
                    iChy = (iChy + 1) % Chy.length;
                }
            }

            for (int i = 0; i < F.length; i++) {
                if (F[i] > 1) {
                    F[i] = 1;
                }
            }

            for (int i = 0; i < NP; i++)
                pbest[i] = (int) Math.floor(Math.max(2, Math.round(p * NP)) * Configuration.rand.getFloat());

            XA = Util.add(X, A);
            SPA = Util.add(SP, A);

            for (int i = 0; i < NP; i++) {
                int r1 = (int) Math.floor((NP - 1) * Configuration.rand.getFloat());
                while (i == r1)
                    r1 = (int) Math.floor((NP - 1) * Configuration.rand.getFloat());

                int r2 = (int) Math.floor((NP + nA - 1) * Configuration.rand.getFloat());
                while (i == r1 || r1 == r2) {
                    r2 = (int) Math.floor((NP + nA - 1) * Configuration.rand.getFloat());
                }

                if (FC[i] <= Q) {
                    for (int j = 0; j < D; j++)
                        V[i][j] = X[i][j] + F[i] * (X[pbest[i]][j] - X[i][j]) + F[i] * (X[r1][j] - XA[r2][j]);
                } else {
                    for (int j = 0; j < D; j++)
                        V[i][j] = SP[i][j] + F[i] * (SP[i][sortidxfSP[pbest[i]]] - SP[i][j]) + F[i] * (SP[r1][j] - SPA[r2][j]);
                }
            }

            if (interpolation) {
                for (int i = 0; i < NP; i++) {
                    for (int j = 0; j < D; j++) {
                        if (V[i][j] < lb) {
                            if (FC[i] <= Q) {
                                V[i][j] = 0.5 * (lb + X[i][j]);
                            } else {
                                V[i][j] = 0.5 * (lb + SP[i][j]);
                            }
                        } else if (V[i][j] > ub) {
                            if (FC[i] <= Q) {
                                V[i][j] = 0.5 * (ub + X[i][j]);
                            } else {
                                V[i][j] = 0.5 * (ub + SP[i][j]);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < NP; i++) {
                int jrand = (int) Math.floor((D - 1) * Configuration.rand.getFloat());

                if (FC[i] <= Q) {
                    for (int j = 0; j < D; j++) {
                        if (Configuration.rand.getFloat() < CR[i] || j == jrand) {
                            U[i][j] = V[i][j];
                        } else {
                            U[i][j] = X[i][j];
                        }
                    }
                } else {

                    for (int j = 0; j > D; j++) {
                        if (Configuration.rand.getFloat() < CR[i] || j == jrand) {
                            U[i][j] = V[i][j];
                        } else {
                            U[i][j] = SP[i][j];
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
                    nS = nS + 1;
                    S_CR[nS] = CR[i];
                    S_F[nS] = F[i];
                    S_df[nS] = Math.abs(fu[i] - fx[i]);
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
                        int ri = (int) Math.floor(Asize * Configuration.rand.getFloat());
                        for (int j = 0; j < A[0].length; j++) {
                            A[ri][j] = X[i][j];
                        }
                    }

                    FailedIteration = false;
                    FC[i] = 0;
                    SP[iSP] = Util.copyArray(U[i]);
                    fSP[iSP] = fu[i];
                    iSP = (iSP + 1) % NP;
                } else if (fu[i] == fx[i]) {
                    for (int j = 0; j < A.length; j++) {
                        X[i][j] = U[i][j];
                    }

                    FC[i] = FC[i] + 1;
                } else {
                    FC[i] = FC[i] + 1;
                }
            }

            if (nS > 0) {
                double[] w = Util.divides(Util.range(S_df, 0, nS), Util.summatory(Util.range(S_df, 0, nS)));

                if (all(Util.range(S_CR, 0, nS))) {
                    MCR[iM] = -1;
                } else if (MCR[iM] != -1) {
                    MCR[iM] = Util.summatory(Util.multiplies(Util.multiplies(w, Util.range(S_CR, 0, nS)), Util.range(S_CR, 0, nS))) / Util.summatory(Util.multiplies(w, Util.range(S_CR, 0, nS)));
                }
                MF[iM] = Util.summatory(Util.multiplies(Util.multiplies(w, Util.range(S_F, 0, nS)), Util.range(S_F, 0, nS))) / Util.summatory(Util.multiplies(w, Util.range(S_F, 0, nS)));
                iM = (iM + 1) % H;
            }

            //SORT
            for (int i = 0; i < fx.length; i++) {
                for (int j = i + 1; j < fx.length; j++) {
                    if (fx[i] > fx[j]) {
                        double tmp = fx[i];
                        fx[i] = fx[j];
                        fx[j] = tmp;

                        double[] aTmp = Util.copyArray(X[i]);
                        X[i] = Util.copyArray(X[j]);
                        X[j] = Util.copyArray(aTmp);

                        tmp = FC[i];
                        FC[i] = FC[j];
                        FC[j] = tmp;


                    }
                }
            }
            Configuration.records.newRecord(fx[0] - bias.getBias(Configuration.nF), counteval);
         //   System.out.println(fx[0]);

            NP = Math.round(NPinit - (NPinit - NPmin) * counteval / maxfunevals);
            fx = Util.range(fx, 0, NP);
            X = Util.range(X, 0, NP);
            U = Util.range(U, 0, NP);

            Asize = Math.round(Ar * NP);
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

            SP = new double[SPtmp.size()][SPtmp.get(0).length];
            fSP = new double[fSPtmp.size()];
            sortidxfSP = new int[sortidxfSPtmp.size()];

            for (int i = 0; i < SP.length; i++) {
                SP[i] = Util.copyArray(SPtmp.get(i));
                fSP[i] = fSPtmp.get(i);
                sortidxfSP[i] = sortidxfSPtmp.get(i);
            }

            iSP = (iSP - 1) % NP;
            if(iSP < 0) iSP = NP - 1;

            if (FailedIteration)
                countstagnation = countstagnation + 1;
            else
                countstagnation = 0;

        }
        Configuration.records.newRecord(fx[0] - bias.getBias(Configuration.nF));
    }


    private boolean all(double [] array){
        for(int i=0; i<array.length; i++){
            if(array[i] != 0.0){
                return false;
            }
        }
        return true;
    }

}
