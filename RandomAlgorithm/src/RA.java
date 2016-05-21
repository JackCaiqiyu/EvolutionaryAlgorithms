import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;

/**
 * Created by framg on 11/05/2016.
 */
public class RA {


    public static void main(String[] args) {
        long start = System.nanoTime();
        ra("CEC05", true);
        //ra("CEC13", false);
      // ra("CEC14", false);
       // ra("CEC15", false);
        long elapsedTime = System.nanoTime() - start;
        System.out.println("TIEMPO TRANSCURRIDO: " + elapsedTime);
    }


    static void ra(String name, boolean debug){
        int nProblems = 0;
        Records records = new Records(50);

        if("CEC05".equals(name)) {
            nProblems = CEC05Benchmark.nProblems();
        }else if("CEC13".equals(name)){
                nProblems = CEC13Benchmark.nProblems();

        }else if("CEC14".equals(name)){
                nProblems = CEC14Benchmark.nProblems();

        }else if("CEC15".equals(name)) {
            nProblems = CEC15Benchmark.nProblems();
        }else{
                System.err.println("No benchmark avaiable.");
                System.exit(0);
        }


        for(int F = 1; F <= nProblems; F++) {
            for(int DIM = 10; DIM <= 10; DIM += 20) {
                double [] pop;
                double fitness;
                int pop_size = 100;
                System.out.println("F: " + F + " ,DIM: " + DIM);
                AllBenchmarks benchmark = null;
                if("CEC05".equals(name)) {
                    benchmark = new CEC05Benchmark(DIM, F);
                    nProblems = CEC05Benchmark.nProblems();

                }else if ("CEC13".equals(name)){
                        benchmark = new CEC13Benchmark(DIM, F);
                        nProblems = CEC13Benchmark.nProblems();

                }else if ("CEC14".equals(name)){
                        benchmark = new CEC14Benchmark(DIM, F);
                        nProblems = CEC14Benchmark.nProblems();

                }else if ("CEC15".equals(name)) {
                    benchmark = new CEC15Benchmark(DIM, F);
                    nProblems = CEC15Benchmark.nProblems();

                }else{
                        System.err.println("No benchmark avaiable.");
                        System.exit(0);

                }
                Rand rand = new Rand();

                records.startRecord();
                for(int run = 0; run < 50; run++) {
                    int n_eval = 0;
                    pop = new double[DIM];
                    double best_value =999999999;

                    while (n_eval < 10000 *  DIM) {

                        if(!debug) {
                            for (int j = 0; j < DIM; j++) {
                                pop[j] = benchmark.lbound() + (benchmark.ubound() - benchmark.lbound()) * rand.getDouble();
                            }
                            fitness = benchmark.f(pop);
                            n_eval++;
                            if(fitness < best_value)
                             best_value = fitness;
                        }else{
                            n_eval += 9900;
                            for (int j = 0; j < DIM; j++) {
                                pop[j] = benchmark.lbound() + (benchmark.ubound() - benchmark.lbound()) * rand.getDouble();
                            }
                            fitness  = benchmark.f(pop);
                            if(fitness < best_value)
                                best_value = fitness;
                        }
                      //  System.out.println("fiteness: " + ( best_value));
                        records.newRecord((best_value), n_eval);

                    }
                    records.endRun(benchmark.bias() - best_value, n_eval,  10000 *  DIM);

                }
                records.endRecord(F, DIM);
            }
        }

        records.exportExcel("RA" + "-" + name);

    }


}
