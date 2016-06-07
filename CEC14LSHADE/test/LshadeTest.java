import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.seeds;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by framg on 22/05/2016.
 */
public class LshadeTest {
    final double zero = AllBenchmarks.objective();
    final int dim = 10;
    final int n_tries = 3;

    public double algorithm(int F, int DIM){
        Configuration.records = new Records();
        Configuration.MAX_FES = 10000 * DIM;
        Configuration.F = F;
        Configuration.N = DIM;
        Configuration.g_arc_rate = 2.6;
        Configuration.epsilon = 10e-8;;
        Rand rand = new Rand();
        Configuration.rand = rand;
        Configuration.benchmark = new CEC14Benchmark(DIM, F);
        Configuration.pop_size = Math.round(DIM * 18);
        Configuration.g_memory_size = 6;
        Configuration.g_p_best_rate = 0.11;

        Configuration.records.startRecord();
        LSHADE lshade = new LSHADE();
        return lshade.execute();
    }


    @Test
    public void function1(){
        int fun = 1;
        double mean = zero;
        double std = zero;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function2(){
        int fun = 2;
        double mean = zero;
        double std = zero;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }


    @Test
    public void function3(){
        int fun = 3;
        double mean = zero;
        double std = zero;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function4(){
        int fun = 4;
        double mean = 29.0;
        double std = 13.0;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function5(){
        int fun = 5;
        double mean = 1.4e+01;
        double std = 8.8e+00;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function6(){
        int fun = 6;
        double mean = 1.8e-02;
        double std = 1.3e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function7(){
        int fun = 7;
        double mean = 3.0e-03;
        double std = 6.5e-03;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }
    @Test
    public void function8(){
        int fun = 8;
        double mean = zero;
        double std = zero;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function9(){
        int fun = 9;
        double mean = 2.3e+00;
        double std = 8.4e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function10(){
        int fun = 10;
        double mean = 8.6e-03;
        double std = 2.2e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function11(){
        int fun = 11;
        double mean = 3.2e+01;
        double std = 3.8e+01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function12(){
        int fun = 12;
        double mean = 6.8e-02;
        double std = 1.9e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function13(){
        int fun = 13;
        double mean = 5.2e-02;
        double std = 1.5e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function14(){
        int fun = 14;
        double mean = 8.1e-02;
        double std = 2.6e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function15(){
        int fun = 15;
        double mean = 3.7e-01;
        double std = 6.9e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function16(){
        int fun = 16;
        double mean = 1.2e+00;
        double std = 3.0e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function17(){
        int fun = 17;
        double mean = 9.8e-01;
        double std = 1.1e+00;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function18(){
        int fun = 18;
        double mean = 2.4e-01;
        double std = 3.1e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }


    @Test
    public void function19(){
        int fun = 19;
        double mean = 7.7e-02;
        double std = 6.4e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function20(){
        int fun = 20;
        double mean = 1.8e-01;
        double std = 1.8e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function21(){
        int fun = 21;
        double mean = 4.1e-01;
        double std = 3.1e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function22(){
        int fun = 22;
        double mean = 4.4e-02;
        double std = 2.8e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function23(){
        int fun = 23;
        double mean = 3.3e+02;
        double std = 0.0e+00;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function24(){
        int fun = 24;
        double mean = 1.1e+02;
        double std = 2.3e+00;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function25(){
        int fun = 25;
        double mean = 1.3e+02;
        double std = 4.0e+01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function26(){
        int fun = 26;
        double mean = 1.0e+02;
        double std = 1.6e-02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function27(){
        int fun = 27;
        double mean = 5.8e+01;
        double std = 1.3e+02;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function28(){
        int fun = 28;
        double mean = 3.8e+02;
        double std = 3.2e+01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function29(){
        int fun = 29;
        double mean = 2.2e+02;
        double std = 4.6e-01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

    @Test
    public void function30(){
        int fun = 30;
        double mean = 4.6e+02;
        double std = 1.3e+01;
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(fun, dim);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < n_tries);


        assertTrue(sucess);
    }

}
