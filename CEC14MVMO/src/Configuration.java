import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;

/**
 * Created by framg on 27/04/2016.
 */
public class Configuration {
    public static Rand rand;
    public static AllBenchmarks benchmark;
    public static double PPL = 1e-50;
    public static Records records;
    public static int DIM;
    public static int max_fes;


    public static int n_par;
    public static int n_tosave;
    public static double fs_factor_start;
    public static double fs_factor_end;
    public static double delta_Shape_dyn;
    public static int local_prob;
    public static double min_eval_LS;
    public static double max_eval_LS;
    public static double ratio_gute_max;
    public static double ratio_gute_min;
    public static int n_random_ini;
    public static int n_random_last;
    public static int local_max;
    public static double shape_ini;
    public static double shape_dyn_ini;
    public static double value_ini;
    public static int r_select;
    public static int mappingST;
}
