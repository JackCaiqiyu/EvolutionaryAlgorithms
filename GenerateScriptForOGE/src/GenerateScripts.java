import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by framg on 09/06/2016.
 */
public class GenerateScripts {
    public static void main(String [] args) {
        String fileName = "Algorithm";

        String queue = "media";
        String name_benchmark = null;
        Integer start_function = null;
        Integer finish_function = null;

        boolean automatic = false;

        for(int i=0; i<args.length; i++){
            if(args[i].equals("-sF")){
                start_function = Integer.valueOf(args[i+1]);
            }
            if(args[i].equals("-fF")){
                finish_function = Integer.valueOf(args[i+1]);
            }
            if(args[i].equals("-b")){
                name_benchmark = args[i+1];
            }
            if(args[i].equals("-q")){
                queue = args[i+1];
            }
            if(args[i].equals("-name")){
                fileName = args[i+1];
            }
            if(args[i].equals("-a")){
                automatic = true;
            }
        }

        if(start_function == null || finish_function == null || name_benchmark == null ){
            System.out.print("No enough arguments.");
            System.exit(0);
        }

        if(!automatic) {
            String scriptName = fileName + name_benchmark + "F" + finish_function;

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#!/bin/bash\n");
            stringBuilder.append("#$ -N " + scriptName + "\n");
            stringBuilder.append("#$ -q " + queue + "\n");
            stringBuilder.append("#$ -o OUT_" + scriptName + ".txt\n");
            stringBuilder.append("#$ -e ERR_" + scriptName + ".txt\n");
            stringBuilder.append("#$ -cwd\n");
            stringBuilder.append("java -jar " + fileName + ".jar" + " -b " + name_benchmark + " -sF " + start_function + " -fF " + finish_function + "\n");


            write("GC_" + scriptName, stringBuilder.toString());

            StringBuilder script = new StringBuilder();
            script.append("qsub " + "GC_" + scriptName + "\n");
            write("execute", script.toString());
        }else{
            
        }
    }


    public static void write(String file_name, String content) {
        File file = new File(file_name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream fop = new FileOutputStream(file, true)) {

            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
