import com.benchmark.Rand;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * Created by framg on 11/05/2016.
 */
public class TestCEC15 {
    @Test
    public void testF1(){
        double [] x10 = {1,2,3,4,5,6,7,8,9,10};
        CEC15Benchmark.files = "/cec15.input_data/";
        CEC15Benchmark benchmark = new CEC15Benchmark(10, 1);
        assertEquals(String.valueOf(17103539820.32798767), String.valueOf(benchmark.f(x10)));
    }


    @Test
    public void testDiff(){
       // CEC15Benchmark.files = "cec15.input_data/";
        //CEC15Benchmark.files = "C:/Users/framg/proyectos/EvolutionaryAlgorithms/AllBenchmarks/src/com/benchmark/cec/cec15/cec15.input_data/";
//        System.out.println("Working Directory = " +
//                System.getProperty("user.dir"));
        CEC15Benchmark.diff_enabled = false;
        for(int i=0; i<CEC15Benchmark.nProblems(); i++) {
            CEC15Benchmark benchmark = new CEC15Benchmark(10, i);
            Rand rand = new Rand();
            double [] x = new double[10];
            for(int j=0; j< 10; j++){
                x[j] = -100 + (100 - -100) * rand.getDouble();
            }
            System.out.println("FUNCTION: " + i);
            Assert.assertTrue(benchmark.f(x) == benchmark.g(x)[0]);
        }
    }


}
