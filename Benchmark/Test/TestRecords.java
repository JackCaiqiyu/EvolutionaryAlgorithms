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

    @Test
    public void createExcelFile() {
        records.write("TEST");
    }

}
