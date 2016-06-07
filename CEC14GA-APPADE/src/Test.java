import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

/**
 * Created by framg on 21/05/2016.
 */
public class Test {
    public static void main(String [] args) {
        run("CEC05");
       // run("CEC13");
      //  run("CEC14");
      //  run("CEC15");

    }


    public static void run(String benchmark){
        int nProblems = 0;
        int runs = 25;// AllBenchmarks.runs();
        Configuration.records = new Records(runs);

        switch (benchmark){
            case "CEC05":
                nProblems = CEC05Benchmark.nProblems();
                break;
            case "CEC13":
                nProblems = CEC13Benchmark.nProblems();
                break;
            case "CEC14":
                nProblems = CEC14Benchmark.nProblems();
                break;
            case "CEC15":
                nProblems = CEC15Benchmark.nProblems();
                break;
            default:
                System.err.println("No benchmark avaiable.");
                System.exit(0);
                break;
        }


        for(int F = 6; F <= 6; F++) {
            for(int DIM = 10; DIM <= 10; DIM += 20) {
                switch (benchmark) {
                    case "CEC05":
                        Configuration.benchmark = new CEC05Benchmark(DIM, F);
                        nProblems = CEC05Benchmark.nProblems();
                        break;
                    case "CEC13":
                        Configuration.benchmark = new CEC13Benchmark(DIM, F);
                        nProblems = CEC13Benchmark.nProblems();
                        break;
                    case "CEC14":
                        Configuration.benchmark = new CEC14Benchmark(DIM, F);
                        nProblems = CEC14Benchmark.nProblems();
                        break;
                    case "CEC15":
                        Configuration.benchmark = new CEC15Benchmark(DIM, F);
                        nProblems = CEC15Benchmark.nProblems();
                        break;
                    default:
                        System.err.println("No benchmark avaiable.");
                        System.exit(0);
                        break;
                }

                Configuration.records.startRecord();
                System.out.println("FUN " + F + " DIM " + DIM);
                for(int run=0; run < runs; run++) {
                    Configuration.benchmark = new CEC14Benchmark(DIM, F);
                    Configuration.random = new Rand(seeds.getSeed(F));
                    Configuration.max_fes = 10000 * DIM;
                    Configuration.FVr_minbound = Configuration.benchmark.lbound();
                    Configuration.FVr_maxbound = Configuration.benchmark.ubound();
                    Configuration.D = DIM;
                    GAAPPADE gaappade = new GAAPPADE();
                    gaappade.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("GAAPPADE" + "-" + benchmark);
    }


}
