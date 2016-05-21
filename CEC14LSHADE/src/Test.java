import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.benchmark;
import com.benchmark.cec.cec05.test_func;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.seeds;

/**
 * Created by framg on 23/04/2016.
 */
public class Test {
    public static void main(String [] args) {

        for (int F = 11; F <= 11; F++) {
            for (int DIM = 10; DIM <= 10; DIM += 20) {
                Configuration.records = new Records();
                for (int run = 0; run < 1; run++) {
                    Configuration.MAX_FES = 10000 * DIM;
                    Configuration.F = F;
                    Configuration.N = DIM;
                    Configuration.g_arc_rate = 2.6;
                    Configuration.epsilon = 10e-8;;
                    Rand rand = new Rand(seeds.getSeed(Configuration.F));
                    Configuration.rand = rand;
                    Configuration.benchmark = new CEC14Benchmark(DIM, F);
                   // benchmark benchmark = new benchmark();
                    //test_func aTestFunc = benchmark.testFunctionFactory(F, DIM);
                   // Configuration.benchmark = aTestFunc;
                    Configuration.pop_size = Math.round(DIM * 18);
                    Configuration.g_memory_size = 6;
                    Configuration.g_p_best_rate = 0.11;


                    LSHADE lshade = new LSHADE();
                    lshade.execute();


                }
            }
        }
    }

}
