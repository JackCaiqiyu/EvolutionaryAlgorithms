import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framg on 19/03/2016.
 */
public class Util {
    public static double inf = 200e299;
    public static double ninf = -200e299;
    public static int iinf = 999999999;

    public static boolean cointains(double [] array, double val){
        for(int i=0; i<array.length; i++){
            if(array[i] == val){
                return true;
            }
        }
        return false;
    }



    public static double [] log (double [] array){
        double [] new_array = new double[array.length];
        for(int i=0; i<array.length; i++){
            new_array[i] = Math.log(array[i]);
        }
        return new_array;
    }

    public static int [] diag(int [][] m){
        int [] diagonal = new int[m.length];

        for(int i=0; i<m.length; i++){
            for(int j=0; j<m[0].length; j++){
                if(i == j){
                    diagonal[i] = m[i][j];
                }
            }
        }
        return diagonal;

    }

    public static double [] diag(double [][] m){
        double [] diagonal = new double[m.length];

        for(int i=0; i<m.length; i++){
            for(int j=0; j<m[0].length; j++){
                if(i == j){
                    diagonal[i] = m[i][j];
                }
            }
        }
        return diagonal;

    }

    public static double [][] diag(double [] m){
        double [][] diagonal = new double[m.length][m.length];
        assignMatrix(diagonal, 0);
        for(int i=0; i<m.length; i++){
            diagonal[i][i] = m[i];
        }

        return diagonal;

    }

    public static double std(double [] array){
        double mean = mean(array);
        double sum = 0;
        for(int i=0; i<array.length; i++){
            sum += (array[i] - mean) * (array[i] - mean);
        }

        return Math.sqrt(sum/array.length);
    }

    public static double mean(double [] array){
        double sum = 0;
        for(int i=0; i<array.length; i++){
            sum += array[i];
        }
        return sum/array.length;
    }


    public static double median(double [] array){
        double [] array_new = Util.copyArray(array);
        array_new = sortNewArray(array);
        return  array_new[Math.round(array_new.length/2)];
    }

    public static double summatory(double [] array){
        double sum = 0;
        for(int i=0; i<array.length; i++){
            sum += array[i];
        }
        return sum;
    }

    public static int summatory(int [] array){
        int sum = 0;
        for(int i=0; i<array.length; i++){
            sum += array[i];
        }
        return sum;
    }

    public static double min(double [] array){
        double min = array[0];
        for(int i=1; i<array.length; i++){
            if(array[i] < min)
                min = array[i];
        }
        return min;
    }

