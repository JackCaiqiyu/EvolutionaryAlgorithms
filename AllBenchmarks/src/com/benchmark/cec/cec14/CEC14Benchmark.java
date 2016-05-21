package com.benchmark.cec.cec14;

import com.benchmark.AllBenchmarks;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by framg on 02/05/2016.
 */
public class CEC14Benchmark extends AllBenchmarks {
    double[] OShift,M,y,z,x_bound;
    int[] SS;
    boolean ini_flag;
    public static String files = "input_data/";

    public CEC14Benchmark(int DIM, int FUN) {
        super(DIM, FUN, 30);
        ini_flag = true;
    }

    public static int nProblems(){
        return 30;
    }


    @Override
    public double f(double[] x) {
        int cf_num=10,i,j;
        int nx = DIM;
        double f = 0.0;
        if (ini_flag) /* initiailization*/
        {
            ini_flag = false;
            y=new double[nx];
            z=new double[nx];
            x_bound=new double[nx];
            for (i=0; i<nx; i++)
                x_bound[i]=100.0;

            if (!(nx==2||nx==10||nx==20||nx==30||nx==50||nx==100))
            {
                System.out.println("\nError: Test functions are only defined for D=2,10,20,30,50,100.");
            }

            if (nx==2&&((FUN>=17&&FUN<=22)||(FUN>=29&&FUN<=30)))
            {
                System.out.println("\nError: hf01,hf02,hf03,hf04,hf05,hf06,cf07&cf08 are NOT defined for D=2.\n");
            }

			/*Load Matrix M*****************************************************/
            File fpt = readFile( files + "M_"+FUN+"_D"+nx+".txt"); //new File( files + "M_"+FUN+"_D"+nx+".txt");//* Load M data *
            Scanner input = null;
            try {
                input = new Scanner(fpt);
            } catch (FileNotFoundException e) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }
            if (!fpt.exists())
            {
                System.out.println("\n Error: Cannot open input file for reading ");
            }

            if (FUN<23)
            {
                M=new double[nx*nx];

                for (i=0;i<nx*nx; i++)
                {
                    M[i]=Double.parseDouble(input.next());
                }
            }
            else
            {
                M=new double[cf_num*nx*nx];

                for (i=0; i<cf_num*nx*nx; i++)
                {
                    M[i]=Double.parseDouble(input.next());
                }

            }
            input.close();


			/*Load shift_data***************************************************/



            if (FUN<23)
            {
                fpt= readFile( files +"shift_data_"+FUN+".txt");  //new File(files +"shift_data_"+FUN+".txt");
                try {
                    input = new Scanner(fpt);
                } catch (FileNotFoundException e) {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }
                if (!fpt.exists())
                {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }

                OShift=new double[nx];
                for(i=0;i<nx;i++)
                {
                    OShift[i]=Double.parseDouble(input.next());
                    if (OShift == null)
                    {
                        System.out.println("\nError: there is insufficient memory available!");
                    }
                }
                input.close();
            }
            else
            {

                OShift=new double[nx*cf_num];

                fpt=  readFile( files +"shift_data_"+FUN+".txt"); //new File(files +"shift_data_"+FUN+".txt");
                FileReader reader = null;
                try {
                    reader = new FileReader(fpt);
                } catch (FileNotFoundException e) {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }
                BufferedReader br = new BufferedReader(reader);
                String[] s = new String[100];

                for (i=0;i<cf_num;i++){
                    try {
                        s[i] = br.readLine();
                    } catch (IOException e) {
                        System.out.println("\n Error: Cannot open input file for reading ");
                    }
                    String[] array = s[i].split("\\s+");
                    double[] temp = new double[array.length-1];

                    for ( int k = 0; k < array.length-1; k++) {
                        temp[k]= Double.parseDouble(array[k+1]);

                    }

                    for (j=0;j<nx;j++){

                        OShift[i*nx+j] = temp[j];

                    }

                }

                try {
                    br.close();
                    reader.close();
                } catch (IOException e) {
                    System.out.println("\n Error: File is in use. ");
                }

                input.close();


            }

            input.close();



			/*Load Shuffle_data*******************************************/

            if (FUN>=17&&FUN<=22)
            {
                fpt =  readFile(files +"shuffle_data_"+FUN+"_D"+nx+".txt"); //new File(files +"shuffle_data_"+FUN+"_D"+nx+".txt");
                //sprintf(FileName, "input_data/shuffle_data_%d_D%d.txt", func_num, nx);
                //fpt = fopen(FileName,"r");
                try {
                    input = new Scanner(fpt);
                } catch (FileNotFoundException e) {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }
                if (!fpt.exists())
                {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }

                //SS=(int *)malloc(nx*sizeof(int));
                SS = new int[nx];

                for(i=0;i<nx;i++)
                {
                    //fscanf(fpt,"%d",&SS[i]);
                    SS[i] = Integer.parseInt(input.next());
                }
            }
            else if (FUN==29||FUN==30)
            {
                //sprintf(FileName, "input_data/shuffle_data_%d_D%d.txt", func_num, nx);
                fpt =  readFile(files +"shuffle_data_"+FUN+"_D"+nx+".txt"); //new File(files +"shuffle_data_"+FUN+"_D"+nx+".txt");
                //fpt = fopen(FileName,"r");
                try {
                    input = new Scanner(fpt);
                } catch (FileNotFoundException e) {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }
                if (!fpt.exists())
                {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }

                //SS=(int *)malloc(nx*cf_num*sizeof(int));
                SS = new int[nx*cf_num];

                for(i=0;i<nx*cf_num;i++)
                {
                    //fscanf(fpt,"%d",&SS[i]);
                    SS[i] = Integer.parseInt(input.next());
                }
            }
            input.close();

        }

