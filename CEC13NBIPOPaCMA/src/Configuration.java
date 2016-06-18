import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;

public class Configuration {
    public static int max_evals;
    public static int MaxEvalsWithSurrogate;
    public static boolean newRestartRules;
    public static boolean BIPOP;
    public static boolean noisy;
   // public static int popSize;
    public static Rand rand = new Rand();
    public static int iSTEPminForHyperOptimization;
    public static int hyper_lambda;
    public static double alpha;
    public static double maxerr;
    public static int maxStepts;
    public static boolean CMAactive;
    public static boolean withSurr;
    public static boolean withFileDisp;
    public static boolean withModelOptimization;
    public static boolean withDisp;
    public static int iterstart;
    public static     int iGlobalRun;
    //public static int MaxIter;

    public static Xcmaes.MyStat myStat;

    public static long seed;
    public static double Ter_err;
    public static int DIM;
    public static int F;
    public static AllBenchmarks benchmark;
    public static Records records;

    public static int MaxTrainingPoints;
}
