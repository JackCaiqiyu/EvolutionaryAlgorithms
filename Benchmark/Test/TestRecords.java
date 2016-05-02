import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

import java.util.Random;

/**
 * Created by framg on 22/04/2016.
 */


public class TestRecords {
    @Test
    public void testStdFunction(){
        Records records = new Records();
        Random random = new Random();
        double [] array = new double[25];
        for(int i=0; i<25; i++){
            array[i] = random.nextFloat();
        }
        assertNotEquals(0, records.std(array));
    }

}
