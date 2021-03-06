import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;
import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by framg on 08/05/2016.
 */
public class LSHADE_SPSTest {
    private final int NTRIES = 3;
    private final double ZERO = AllBenchmarks.objective();

    public boolean tryAlgorithm(int F, double mean, double std) {
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(F);
            System.out.println("Fit " + tries + ": " + value);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        } while (!sucess && tries < NTRIES);


        return sucess;
    }

    public double algorithm(int F) {

        int DIM = 10;

        Configuration.rand = new Rand();
        Configuration.benchmark = new CEC15Benchmark(DIM, F);

        AutoConfigure autoConfigure = new AutoConfigure(DIM, F);
        autoConfigure.auto_configure();

        Configuration.D = DIM;
        Configuration.nF = F;
        Configuration.maxfunevals = 10000 * DIM;
        Configuration.isRecordsActive = false;
        Configuration.EarlyStop = "auto";
        Configuration.ConstraintHandling = "Interpolation";
        Configuration.noise = false;
        Configuration.Xinitial = null;

        LSHADE_SPS algorithm = new LSHADE_SPS();
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
        double mean = 1.80E+01;
        double std = 6.01E+00;
        assertTrue(tryAlgorithm(3, mean, std));
    }

    @Test
    public void function4(){
        double mean = 1.17E+00;
        double std = 9.69E-1;
        assertTrue(tryAlgorithm(4, mean, std));
    }

    @Test
    public void function5(){
        double mean = 2.69E+01;
        double std = 3.43E+01;
        assertTrue(tryAlgorithm(5, mean, std));
    }

    @Test
    public void functions6(){
        double mean = 1.23E-01;
        double std = 2.86E-01;
        assertTrue(tryAlgorithm(6, mean, std));
    }

    @Test
    public void function7(){
        double mean = 2.78E-02;
        double std = 2.57E-02;
        assertTrue(tryAlgorithm(7, mean, std));
    }

    @Test
    public void function8(){
        double mean = 8.81E-04;
        double std = 1.92E-03;
        assertTrue(tryAlgorithm(8, mean, std));
    }

    @Test
    public void function9(){
        double mean = 1.00E+02;
        double std = 3.36E-02;
        assertTrue(tryAlgorithm(9, mean, std));
    }

    @Test
    public void function10(){
        double mean = 2.17E+02;
        double std = 3.78E-03;
        assertTrue(tryAlgorithm(10, mean, std));
    }

    @Test
    public void function11(){
        double mean = 4.72E+01;
        double std = 1.10E+02;
        assertTrue(tryAlgorithm(11, mean, std));
    }

    @Test
    public void function12(){
        double mean = 1.01E+02;
        double std = 1.37E-01;
        assertTrue(tryAlgorithm(12, mean, std));
    }

    @Test
    public void function13(){
        double mean = 3.04E-02;
        double std = 4.67E-05;
        assertTrue(tryAlgorithm(13, mean, std));
    }

    @Test
    public void function14(){
        double mean = 6.56E+02;
        double std = 1.14E+03;
        assertTrue(tryAlgorithm(14, mean, std));
    }

    @Test
    public void function15(){
        double mean = 1.0000E+02;
        double std = ZERO;
        assertTrue(tryAlgorithm(15, mean, std));
    }


}
