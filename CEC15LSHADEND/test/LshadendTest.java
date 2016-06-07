import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by framg on 06/06/2016.
 */
public class LshadendTest {
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
        int DIM = 10;
        Configuration.records = new Records();
        Configuration.D = DIM;
        Configuration.nF = F;
        Configuration.max_nfes = 10000 * DIM;
        Configuration.rand = new Rand();
        Configuration.records.startRecord();
        Configuration.benchmark = new CEC15Benchmark(DIM, F);
        LSHADEND algorithm = new LSHADEND();
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
        double mean = 1.6082E+01;
        double std = 8.0215E+00;
        assertTrue(tryAlgorithm(3, mean, std));
    }

    @Test
    public void function4(){
        double mean = 2.7703E+00;
        double std = 9.8121E-01;
        assertTrue(tryAlgorithm(4, mean, std));
    }

    @Test
    public void function5(){
        double mean = 1.2486E+01;
        double std = 1.8063E+01;
        assertTrue(tryAlgorithm(5, mean, std));
    }

    @Test
    public void functions6(){
        double mean = 6.1416E-01;
        double std = 6.5245E-01;
        assertTrue(tryAlgorithm(6, mean, std));
    }

    @Test
    public void function7(){
        double mean = 5.5299E-02;
        double std = 1.2892E-01;
        assertTrue(tryAlgorithm(7, mean, std));
    }

    @Test
    public void function8(){
        double mean = 3.0355E-01;
        double std = 2.5852E-01;
        assertTrue(tryAlgorithm(8, mean, std));
    }

    @Test
    public void function9(){
        double mean = 1.0014E+02;
        double std = 2.5251E-02;
        assertTrue(tryAlgorithm(9, mean, std));
    }

    @Test
    public void function10(){
        double mean = 2.1654E+02;
        double std = ZERO;
        assertTrue(tryAlgorithm(10, mean, std));
    }

    @Test
    public void function11(){
        double mean = 1.2442E+02;
        double std = 1.4843E+02;
        assertTrue(tryAlgorithm(11, mean, std));
    }

    @Test
    public void function12(){
        double mean = 1.0073E+02;
        double std = 2.2200E-01;
        assertTrue(tryAlgorithm(12, mean, std));
    }

    @Test
    public void function13(){
        double mean = 3.0504E-02;
        double std = 6.2835E-05;
        assertTrue(tryAlgorithm(13, mean, std));
    }

    @Test
    public void function14(){
        double mean = 4.6487E+03;
        double std = 1.9886E+03;
        assertTrue(tryAlgorithm(14, mean, std));
    }

    @Test
    public void function15(){
        double mean = 1.0000E+02;
        double std = ZERO;
        assertTrue(tryAlgorithm(15, mean, std));
    }

}
