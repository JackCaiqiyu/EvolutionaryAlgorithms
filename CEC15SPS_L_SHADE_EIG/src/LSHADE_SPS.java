import java.util.ArrayList;
import java.util.List;

public class LSHADE_SPS {
    int counteval, maxfunevals, NP, usefunevals, D, H, countstagnation, NPinit, NPmin, iChy, countiter, iM, nA, Q, Asize, iSP;
    boolean reachftarget, solutionconvergence, functionvalueconvergence, stagnation, interpolation, EarlyStopOnFitness, AutoEarlyStop, EarlyStopOnTolFun;
    double TolX, TolFun, TolStagnationIteration, ftarget, ub, lb,  p;

    float Ar;

    double [] fx, MCR, F, MF, Chy, FC,  CR, S_CR, S_F, S_df, fSP;
    double [][] X, V, XA, SP, SPA, U, A;
    int [] pbest, sortidxfSP, rr;

    private void initialize() {


        ub = Bounds.getUpperBound(Configuration.nF);
        lb = Bounds.getLowerBound(Configuration.nF);
        NP = Configuration.NP;
        CR = new double[NP];
        Util.assignArray(CR, Configuration.CR);
        F = new double[NP];
        Util.assignArray(F, 0.5);
        D = Configuration.D;
        Ar = 2.6f;
        p = 0.11;
        H = 6;
        NPmin = 4;
        Q = 64;

        ftarget = Util.ninf;
        TolFun = 0;
        TolStagnationIteration = Util.inf;
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
        //TODO
/*
        X		= [];
        fx		= [];
        A		= [];
        nA		= [];
        MCR		= [];
        MF		= [];
        iM		= [];
        FC		= [];
        SP		= [];
        fSP		= [];
        iSP		= [];
        counteval = [];
        countiter = [];
        countstagnation = [];
*/



        counteval = 0;
        countiter = 1;
        countstagnation = 0;


        X = new double[D][NP];
        for(int j=0; j<D; j++) {
            for (int i = 0; i < NP; i++) {
                X[j][i]=lb + (ub - lb) * Configuration.rand.getFloat(0, D-1);
            }
        }

        //TODO evaluation


        Asize = Math.round(Ar * NP);
        A = new double[D][Asize];
        for(int j=0; j<D; j++){
            for (int i = 0 ; i< Asize; i++) {
                A[j][i]=lb + (ub - lb) * Configuration.rand.getFloat(0, D-1);
            }
        }

        nA = 0;

        //TODO que carajo es esto?
       /* if (!EpsilonMethod) {


            [fx, fidx]=sort(fx);
            X = X(:,fidx);
        }else {
            PsaiFx =[psai_x ', fx'];
            [~, SortingIndex]=sortrows(PsaiFx);
            X = X(:,SortingIndex);
            fx = fx(SortingIndex);
            psai_x = psai_x(SortingIndex);
        }*/

        MF = new double[H];
        Util.assignArray(MF, F);

        MCR = new double[H];
        Util.assignArray(MCR, CR);

        iM = 1;

        FC = new double[NP];
        Util.assignArray(FC, 0);


        SP = Util.copyMatrix(X);
        fSP = Util.copyArray(fx);
        //TODO eval
        //fSP = feval(fitfun, SP, CEC15_fnum);
        counteval = counteval + NP;

        //TODO record


        iSP = 1;

        V = Util.copyMatrix(X);
        U = Util.copyMatrix(X);
        S_CR = new double[NP];
        Util.assignArray(S_CR, 0);
        S_F = new double[NP];
        Util.assignArray(S_F, 0);
        S_df = new double[NP];
        Util.assignArray(S_df, 0);
        Chy = cauchyrnd(0, 0.1, NP + 10); //TODO que es esto?
        iChy = 1;
        sortidxfSP = Util.sortOnlyIndexs(fSP);


    }



