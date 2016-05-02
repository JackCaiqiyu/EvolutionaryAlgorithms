/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 *
 * @author QIN
 */
public class IOUtils {
    private static boolean createDir(String f) {
        if (null == f)
            return true;
        if (new File(f).exists())
            return true;
        else {
            createDir(new File(f).getParent());
            return new File(f).mkdir(); 
        }  
    }
    public static void flush(String filename, String contents) {
        try {
            if (!createDir(new File(filename).getParent())) {
                
                System.out.println("Can't make directory.");
                return;
            }
//            String fn      = filename;
//     
//            
//            
//            
//            
//            String dirname = null;
//
//            if (fn.contains("/")) {
//                int slash = fn.lastIndexOf('/');
//
//                dirname = fn.substring(0, slash);
//            }
//
//            if (fn.contains("\\")) {
//                int slash = fn.lastIndexOf('\\');
//
//                dirname = fn.substring(0, slash);
//            }
//
//            if (dirname != null) {
//                File dir = new File(dirname);
//
//                if (!dir.exists()) {
//                    System.out.print("Create " + dir);
//                    dir.mkdirs();
//                    System.out.println(dir.exists()? " done." : " failed.");
//                }
//            }

            File        f   = new File(filename);
            PrintStream out = new PrintStream(new FileOutputStream(f));
            out.print(contents);
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found when open output file.");
            System.out.println("Check :" + filename);
        }
    }    
}
