import com.benchmark.cec.cec13.CEC13Benchmark;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Created by framg on 11/05/2016.
 */
public class TestCEC13 {
    final double [] x10 = {1,2,3,4,5,6,7,8,9,10};

    @Test
    public void fuction3(){
        CEC13Benchmark benchmark = new CEC13Benchmark(10, 3);
        System.out.println(benchmark.f(x10));
        assertEquals(String.valueOf(500302553152513572864.000000), String.valueOf(benchmark.f(x10)));
    }

    @Test
    public void fuction9(){
        CEC13Benchmark benchmark = new CEC13Benchmark(10, 9);
        System.out.println(benchmark.f(x10));
        assertEquals(String.valueOf(-581.627990), String.valueOf(benchmark.f(x10)));
    }

}
