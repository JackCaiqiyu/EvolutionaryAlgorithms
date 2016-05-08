import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;


import java.io.IOException;
import java.util.Random;

/**
 * Created by framg on 22/04/2016.
 */


public class TestRecords {
    Records records;

    @Before
    public void init(){
        records = new Records();
    }


    @Test
    public void testStdFunction(){
        Random random = new Random();
        double [] array = new double[25];
        for(int i=0; i<25; i++){
            array[i] = random.nextFloat();
        }
        assertNotEquals(0, records.std(array));
    }

    //@Test
    //public void createExcelFile() {
     //   records.write("TEST");
   // }

    @Test
    public void testCEC15benchmark(){
        //CEC15Problems problems = new CEC15Problems();
       // double [] x ={1,2,3,4,5,6,7,8,9,10};
       // double f[] = problems.eval(x, 10, 1, 8);
       // System.out.println(f[0]);
    }
}
