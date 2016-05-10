import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by framg on 08/05/2016.
 */
public class Matrix {

    public static double [] multiplies (double [][] m, double[] a){
        RealMatrix mReal = new Array2DRowRealMatrix(m);
        RealMatrix aReal = new Array2DRowRealMatrix(a);
        return Util.copyArray(mReal.multiply(aReal).transpose().getData()[0]);
    }

    public static double [] multipliesElementByElement(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        if(a1.length != a2.length){
            System.err.println("Array multiplication different sizes.");
            System.exit(0);
        }
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] +  a2[i];
        }
        return aNew;
    }
}