    public void execute(){
        while(true) {
            boolean outofmaxfunevals = counteval > maxfunevals - NP;
            boolean outofusefunevals = counteval > usefunevals - NP;

            if (!EarlyStopOnFitness && !AutoEarlyStop && !EarlyStopOnTolFun) {
                if (outofmaxfunevals || outofusefunevals)
                    //TODO se termina
                    return;
            } else if (AutoEarlyStop) {
                reachftarget = Util.min(fx) <= ftarget;
                TolX = 10 * Math.ulp(Util.mean(X));
                solutionconvergence = Util.std(X) <= TolX;
                TolFun = 10 * Math.ulp(Util.mean(fx));
                functionvalueconvergence = Util.std(fx) <= TolFun;
                stagnation = countstagnation >= TolStagnationIteration;

                if (outofmaxfunevals || outofusefunevals || reachftarget || solutionconvergence || functionvalueconvergence || stagnation) {
                    return; //TODO se termima
                }
            } else if (EarlyStopOnFitness) {
                reachftarget = Util.min(fx) <= ftarget;

                if (outofmaxfunevals || outofusefunevals || reachftarget)
                    return; //TODO se termina
            } else if (EarlyStopOnTolFun) {
                functionvalueconvergence = Util.std(fx) <= options.TolFuun;
                if (outofmaxfunevals || outofusefunevals || functionvalueconvergence)
                    return; //TODO se termina
            }

            countiter = countiter + 1;

            int r = (int) Math.floor(1 + H * Configuration.rand.getInt(0, NP - 1));
            for (int i = 0; i < CR.length; i++)
                CR[i] = MCR[r] + (0.1 * Configuration.rand.uniform(0, NP - 1));

            for (int i = 0; i < CR.length; i++)
                if (CR[i] < 0 || MCR[r] == -1)
                    CR[i] = 0;

            for (int i = 0; i < CR.length; i++)
                if (CR[i] > 1)
                    CR[i] = 1;


            Util.assignArray(F, 0); //TODO crear F de tamaño NP
            for (int i = 0; i < NP; i++) {
                while (F[i] <= 0) {
                    F[i] = MF[rr[i]] + Chy[iChy]; //TODO chy podria ser una matriz
                    iChy = iChy % Chy.length;
                }
            }

            for (int i = 0; i < F.length; i++) { //TODO asegurarse que F no es matriz
                if (F[i] > 1) {
                    F[i] = 1;
                }
            }
            //TODO
           /* pbest = 1 + floor(max(2, round(p * NP)) * rand(1, NP));
            XA = [X, A];
            SPA = [SP, A];*/
            for (int i = 0; i < pbest.length; i++)
                pbest[i] = 1 + (int) Math.floor(Math.max(2, Math.round(p * NP)) * Configuration.rand.getInt(0, NP - 1));

            XA = Util.add(X, A);

            for (int i = 0; i < NP; i++) {
                int r1 = (int) Math.floor((NP - 1) * Configuration.rand.getFloat());
                while (i == r1)
                    r1 = (int) Math.floor((NP - 1) * Configuration.rand.getFloat());

                int r2 = (int) Math.floor((NP + nA - 1) * Configuration.rand.getFloat());
                while (i == r1 || r1 == r2) {
                    r2 = (int) Math.floor((NP + nA - 1) * Configuration.rand.getFloat());
                }

                if (FC[i] <= Q) {
                    for (int ii = 0; ii < V.length; ii++)
                        V[ii][i] = X[ii][i] + F[i] * (X[ii][pbest[i]] - X[ii][i]) + F[i] * (X[ii][r1] - XA[ii][r2]); //TODO check funciona correctamente
                } else {
                    for (int ii = 0; ii < V.length; ii++)
                        V[ii][i] = SP[ii][i] + F[i] * (SP[ii][sortidxfSP[pbest[i]]] - SP[ii][i]) + F[i] * (SP[ii][r1] - SPA[ii][r2]);
                }
            }

            if (interpolation) {
                for (int i = 0; i < NP; i++) {
                    for (int j = 0; j < D; j++) {
                        if (V[j][i] < lb) {
                            if (FC[i] <= Q) {
                                V[j][i] = 0.5 * (lb + X[j][i]);
                            } else {
                                V[j][i] = 0.5 * (lb + SP[j][i]);
                            }
                        } else if (V[j][i] > ub) {
                            if (FC[i] <= Q) {
                                V[j][i] = 0.5 * (ub + X[j][i]);
                            } else {
                                V[j][i] = 0.5 * (ub + SP[j][i]);
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
                            U[j][i] = V[j][i];
                        } else {
                            U[j][i] = X[j][i];
                        }
                    }
                } else {

                    for (int j = 0; j > D; j++) {
                        if (Configuration.rand.getFloat() < CR[i] || j == jrand) {
                            U[j][i] = V[j][i];
                        } else {
                            U[j][i] = SP[j][i];
                        }
                    }
                }
            }

            double[] fu = null;

            //TODO llamar a CEC05 benchmark

            boolean FailedIteration = true;
            int nS = 0;
            for (int i = 0; i < NP; i++) {
                if (fu[i] < fx[i]) {
                    nS = nS + 1;
                    S_CR[nS] = CR[i];
                    S_F[nS] = F[i];
                    S_df[nS] = Math.abs(fu[i] - fx[i]);
                    for (int ii = 0; ii < X.length; ii++) {
                        X[ii][i] = U[ii][i];
                    }
                    fx[i] = fu[i];

                    if (nA < Asize) {
                        for (int ii = 0; ii < A.length; ii++) {
                            A[ii][nA + 1] = X[ii][i];
                        }


                        nA = nA + 1;
                    } else {
                        int ri = (int) Math.floor(1 + Asize * Configuration.rand.getFloat());
                        for (int ii = 0; ii < A.length; ii++) {
                            A[ii][ri] = X[ii][i];
                        }
                    }

                    FailedIteration = false;
                    FC[i] = 0;
                    for (int ii = 0; ii < A.length; ii++) {
                        SP[ii][iSP] = U[ii][i];
                    }
                    fSP[iSP] = fu[i];
                    iSP = (iSP % NP) - 1;
                } else if (fu[i] == fx[i]) {
                    for (int ii = 0; ii < A.length; ii++) {
                        X[ii][i] = U[ii][i];
                    }

                    FC[i] = FC[i] + 1;
                } else {
                    FC[i] = FC[i] + 1;
                }
            }

            if (nS > 0) {
                double[] w = Util.divides(Util.range(S_df, 0, nS), Util.summatory(Util.range(S_df, 0, nS))); //TODO si S_df es de tamaño nS dejarlo asi, sino habra que hacer la sumatoria del rango (i - S_df]

                if (all(Util.range(S_CR, 0, nS))) {
                    MCR[iM] = -1;
                } else if (MCR[iM] != -1) {
                    MCR[iM] = Util.summatory(Util.multiplies(Util.multiplies(w, Util.range(S_CR, 0, nS)), Util.range(S_CR, 0, nS))) / Util.summatory(Util.multiplies(w, Util.range(S_CR, 0, nS)));
                    //MCR[iM] = Util.sum(w. * S_CR(1:nS).*S_CR(1:nS))/sum(w. * S_CR(1:nS));
                }
                MF[iM] = Util.summatory(Util.multiplies(Util.multiplies(w, Util.range(S_F, 0, nS)), Util.range(S_F, 0, nS))) / Util.summatory(Util.multiplies(w, Util.range(S_F, 0, nS)));
                //MF[iM] = sum(w. * S_F(1:nS).*S_F(1:nS))/sum(w. * S_F(1:nS));
                iM = (iM % H) - 1;
            }


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


            NP = Math.round(NPinit - (NPinit - NPmin) * counteval / maxfunevals);
            fx = Util.range(fx, 0, NP);
            for (int i = 0; i < X.length; i++)
                X[i] = Util.range(X[i], 0, NP);

            for (int i = 0; i < U.length; i++)
                U[i] = Util.range(U[i], 0, NP);

            Asize = Math.round(Ar * NP);
            if (nA > Asize) {
                nA = Asize;
                for (int i = 0; i < A.length; i++)
                    A[i] = Util.range(A[i], 0, Asize);
            }

           /* FC = Util.range(FC, 0, NP);
            int [] indexs = Util.sort(fSP);
            List<double[]> SPtmp = new ArrayList<>();
            for(int i=0; i<indexs.length; i++){
                if(indexs[i] <= NP){
                    SPtmp.add()
                }
            }*/




            /*

            double [][] SPtmp = new double[SP.length][SP[0].length];
            double [][] fSPtmp = new double[fSP.length][fSP[0].length];
            for(int i=0; i<indexs.length; i++){
                if(indexs[i] <= NP){
                    SPtmp[i] = Util.copyArray(SP[i]);
                    fSP[i] = Util.copyArray(fSP[i]);

                }
            }*/

    /*
        FC = FC(1 : NP);
        [~, sortidxfSP] = sort(fSP);
        remainingfSPidx = sortidxfSP <= NP;
        SP = SP(:, remainingfSPidx);
        fSP = fSP(:, remainingfSPidx);
        sortidxfSP = sortidxfSP(remainingfSPidx);
        iSP	= mod(iSP - 1, NP) + 1;
     */
            FC = Util.range(FC, 0, NP - 1);
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

            iSP = (iSP - 1 % NP) - 1;

            if (FailedIteration)
                countstagnation = countstagnation + 1;
            else
                countstagnation = 0;

        }
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
