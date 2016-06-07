import cmaes.CmaesConfiguration;
import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec13.CEC13Benchmark;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class IcmaesilsTest {
    private final int NTRIES = 3;
    private final double ZERO = AllBenchmarks.objective();

    public boolean tryAlgorithm(int F, double mean, double std) {
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(F);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < NTRIES);


        return sucess;
    }

    public double algorithm(int F) {
        Configuration.records = new Records();
        Configuration.records.startRecord();
        Configuration.DIM = 10;
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


        Configuration.benchmark = new CEC13Benchmark(10, F);
        // Configuration.rand = new Rand(seeds.getSeed(F));
        Configuration.rand = new Rand();
        Configuration.max_fes = 10000 * 10;

        ICMAESILS algorithm = new ICMAESILS();
        return algorithm.execute();
    }

    @Test
    public void function1(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(1, mean, std));
    }

    @Test
    public void function2(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(2, mean, std));
    }

    @Test
    public void function3(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(3, mean, std));
    }

    @Test
    public void function4(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(4, mean, std));
    }

    @Test
    public void function5(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(5, mean, std));
    }

    @Test
    public void function6(){
        double mean = 3.89E+00;
        double std = 4.80E+00;
        assertTrue(tryAlgorithm(6, mean, std));
    }

    @Test
    public void function7(){
        double mean = 4.91E-06;
        double std = 1.34E-05;
        assertTrue(tryAlgorithm(7, mean, std));
    }

    @Test
    public void function8(){
        double mean = 2.04E+01;
        double std = 7.61E-02;
        assertTrue(tryAlgorithm(8, mean, std));
    }

    @Test
    public void function9(){
        double mean = 2.86E-01;
        double std = 5.38E-01;
        assertTrue(tryAlgorithm(9, mean, std));
    }

    @Test
    public void function10(){
        double mean = 1.00E-08;
        double std = ZERO;
        assertTrue(tryAlgorithm(10, mean, std));
    }

    @Test
    public void function11(){
        double mean = 4.77E-01;
        double std = 5.71E-01;
        assertTrue(tryAlgorithm(11, mean, std));
    }

    @Test
    public void function12(){
        double mean = 2.34E-01;
        double std = 4.26E-01;
        assertTrue(tryAlgorithm(12, mean, std));
    }

    @Test
    public void function13(){
        double mean = 3.33E-01;
        double std = 4.73E-01;
        assertTrue(tryAlgorithm(13, mean, std));
    }

    @Test
    public void function14(){
        double mean = 5.08E+01;
        double std = 9.99E+01;
        assertTrue(tryAlgorithm(14, mean, std));
    }

    @Test
    public void function15(){
        double mean = 4.42E+01;
        double std = 1.02E+02;
        assertTrue(tryAlgorithm(15, mean, std));
    }

    @Test
    public void function16(){
        double mean = 3.73E-01;
        double std = 3.00E-01;
        assertTrue(tryAlgorithm(16, mean, std));
    }

    @Test
    public void function17(){
        double mean = 1.12E+01;
        double std = 5.08E-01;
        assertTrue(tryAlgorithm(17, mean, std));
    }

    @Test
    public void function18(){
        double mean = 1.12E+01;
        double std = 5.01E-01;
        assertTrue(tryAlgorithm(18, mean, std));
    }

    @Test
    public void function19(){
        double mean = 6.98E-01;
        double std = 1.50E-01;
        assertTrue(tryAlgorithm(19, mean, std));
    }

    @Test
    public void function20(){
        double mean = 2.72E+00;
        double std = 5.24E-01;
        assertTrue(tryAlgorithm(20, mean, std));
    }

    @Test
    public void function21(){
        double mean = 2.18E+02;
        double std = 1.11E+02;
        assertTrue(tryAlgorithm(21, mean, std));
    }

    @Test
    public void function22(){
        double mean = 1.66E+02;
        double std = 8.10E+01;
        assertTrue(tryAlgorithm(22, mean, std));
    }

    @Test
    public void function23(){
        double mean = 4.08E+01;
        double std = 2.08E+01;
        assertTrue(tryAlgorithm(23, mean, std));
    }

    @Test
    public void function24(){
        double mean = 1.32E+02;
        double std = 3.25E+01;
        assertTrue(tryAlgorithm(24, mean, std));
    }

    @Test
    public void function25(){
        double mean = 1.92E+02;
        double std = 2.47E+01;
        assertTrue(tryAlgorithm(25, mean, std));
    }

    @Test
    public void function26(){
        double mean = 1.18E+02;
        double std = 1.30E+01;
        assertTrue(tryAlgorithm(26, mean, std));
    }


    @Test
    public void function27(){
        double mean = 3.25E+02;
        double std = 4.20E+01;
        assertTrue(tryAlgorithm(27, mean, std));
    }


    @Test
    public void function28(){
        double mean = 2.24E+02;
        double std = 1.01E+02;
        assertTrue(tryAlgorithm(28, mean, std));
    }

}
