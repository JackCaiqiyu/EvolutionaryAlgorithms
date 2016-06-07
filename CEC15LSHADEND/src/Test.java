import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

/**
 * Created by framg on 11/04/2016.
 */
public class Test {
    public static void main(String [] args) {

        run("CEC05",1,CEC05Benchmark.nProblems());
        run("CEC13",1,CEC13Benchmark.nProblems());
        run("CEC14",1,CEC14Benchmark.nProblems());
        run("CEC15",1,CEC15Benchmark.nProblems());

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
                    Configuration.D = DIM;
                    Configuration.nF = F;
                    Configuration.max_nfes = 10000 * DIM;
                    Configuration.rand = new Rand(seeds.getSeed(Configuration.nF));
                    LSHADEND algorithm = new LSHADEND();
                    algorithm.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("LSHADEN" + "-" + "P" +nProblems + "-" + benchmark);
    }
}