                testfunc14 function = new testfunc14(DIM);
            //testfunc function = new testfunc();

            switch(FUN)
            {
                case 1:
                    f=function.ellips_func(x,f,nx,OShift,M,1,1);
                    break;
                case 2:
                    f=function.bent_cigar_func(x,f,nx,OShift,M,1,1);
                    break;
                case 3:
                    f=function.discus_func(x,f,nx,OShift,M,1,1);
                    break;
                case 4:
                    f=function.rosenbrock_func(x,f,nx,OShift,M,1,1);
                    break;
                case 5:
                    f=function.ackley_func(x,f,nx,OShift,M,1,1);
                    break;
                case 6:
                    f=function.weierstrass_func(x,f,nx,OShift,M,1,1);
                    break;
                case 7:
                    f=function.griewank_func(x,f,nx,OShift,M,1,1);
                    break;
                case 8:
                    f=function.rastrigin_func(x,f,nx,OShift,M,1,0);
                    break;
                case 9:
                    f=function.rastrigin_func(x,f,nx,OShift,M,1,1);
                    break;
                case 10:
                    f=function.schwefel_func(x,f,nx,OShift,M,1,0);
                    break;
                case 11:
                    f=function.schwefel_func(x,f,nx,OShift,M,1,1);
                    break;
                case 12:
                    f=function.katsuura_func(x,f,nx,OShift,M,1,1);
                    break;
                case 13:
                    f=function.happycat_func(x,f,nx,OShift,M,1,1);
                    break;
                case 14:
                    f=function.hgbat_func(x,f,nx,OShift,M,1,1);
                    break;
                case 15:
                    f=function.grie_rosen_func(x,f,nx,OShift,M,1,1);
                    break;
                case 16:
                    f=function.escaffer6_func(x,f,nx,OShift,M,1,1);
                    break;
                case 17:
                    f=function.hf01(x,f,nx,OShift,M,SS,1,1);
                    break;
                case 18:
                    f=function.hf02(x,f,nx,OShift,M,SS,1,1);
                    break;
                case 19:
                    f=function.hf03(x,f,nx,OShift,M,SS,1,1);
                    break;
                case 20:
                    f=function.hf04(x,f,nx,OShift,M,SS,1,1);
                    break;
                case 21:
                    f=function.hf05(x,f,nx,OShift,M,SS,1,1);
                    break;
                case 22:
                    f=function.hf06(x,f,nx,OShift,M,SS,1,1);
                    break;
                case 23:
                    f=function.cf01(x,f,nx,OShift,M,1);
                    break;
                case 24:
                    f=function.cf02(x,f,nx,OShift,M,1);
                    break;
                case 25:
                    f=function.cf03(x,f,nx,OShift,M,1);
                    break;
                case 26:
                    f=function.cf04(x,f,nx,OShift,M,1);
                    break;
                case 27:
                    f=function.cf05(x,f,nx,OShift,M,1);
                    break;
                case 28:
                    f=function.cf06(x,f,nx,OShift,M,1);
                    break;
                case 29:
                    f=function.cf07(x,f,nx,OShift,M,SS,1);
                    break;
                case 30:
                    f=function.cf08(x,f,nx,OShift,M,SS,1);
                    break;

                default:
                    System.err.println("\nError: There are only 30 test functions in this test suite!");
                    System.exit(0);
                    break;
            }


        return f + bias();
    }

    @Override
    public double bias() {
        switch(FUN)
        {
            case 1:
                return 100.0;
                
            case 2:
                return 200.0;
                
            case 3:
                return 300.0;
                
            case 4:
                return 400.0;
                
            case 5:
                return 500.0;
                
            case 6:
                return 600.0;
                
            case 7:
                return 700.0;
                
            case 8:
                return 800.0;
                
            case 9:
                return 900.0;
                
            case 10:
                return 1000.0;
                
            case 11:
                return 1100.0;
                
            case 12:
                return 1200.0;
                
            case 13:
                return 1300.0;
                
            case 14:
                return 1400.0;
                
            case 15:
                return 1500.0;
                
            case 16:
                return 1600.0;
                
            case 17:
                return 1700.0;
                
            case 18:
                return 1800.0;
                
            case 19:
                return 1900.0;
                
            case 20:
                return 2000.0;
                
            case 21:
                return 2100.0;
                
            case 22:
                return 2200.0;
                
            case 23:
                return 2300.0;
                
            case 24:
                return 2400.0;
                
            case 25:
                return 2500.0;
                
            case 26:
                return 2600.0;
                
            case 27:
                return 2700.0;
                
            case 28:
                return 2800.0;
                
            case 29:
                return 2900.0;
                
            case 30:
                return 3000.0;
                

            default:
                System.err.println("\nError: There are only 30 test functions in this test suite!");
                System.exit(0);
                
        }
        return 0;

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
