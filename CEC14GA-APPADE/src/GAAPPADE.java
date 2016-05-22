import java.util.ArrayList;
import java.util.List;

/**
 * Created by framg on 21/05/2016.
 */
public class GAAPPADE {
    int I_nfeval, NP, NPP, ND, D, condMax;
    double rm, condC;
    double [] vall, mu;
    double [][] popl, Q;
    double gbest;

    double p;

    public GAAPPADE(){
        D = Configuration.D;
        NP = 100;
        NPP = 20 * D;
        p = 0.05;
        ND = 2;
        rm = 1/Math.exp(1);
        condMax = 2;
        condC = 1;

        Q = Matlab.eye(ND);

        mu = new double[2];
        mu[0] = 0.5;
        mu[1] = 0.5;

        popl = popInitialize(Configuration.FVr_minbound, Configuration.FVr_maxbound);
        vall = new double[NPP];
        for(int i=0; i<NPP; i++){
            vall[i] = Configuration.benchmark.f(popl[i]);
        }
        I_nfeval= NPP;
        gbest = Util.min(vall);
    }

    public void execute(){



        while (I_nfeval < Configuration.max_fes && Math.abs(Configuration.benchmark.bias() - gbest) > Configuration.benchmark.objective()){
            double [] r = new double[NP];
            int [] app;

            for(int i=0; i<NP; i++){
                r[i] = rm + 0.1 * Configuration.random.gaussian();
                if(r[i] < 10e-50){
                    r[i] = 10e-50;
                }
            }
            if(Statistics.mean(vall) <= Statistics.median(vall)){
                int ts = 5;
                int [] wins = new int[NPP];
                Util.assignArray(wins, 0);

//                int [] mem = new int[ts];
//                for(int i=0; i<ts; i++){
//                    mem[i] = Configuration.random.getInt(0, NPP-1);
//                }

                for(int i=0; i<NPP; i++){
                    for(int k=0; k<ts; k++){
                        int mem = Configuration.random.getInt(0, NPP-1);
                        if(vall[i] < vall[mem]){
                            wins[i]++;
                        }
                    }

                }
                app = Util.sortDescend(wins);

            }else{
                app = Configuration.random.randperm(NPP);
            }
            double [][] pop = Util.copyMatrix(popl);
            pop = Util.sortByIndexs(pop, app);
            pop = Util.range(pop, 0, NP);

            double [] valParents = Util.copyArray(vall);
            valParents = Util.sortByIndexs(valParents, app);
            valParents = Util.range(valParents, 0, NP);

            int [] indBest = Util.sortOnlyIndexs(valParents);
            double [] valBest = Util.sortNewArray(valParents);


            double [][] arz = new double[NP][ND];
            for(int i=0; i<NP; i++){
                for(int j=0; j<ND; j++){
                    arz[i][j] = Configuration.random.gaussian();
                }
            }

//            double [][] part1 = new double[NP][ND];
//            for(int i=0; i<NP; i++){
//                for(int j=0; j<ND; j++){
//                    part1[i][j] = mu[j] + r[i];
//                }
//            }

            //double [][] arx = Matrix.multipliesElementByElement(part1, Matrix.multiplies(Q, Matrix.transpose(arz)));
            double [][] arx = Matrix.add(Util.duplicateArray(mu, NP), Matrix.multipliesElementByElement(Matrix.transpose(Util.duplicateArray(r, ND)) ,Matrix.multiplies(Q, Matrix.transpose(arz))));
           // arx = Matrix.transpose(arx);
            for(int i=0; i<NP; i++){
                if(arx[0][i] <= 0){
                    arx[0][i] = 0.001;
                }

                if(arx[0][i] > 1){
                    arx[0][i] = 1;
                }

                if(arx[1][i] <= 0){
                    arx[1][i] = 0;
                }

                if(arx[1][i] > 1){
                    arx[1][i] = 1;
                }
            }

            double [] F = Util.copyArray(arx[0]);
            double [] CR = Util.copyArray(arx[1]);

            int [] r0 = new int[NP];
            int [][] rTmp = gnR1R2(NP, null, r0);
            int [] r1 = Util.copyArray(rTmp[0]);
            int [] r2 = Util.copyArray(rTmp[1]);

            int pNP = Math.max((int)Math.round(p*NP), 2);
            int [] randindex = new int[NP];
            for(int i=0; i<NP; i++){
                randindex[i] = (int)Math.ceil(Configuration.random.getDouble() * pNP);
                if(randindex[i] < 0){
                    randindex[i] = 0;
                }
            }

            double [][] pbest = new double[NP][];
            for(int i=0; i<NP; i++){
                pbest[i] = Util.copyArray(pop[indBest[randindex[i]]]);
            }

            double [][] popR1 = Util.sortByIndexs(pop, r1);
            double [][] popR2 = Util.sortByIndexs(pop, r2);
            double [][] vipart1 = Matrix.add(Matrix.sub(pbest, pop), Matrix.sub(popR1, popR2));
            double [][] vipart2 = Matrix.multipliesElementByElement(vipart1, Util.duplicateArray(F, D));
            double [][] vi = Util.addMatrix(pop, vipart2);

            for(int i=0; i<NP; i++){
                for(int j=0; j<D; j++){
                    if(vi[i][j] < Configuration.benchmark.lbound()){
                        vi[i][j] = Configuration.benchmark.lbound();
                    }
                    if(vi[i][j] > Configuration.benchmark.ubound()){
                        vi[i][j] = Configuration.benchmark.ubound();
                    }
                }
            }

            boolean [][] mask = new boolean[NP][D];
            for(int i=0; i<NP; i++){
                for(int j=0; j<D; j++){
                    mask[i][j] = Configuration.random.getDouble() > CR[i];
                }
            }
            int [] rows = new int[NP];
            int [] cols = new int[NP];

            for(int i=0; i<NP; i++){
                rows[i] = i;
                cols[i] = (int)Math.floor(Configuration.random.getDouble() * (D-1));
            }

            setMaskJrand(rows, cols, mask);

            double [][] ui = Util.copyMatrix(vi);
            for(int i=0; i<NP; i++){
                for(int j=0; j<D; j++){
                    if(mask[i][j] == true){
                        ui[i][j] = pop[i][j];
                    }
                }
            }


            double [] valOffSpring = new double[NP];
            for(int i=0; i<NP; i++){
                valOffSpring[i] = Configuration.benchmark.f(ui[i]);
            }
            I_nfeval += NP;

            double [] imp = new double[NP];
            for(int i=0; i<NP; i++){
                imp[i] = valParents[i] - valOffSpring[i];
            }

            int [] I = new int[NP];
            for(int i=0; i<NP; i++){
                if(valParents[i] > valOffSpring[i]){
                    valParents[i] = valOffSpring[i];
                    I[i] = 2;
                }else{
                    I[i] = 1;
                }

            }

            for(int i=0; i<NP; i++){
                if(I[i] == 2){
                    pop[i] = Util.copyArray(ui[i]);
                }
            }

            int cd = -1; //Worst value
            double ac = 0;
            for(int i=0; i<NP; i++){
                if(I[i] == 2){
                    if(ac < imp[i]){
                        ac = imp[i];
                        cd = i;
                    }
                }
            }


            List<Double> gimpTmp = new ArrayList<>();
            List<Double> goodrTmp = new ArrayList<>();

            for(int i=0; i<NP; i++){
                if(I[i] == 2){
                    gimpTmp.add(imp[i]);
                    goodrTmp.add(r[i]);
                }
            }

            double [] gimp = Util.listToArrayDouble(gimpTmp);
            gimp = Util.divideArray(gimp, Util.summatory(gimp));
            double [] goodr =Util.listToArrayDouble(goodrTmp);
//            boolean goodrIsEmpty = true;
//            for(int i=0; i<NP; i++){
//                if(I[i] == 2){
//                    goodr[i] = r[i];
//                    goodrIsEmpty = false;
//                }
//            }
            for(int i=0; i <NP; i++){
                popl[app[i]] = Util.copyArray(pop[i]);
            }
            for(int i=0; i<NP; i++){
                vall[app[i]] = valParents[i];
            }

            double c = 0.1;
            gbest = Util.min(vall);
            Configuration.records.newRecord(Math.abs(Configuration.benchmark.bias() - gbest), I_nfeval);
           //// System.out.println("Eval: " + gbest + " at: " + I_nfeval);

            if (!goodrTmp.isEmpty()){
                rm = (1-c)* rm +  c*Util.summatory(Matrix.multipliesElementByElement(gimp, goodr));
            }

            if(containsI2(I)){
                int l = cd;
                mu = Matrix.add(Matrix.multipliesByElement(mu, (1-c)), Matrix.multipliesByElement(Matrix.transpose(arx)[l], c));
                if(condC < condMax){
                    double [][] deltaC = Matrix.add(Matrix.multipliesByElement(Matlab.eye(ND), 1-c), Matrix.multipliesByElement(Matrix.multiplies_nx1x1xn(arz[l], arz[l]), c));
                    deltaC = Matrix.add(Matlab.triu(deltaC, 0), Matrix.transpose(Matlab.triu(deltaC, 1)));
                    double [][] eigVals = Matlab.eigD(deltaC);
                    double [][] B = Matlab.eigV(deltaC);
                    double [][] deltaQ = Matrix.multiplies(Matrix.multiplies(B ,Matlab.diag(Util.sqrt(Matlab.diag(eigVals)))), Matrix.transpose(B)); //TODO cchek si necesita traspose o no
                    Q = Matrix.multiplies(Q, deltaQ);

                }
            }else{
                Q = Matrix.add(Q, Matrix.multipliesByElement(Matlab.eye(ND), 1/ND));
            }

            double detQ = Matlab.det(Q);

            Q = Matrix.dividesByElement(Q, Math.pow(detQ, 1/ND));
            double [][] eigVals = Matlab.eigD(Matrix.multiplies(Q, Matrix.transpose(Q)));

            condC = Util.max(Matlab.diag(eigVals)) / Util.min(Matlab.diag(eigVals));

        }
        Configuration.records.endRun(Math.abs(Configuration.benchmark.bias() - Util.min(vall)), I_nfeval, Configuration.max_fes);
        System.out.println("Eval: " + Math.abs(Configuration.benchmark.bias() - Util.min(vall)) + " at: " + I_nfeval);

    }

