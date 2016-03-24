public class LSHADE_SPS {
    int counteval, maxfunevals, NP, usefunevals, D;

    int H;

    boolean EarlyStopOnFitness, AutoEarlyStop, EarlyStopOnTolFun;

    boolean reachftarget, solutionconvergence, functionvalueconvergence, stagnation, interpolation;

    double ftarget;
    int countstagnation, NPinit, NPmin;
    double TolX, TolFun, TolStagnationIteration;

    double [] fx, MCR, F, MF, Chy, FC, lb, ub, CR, S_CR, S_F, S_df;
    int [] rr;
    int iChy, Ar;
    int countiter, iM;
    int nA, Q, Asize, iSP;
    double [][] X, V, XA, SP, SPA, U, A;
    int [] pbest;

    int p;

    public void execute(){
        boolean outofmaxfunevals = counteval > maxfunevals - NP;
        boolean outofusefunevals = counteval > usefunevals - NP;

        if(!EarlyStopOnFitness && !AutoEarlyStop && !EarlyStopOnTolFun){
            if(outofmaxfunevals || outofusefunevals)
                //TODO se termina
                return;
        }else if(AutoEarlyStop){
            reachftarget = Util.min(fx) <= ftarget;
            TolX = 10 * Math.ulp(Util.mean(X));
            solutionconvergence = Util.std(X) <= TolX;
            TolFun = 10 * Math.ulp(Util.mean(fx));
            functionvalueconvergence = Util.std(fx) <= TolFun;
            stagnation = countstagnation >= TolStagnationIteration;

            if (outofmaxfunevals || outofusefunevals || reachftarget || solutionconvergence || functionvalueconvergence || stagnation) {
                return; //TODO se termima
            }
        }else if(EarlyStopOnFitness){
            reachftarget = Util.min(fx) <= ftarget;

            if (outofmaxfunevals ||outofusefunevals || reachftarget)
                return; //TODO se termina
        }else if(EarlyStopOnTolFun){
            functionvalueconvergence  = Util.std(fx) <= options.TolFuun;
            if (outofmaxfunevals || outofusefunevals || functionvalueconvergence)
                return; //TODO se termina
        }

        countiter = countiter + 1;

        int r = (int) Math.floor(1 + H * Configuration.rand.getInt(0, NP-1));
        for(int i=0; i<CR.length; i++)
            CR[i] = MCR[r] + (0.1 * Configuration.rand.uniform(0, NP-1));

        for(int i=0; i<CR.length; i++)
            if(CR[i] < 0 || MCR[r] == -1)
                CR[i] = 0;

        for(int i=0; i<CR.length; i++)
            if(CR[i] > 1)
                CR[i] = 1;


        Util.assignArray(F, 0); //TODO crear F de tamaño NP
        for(int i=0; i<NP; i++){
            while(F[i] <= 0){
                F[i] = MF[rr[i]] + Chy[iChy]; //TODO chy podria ser una matriz
                iChy = iChy % Chy.length;
            }
        }

        for(int i=0; i<F.length; i++){ //TODO asegurarse que F no es matriz
            if(F[i] > 1){
                F[i] = 1;
            }
        }
        //TODO
       /* pbest = 1 + floor(max(2, round(p * NP)) * rand(1, NP));
        XA = [X, A];
        SPA = [SP, A];*/
        for(int i=0; i<pbest.length; i++)
            pbest[i] = 1 + (int) Math.floor(Math.max(2, Math.round(p * NP)) * Configuration.rand.getInt(0, NP-1));

        XA = Util.add(X, A);

        for(int i=0; i<NP; i++){
            int r1 = (int) Math.floor((NP - 1) * Configuration.rand.getFloat());
            while (i == r1)
                r1 = (int) Math.floor((NP - 1) * Configuration.rand.getFloat());

            int r2 = (int) Math.floor((NP + nA - 1) * Configuration.rand.getFloat());
            while (i == r1 || r1 == r2){
                r2 = (int) Math.floor((NP + nA - 1) * Configuration.rand.getFloat());
            }

            if(FC[i] <= Q){
                for(int ii = 0; ii< V.length; ii++)
                    V[ii][i] = X[ii][i] + F[i] * (X[ii][pbest[i]] - X[ii][i]) + F[i] * (X[ii][r1] - XA[ii] [r2]); //TODO check funciona correctamente
            }else{
                for(int ii = 0; ii< V.length; ii++)
                    V[ii][i] = SP[ii][i] + F[i] * (SP[ii][sortidxfSP[pbest[i]]] - SP[ii][i]) + F[i] * (SP[ii][r1] - SPA[ii][r2]);
            }
        }

        if (interpolation) {
            for (int i=0; i<NP; i++){
                for (int j=0; j<D; j++){
                    if (V[j] [i] < lb[j]){
                        if (FC[i] <= Q){
                            V[j] [i] = 0.5 * (lb[j] + X[j] [i)]);
                        }else {
                            V[j][i] = 0.5 * (lb[j] + SP[j][i]);
                        }
                    }else if (V [j] [i] >ub[j]) {
                        if (FC[i] <= Q) {
                            V[j][i] = 0.5 * (ub[j] + X[j][i]);
                        } else {
                            V[j][i] = 0.5 * (ub[j] + SP[j][i]);
                        }
                    }
                }
            }
        }

        for (int i=0; i<NP; i++) {
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

        double [] fu;
        double [][] fSP;
        //TODO llamar a CEC05 benchmark

        boolean FailedIteration = true;
        int nS = 0;
        for (int i=0; i<NP; i++) {
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
            double [] w = Util.divides(Util.range(S_df, 0, nS) , Util.summatory(Util.range(S_df,0,nS))); //TODO si S_df es de tamaño nS dejarlo asi, sino habra que hacer la sumatoria del rango (i - S_df]

            if (all(Util.range(S_CR,0,nS))) {
                MCR[iM] = -1;
            }else if (MCR [iM] != -1) {
                MCR[iM] = Util.summatory(Util.multiplies(Util.multiplies(w, Util.range(S_CR, 0, nS)), Util.range(S_CR, 0, nS)))/ Util.summatory(Util.multiplies(w, Util.range(S_CR, 0, nS)));
                //MCR[iM] = Util.sum(w. * S_CR(1:nS).*S_CR(1:nS))/sum(w. * S_CR(1:nS));
            }
            MF[iM] = Util.summatory(Util.multiplies(Util.multiplies(w, Util.range(S_F, 0, nS)), Util.range(S_F, 0, nS)))/ Util.summatory(Util.multiplies(w, Util.range(S_F, 0, nS)));
            //MF[iM] = sum(w. * S_F(1:nS).*S_F(1:nS))/sum(w. * S_F(1:nS));
            iM = (iM % H) - 1;
        }




        for(int i=0; i<fx.length; i++){
            for(int j=i+1; j<fx.length; j++){
                if(fx[i] > fx[j]){
                    double tmp = fx[i];
                    fx[i] = fx[j];
                    fx[j] = tmp;

                    double [] aTmp = Util.copyArray(X[i]);
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
        for(int i=0; i<X.length; i++)
            X[i] = Util.range(X[i], 0, NP);

        for(int i=0; i<U.length; i++)
            U[i] = Util.range(U[i], 0 , NP);

        Asize = Math.round(Ar * NP);
        if (nA > Asize) {
            nA = Asize;
            for(int i=0; i<A.length; i++)
                A[i] = Util.range(A[i], 0, Asize);
        }

        FC = Util.range(FC, 0, NP);
        //[~, sortidxfSP] = sort(fSP); //TODO
        int [] indexs = Util.sort(fSP);
        double [][] SPtmp = new double[SP.length][SP[0].length];
        double [][] fSPtmp = new double[fSP.length][fSP[0].length];
        for(int i=0; i<indexs.length; i++){
            if(indexs[i] <= NP){
                SPtmp[i] = Util.copyArray(SP[i]);
                fSP[i] = Util.copyArray(fSP[i]);

            }
        }



        remainingfSPidx = sortidxfSP <= NP;
        SP = SP(:, remainingfSPidx);
        fSP = fSP(:, remainingfSPidx);
        sortidxfSP = sortidxfSP(remainingfSPidx);
        iSP	= (iSP - 1 % NP) - 1;

        if (FailedIteration)
            countstagnation = countstagnation + 1;
        else
            countstagnation = 0;


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
