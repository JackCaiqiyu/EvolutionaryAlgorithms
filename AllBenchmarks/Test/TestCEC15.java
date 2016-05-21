import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * Created by framg on 11/05/2016.
 */
public class TestCEC15 {
    @Test
    public void testF1(){
        double [] x10 = {1,2,3,4,5,6,7,8,9,10};
        CEC15Benchmark.files = "C:/Users/framg/proyectos/EvolutionaryAlgorithms/AllBenchmarks/src/com/benchmark/cec/cec15/input_data/";
        CEC15Benchmark benchmark = new CEC15Benchmark(10, 1);
        assertEquals(String.valueOf(17103539820.32798767), String.valueOf(benchmark.f(x10)));
    }


}
