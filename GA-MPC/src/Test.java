import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Test {

    public static void main(String [] args) {
        Algorithm algorithm = new Algorithm();
       algorithm.execute();


       // writeInFile();



    }

    static void writeInFile(){
        FileOutputStream fop = null;
        File file;
        Random rand = new Random();
        rand.setSeed(0);
        try {

            file = new File("C:\\newfile.txt");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            for(int i=0; i<999; i++){
                String content = Integer.toString(rand.nextInt(3)) + " ,";
                byte[] contentInBytes = content.getBytes();
                fop.write(contentInBytes);
            }
            // get the content in bytes



       //     fop.flush();
            fop.close();

           // System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
