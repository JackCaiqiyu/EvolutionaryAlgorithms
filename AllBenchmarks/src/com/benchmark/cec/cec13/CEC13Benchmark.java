package com.benchmark.cec.cec13;

import com.benchmark.AllBenchmarks;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by framg on 02/05/2016.
 */
public class CEC13Benchmark extends AllBenchmarks {
    boolean init_flag;
    double[] OShift, M;
    public static String files = "input_data/";

    public CEC13Benchmark(int DIM, int FUN) {
        super(DIM, FUN, 28);
        //URL url = getClass().getResource("input_data/");
       // files = url.getPath();
        init_flag = true;
    }

    public static int nProblems(){
        return 28;
    }

    @Override
    public double f(double[] x) {
        int cf_num = 10, i;
        double f = 0.0;
        int nx = DIM;

        if (init_flag) {
            init_flag = false;

            if (!(nx == 2 || nx == 5 || nx == 10 || nx == 20 || nx == 30 || nx == 40 || nx == 50 || nx == 60 || nx == 70 || nx == 80 || nx == 90 || nx == 100)) {
                System.err.println("\nError: Test functions are only defined for D=2,5,10,20,30,40,50,60,70,80,90,100.");
                System.exit(0);
            }

            File fpt = readFile(files +"M_D" + nx + ".txt"); //new File(files +"M_D" + nx + ".txt");//* Load M data *
            Scanner input = null;
            try {
                input = new Scanner(fpt);
            } catch (FileNotFoundException e) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }
            if (!fpt.exists()) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }

            M = new double[cf_num * nx * nx];

            for (i = 0; i < cf_num * nx * nx; i++) {
                M[i] = Double.parseDouble(input.next());
            }
            input.close();


            fpt = readFile(files + "shift_data.txt");  // new File(files + "shift_data.txt");
            try {
                input = new Scanner(fpt);
            } catch (FileNotFoundException e) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }
            if (!fpt.exists()) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }
            OShift = new double[nx * cf_num];
            for (i = 0; i < cf_num * nx; i++) {
                OShift[i] = Double.parseDouble(input.next());
            }
            input.close();
        }

        testfunc function = new testfunc(DIM);
        switch (FUN)

        {
            case 1:
                f = function.sphere_func(x, f, nx, OShift, M, 1);
                break;
            case 2:
                f = function.ellips_func(x, f, nx, OShift, M, 1);
                break;
            case 3:
                f = function.bent_cigar_func(x, f, nx, OShift, M, 1);
                break;
            case 4:
                f = function.discus_func(x, f, nx, OShift, M, 1);
                break;
            case 5:
                f = function.dif_powers_func(x, f, nx, OShift, M, 0);
                break;
            case 6:
                f = function.rosenbrock_func(x, f, nx, OShift, M, 1);
                break;
            case 7:
                f = function.schaffer_F7_func(x, f, nx, OShift, M, 1);
                break;
            case 8:
                f = function.ackley_func(x, f, nx, OShift, M, 1);
                break;
            case 9:
                f = function.weierstrass_func(x, f, nx, OShift, M, 1);
                break;
            case 10:
                f = function.griewank_func(x, f, nx, OShift, M, 1);
                break;
            case 11:
                f = function.rastrigin_func(x, f, nx, OShift, M, 0);
                break;
            case 12:
                f = function.rastrigin_func(x, f, nx, OShift, M, 1);
                break;
            case 13:
                f = function.step_rastrigin_func(x, f, nx, OShift, M, 1);
                break;
            case 14:
                f = function.schwefel_func(x, f, nx, OShift, M, 0);
                break;
            case 15:
                f = function.schwefel_func(x, f, nx, OShift, M, 1);
                break;
            case 16:
                f = function.katsuura_func(x, f, nx, OShift, M, 1);
                break;
            case 17:
                f = function.bi_rastrigin_func(x, f, nx, OShift, M, 0);
                break;
            case 18:
                f = function.bi_rastrigin_func(x, f, nx, OShift, M, 1);
                break;
            case 19:
                f = function.grie_rosen_func(x, f, nx, OShift, M, 1);
                break;
            case 20:
                f = function.escaffer6_func(x, f, nx, OShift, M, 1);
                break;
            case 21:
                f = function.cf01(x, f, nx, OShift, M, 1);
                break;
            case 22:
                f = function.cf02(x, f, nx, OShift, M, 0);
                break;
            case 23:
                f = function.cf03(x, f, nx, OShift, M, 1);
                break;
            case 24:
                f = function.cf04(x, f, nx, OShift, M, 1);
                break;
            case 25:
                f = function.cf05(x, f, nx, OShift, M, 1);
                break;
            case 26:
                f = function.cf06(x, f, nx, OShift, M, 1);
                break;
            case 27:
                f = function.cf07(x, f, nx, OShift, M, 1);
                break;
            case 28:
                f = function.cf08(x, f, nx, OShift, M, 1);
                break;
            default:
                System.out.println("\nError: There are only 28 test functions in this test suite!");
                System.exit(0);
                f = 0.0;
                break;

        }
        return f + bias();
    }

    @Override
    public double bias() {
        switch (FUN) {
            case 1:
                return -1400.0;
            case 2:
                return -1300.0;

            case 3:
                return -1200.0;

            case 4:
                return -1100.0;

            case 5:
                return -1000.0;

            case 6:
                return -900.0;

            case 7:
                return -800.0;

            case 8:
                return -700.0;

            case 9:
                return -600.0;

            case 10:
                return -500.0;

            case 11:
                return -400.0;

            case 12:
                return -300.0;

            case 13:
                return -200.0;

            case 14:
                return -100.0;

            case 15:
                return 100.0;

            case 16:
                return 200.0;

            case 17:
                return 300.0;

            case 18:
                return 400.0;

            case 19:
                return 500.0;

            case 20:
                return 600.0;

            case 21:
                return 700.0;

            case 22:
                return 800.0;

            case 23:
                return 900.0;

            case 24:
                return 1000.0;

            case 25:
                return 1100.0;

            case 26:
                return 1200.0;

            case 27:
                return 1300.0;

            case 28:
                return 1400.0;

            default:
                System.out.println("\nError: There are only 28 test functions in this test suite!");
                return 0.0;

        }

    }


    @Override
    public double lbound() {
        return -100;
    }

    @Override
    public double ubound() {
        return 100;
    }


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
