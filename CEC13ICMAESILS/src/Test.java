import cmaes.CmaesConfiguration;
import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

/**
 * Created by framg on 17/04/2016.
 */
public class Test {
    public static void main(String [] args) {

//
//        for(int F=1; F<=25; F++) {
//            for (int DIM = 10; DIM <= 50; DIM += 20) {
//                Configuration.records = new Records();
//                for(int run =0; run<1; run++) {
//                    Configuration.DIM = DIM;
//                    Configuration.F = F;
//
//                    Configuration.tunea = 9.687;
//                    Configuration.tuneb = 1.614;
//                    Configuration.tunec = 0.6825;
//                    Configuration.tuned = 3.245;
//                    Configuration.tunee = -9.023;
//                    Configuration.tunef = -10.82;
//                    Configuration.tuneg = -16.26;
//                    Configuration.getlearn_perbudget = 0.15;
//                    Configuration.getmtsls1_iterbias_choice = 0.01910;
//                    Configuration.getmtsls1_initstep_rate = 0.6703;
//                    Configuration.getmtsls1per_ratedim = 1;
//
//
//                    Configuration.seed = seeds.getSeed(Configuration.F);
//                    Rand rand = new Rand(seeds.getSeed(Configuration.F));
//                    Configuration.rand = rand;
//                    benchmark benchmark = new benchmark();
//                    test_func test_func = benchmark.testFunctionFactory(Configuration.F, Configuration.DIM);
//                    Configuration.benchmark = test_func;
//                    System.out.println("Fun: "+ F + "Run: " + run + "DIM: " + DIM);
//                    ICMAESILS algorithm = new ICMAESILS();
//                    algorithm.execute();
//                }
//                Configuration.records.write(DIM, F, "ICMAESILS", false);
//            }
//        }

        run("CEC13");
    }


    public static void run(String benchmark){
        int nProblems = 0;
        int runs = AllBenchmarks.runs();
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

//nProblems
        for(int F = 8; F <= 8; F++) {
            for(int DIM = 30; DIM <= 30; DIM += 20) {
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
                for(int run=0; run < 1; run++) {
                    Configuration.DIM = DIM;
                    Configuration.F = F;

                    CmaesConfiguration.tunea = 9.687;
                    CmaesConfiguration.tuneb = 1.614;

                    Configuration.tunec = 0.6825;
                    Configuration.tuned = 3.245;

                    CmaesConfiguration.tunee = -9.023;
                    CmaesConfiguration.tunef = -10.82;
                    CmaesConfiguration.tuneg = -16.26;
                    Configuration.getlearn_perbudget = 0.15;
                    Configuration.getmtsls1_iterbias_choice = 0.01910;
                    Configuration.getmtsls1_initstep_rate = 0.6703;
                    Configuration.getmtsls1per_ratedim = 1;





                    Configuration.benchmark = new CEC13Benchmark(DIM, F);
                   // Configuration.rand = new Rand(seeds.getSeed(F));
                    Configuration.rand = new Rand();
                    Configuration.max_fes = 10000 * DIM;

                    ICMAESILS algorithm = new ICMAESILS();
                    algorithm.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("ICMAESILS" + "-" + benchmark);
    }


}
