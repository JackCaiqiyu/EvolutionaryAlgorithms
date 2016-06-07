import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;

public class Configuration {
    public static int popSize= 90;
    public static int max_eval;
    public static float p = 0.1f; // the used randomized operator probablity
    public static int dim;
    public static int I_fno;
    public static Rand rand;
    public static double Ter_Err = 10e-8;
    public static Records records;
    public static AllBenchmarks benchmark;
}
