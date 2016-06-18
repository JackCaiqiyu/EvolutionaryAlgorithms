import CMAES.CMAES;
import CMAES.Options;
import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

public class Test {
    public static void main(String [] args) {
        run("CEC05",1,1);
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
        int nProblems = finish_problem;
        int runs =1; AllBenchmarks.runs();
        Configuration.records = new Records(runs);

        for(int F = start_problem; F <= finish_problem; F++) {
            Configuration.rand = new Rand(seeds.getSeed(F));
            for(int DIM = 10; DIM <= 10; DIM += 20) {
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

                AutoConfigure autoConfigure = new AutoConfigure(DIM, F);
                autoConfigure.configure();

                Configuration.records.startRecord();
                System.out.println("FUN " + F + " DIM " + DIM);
                for(int run=0; run < runs; run++) {

                    Configuration.BIPOP = true;
                    Configuration.withModelOptimization = true;
                    Configuration.hyper_lambda = 20;
                    Configuration.iSTEPminForHyperOptimization = 1;
                    Configuration.maxStepts = 20;
                    Configuration.alpha = 0.20;
                    Configuration.iterstart = 10;


                    Configuration.DIM = DIM;
                    Configuration.F = F;
                    Configuration.max_evals = 10000 * DIM;

                    NBIPOPaCMA nbipoPaCMA = new NBIPOPaCMA();
                    nbipoPaCMA.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("GAMPC" + "-" + "P" +nProblems + "-" + benchmark);
    }



}
