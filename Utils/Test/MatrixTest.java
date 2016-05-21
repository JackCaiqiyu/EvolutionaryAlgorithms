
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



/**
 * Created by framg on 14/05/2016.
 */

public class MatrixTest {


    @Test
    public void multiplicationTest(){
        double [][] m = {{1, 2, 3} , {-1, -2, -3}, {4,5,6}};
        double [][] m2 = {{1, 2, 3} , {-1, -2, -3}};
        double [] a = {3, 3, 3};

        double [][] b= {{3, 3}, {2,2}};
        double [][] algo = Matrix.multiplies(m2, b);
       // b = Matrix.multiplies(m, a);

//        assertEquals(String.valueOf(18.0), String.valueOf(b[0]));
//        assertEquals(String.valueOf(-18.0),String.valueOf(b[1]));
//        assertEquals(String.valueOf(45.0), String.valueOf(b[2]));
    }

    @Test
    public void eigTest(){
        double [][] m = {{1, 2, 3} , {-1, -2, -3}, {4,5,6}};
      //  double [][] m = {{1, -1, 4} , {2, -2, 5}, {3,-3,6}};
//        RealMatrix CRealMatrix = new Array2DRowRealMatrix(m);
//        EigenDecomposition eigenDecomposition = new EigenDecomposition(CRealMatrix);
//        System.out.println(eigenDecomposition.getV());
//        System.out.println(eigenDecomposition.getVT());
//        System.out.println(eigenDecomposition.getD());

//
//        double [][] m2 = {{ 3.0  ,   -2.0  ,    -0.9  ,   2*Math.ulp(1)},
//                {-2.0  ,    4.0     ,  1.0 ,   -Math.ulp(1)},
//                {-Math.ulp(1)/4   , Math.ulp(1)/2 ,   -1.0    , 0},
//                {-0.5 ,    -0.5 ,      0.1  ,   1.0}};
////

        RealMatrix CRealMatrix = new Array2DRowRealMatrix(m);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(CRealMatrix);
        System.out.println(eigenDecomposition.getV().multiply(eigenDecomposition.getD()).subtract(CRealMatrix.multiply(eigenDecomposition.getV())));

//        Jama.Matrix matrix = new Matrix(m);
//        EigenvalueDecomposition eig = new EigenvalueDecomposition(matrix);
//        double [][] v = eig.getV().getArray();
//                for(int i=0; i<v.length; i++) {
//                    for (int j = 0; j < v[0].length; j++) {
//                        System.out.println(v[i][j]);
//                    }
//
//                }
       // LinearMathUtils.eigenSolveSymmetric();

    }

    @Test
    public void covTest(){
        double [][] m = {{1, 2, 3} , {-1, -2, -3}, {4,5,6}};
        Covariance covariance = new Covariance(m);
        double [][] res = covariance.getCovarianceMatrix().getData();
        //System.out.println(covariance.getCovarianceMatrix());
        System.out.println(Math.ulp(0));
//        for(int i=0; i<res.length; i++)
//            for(int j=0; j<res[0].length; j++){
//                System.out.println(res[i][j]);
//            }
    }



}
