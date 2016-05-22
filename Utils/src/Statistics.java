/**
 * Created by framg on 21/05/2016.
 */
public class Statistics {

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
        double [] array_new;
        array_new = Util.sortNewArray(array);
        return  array_new[Math.round(array_new.length/2)];
    }

}
