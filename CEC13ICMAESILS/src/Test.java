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

        String name_benchmark = null;
        Integer start_function = null;
        Integer finish_function = null;
        for(int i=0; i<args.length; i++){
            if(args[i].equals("-sF")){
                start_function = Integer.valueOf(args[i+1]);
            }
            if(args[i].equals("-fF")){
                finish_function = Integer.valueOf(args[i+1]);
            }
            if(args[i].equals("-b")){
                name_benchmark = args[i+1];
            }
        }

        if(name_benchmark != null) {
            if (start_function == null) {
                start_function = 1;
            }
            if (finish_function == null) {
                switch (name_benchmark) {
                    case "CEC05":
                        finish_function = CEC05Benchmark.nProblems();
                        break;
                    case "CEC13":
                        finish_function = CEC13Benchmark.nProblems();
                        break;
                    case "CEC14":
                        finish_function = CEC14Benchmark.nProblems();
                        break;
                    case "CEC15":
                        finish_function = CEC15Benchmark.nProblems();
                        break;
                    default:
                        System.err.println("No benchmark avaiable.");
                        System.exit(0);
                        break;
                }
            }

            run(name_benchmark, start_function, finish_function);
        }
    }


    public static void run(String benchmark, int start_problem, int finish_problem){
        int runs = AllBenchmarks.runs();
        Configuration.records = new Records(runs);

        for(int F = start_problem; F <= finish_problem; F++) {
            Configuration.rand = new Rand(seeds.getSeed(F));
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

                    Configuration.max_fes = 10000 * DIM;

                    ICMAESILS algorithm = new ICMAESILS();
                    algorithm.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("ICMAESILS" + "-" + "P" + finish_problem + "-" + benchmark);
    }


}
