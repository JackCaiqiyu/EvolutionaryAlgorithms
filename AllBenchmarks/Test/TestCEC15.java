import com.benchmark.AllBenchmarks;
import com.benchmark.Rand;
import com.benchmark.cec.cec05.CEC05Benchmark;
import com.benchmark.cec.cec15.CEC15Benchmark;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Assert;
import org.junit.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;


/**
 * Created by framg on 11/05/2016.
 */
public class TestCEC15 {
    @Test
    public void testF1(){
        double [] x10 = {1,2,3,4,5,6,7,8,9,0};
        //CEC15Benchmark.files = "/cec15.input_data/";
        CEC15Benchmark benchmark = new CEC15Benchmark(10, 1);
        assertEquals(String.valueOf(2.745612482267251e+09), String.valueOf(benchmark.f(x10)));

        double n= -21321.32432;
        double m = -23432.12321;
        DerivativeStructure diff = new DerivativeStructure(1, 2, 0, n);
//        System.out.println(diff.remainder(m).getValue());
//        System.out.println(n%m);
//        System.out.println(mod(n,m));
//
//        System.out.println(AllBenchmarks.mod(diff, m).getValue());

        System.out.println(5.0/3.0 * 8.0);

    }

    public double mod(double a, double b){
        double aa = Math.abs(a);
        double bb = Math.abs(b);
        double cc = aa - Math.floor(aa/bb)*bb;

        if(a < 0 ){
            return cc * -1;
        }else{
            return cc;
        }
    }


    @Test
    public void testDiff(){
        CEC15Benchmark.diff_enabled = false;
        for(int i=1; i<=CEC15Benchmark.nProblems(); i++) {
            CEC15Benchmark benchmark = new CEC15Benchmark(10, i);
            Rand rand = new Rand();
            double [] x = new double[10];
            for(int j=0; j< 10; j++){
                x[j] = -100 + (100 - -100) * rand.getDouble();
            }
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);
            System.out.println("FUNCTION: " + i);
           // Assert.assertTrue(benchmark.f(x) == benchmark.g(x)[0]);
            assertEquals(String.valueOf(df.format(benchmark.f(x))), String.valueOf(df.format(benchmark.g(x)[0])));
        }
        CEC15Benchmark.diff_enabled = true;
        for(int i=1; i<=CEC15Benchmark.nProblems(); i++) {
            CEC15Benchmark benchmark = new CEC15Benchmark(10, i);
            Rand rand = new Rand();
            double [] x = new double[10];
            for(int j=0; j< 10; j++){
                x[j] = -100 + (100 - -100) * rand.getDouble();
            }
            System.out.println("FUNCTION: " + i);
            benchmark.g(x);
        }
    }




}
