import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec13.CEC13Benchmark;
import com.benchmark.cec.cec14.CEC14Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import com.benchmark.seeds;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by framg on 18/06/2016.
 */
public class NBIPOPaCMATest {
    private final int NTRIES = 3;
    private final double ZERO = AllBenchmarks.objective();

    public boolean tryAlgorithm(int F, double mean, double std) {
        boolean sucess = false;
        int tries = 0;

        do {
            double value = algorithm(F);
            System.out.println("Try: " + tries + " fit: " + value);
            if (value <= mean + std) {
                sucess = true;
            }
            tries++;
        }while(!sucess && tries < NTRIES);


        return sucess;
    }

    public double algorithm(int F) {
        int DIM = 10;
        Configuration.rand = new Rand();
        Configuration.benchmark = new CEC13Benchmark(DIM, F);


        AutoConfigure autoConfigure = new AutoConfigure(DIM, F);
        int []configuration = autoConfigure.configure();

        if(configuration[0] == 0){
            Configuration.BIPOP = false;
        }else{
            Configuration.BIPOP = true;
        }

        if(configuration[1] == 0){
            Configuration.newRestartRules = false;
        }else{
            Configuration.newRestartRules = true;
        }

        if(configuration[2] == 0){
            Configuration.CMAactive = false;
        }else{
            Configuration.CMAactive = true;
        }


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
        return  nbipoPaCMA.execute();
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
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(6, mean, std));
    }

    @Test
    public void function7(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(7, mean, std));
    }

    @Test
    public void function8(){
        double mean = 20.339;
        double std = 0.090;
        assertTrue(tryAlgorithm(8, mean, std));
    }

    @Test
    public void function9(){
        double mean = 0.232;
        double std = 0.440;
        assertTrue(tryAlgorithm(9, mean, std));
    }

    @Test
    public void function10(){
        double mean = ZERO;
        double std = ZERO;
        assertTrue(tryAlgorithm(10, mean, std));
    }

    @Test
    public void function11(){
        double mean = 0.364;
        double std = 0.506;
        assertTrue(tryAlgorithm(11, mean, std));
    }

    @Test
    public void function12(){
        double mean = 0.238;
        double std = 0.542;
        assertTrue(tryAlgorithm(12, mean, std));
    }

    @Test
    public void function13(){
        double mean = 0.484;
        double std = 0.676;
        assertTrue(tryAlgorithm(13, mean, std));
    }

    @Test
    public void function14(){
        double mean = 114.997;
        double std = 92.377;
        assertTrue(tryAlgorithm(14, mean, std));
    }

    @Test
    public void function15(){
        double mean = 158.161;
        double std = 117.317;
        assertTrue(tryAlgorithm(15, mean, std));
    }

    @Test
    public void function16(){
        double mean = 0.120;
        double std = 0.263;
        assertTrue(tryAlgorithm(16, mean, std));
    }

    @Test
    public void function17(){
        double mean = 11.334;
        double std = 0.545;
        assertTrue(tryAlgorithm(17, mean, std));
    }

    @Test
    public void function18(){
        double mean = 11.288;
        double std = 1.276;
        assertTrue(tryAlgorithm(18, mean, std));
    }

    @Test
    public void function19(){
        double mean = 0.525;
        double std = 0.139;
        assertTrue(tryAlgorithm(19, mean, std));
    }

    @Test
    public void function20(){
        double mean = 2.726;
        double std = 0.650;
        assertTrue(tryAlgorithm(20, mean, std));
    }

    @Test
    public void function21(){
        double mean = 152.941;
        double std = 50.410;
        assertTrue(tryAlgorithm(21, mean, std));
    }

    @Test
    public void function22(){
        double mean = 175.131;
        double std = 114.655;
        assertTrue(tryAlgorithm(22, mean, std));
    }

    @Test
    public void function23(){
        double mean = 174.230;
        double std = 122.831;
        assertTrue(tryAlgorithm(23, mean, std));
    }

    @Test
    public void function24(){
        double mean = 119.885;
        double std = 32.220;
        assertTrue(tryAlgorithm(24, mean, std));
    }

    @Test
    public void function25(){
        double mean = 176.972;
        double std = 39.918;
        assertTrue(tryAlgorithm(25, mean, std));
    }

    @Test
    public void function26(){
        double mean = 111.035;
        double std = 24.986;
        assertTrue(tryAlgorithm(26, mean, std));
    }


    @Test
    public void function27(){
        double mean = 316.684;
        double std = 29.556;
        assertTrue(tryAlgorithm(27, mean, std));
    }


    @Test
    public void function28(){
        double mean = 249.020;
        double std = 88.029;
        assertTrue(tryAlgorithm(28, mean, std));
    }

}
