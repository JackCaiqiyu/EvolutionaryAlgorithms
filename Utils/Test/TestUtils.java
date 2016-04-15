import junit.framework.Assert;
import org.junit.Test;

import java.util.Random;
import static org.junit.Assert.assertEquals;
/**
 * Created by framg on 13/04/2016.
 */

public class TestUtils {
    @Test
    public void testSortByIndexs(){
        double [][] m = new double[20][10];
        for(int i=0; i<20; i++){
            for(int j=0; j<10; j++){
                m[i][j] = i;
            }
        }
        //Random rand = new Random();

        int [] indexs = new int[20];
       // int pos;
       // int aux;
        for(int i=0; i<20; i++){
            indexs[i] = (i+8)%20;
        }

        /*for(int i=0; i<20; i++){
            pos = rand.nextInt(20);
            aux = indexs[i];
            indexs[i] = indexs[pos];
            indexs[pos] = aux;
        }*/

        m = Util.sortByIndexs(m, indexs);


        assertEquals(8,(int) m[0][0]);
        assertEquals(0 ,(int) m[12][0]);


    }





}

