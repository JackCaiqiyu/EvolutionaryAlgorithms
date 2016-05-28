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

        int [] indexs = new int[20];
        for(int i=0; i<20; i++){
            indexs[i] = (i+8)%20;
        }


        m = Util.sortByIndexs(m, indexs);


        assertEquals(8,(int) m[0][0]);
        assertEquals(0 ,(int) m[12][0]);


    }

    @Test
    public void testSort(){
        double [] a = new double[20];
        for(int i=0; i<20; i++){
            a[i] = (i+8)%20;
        }
        int [] index = new int[20];

        index = Util.sort(a);


        double aa = 5.0 * 3.0 / 2.0 ;
        double bb = 3.0 / 2.0 * 5.0 ;
        assertEquals(String.valueOf(0.0), String.valueOf(a[0]));
        assertEquals( 12,index[0]);
    }


}

