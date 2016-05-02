/**
 * Created by framg on 27/04/2016.
 */
public class MVMO {
    double [] x_normalized;


    public void execute(){
        boolean yes_n_randomly;
        boolean yes_fs_factor;
        double ratio_gute_max;
        double ratio_gute_min;
        int n_par_last;
        int n_par;
        int n_eval;
        int n_randomly_ini;
        int n_randomly_last;
        double delta_nrandomly;

        double fs_factor_start;
        double fs_factor_end;

        while(true){
            for(int ipp=0; ipp<parameter.n_par; ipp++){
                double ff = proc.i_eval / n_eval;
                double ff2 = ff*ff;
                double one_minus_ff2 = 1.0 - ff2*0.99;
                double streu = one_minus_ff2*0.5;

                double border_gute0 = ratio_gute_max-ff*(ratio_gute_max-ratio_gute_min);
                int border_gute = Math.round(n_par_last * border_gute0);
                if(border_gute < 3 || n_par-border_gute < 1){
                    border_gute = n_par;
                }

                int n_randomly_X;
                int n_randomly;
                if(yes_n_randomly){
                    n_randomly_X = Math.round(n_randomly_ini-ff*delta_nrandomly);
                    n_randomly = Math.round(n_randomly_last + Configuration.rand.getFloat() * (n_randomly_X-n_randomly_last));
                }

                double fs_factor0;
                if(yes_fs_factor){
                    fs_factor0 = fs_factor_start + ff*(fs_factor_end - fs_factor_start);
                }


                x_normalized[ipp] = Util.addArray(Util.multiplyArray(x_normalized[ipp], parameter.scaling), ps.x_min);



            }
        }
    }

}
