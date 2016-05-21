package com.benchmark.cec.cec05;

import java.io.*;
import java.net.URL;

/**
 * Created by framg on 12/05/2016.
 */
public class READFILEJAR {
    public File readFile(String name){
        File file = null;
        String resource = name;
       // URL url = getClass().getClassLoader().getResource("com/benchmark/cec/cec05/supportData/fbias_data.txt");

        URL res = getClass().getResource(resource);
        if (res.toString().startsWith("jar:")) {
            try {
                InputStream input = getClass().getResourceAsStream(resource);
                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1048576];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                file.deleteOnExit();
            } catch (IOException ex) {
                System.err.print(ex);
                System.exit(0);
            }
        } else {
            //this will probably work in your IDE, but not from a JAR
            file = new File(res.getFile());
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }
        return file;
    }
}