    private boolean containsI2(int [] I){
        for(int i=0; i<I.length; i++){
            if(I[i] == 2){
                return true;
            }
        }
        return false;
    }


    private int [][] gnR1R2(Integer NP1, Integer NP2, int [] r0){
        int [][] r = new int[2][r0.length];
        int NP0 = r0.length;
        int [] r1 = new int[NP0];
        int [] r2 = new int[NP0];
        if(NP2 == null){
            NP2 = NP1;
        }

        for(int i=0; i<NP0; i++){
            r1[i] = (int)Math.floor(Configuration.random.getDouble() * (NP1-1));
        }

        for (int i=0; i<99999999; i++){
            List<Integer> pos = new ArrayList<>();
            int sum = 0;
            for(int j=0; j<NP0; j++){
                if(r1[j] == r0[j]){
                    sum++;
                    pos.add(j);
                }
            }
            if(sum == 0){
                break;
            }else{
                for(Integer j : pos){
                    r1[j] = (int)Math.floor(Configuration.random.getDouble() * (NP1-1));
                }
            }
            if( i > 1000){
                System.exit(0);
            }

        }

        for(int i=0; i<NP0; i++){
            r2[i] = (int)Math.floor(Configuration.random.getDouble() * (NP2-1));
        }

        for (int i=0; i<99999999; i++){
            List<Integer> pos = new ArrayList<>();
            int sum = 0;
            for(int j=0; j<NP0; j++){
                if((r2[j] == r0[j])||(r2[j] == r1[j])){
                    sum++;
                    pos.add(j);
                }
            }
            if(sum == 0){
                break;
            }else{

                for(Integer j : pos){
                    r2[j] = (int)Math.floor(Configuration.random.getDouble() * (NP2-1));
                }
            }
            if( i > 1000){
                System.exit(0);
            }

        }

        r[0] = Util.copyArray(r1);
        r[1] = Util.copyArray(r2);
        return r;
    }

    private void setMaskJrand(int [] rows, int [] cols, boolean[][] mask){
        for(int i=0; i<rows.length; i++){
            mask[rows[i]][cols[i]] = false;
        }
    }

    private double [][] popInitialize(double LB, double UB){
        double radBound = (UB - LB)/2;
        double cenBound = (UB + LB)/2;
        double [][] pop = new double[NPP][D];

        for(int i=0; i<NPP; i++){
            for(int j=0; j<D; j++){
                pop[i][j] = radBound * (2*Configuration.random.getDouble()-1);
                pop[i][j] += cenBound;
            }
        }
        return pop;
    }

}
