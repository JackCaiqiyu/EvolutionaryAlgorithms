import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.linear.LUDecomposition;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Created by framg on 21/05/2016.
 */
public class MatlabTest {

    @Test
    public void diagTest(){
        double [] a = {1, 2, 3};
        double [][] algo = Matlab.diag(a);

        Matlab.diag(algo);

        System.out.println((long)1e299);

    }

    @Test
    public void detTest(){
        double [][] a = {{0,2,2},{-1,2,-1}, {3,-3,3}};
        double [][] b = {{1,-2,-22},{-1,2,-1}, {4,-3,21}};

        assertEquals(-6, (int)Matlab.det(a));
        assertEquals(115, (int)Matlab.det(b));

    }

    @Test
    public void fminconTest(){
        Matlab.fminconOutput output1 = new Matlab.fminconOutput();
        output1.evals = 4;

        Matlab.fminconOutput output2 = new Matlab.fminconOutput();
        output2.evals = 5;


        Assert.assertNotEquals(output1.evals, output2.evals);
    }
}
