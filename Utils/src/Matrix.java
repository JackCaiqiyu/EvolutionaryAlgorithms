import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Created by framg on 08/05/2016.
 */
public class Matrix {


    public static double [] power2 (double [] m){
        double [] aNew = new double[m.length];

        for(int i=0; i<aNew.length; i++) {
            aNew[i] = m[i] * m[i];
        }

        return aNew;
    }

    public static double [][] multiplies (double [][] m, double a){
        double [][] aNew = new double[m.length][m[0].length];

        for(int i=0; i<aNew.length; i++) {
            for (int j = 0; j < aNew[0].length; j++) {
                aNew[i][j] = m[i][j] * a;
            }
        }

        return aNew;
    }


    public static double [] multiplies (double [][] m, double[] a){
        RealMatrix mReal = new Array2DRowRealMatrix(m);
        RealMatrix aReal = new Array2DRowRealMatrix(a);
        return Util.copyArray(mReal.multiply(aReal).transpose().getData()[0]);
    }

    public static double [][] multiplies (double [][] m, double[][] a){
        RealMatrix mReal = new Array2DRowRealMatrix(m);
        RealMatrix aReal = new Array2DRowRealMatrix(a);
        return mReal.multiply(aReal).getData();
    }

    public static double [][] multiplies_nx1x1xn (double [] m, double[] a){
        RealMatrix mReal = new Array2DRowRealMatrix(m);
        RealMatrix aReal = new Array2DRowRealMatrix(a);
        return mReal.multiply(aReal.transpose()).getData();
    }


    public static double [] multipliesElementByElement(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        if(a1.length != a2.length){
            System.err.println("Array multiplication different sizes.");
            System.exit(0);
        }
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] *  a2[i];
        }
        return aNew;
    }

    public static double [] add(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        if(a1.length != a2.length){
            System.err.println("Array addition different sizes.");
            System.exit(0);
        }
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] +  a2[i];
        }
        return aNew;
    }

    public static double [][] multipliesElementByElement(double [][] m1, double [][] m2){
        double [][] aNew = new double [m1.length][m1[0].length];
        if(m1.length != m2.length){
            System.err.println("Array multiplication different sizes.");
            System.exit(0);
        }
        for(int i=0; i<aNew.length; i++){
            for(int j=0; j<aNew[0].length; j++) {
                aNew[i][j] = m1[i][j] * m2[i][j];
            }
        }
        return aNew;
    }

    public static double[][] add(double [][] m1, double [][] m2){
        double [][] m_new = new double [m1.length] [m1[0].length];

        for(int i=0; i<m1.length; i++){
            for(int j=0; j<m1[0].length; j++){
                m_new[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return m_new;
    }

    public static double[][] sub(double [][] m1, double [][] m2){
        double [][] m_new = new double [m1.length] [m1[0].length];

        for(int i=0; i<m1.length; i++){
            for(int j=0; j<m1[0].length; j++){
                m_new[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return m_new;
    }


    public static double[][] divides(double [][] m1, double [][] m2){
        double [][] m_new = new double [m1.length] [m1[0].length];

        for(int i=0; i<m1.length; i++){
            for(int j=0; j<m1[0].length; j++){
                m_new[i][j] = m1[i][j] / m2[i][j];
            }
        }
        return m_new;
    }

    public static double [] multipliesByElement(double [] a1, double  value){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] *  value;
        }
        return aNew;
    }



    public static double [][] multipliesByElement(double [][] m1, double value){
        double [][] aNew = new double [m1.length][m1[0].length];

        for(int i=0; i<aNew.length; i++){
            for(int j=0; j<aNew[0].length; j++) {
                aNew[i][j] = m1[i][j] * value;
            }
        }
        return aNew;
    }

    public static double [][] dividesByElement(double [][] m1, double value){
        double [][] aNew = new double [m1.length][m1[0].length];

        for(int i=0; i<aNew.length; i++){
            for(int j=0; j<aNew[0].length; j++) {
                aNew[i][j] = m1[i][j] / value;
            }
        }
        return aNew;
    }

    public static double [][] transpose(double [][] m){
        RealMatrix mReal = new Array2DRowRealMatrix(m);
        return mReal.transpose().getData();
    }



}
