
public class AutoConfigure {
    double [][] X;

    
    
    
    public void optimizeDE () {
        int D = Configuration.D;
        double[] lb = {
                0,        // NP - NPmin
                0.0,    // F
                0.0,    // CR
                0.0,    // ER
                0.0,    // p
                2,        // H
                1,        // Q
                1,        // Ar
                0.0,    // cw
                0.0,    // erw
                0.0,    // CRmin
                0.0,    // CRmax - CRmin
                4,        // NPmin
                0.0,    // crw
                Math.ulp(1)};    // fw


        double [] ub = {
                20 * D,    // NP - NPmin
                1.0,    // F
                1.0,    // CR
                1.0,    // ER
                1.0,    // p
                20 * D,    // H
                20 * D,    // Q
                5,        // Ar
                1.0,    // cw
                1.0,    // erw
                1.0,    // CRmin
                1.0,    // CRmax - CRmin
                20 * D,    // NPmin
                1.0,    // crw
                1.0    // fw
        };

        double [] prior1 = {
                D * 19 - 4,    // NP - NPmin
                0.5,    // F
                0.5,    // CR
                1.0,    // ER
                0.11,    // p
                6,        // H
                64,        // Q
                2.6,    // Ar
                0.3,    // cw
                0.2,    // erw
                0.05,    // CRmin
                0.25,    // CRmax - CRmin
                4,        // NPmin
                0.1,    // crw
                0.1    // fw
        };

        double [] prior2 = {
                D * 18 - 4,    // NP - NPmin
                0.5,    // F
                0.5,    // CR
                0.0,    // ER
                0.11,    // p
                6,        // H
                20 * D, // Q
                2.6,    // Ar
                0.0,    // cw
                0.0,    // erw
                0.0,    // CRmin
                1.0,    // CRmax - CRmin
                4,        // NPmin
                0.1,    // crw
                0.1    // fw
        };

        int params_n = prior1.length;
        int NP = 2 * prior1.length;
        //options.NP = NP;
        //options.initial.X = repmat(lb, 1, NP) + repmat(ub - lb, 1, NP) .* rand(numel(prior1), NP);

        double [][] initialX = new double[NP][15];
        for(int i=0; i<NP; i++){
            for(int j=0; j<params_n; j++){
                initialX[i][j] = lb[j] + ((ub[j] - lb[j]) * Configuration.rand.getDouble());
            }
        }


        initialX[0] = Util.copyArray(prior1);
        initialX[1] = Util.copyArray(prior2);

        //options.initial.X(:, 1) = prior1;
        //options.initial.X(:, 2) = prior2;
        //int Q = 1;
        //options.Q = 1;
        //options.ftarget = ftarget;
       // options.Display = 'iter';
        //options.Plotting = 'off';
        //options.EarlyStop = 'auto';
        //boolean Noise	= true;

        double best_fit = Util.inf;
        int best_index;

        for(int t=0; t<params_n; t++){
            selectConfiguration(t);
            LSHADE_SPS algorithm = new LSHADE_SPS();
            double fit = algorithm.execute();
            if(best_fit > fit){
                best_fit = fit;
                best_index = t;
            }
        }



    }

    public void selectConfiguration(int t){
        Configuration.NP = (int)Math.round(X[t][0]) + (int)Math.round(X[t][12]);
        Configuration.F = X[t][1];
        Configuration.CR = X[t][2];
        Configuration.ER  = X[t][3];
        Configuration.p = X[t][4];
        Configuration.H = (int)Math.round(X[t][5]);
        Configuration.Q = (int)Math.round(X[t][6]);
        Configuration.Ar =(int)Math.round(X[t][7]);
        Configuration.cw = X[t][8];
        Configuration.erw = X[t][9];
        Configuration.CRmin = X[t][10];
        Configuration.CRmax = X[t][10] +  X[t][11];
        Configuration.NPmin = (int)Math.round(X[t][12]);
        Configuration.crw = X[t][13];
        Configuration.fw = X[t][14];
    }



    
    
    
}
