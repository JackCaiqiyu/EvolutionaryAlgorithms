/**
 * Created by framg on 19/03/2016.
 */
public class Util {
    public static double inf = 999999999;
    public static double ninf = -999999999;
    public static int iinf = 999999999;


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

    public static double summatory(double [] array){
        double sum = 0;
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


    public static double[] copyArray(double [] aOld){
        double [] aNew = new double [aOld.length];
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

        for(int i=0; i<old.length; i++)
            for(int j=0; j<old[i].length; j++)
                aNew[i][j] = old[i][j];

        return aNew;
    }


    public static double[] multiplyArray(double [] aOld, double value){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i] * value;
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


    public static double [] multiplies(double [] a1, double [] a2){
        double [] aNew = new double [a1.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = a1[i] * a2[i];
        }
        return aNew;
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
        double [][] newArray = new double[dim][array[0].length];

        for(int i=low; i<up; i++){
            newArray[i] = Util.copyArray(array[i]);
        }

        return newArray;

    }

    public static double[][] add(double [][] a1, double [][] a2){
        double [][] a = new double[a1.length][a1.length + a2.length];
        int index = 0;

        for(int i=0; i<a1.length; i++) {
            a[index] = copyArray(a1[i]);
            index++;
        }

        for(int i=0; i<a2.length; i++) {
            a[index] = copyArray(a2[i]);
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



}
