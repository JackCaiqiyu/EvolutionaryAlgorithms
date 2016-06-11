import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec05.benchmark;
import com.benchmark.cec.cec05.test_func;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

/**
 * Created by framg on 23/04/2016.
 */
public class Test {
    public static void main(String [] args) {


        //run("CEC05",20,CEC05Benchmark.nProblems());
        //run("CEC13",21, 28);
       // run("CEC14",21,30);
        run("CEC15",11,15);
    }

    public static void run(String benchmark, int start_problem, int finish_problem){
        int nProblems = finish_problem;
        int runs = AllBenchmarks.runs();
        Configuration.records = new Records(runs);

        for(int F = start_problem; F <= finish_problem; F++) {
            for(int DIM = 10; DIM <= 50; DIM += 20) {
                switch (benchmark) {
                    case "CEC05":
                        Configuration.benchmark = new CEC05Benchmark(DIM, F);
                        break;
                    case "CEC13":
                        Configuration.benchmark = new CEC13Benchmark(DIM, F);
                        break;
                    case "CEC14":
                        Configuration.benchmark = new CEC14Benchmark(DIM, F);
                        break;
                    case "CEC15":
                        Configuration.benchmark = new CEC15Benchmark(DIM, F);
                        break;
                    default:
                        System.err.println("No benchmark avaiable.");
                        System.exit(0);
                        break;
                }

                Configuration.records.startRecord();
                System.out.println("FUN " + F + " DIM " + DIM);
                for(int run=0; run < runs; run++) {
                    Configuration.MAX_FES = 10000 * DIM;
                    Configuration.F = F;
                    Configuration.N = DIM;
                    Configuration.g_arc_rate = 2.6;
                    Configuration.epsilon = 10e-8;;
                    Rand rand = new Rand();
                    Configuration.rand = rand;
                    Configuration.pop_size = Math.round(DIM * 18);
                    Configuration.g_memory_size = 6;
                    Configuration.g_p_best_rate = 0.11;


                    LSHADE lshade = new LSHADE();
                    lshade.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("LSHADE" + "-" + "P" +nProblems + "-" + benchmark);
    }

}
