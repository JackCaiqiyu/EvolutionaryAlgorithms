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
public class GaappadeTest {
    final double zero = AllBenchmarks.objective();
    final int dim = 10;
    final int n_tries = 3;

    public double algorithm(int F, int DIM){
        Configuration.records = new Records();
        Configuration.benchmark = new CEC14Benchmark(DIM, F);
        Configuration.random = new Rand();
        Configuration.max_fes = 10000 * DIM;
        Configuration.FVr_minbound = Configuration.benchmark.lbound();
        Configuration.FVr_maxbound = Configuration.benchmark.ubound();
        Configuration.D = DIM;
        System.out.println("FUN " + F + " DIM " + DIM);
        Configuration.records.startRecord();
        GAAPPADE gaappade = new GAAPPADE();
        return gaappade.execute();
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
        double mean = 2.82E+01;
        double std = 1.35E+01;
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
        double mean = 1.96E+01;
        double std = 1.85E+00;
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
        double mean = 1.40E-01;
        double std = 3.64E-01;
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
        double mean = 3.53E-03;
        double std = 4.41E-03;
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
        double mean = 3.42E+00;
        double std = 8.75E-01;
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
        double mean = 6.00E-01;
        double std = 2.34E+00;
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
        double mean = 1.60E+02;
        double std = 1.10E+02;
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
        double mean = 1.47E-01;
        double std = 4.09E-02;
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
        double mean = 6.54E-02;
        double std = 1.34E-02;
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
        double mean = 9.32E-02;
        double std = 3.22E-02;
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
        double mean = 5.83E-01;
        double std = 9.74E-02;
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
        double mean = 2.01E+00;
        double std = 3.03E-01;
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
        double mean = 9.09E+00;
        double std = 4.79E+00;
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
        double mean = 2.09E-01;
        double std = 1.49E-01;
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
        double mean = 2.60E-01;
        double std = 1.52E-01;
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
        double mean = 4.33E-01;
        double std = 1.48E-01;
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
        double mean = 4.52E-01;
        double std = 2.41E-01;
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
        double mean = 3.18E+00;
        double std = 1.14E+00;
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
        double mean = 3.29E+02;
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
    public void function24(){
        int fun = 24;
        double mean = 1.08E+02;
        double std = 2.08E+00;
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
        double mean = 1.68E+02;
        double std = 4.11E+01;
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
        double mean = 1.00E+02;
        double std = 1.74E-02;
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
        double mean = 9.56E+01;
        double std = 1.63E+02;
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
        double mean = 3.84E+02;
        double std = 3.36E+01;
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
        double mean = 2.22E+02;
        double std = 6.81E-01;
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
        double mean = 4.68E+02;
        double std = 1.90E+01;
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
