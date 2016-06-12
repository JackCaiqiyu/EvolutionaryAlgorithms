import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;

/**
 * Created by framg on 17/03/2016.
 */
public class Test {
    public static void main(String [] args) {
//        run("CEC05",1,CEC05Benchmark.nProblems());
//        run("CEC13",1,CEC13Benchmark.nProblems());
//        run("CEC14",1,CEC14Benchmark.nProblems());
        run("CEC15",1,1);


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
        int runs = 1;AllBenchmarks.runs();
        Configuration.records = new Records(runs);

        for(int F = start_problem; F <= finish_problem; F++) {
            Configuration.rand = new Rand();
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
                Configuration.DIM = DIM;
                Configuration.max_fes = 10000 * DIM;
                AutoConfigure autoConfigure = new AutoConfigure(DIM, F);
                autoConfigure.auto_configure();
                Configuration.max_fes = 10000 * DIM;
//                Configuration.n_par = 150;
//                Configuration.n_tosave = 15;
//                Configuration.fs_factor_start = 1;
//                Configuration.fs_factor_end= 25;
//                Configuration.delta_Shape_dyn = 0.1;
//                Configuration.local_prob = 10;
//                Configuration.min_eval_LS = Math.round(0.05 * 10000 * DIM);
//                Configuration.max_eval_LS = Math.round(1.00 * 10000 * DIM) ;
//                Configuration.ratio_gute_max = 0.8;
//                Configuration.ratio_gute_min = 0.3;;
//                Configuration.n_random_ini = 6;
//                Configuration.n_random_last = 2;
//                Configuration.local_max = 10000;
//                Configuration.shape_ini = 350;;
//                Configuration.shape_dyn_ini = 350;
//                Configuration.value_ini = 0.9;
//                Configuration.r_select = 1;
//                Configuration.mappingST = 2;

                Configuration.records.startRecord();
                System.out.println("FUN " + F + " DIM " + DIM);
                for(int run=0; run < 1; run++) {


                    MVMO algorithm = new MVMO();
                    algorithm.execute();
                }
                Configuration.records.endRecord(F, DIM);
            }
        }
        Configuration.records.exportExcel("MVMO" + "-" + "P" +nProblems + "-" + benchmark);
    }
}