    public static int minOnlyIndex(double [] array){
        int index = 0;
        double min = array[0];
        for(int i=1; i<array.length; i++){
            if(array[i] < min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    public static boolean[] copyArray(boolean [] aOld){
        boolean [] aNew = new boolean [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }


    public static double[] copyArray(double [] aOld){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }

    public static int[] copyArray(int [] aOld){
        int [] aNew = new int [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }

    public static void assignMatrix(double [][] array, double value){
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j] = value;
    }

    public static void assignArray(int [] array, int value){
        for(int i=0; i<array.length; i++)

                array[i] = value;
    }

    public static void assignArray(boolean [] array, boolean value){
        for(int i=0; i<array.length; i++)

            array[i] = value;
    }

    public static void assignArray(double [] array, double value){
        for(int i=0; i<array.length; i++)

            array[i] = value;
    }

    public static void assignArray(double [] array, double [] values){
        for(int i=0; i<array.length; i++)

            array[i] = values[i];
    }


    public static double[][] copyMatrix(double [][] old){
        double [][] aNew = new double[old.length][old[0].length];

        for(int i=0; i<aNew.length; i++) {
            for (int j = 0; j < aNew[0].length; j++) {
                aNew[i][j] = old[i][j];
            }
        }

        return aNew;
    }

    public static double[][] multiplies(double [][] old, double value){
        double [][] aNew = new double[old.length][old[0].length];

        for(int i=0; i<aNew.length; i++) {
            for (int j = 0; j < aNew[0].length; j++) {
                aNew[i][j] = old[i][j] * value;
            }
        }

        return aNew;
    }


    public static double[] addArray(double [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i] +  value;
        }
        return aNew;
    }


    public static double[] multiplyArray(double [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i] * value;
        }
        return aNew;
    }

    public static double[][] addMatrix(double [][] m1, double [][] m2){
        double [][] m_new = new double [m1.length] [m1[0].length];

        for(int i=0; i<m1.length; i++){
            for(int j=0; j<m1[0].length; j++){
                m_new[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return m_new;
    }

    public static double[][] subMatrix(double [][] m1, double [][] m2){
        double [][] m_new = new double [m1.length] [m1[0].length];

        for(int i=0; i<m1.length; i++){
            for(int j=0; j<m1[0].length; j++){
                m_new[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return m_new;
    }

    public static double[][] mulMatrix(double [][] m1, double [][] m2){
        double [][] m_new = new double [m1.length] [m1[0].length];

        for(int i=0; i<m1.length; i++){
            for(int j=0; j<m1[0].length; j++){
                m_new[i][j] = m1[i][j] * m2[i][j];
            }
        }

        return m_new;
    }


    public static double[] subArrayRight(int [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = value - aOld[i];
        }
        return aNew;
    }


    public static double[] subArray(double [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i] - value;
        }
        return aNew;
    }

    public static double[] divideArray(double [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i] / value;
        }
        return aNew;
    }

    public static double[] divideInverseArray(double [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = value / aOld[i];
        }
        return aNew;
    }

    public static double [] divides(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] / a2[i];
        }
        return aNew;
    }

    public static double [] divides(double [] a1, double a2){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] / a2;
        }
        return aNew;
    }

    public static int [] subtraction(int [] a1, int [] a2){
        int [] aNew = new int [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] - a2[i];
        }
        return aNew;
    }

    public static double [] subtraction(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] - a2[i];
        }
        return aNew;
    }

    public static double [] addition(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] + a2[i];
        }
        return aNew;
    }


    public static double [] multiplies(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] * a2[i];
        }
        return aNew;
    }

    public static double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public static double [][] multiplies_lxm_mxn(double [][] a_old, double [][] b){

        double [][] a = Util.transposeMatrix(a_old);
        double [][] c = new double[a.length][b.length];

        int l = a.length;
        int n = b.length;
        int m = a_old.length;

        for (int i=0; i<l; ++i)
            for (int j=0; j<n; ++j)
                for (int k=0; k<m; ++k)
                    c[i][k] += a[i][k] * b[k][j];

        return c;
    }



    public static double[] multiplies_1xn_mxn(double [] a, double [][] matrix){
        double [][] b = transposeMatrix(matrix);
        double [] c = new double[a.length];
        int n = matrix.length;
        int m = b.length;

            for (int j=0; j<n; ++j)
                for (int k=0; k<m; ++k)
                    c[k] += a[k] * b[k][j];

        return c;
    }

    public static double[] multiplies_mxn_mx1(double [][] matrix, double [] b){
        double [][] a = transposeMatrix(matrix);
        double [] c = new double[a.length];

        int n = 1;
        int m = b.length;
        int l = a.length;

        for (int i=0; i<l; ++i)
                for (int k=0; k<m; ++k)
                    c[i] += a[i][k] * b[k];

        return c;
    }

    public static double[][] multiplies_lx1_1xn(double [] a, double [] b){
        double [][] c = new double[a.length][b.length];
        for(int i=0; i<a.length; i++){
            for(int j=0; j<b.length; j++){
                c[i][j] = a[i] * b[j];
            }
        }
        return c;
    }

    public static double multiplies_1xn_nx1(double [] a1, double [] a2){
        //double [] aNew = new double [a1.length];
        double sum = 0;
        /*
        for (int i=0; i<l; ++i)
            for (int j=0; j<n; ++j)
                for (int k=0; k<m; ++k)
                    c[i][k] += a[i][k] * b[k][j]

                            */


        for(int i=0; i < a1.length; i++){
            sum += a1[i] * a2[i];
        }


        return sum;
    }


    public static double [][] power(double [][] m, double p){
        double [][] m_new = new double[m.length][];
        for(int i=0; i<m_new.length; i++){
            for(int j=0; j<m_new[0].length; j++) {
                m_new[i][j] = Math.pow(m_new[i][j], p);
            }
        }
        return  m_new;
    }

    public static double [] power(double [] a, double p){
        double [] a_new = new double[a.length];
        for(int i=0; i<a_new.length; i++){
            a_new[i] = Math.pow(a[i], p);
        }
        return  a_new;
    }

    public static double [] power(double [] a, int p){
        double [] a_new = new double[a.length];
        for(int i=0; i<a_new.length; i++){
            a_new[i] = Math.pow(a[i], p);
        }
        return  a_new;
    }

    public static double [] power2(double [] a){
        double [] a_new = new double[a.length];
        for(int i=0; i<a_new.length; i++){
            a_new[i] = a[i] * a[i];
        }
        return  a_new;
    }

    public static double [] range(double [] array, int low, int up){
        int dim = up - low;
        double [] newArray = new double[dim];

        for(int i=low; i<up; i++){
            newArray[i] = array[i];
        }
        return  newArray;
    }

    public static double [][] range (double [][] array, int low, int up){
        int dim = up - low;
        double [][] newArray = new double[dim][];

        for(int i=low; i<up; i++){
            newArray[i] = Util.copyArray(array[i]);
        }

        return newArray;

    }

    public static double [][] removeRow(double [][] m, int index){
        double [][] new_m = new double[m.length-1][];
        int new_i = 0;
        for(int i=0; i<m.length; i++){
            if(i != index) {
                new_m[new_i] = Util.copyArray(m[i]);
                new_i++;
            }
        }
        return  new_m;
    }

    public static double [] removeRow(double [] a, int index){
        double [] new_a = new double[a.length-1];

        int new_i = 0;
        for(int i=0; i<a.length; i++){
            if(i != index) {
                new_a[new_i] = a[i];
                new_i++;
            }
        }
        return  new_a;
    }



    public static double[][] add(double [][] a1, double [][] a2){
        int rows = a1.length + a2.length;

        double [][] a = new double[rows][];

        for(int i=0; i<a1.length; i++){
                a[i] = Util.copyArray(a1[i]);
        }
        int index = 0;
        for(int i=a1.length; i<a2.length + a1.length; i++){
            a[i] =Util.copyArray(a2[index]);
            index++;
        }


        return a;
    }

    public static double[] add(double [] a1, double [] a2){
        int rows = a1.length + a2.length;

        double [] a = new double[rows];


        for(int i=0; i<a1.length; i++){
            a[i] = a1[i];
        }


        int index = 0;
        for(int i=a1.length; i<a2.length + a1.length; i++){
            a[i] = a2[index];
            index++;
        }

        return a;
    }


    public static int [] sort(double [] array){
        int [] indexs = new int[array.length];
        for(int i=0; i<indexs.length; i++)
            indexs[i] = i;

        for(int i=0; i<array.length; i++){
            for(int j=i+1; j<array.length; j++){
                if(array[i] > array[j]){
                    double tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;

                    int tmpi = indexs[i];
                    indexs[i] = indexs[j];
                    indexs[j] = tmpi;
                }
            }
        }

        return indexs;
    }

    public static int [] sortDescend(int [] array){
        int [] indexs = new int[array.length];
        for(int i=0; i<indexs.length; i++)
            indexs[i] = i;

        for(int i=0; i<array.length; i++){
            for(int j=i+1; j<array.length; j++){
                if(array[i] < array[j]){
                    int tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;

                    int tmpi = indexs[i];
                    indexs[i] = indexs[j];
                    indexs[j] = tmpi;
                }
            }
        }

        return indexs;
    }


    public static double [] sortNewArray(double [] array){
        double [] new_array = Util.copyArray(array);
        double tmp;
        for(int i=0; i<new_array.length; i++){
            for(int j=i+1; j<new_array.length; j++) {
                if(new_array[i] > new_array[j] ) {
                    tmp = new_array[i];
                    new_array[i] = new_array[j];
                    new_array[j] = tmp;
                }
            }
        }
        return new_array;
    }

    public static int [] sortOnlyIndexs(double [] a){
        double [] array = copyArray(a);
        int [] indexs = new int[array.length];
        for(int i=0; i<indexs.length; i++)
            indexs[i] = i;

        for(int i=0; i<array.length; i++){
            for(int j=i+1; j<array.length; j++){
                if(array[i] > array[j]){
                    double tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;

                    int tmpi = indexs[i];
                    indexs[i] = indexs[j];
                    indexs[j] = tmpi;
                }
            }
        }

        return indexs;
    }

    public static double [][] sortByIndexs(double [][] m, int [] indexs){
        double [][] tmp = new double[indexs.length][];
        for(int i=0; i<indexs.length; i++){
            tmp[i] = Util.copyArray(m[indexs[i]]);
        }
        return tmp;
    }

    public static int [] sortByIndexs(int [] a, int [] indexs){
        int [] tmp = Util.copyArray(a);
        for(int i=0; i<indexs.length; i++){
            tmp[i] = a[indexs[i]];
        }
        return tmp;
    }

    public static double [] sortByIndexs(double [] a, int [] indexs){
        double [] tmp = Util.copyArray(a);
        for(int i=0; i<indexs.length; i++){
            tmp[i] = a[indexs[i]];
        }
        return tmp;
    }

    public static double [] abs(double [] array){
        double [] new_array = new double[array.length];
        for(int i=0; i<array.length; i++){
            new_array[i] = Math.abs(array[i]);
        }
        return  new_array;
    }

    public static int[] find(double [] array, String operator ,double condition){
        List<Integer> index = new ArrayList<>();


        if(operator.equals("==")) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == condition) { //TODO mejor forma de comrpar doubles?
                    index.add(i);
                }
            }
        }else if(operator.equals("<=")){
            for (int i = 0; i < array.length; i++) {
                if (array[i] <= condition) { //TODO mejor forma de comrpar doubles?
                    index.add(i);
                }
            }
        }

        int [] tmp = new int[index.size()];
        for(int i=0; i<tmp.length; i++){
            tmp[i] = index.get(i);
        }

        return tmp;

    }

    static double[] min(double [] array, double value){
        double [] out = copyArray(array);
        for(int i=0; i<out.length; i++){
            if(out[i] > value){
                out[i] = value;
            }
        }
        return out;
    }

    static double[] max(double [] array, double value){
        double [] out = copyArray(array);
        for(int i=0; i<out.length; i++){
            if(out[i] < value){
                out[i] = value;
            }
        }
        return out;
    }

    static double max(double [] array){
        double out = 0;


        for(int i=0; i<array.length; i++){
            if(array[i] > out){
                out = array[i];
            }
        }
        return out;
    }

    static int[] max(int [] array, int value){
        int [] out = copyArray(array);
        for(int i=0; i<out.length; i++){
            if(out[i] < value){
                out[i] = value;
            }
        }
        return out;
    }

    static double [][] duplicateArray(double [] array, int n){
        double[][] matrix = new double[array.length][n];

        for(int i=0; i <array.length; i++){
            for(int j=0; j<n; j++){
                matrix[i][j] = array[i];
            }
        }
        return matrix;
    }

    static double [][] eraseMember (double [][] matrix, int pos){
        double [][] new_matrix = new double[matrix.length-1][matrix[0].length];

        int index = 0;
        for(int i=0; i<matrix.length; i++){
            if(i != pos) {
                new_matrix[index] = Util.copyArray(matrix[i]);
                index++;
            }
        }
        return new_matrix;
    }

    static double [] eraseMember (double [] array, int pos){
        double [] new_array = new double[array.length-1];

        int index = 0;
        for(int i=0; i<array.length; i++){
            if(i != pos) {
                new_array[index] = array[i];
                index++;
            }
        }
        return new_array;
    }

    static int[] cutArray (int [] array, int n){
        int [] new_array = new int[n];
        for(int i=0; i<n; i++){
            new_array[i] = array[i];
        }

        return  new_array;
    }



    static List<Integer>  arrayToList(int [] array){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<array.length; i++){
            list.add(array[i]);
        }
        return list;
    }

    static double [] listToArrayDouble(List<Double> list){
        double [] array = new double[list.size()];
        for(int i=0; i<array.length; i++){
            array[i] = list.get(i);
        }
        return  array;
    }


    static List<Double>  arrayToList(double [] array){
        List<Double> list = new ArrayList<>();
        for(int i=0; i<array.length; i++){
            list.add(array[i]);
        }
        return list;
    }


    static int [] listToArray(List<Integer> list){
        int [] array = new int[list.size()];
        for(int i=0; i<array.length; i++){
            array[i] = list.get(i);
        }
        return  array;
    }



    static double [][] listVectorToArray(List<double[]> list){
        double [][] array = new double[list.size()][];
        for(int i=0; i<array.length; i++){
            array[i] = Util.copyArray(list.get(i));
        }
        return  array;
    }



    static boolean contains(int [] array, int n){
        for(int i=0; i<array.length; i++){
            if(array[i] == n){
                return true;
            }
        }
        return  false;
    }

    static double [] sqrt(double [] a){
        double [] aNew = new double[a.length];
        for(int i=0;i < a.length; i++){
            aNew[i] = Math.sqrt(a[i]);
        }
        return aNew;
    }



    // return the Euclidean norm of this Vector
    static double norm(double [] array) {
        double sum = 0;
        for(int i=0; i<array.length; i++){
            sum += array[i] * array[i];
        }

        return Math.sqrt(sum);
    }



    public static DerivativeStructure mod(DerivativeStructure a, double b){
        //double aa = Math.abs(a);
        DerivativeStructure aa = a.abs();
        double bb = Math.abs(b);
        //double cc = aa - Math.floor(aa/bb)*bb;
        DerivativeStructure cc = aa.subtract(aa.divide(bb).floor().multiply(bb));
        if(aa.getValue() < 0 ){
            return cc.negate();
        }else{
            return cc;
        }
    }


}
