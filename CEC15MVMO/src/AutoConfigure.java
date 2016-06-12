import com.benchmark.Rand;

/**
 * Created by framg on 09/06/2016.
 */
public class AutoConfigure {
    double [] lb ={40, 5, 0.01, 2, 0.1 ,0, 0.05, 1 ,0.4, 0.1, 2, 1, 1, 0, 0.1, 0.09, 1, 1};
    double [] ub ={300, 15, 1, 65, 0.1 ,100, 0.95, 1,1, 0.95, 9, 5, 22500, 350, 350, 2.95, 2, 2};
    double [][] X;

    int DIM;
    int F;

    public AutoConfigure(int DIM, int F){
        this.DIM = DIM;
        this.F = F;
    }

    public void auto_configure(){
        //Configuration.rand = new Rand();
        int best = optimizeDE();
        selectConfiguration(best);
    }

    private int optimizeDE () {

        Configuration.DIM = DIM;
        int D = Configuration.DIM;

        int params_n =  lb.length;
        int NP = 2 * lb.length;

        X = new double[NP][params_n];
        for(int i=0; i<NP; i++){
            for(int j=0; j<params_n; j++){
                X[i][j] = lb[j] + ((ub[j] - lb[j]) * Configuration.rand.getDouble());
            }
        }



        double best_fit = 1e200;
        int best_index = 0;

//        switch (D){
//            case 10:
//                Configuration.max_fes = 6000;
//                break;
//            case 30:
//                Configuration.max_fes = 2000;
//                break;
//            case 50:
//                Configuration.max_fes = 1200;
//                break;
//            default:
//                System.err.print("Not valid dimension.");
//                System.exit(0);
//        }


        //for(int t=0; t<params_n; t++){
        for(int t=0; t<10; t++){
            selectConfiguration(t);
            MVMO algorithm = new MVMO();
            double fit = algorithm.execute();
            if(best_fit > fit){
                best_fit = fit;
                best_index = t;
            }
        }

        return best_index;

    }


    private void selectConfiguration(int t){
        Configuration.n_par = (int)Math.round(X[t][0]);
        Configuration.n_tosave = (int)Math.round(X[t][1]);
        Configuration.fs_factor_start = X[t][2];
        Configuration.fs_factor_end= X[t][3];
        Configuration.delta_Shape_dyn = X[t][4];
        Configuration.local_prob = (int)Math.round(X[t][5]);
        Configuration.min_eval_LS = Math.round(X[t][6] * Configuration.max_fes);
        Configuration.max_eval_LS = Math.round(X[t][7] * Configuration.max_fes) ;
        Configuration.ratio_gute_max = X[t][8];
        Configuration.ratio_gute_min = X[t][9];;
        Configuration.n_random_ini = (int)Math.round(X[t][10]);
        Configuration.n_random_last = (int)Math.round(X[t][11]);
        Configuration.local_max = (int)Math.round(X[t][12]);
        Configuration.shape_ini = X[t][13];;
        Configuration.shape_dyn_ini = X[t][14];;
        Configuration.value_ini = X[t][15];;
        Configuration.r_select = (int)Math.round(X[t][16]);
        Configuration.mappingST = (int)Math.round(X[t][17]);
    }




}
