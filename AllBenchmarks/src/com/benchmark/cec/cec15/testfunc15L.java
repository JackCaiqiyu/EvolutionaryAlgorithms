package com.benchmark.cec.cec15;/*
  CEC15 Test Function Suite for Single Objective Optimization
  BO Zheng (email: zheng.b1988@gmail.com) 
  Nov. 20th 2014
*/


import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import java.io.File;
import java.util.Scanner;

public class testfunc15L {
    final double INF = 1.0e99;
    final double EPS = 1.0e-14;
    final double E = 2.7182818284590452353602874713526625;
    final double PI = 3.1415926535897932384626433832795029;
    //                   1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
    int[] cf_nums = {0, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 5, 5, 5, 7, 10};

    int[] bShuffle = {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0};

    double[] OShift, M, y, z, x_bound, bias;
    int ini_flag, n_flag, func_flag;
    int[] SS;


    testfunc15L(int DIM) {
        y = new double[DIM];
        z = new double[DIM];
    }

    //double sphere_func (double[] , double , int , double[] ,double[] ,int ,int) /* Sphere */
    //double ellips_func (double[] , double , int , double[] ,double[] ,int ,int) /* Ellipsoidal */
    //double bent_cigar_func (double[] , double , int , double[] ,double[] ,int ,int) /* Bent_Cigar */
    //double discus_func_func (double[] , double , int , double[] ,double[] ,int ,int) /* Discus */
    //double dif_powers_func (double[] , double , int , double[] ,double[] ,int ,int) /* Different Powers  */
    //double rosenbrock_func (double[] , double , int , double[] ,double[] ,int ,int) /* Rosenbrock's  */
    //double schaffer_F7_func (double[] , double , int , double[] ,double[] ,int ,int) /* Schwefel's F7  */
    //double ackley_func (double[] , double , int , double[] ,double[] ,int ,int) /* Ackley's  */
    //double rastrigin_func (double[] , double , int , double[] ,double[] ,int ,int) /* Rastrigin's  */
    //double weierstrass_func (double[] , double , int , double[] ,double[] ,int ,int) /* Weierstrass's  */
    //double griewank_func (double[] , double , int , double[] ,double[] ,int ,int) /* Griewank's  */
    //double schwefel_func (double[] , double , int , double[] ,double[] ,int ,int) /* Schwefel's */
    //double katsuura_func (double[] , double , int , double[] ,double[] ,int ,int) /* Katsuura */
    //double bi_rastrigin_func (double[] , double , int , double[] ,double[] ,int ,int) /* Lunacek Bi_rastrigin  */
    //double grie_rosen_func (double[] , double , int , double[] ,double[] ,int ,int) /* Griewank-Rosenbrock  */
    //double escaffer6_func (double[] , double , int , double[] ,double[] ,int ,int) /* Expanded Scaffer��s F6  */
    //double step_rastrigin_func (double[] , double , int , double[] ,double[] ,int ,int) /* Noncontinuous Rastrigin's  */
    //double happycat_func (double[] , double , int , double[] ,double[] ,int ,int) /* HappyCat  */
    //double hgbat_func (double[] , double , int , double[] ,double[] ,int ,int) /* HGBat  */


    //double hf01 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 1 */
    //double hf02 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 2 */
    //double hf03 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 3 */


    //double cf01 (double[] , double[] , int , double[] ,double[] , double[] ,int ) /* Composition Function 1 */
    //double cf02 (double[] , double[] , int , double[] ,double[] ,double[] ,int ) /* Composition Function 2 */
    //double cf03 (double[] , double[] , int , double[] ,double[] ,double[] ,int ) /* Composition Function 3 */
    //double cf04 (double[] , double[] , int , double[] ,double[] ,double[] ,int ) /* Composition Function 4 */
    //double cf05 (double[] , double[] , int , double[] ,double[] ,double[] ,int ) /* Composition Function 5 */
    //double cf06 (double[] , double[] , int , double[] ,double[] ,double[] ,int ) /* Composition Function 6 */
    //double cf07 (double[] , double[] , int , double[] ,double[] ,double[] ,int ) /* Composition Function 7 */


    //void shiftfunc (double[] , double[] , int ,double[] )
    //void rotatefunc (double[] , double[] , int ,double[] )
    //void sr_func (double[] .double[] ,int ,double[] ,double[] ,double ,int ,int )/* shift and rotate*/
    //double cf_cal(double[] , double , int , double[] ,double[] ,double[] ,double[] , int )


    void test_func(double[] x, double[] f, int nx, int mx, int func_num) throws Exception {
        //int cf_num=10,i,j;
        int cf_num, i, j;
        cf_num = cf_nums[func_num];

        if (ini_flag == 1) {
            if ((n_flag != nx) || (func_flag != func_num)) /* check if nx or func_num are changed, reinitialization*/ {
                ini_flag = 0;
            }
        }
        if (ini_flag == 0) /* initiailization*/ {

            y = new double[nx];
            z = new double[nx];
            x_bound = new double[nx];
            for (i = 0; i < nx; i++)
                x_bound[i] = 100.0;

            if (!(nx == 2 || nx == 10 || nx == 20 || nx == 50 || nx == 100)) {
                System.out.println("\nError: Test functions are only defined for D=2,10,30,50,100.");
            }

            if (nx == 2 && ((func_num >= 6 && func_num <= 8) || (func_num == 10) || (func_num == 13))) {
                System.out.println("\nError: hf01,hf02,hf03,cf02&cf05 are NOT defined for D=2.\n");
            }

			/*Load Matrix M*****************************************************/
            File fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/cec15.input_data/M_" + func_num + "_D" + nx + ".txt");//* Load M data *
            Scanner input = new Scanner(fpt);
            if (!fpt.exists()) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }

            M = new double[cf_num * nx * nx];


            for (i = 0; i < cf_num * nx * nx; i++) {
                M[i] = Double.parseDouble(input.next());
            }
            //System.out.println(cf_num*nx*nx-1);

			/*if (func_num<9)
			{
				M=new double[nx*nx];

				for (i=0;i<nx*nx; i++)
				{
					M[i]=input.nextDouble();
				}
			}
			else
			{
				M=new double[cf_num*nx*nx];

				for (i=0; i<cf_num*nx*nx; i++)
				{
						M[i]=input.nextDouble();
				}

			}*/

            input.close();


			/*Load Bias_value bias*************************************************/

			/*if (func_num>=9){

			fpt = new File("cec15.input_data/bias_"+func_num+".txt");//* Load bias data *
			input = new Scanner(fpt);
			if (!fpt.exists())
			{
			    System.out.println("\n Error: Cannot open input file for reading ");
			}

			bias=new double[cf_num];

			for (i=0;i<cf_num; i++)
			{
				bias[i]=input.nextDouble();
			}
			input.close();
			}*/

            if (cf_num > 1) {

                fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/cec15.input_data/bias_" + func_num + ".txt");//* Load bias data *
                input = new Scanner(fpt);
                if (!fpt.exists()) {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }
                bias = new double[cf_num];
                for (i = 0; i < cf_num; i++) {
                    bias[i] = Double.parseDouble(input.next());
                }
                input.close();


            }




			/*Load shift_data***************************************************/

            fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/cec15.input_data/shift_data_" + func_num + ".txt");
            input = new Scanner(fpt);
            if (!fpt.exists()) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }

            OShift = new double[cf_num * nx];

			/*for(i=0;i<cf_num*nx;i++)
			{
				OShift[i]=input.nextDouble();
				//System.out.println(OShift[i]);
			}*/
            if (func_num < 9) {
                for (i = 0; i < nx * cf_nums[func_num]; i++) {

                    OShift[i] = Double.parseDouble(input.next());
                }
            } else {
                for (i = 0; i < cf_nums[func_num] - 1; i++) {
                    for (j = 0; j < nx; j++) {
                        OShift[i * nx + j] = input.nextDouble();
                    }
                    String sss = input.nextLine();

                    //System.out.println(OShift[i*nx+j]);
                }
                for (j = 0; j < nx; j++) {
                    OShift[(cf_nums[func_num] - 1) * nx + j] = input.nextDouble();
                }

            }

			/*for(i=0;i<cf_num*nx;i++)
			{
				//OShift[i]=input.nextDouble();
				System.out.println(OShift[i]);
			}*/


            input.close();


			/*if (func_num<9)
			{
				fpt=new File("cec15.input_data/shift_data_"+func_num+".txt");
				input = new Scanner(fpt);
				if (!fpt.exists())
				{
					System.out.println("\n Error: Cannot open input file for reading ");
				}

				OShift=new double[nx];
				for(i=0;i<nx;i++)
				{
					OShift[i]=input.nextDouble();
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

				fpt=new File("cec15.input_data/shift_data_"+func_num+".txt");
				FileReader reader = new FileReader(fpt);
				BufferedReader br = new BufferedReader(reader);
				String[] s = new String[100];

				for (i=0;i<cf_num;i++){
					s[i] = br.readLine();
					String[] array = s[i].split("\\s+");
					double[] temp = new double[array.length-1];

					for ( int k = 0; k < array.length-1; k++) {
					    temp[k]= Double.parseDouble(array[k+1]);

					}

					for (j=0;j<nx;j++){

						OShift[i*nx+j] = temp[j];

					}

				}

				br.close();
				reader.close();
				input.close();


			}

			input.close();*/






			/*Load Shuffle_data*******************************************/

            if (bShuffle[func_num] == 1) {
                fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/cec15.input_data/shuffle_data_" + func_num + "_D" + nx + ".txt");
                input = new Scanner(fpt);
                if (!fpt.exists()) {
                    System.out.println("\n Error: Cannot open input file for reading ");
                }
                SS = new int[cf_num * nx];

                for (i = 0; i < cf_num * nx; i++) {
                    //fscanf(fpt,"%d",&SS[i]);
                    SS[i] = Integer.parseInt(input.next());
                }
                input.close();
            }






			/*if (func_num>=6&&func_num<=8)
			{
				fpt = new File("cec15.input_data/shuffle_data_"+func_num+"_D"+nx+".txt");
				//sprintf(FileName, "cec15.input_data/shuffle_data_%d_D%d.txt", func_num, nx);
				//fpt = fopen(FileName,"r");
				input = new Scanner(fpt);
				if (!fpt.exists())
				{
				    System.out.println("\n Error: Cannot open input file for reading ");
				}

				//SS=(int *)malloc(nx*sizeof(int));
				SS = new int[nx];

				for(i=0;i<nx;i++)
				{
					//fscanf(fpt,"%d",&SS[i]);
					SS[i] = input.nextInt();
				}
			}
			else if (func_num==10||func_num==13)
			{
				//sprintf(FileName, "cec15.input_data/shuffle_data_%d_D%d.txt", func_num, nx);
				fpt = new File("cec15.input_data/shuffle_data_"+func_num+"_D"+nx+".txt");
				//fpt = fopen(FileName,"r");
				input = new Scanner(fpt);
				if (!fpt.exists())
				{
				    System.out.println("\n Error: Cannot open input file for reading ");
				}

				//SS=(int *)malloc(nx*cf_num*sizeof(int));
				SS = new int[nx*cf_num];

				for(i=0;i<nx*cf_num;i++)
				{
					//fscanf(fpt,"%d",&SS[i]);
					SS[i] = input.nextInt();
				}
			}
			input.close();

			n_flag=nx;
			func_flag=func_num;
			ini_flag=1;


		}*/


            double[] t = new double[nx];

            for (i = 0; i < mx; i++) {
                for (j = 0; j < nx; j++) {
                    t[j] = x[i * nx + j];
                }

                switch (func_num) {
                    case 1:
                        f[i] = ellips_func(t, f[i], nx, OShift, M, 1, 1);
                        f[i] += 100.0;
                        break;
                    case 2:
                        f[i] = bent_cigar_func(t, f[i], nx, OShift, M, 1, 1);
                        f[i] += 200.0;
                        break;
                    case 3:
                        f[i] = ackley_func(t, f[i], nx, OShift, M, 1, 1);
                        f[i] += 300.0;
                        break;
                    case 4:
                        f[i] = rastrigin_func(t, f[i], nx, OShift, M, 1, 1);
                        f[i] += 400.0;
                        break;
                    case 5:
                        f[i] = schwefel_func(t, f[i], nx, OShift, M, 1, 1);
                        f[i] += 500.0;
                        break;
                    case 6:
                        f[i] = hf01(t, f[i], nx, OShift, M, SS, 1, 1);
                        f[i] += 600.0;
                        break;
                    case 7:
                        f[i] = hf02(t, f[i], nx, OShift, M, SS, 1, 1);
                        f[i] += 700.0;
                        break;
                    case 8:
                        f[i] = hf03(t, f[i], nx, OShift, M, SS, 1, 1);
                        f[i] += 800.0;
                        break;
                    case 9:
                        f[i] = cf01(t, f[i], nx, OShift, M, bias, 1);
                        f[i] += 900.0;
                        break;
                    case 10:
                        f[i] = cf02(t, f[i], nx, OShift, M, SS, bias, 1);
                        f[i] += 1000.0;
                        break;
                    case 11:
                        f[i] = cf03(t, f[i], nx, OShift, M, bias, 1);
                        f[i] += 1100.0;
                        break;
                    case 12:
                        f[i] = cf04(t, f[i], nx, OShift, M, bias, 1);
                        f[i] += 1200.0;
                        break;
                    case 13:
                        f[i] = cf05(t, f[i], nx, OShift, M, SS, bias, 1);
                        f[i] += 1300.0;
                        break;
                    case 14:
                        f[i] = cf06(t, f[i], nx, OShift, M, bias, 1);
                        f[i] += 1400.0;
                        break;
                    case 15:
                        f[i] = cf07(t, f[i], nx, OShift, M, bias, 1);
                        f[i] += 1500.0;
                        break;


                    default:
                        System.out.println("\nError: There are only 15 test functions in this test suite!");
                        f[i] = 0.0;
                        break;
                }

            }

        }
    }


    double ellips_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Ellipsoidal */ {
        int i;
        f = 0.0;
        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            f += Math.pow(10.0, 6.0 * i / (nx - 1)) * z[i] * z[i];
        }
        return f;
    }


    double[] ellips_func_diff(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Ellipsoidal */ {
        int i;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            g[i] = 0;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            //DerivativeStructure diff = new DerivativeStructure(1, 2, 0, z[i]);
            //for(int j=0; j<10; j++){
            sum = sum.add(diffs[i].pow(2).multiply(Math.pow(10.0, 6.0 * i / (nx - 1))));

            //}
           // g[i] = diff.getPartialDerivative(1);
        }

        if(diff_enabled){
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = sum.getPartialDerivative(indexs);
            }
        }else{
            g[0] = sum.getValue();
        }

        return g;
    }

    DerivativeStructure ellips_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Ellipsoidal */ {
        int i;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            g[i] = 0;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            //DerivativeStructure diff = new DerivativeStructure(1, 2, 0, z[i]);
            //for(int j=0; j<10; j++){
            sum = sum.add(diffs[i].pow(2).multiply(Math.pow(10.0, 6.0 * i / (nx - 1))));

            //}
            // g[i] = diff.getPartialDerivative(1);
        }



        return sum;
    }

    double bent_cigar_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Bent_Cigar */ {
        int i;
        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        f = z[0] * z[0];
        for (i = 1; i < nx; i++) {
            f += Math.pow(10.0, 6.0) * z[i] * z[i];
        }
        return f;
    }

    double[] bent_cigar_func_diff(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Bent_Cigar */ {
        int i;
        double[] g = new double[nx];
        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        //double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            g[i] = 0;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure f = diffs[0].pow(2);
        for (i = 1; i < nx; i++) {
            f= f.add(diffs[i].pow(2).multiply(Math.pow(10, 6)));

        }

        if(diff_enabled){
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = f.getPartialDerivative(indexs);
            }
        }else{
            g[0] = f.getValue();
        }

        return g;
    }

    DerivativeStructure bent_cigar_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Bent_Cigar */ {
        int i;
        double[] g = new double[nx];
        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        //double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            g[i] = 0;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure f = diffs[0].pow(2);
        for (i = 1; i < nx; i++) {
            f= f.add(diffs[i].pow(2).multiply(Math.pow(10, 6)));

        }


        return f;
    }

    double discus_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Discus */ {
        int i;
        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        f = Math.pow(10.0, 6.0) * z[0] * z[0];
        for (i = 1; i < nx; i++) {
            f += z[i] * z[i];
        }

        return f;
    }

    double rosenbrock_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Rosenbrock's */ {
        int i;
        double tmp1, tmp2;
        f = 0.0;
        sr_func(x, z, nx, Os, Mr, 2.048 / 100.0, s_flag, r_flag);/*shift and rotate*/
        z[0] += 1.0; //shift to origin
        for (i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0; //shift to orgin
            tmp1 = z[i] * z[i] - z[i + 1];
            tmp2 = z[i] - 1.0;
            f += 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        }


        return f;
    }

    double [] rosenbrock_func_diff(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Rosenbrock's */ {
        int i;
       // double tmp1, tmp2;
        //f = 0.0;
        sr_func(x, z, nx, Os, Mr, 2.048 / 100.0, s_flag, r_flag);/*shift and rotate*/
        double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            g[i] = 0;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        diffs[0] = diffs[0].add(1.0);
       // z[0] += 1.0; //shift to origin
        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx - 1; i++) {
//            z[i + 1] += 1.0; //shift to orgin
//            tmp1 = z[i] * z[i] - z[i + 1];
//            tmp2 = z[i] - 1.0;
//            f += 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
            diffs[i+1] = diffs[i+1].add(1.0);
            DerivativeStructure tmp1 = diffs[i].pow(2).subtract(diffs[i+1]);
            DerivativeStructure tmp2 = diffs[i].subtract(1.0);
            sum = sum.add(tmp2.pow(2).add(tmp1.pow(2).multiply(100.0)));
        }


        if(diff_enabled){
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = sum.getPartialDerivative(indexs);
            }
        }else{
            g[0] = sum.getValue();
        }

        return g;
    }

    DerivativeStructure rosenbrock_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Rosenbrock's */ {
        int i;
        // double tmp1, tmp2;
        //f = 0.0;
        sr_func(x, z, nx, Os, Mr, 2.048 / 100.0, s_flag, r_flag);/*shift and rotate*/
        double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            g[i] = 0;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        diffs[0] = diffs[0].add(1.0);
        // z[0] += 1.0; //shift to origin
        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx - 1; i++) {
//            z[i + 1] += 1.0; //shift to orgin
//            tmp1 = z[i] * z[i] - z[i + 1];
//            tmp2 = z[i] - 1.0;
//            f += 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
            diffs[i+1] = diffs[i+1].add(1.0);
            DerivativeStructure tmp1 = diffs[i].pow(2).subtract(diffs[i+1]);
            DerivativeStructure tmp2 = diffs[i].subtract(1.0);
            sum = sum.add(tmp2.pow(2).add(tmp1.pow(2).multiply(100.0)));
        }


//        if(diff_enabled){
//            for (i = 0; i < nx; i++) {
//                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//                indexs[i] = 1;
//                g[i] = sum.getPartialDerivative(indexs);
//            }
//        }else{
//            g[0] = sum.getValue();
//        }

        return sum;
    }

    double ackley_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Ackley's  */ {
        int i;
        double sum1, sum2;
        sum1 = 0.0;
        sum2 = 0.0;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            sum1 += z[i] * z[i];
            sum2 += Math.cos(2.0 * PI * z[i]);
        }
        sum1 = -0.2 * Math.sqrt(sum1 / nx);
        sum2 /= nx;
        f = E - 20.0 * Math.exp(sum1) - Math.exp(sum2) + 20.0;

        return f;
    }

    double[] ackley_func_diff(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Ackley's  */ {
        int i;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum1 = diffs[0].getField().getZero();
        DerivativeStructure sum2 = diffs[0].getField().getZero();
        for (int j = 0; j < nx; j++) {
            sum1 = sum1.add(diffs[j].pow(2));
            sum2 = sum2.add(diffs[j].multiply(2 * PI).cos());
        }

        sum1 = sum1.divide(nx).sqrt().multiply(-0.2);
        sum2 = sum2.divide(nx);
        DerivativeStructure diff = sum1.exp().multiply(20.0).subtract(sum2.exp().add(20.0)).subtract(E);

        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = diff.getPartialDerivative(indexs);
            }
        }else{
            g[0] = diff.getValue();
        }

        return g;
    }

    DerivativeStructure ackley_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Ackley's  */ {
        int i;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        double[] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum1 = diffs[0].getField().getZero();
        DerivativeStructure sum2 = diffs[0].getField().getZero();
        for (int j = 0; j < nx; j++) {
            sum1 = sum1.add(diffs[j].pow(2));
            sum2 = sum2.add(diffs[j].multiply(2 * PI).cos());
        }

        sum1 = sum1.divide(nx).sqrt().multiply(-0.2);
        sum2 = sum2.divide(nx);
        DerivativeStructure diff = sum1.exp().multiply(20.0).subtract(sum2.exp().add(20.0)).subtract(E);


        return diff;
    }

    double weierstrass_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Weierstrass's  */ {
        int i, j, k_max;
        double sum, sum2 = 0, a, b;

        sr_func(x, z, nx, Os, Mr, 0.5 / 100.0, s_flag, r_flag);/*shift and rotate*/


        a = 0.5;
        b = 3.0;
        k_max = 20;
        f = 0.0;
        for (i = 0; i < nx; i++) {
            sum = 0.0;
            sum2 = 0.0;
            for (j = 0; j <= k_max; j++) {
                sum += Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j) * (z[i] + 0.5));
                sum2 += Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j) * 0.5);
            }
            f += sum;
        }
        f -= nx * sum2;

        return f;
    }

    double [] weierstrass_func_diff(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Weierstrass's  */ {
        int i, j, k_max;
        double  a, b;

        sr_func(x, z, nx, Os, Mr, 0.5 / 100.0, s_flag, r_flag);/*shift and rotate*/
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }


        a = 0.5;
        b = 3.0;
        k_max = 20;
        //f = 0.0;
        double sum2 = 0;
        DerivativeStructure f = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            DerivativeStructure sum = diffs[0].getField().getZero();
            //DerivativeStructure sum2 = diffs[0].getField().getZero();
            sum2 = 0;
            for (j = 0; j <= k_max; j++) {
               // sum += Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j) * (z[i] + 0.5));
                sum2 += Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j) * 0.5);
                sum = sum.add(diffs[i].add(0.5).multiply(Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j))));
                //sum2 = sum2.add(diffs[i])
            }
            f = f.add(sum);
        }
       // f -= nx * sum2;
        f = f.subtract(nx * sum2);


        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = f.getPartialDerivative(indexs);
            }
        }else{
            g[0] = f.getValue();
        }

        return g;
    }

    DerivativeStructure weierstrass_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Weierstrass's  */ {
        int i, j, k_max;
        double  a, b;

        sr_func(x, z, nx, Os, Mr, 0.5 / 100.0, s_flag, r_flag);/*shift and rotate*/
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }


        a = 0.5;
        b = 3.0;
        k_max = 20;
        //f = 0.0;
        double sum2 = 0;
        DerivativeStructure f = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            DerivativeStructure sum = diffs[0].getField().getZero();
            //DerivativeStructure sum2 = diffs[0].getField().getZero();
            sum2 = 0;
            for (j = 0; j <= k_max; j++) {
                // sum += Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j) * (z[i] + 0.5));
                sum2 += Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j) * 0.5);
                sum = sum.add(diffs[i].add(0.5).multiply(Math.pow(a, j) * Math.cos(2.0 * PI * Math.pow(b, j))));
                //sum2 = sum2.add(diffs[i])
            }
            f = f.add(sum);
        }
        // f -= nx * sum2;
        f = f.subtract(nx * sum2);


//        if(diff_enabled) {
//            for (i = 0; i < nx; i++) {
//                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//                indexs[i] = 1;
//                g[i] = f.getPartialDerivative(indexs);
//            }
//        }else{
//            g[0] = f.getValue();
//        }

        return f;
    }

    double griewank_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank's  */ {
        int i;
        double s, p;

        sr_func(x, z, nx, Os, Mr, 600.0 / 100.0, s_flag, r_flag);/*shift and rotate*/

        s = 0.0;
        p = 1.0;
        for (i = 0; i < nx; i++) {
            s += z[i] * z[i];
            p *= Math.cos(z[i] / Math.sqrt(1.0 + i));
        }
        f = 1.0 + s / 4000.0 - p;

        return f;
    }

    double [] griewank_func_diff(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Griewank's  */ {
        int i;
        //double s, p;

        sr_func(x, z, nx, Os, Mr, 600.0 / 100.0, s_flag, r_flag);/*shift and rotate*/
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }
        DerivativeStructure s = diffs[0].getField().getZero();
        DerivativeStructure p = diffs[0].getField().getZero();
       // s = 0.0;
       // p = 1.0;
        for (i = 0; i < nx; i++) {
            //s += z[i] * z[i];
           // p *= Math.cos(z[i] / Math.sqrt(1.0 + i));
            s = s.add(diffs[i].pow(2));
            p = p.multiply(diffs[i].divide(Math.sqrt(1.0 + i)).cos());
        }

        DerivativeStructure diff = s.divide(4000.0).subtract(p).add(1.0);
        for(i = 0; i<nx; i++){

        }
        //f = 1.0 + s / 4000.0 - p;

        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = diff.getPartialDerivative(indexs);
            }
        }else{
            g[0] = diff.getValue();
        }

        return g;
    }

    DerivativeStructure griewank_func_diff_ds(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Griewank's  */ {
        int i;
        //double s, p;

        sr_func(x, z, nx, Os, Mr, 600.0 / 100.0, s_flag, r_flag);/*shift and rotate*/
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }
        DerivativeStructure s = diffs[0].getField().getZero();
        DerivativeStructure p = diffs[0].getField().getZero();
        // s = 0.0;
        // p = 1.0;
        for (i = 0; i < nx; i++) {
            //s += z[i] * z[i];
            // p *= Math.cos(z[i] / Math.sqrt(1.0 + i));
            s = s.add(diffs[i].pow(2));
            p = p.multiply(diffs[i].divide(Math.sqrt(1.0 + i)).cos());
        }

        DerivativeStructure diff = s.divide(4000.0).subtract(p).add(1.0);
        for(i = 0; i<nx; i++){

        }
        //f = 1.0 + s / 4000.0 - p;

//        if(diff_enabled) {
//            for (i = 0; i < nx; i++) {
//                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//                indexs[i] = 1;
//                g[i] = diff.getPartialDerivative(indexs);
//            }
//        }else{
//            g[0] = diff.getValue();
//        }

        return diff;
    }

    double rastrigin_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Rastrigin's  */ {
        int i;
        f = 0.0;


		/*for (int j=0;j<nx;j++)
		{
			System.out.println(Os[j]);
		}*/

        sr_func(x, z, nx, Os, Mr, 5.12 / 100.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            f += (z[i] * z[i] - 10.0 * Math.cos(2.0 * PI * z[i]) + 10.0);
        }

        return f;
    }

    double [] rastrigin_func_diff(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Rastrigin's  */ {
        int i;



		/*for (int j=0;j<nx;j++)
		{
			System.out.println(Os[j]);
		}*/

        sr_func(x, z, nx, Os, Mr, 5.12 / 100.0, s_flag, r_flag);/*shift and rotate*/

//        for (i = 0; i < nx; i++) {
//            f += (z[i] * z[i] - 10.0 * Math.cos(2.0 * PI * z[i]) + 10.0);
//        }
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            sum = sum.add(diffs[i].pow(2).subtract(diffs[i].multiply(PI*2.0).cos().multiply(10.0)).add(10));
        }

        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = sum.getPartialDerivative(indexs);
            }
        }else{
            g[0] = sum.getValue();
        }

        return g;
    }


    DerivativeStructure rastrigin_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Rastrigin's  */ {
        int i;



		/*for (int j=0;j<nx;j++)
		{
			System.out.println(Os[j]);
		}*/

        sr_func(x, z, nx, Os, Mr, 5.12 / 100.0, s_flag, r_flag);/*shift and rotate*/

//        for (i = 0; i < nx; i++) {
//            f += (z[i] * z[i] - 10.0 * Math.cos(2.0 * PI * z[i]) + 10.0);
//        }
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            sum = sum.add(diffs[i].pow(2).subtract(diffs[i].multiply(PI*2.0).cos().multiply(10.0)).add(10));
        }



        return sum;
    }

    double schwefel_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Schwefel's  */ {
        int i;
        double tmp;

        sr_func(x, z, nx, Os, Mr, 1000.0 / 100.0, s_flag, r_flag);/*shift and rotate*/


        f = 0;
        for (i = 0; i < nx; i++) {
            z[i] += 4.209687462275036e+002;
            if (z[i] > 500) {
                f -= (500.0 - (z[i] % 500)) * Math.sin(Math.pow(500.0 - (z[i] % 500), 0.5));
                tmp = (z[i] - 500.0) / 100;
                f += tmp * tmp / nx;
            } else if (z[i] < -500) {
                f -= (-500.0 + (Math.abs(z[i]) % 500)) * Math.sin(Math.pow(500.0 - (Math.abs(z[i]) % 500), 0.5));
                tmp = (z[i] + 500.0) / 100;
                f += tmp * tmp / nx;
            } else
                f -= z[i] * Math.sin(Math.pow(Math.abs(z[i]), 0.5));
        }
        f = 4.189828872724338e+002 * nx + f;

        return f;
    }

    double [] schwefel_func_diff(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Schwefel's  */ {
        int i;

        sr_func(x, z, nx, Os, Mr, 1000.0 / 100.0, s_flag, r_flag);/*shift and rotate*/

        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            z[i] += 4.209687462275036e+002;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            //z[i] += 4.209687462275036e+002;
           // DerivativeStructure diff = new DerivativeStructure(1, 2, 0, z[i]);

            if (z[i] > 500) {
                sum = sum.subtract(diffs[i].remainder(500).subtract(500.0).negate()).multiply((diffs[i].remainder(500).subtract(500).negate().pow(0.5)).sin());
                sum = sum.add((diffs[i].subtract(500.0).divide(100)).pow(2).divide(nx));
//                f -= (500.0 - (z[i] % 500)) * Math.sin(Math.pow(500.0 - (z[i] % 500), 0.5));
//                tmp = (z[i] - 500.0) / 100;
//                f += tmp * tmp / nx;
               // diffs[i].
            } else if (z[i] < -500) {
                sum = sum.subtract(diffs[i].abs().remainder(500).subtract(500.0)).multiply((diffs[i].abs().remainder(500).subtract(500).negate().pow(0.5)).sin());
                sum = sum.add((diffs[i].add(500.0).divide(100)).pow(2).divide(nx));
              //  f -= (-500.0 + (Math.abs(z[i]) % 500)) * Math.sin(Math.pow(500.0 - (Math.abs(z[i]) % 500), 0.5));
                //tmp = (z[i] + 500.0) / 100;
              //  f += tmp * tmp / nx;
            } else{
                sum = sum.subtract(diffs[i].multiply(diffs[i].abs().pow(0.5).sin()));
            }
              //  f -= z[i] * Math.sin(Math.pow(Math.abs(z[i]), 0.5));
        }
        DerivativeStructure diff = sum.add(nx * 4.189828872724338e+002);


      //  f = 4.189828872724338e+002 * nx + f;
        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = diff.getPartialDerivative(indexs);
            }

            return g;
        }else{
            g[0] = diff.getValue();

            return g;
        }
    }

    DerivativeStructure schwefel_func_diff_ds(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Schwefel's  */ {
        int i;

        sr_func(x, z, nx, Os, Mr, 1000.0 / 100.0, s_flag, r_flag);/*shift and rotate*/

        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            z[i] += 4.209687462275036e+002;
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure sum = diffs[0].getField().getZero();
        for (i = 0; i < nx; i++) {
            //z[i] += 4.209687462275036e+002;
            // DerivativeStructure diff = new DerivativeStructure(1, 2, 0, z[i]);

            if (z[i] > 500) {
                sum = sum.subtract(diffs[i].remainder(500).subtract(500.0).negate()).multiply((diffs[i].remainder(500).subtract(500).negate().pow(0.5)).sin());
                sum = sum.add((diffs[i].subtract(500.0).divide(100)).pow(2).divide(nx));
//                f -= (500.0 - (z[i] % 500)) * Math.sin(Math.pow(500.0 - (z[i] % 500), 0.5));
//                tmp = (z[i] - 500.0) / 100;
//                f += tmp * tmp / nx;
                // diffs[i].
            } else if (z[i] < -500) {
                sum = sum.subtract(diffs[i].abs().remainder(500).subtract(500.0)).multiply((diffs[i].abs().remainder(500).subtract(500).negate().pow(0.5)).sin());
                sum = sum.add((diffs[i].add(500.0).divide(100)).pow(2).divide(nx));
                //  f -= (-500.0 + (Math.abs(z[i]) % 500)) * Math.sin(Math.pow(500.0 - (Math.abs(z[i]) % 500), 0.5));
                //tmp = (z[i] + 500.0) / 100;
                //  f += tmp * tmp / nx;
            } else{
                sum = sum.subtract(diffs[i].multiply(diffs[i].abs().pow(0.5).sin()));
            }
            //  f -= z[i] * Math.sin(Math.pow(Math.abs(z[i]), 0.5));
        }
        DerivativeStructure diff = sum.add(nx * 4.189828872724338e+002);


        //  f = 4.189828872724338e+002 * nx + f;
        return diff;
    }

//    DerivativeStructure schwefel_func_diff(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Schwefel's  */ {
//        int i;
//
//        sr_func(x, z, nx, Os, Mr, 1000.0 / 100.0, s_flag, r_flag);/*shift and rotate*/
//
//        double [] g = new double[nx];
//        DerivativeStructure[] diffs = new DerivativeStructure[nx];
//        for (i = 0; i < nx; i++) {
//            z[i] += 4.209687462275036e+002;
//            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
//        }
//
//        DerivativeStructure sum = diffs[0].getField().getZero();
//        for (i = 0; i < nx; i++) {
//            //z[i] += 4.209687462275036e+002;
//            // DerivativeStructure diff = new DerivativeStructure(1, 2, 0, z[i]);
//
//            if (z[i] > 500) {
//                sum = sum.subtract(diffs[i].remainder(500).subtract(500.0).negate()).multiply((diffs[i].remainder(500).subtract(500).negate().pow(0.5)).sin());
//                sum = sum.add((diffs[i].subtract(500.0).divide(100)).pow(2).divide(nx));
////                f -= (500.0 - (z[i] % 500)) * Math.sin(Math.pow(500.0 - (z[i] % 500), 0.5));
////                tmp = (z[i] - 500.0) / 100;
////                f += tmp * tmp / nx;
//                // diffs[i].
//            } else if (z[i] < -500) {
//                sum = sum.subtract(diffs[i].abs().remainder(500).subtract(500.0)).multiply((diffs[i].abs().remainder(500).subtract(500).negate().pow(0.5)).sin());
//                sum = sum.add((diffs[i].add(500.0).divide(100)).pow(2).divide(nx));
//                //  f -= (-500.0 + (Math.abs(z[i]) % 500)) * Math.sin(Math.pow(500.0 - (Math.abs(z[i]) % 500), 0.5));
//                //tmp = (z[i] + 500.0) / 100;
//                //  f += tmp * tmp / nx;
//            } else{
//                sum = sum.subtract(diffs[i].multiply(diffs[i].abs().pow(0.5).sin()));
//            }
//            //  f -= z[i] * Math.sin(Math.pow(Math.abs(z[i]), 0.5));
//        }
//        DerivativeStructure diff = sum.add(nx * 4.189828872724338e+002);
//
//
//        return diff;
//    }

    double katsuura_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Katsuura  */ {
        int i, j;
        double temp, tmp1, tmp2, tmp3;
        tmp3 = Math.pow(1.0 * nx, 1.2);

        sr_func(x, z, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/


        f = 1.0;
        for (i = 0; i < nx; i++) {
            temp = 0.0;
            for (j = 1; j <= 32; j++) {
                tmp1 = Math.pow(2.0, j);
                tmp2 = tmp1 * z[i];
                temp += Math.abs(tmp2 - Math.floor(tmp2 + 0.5)) / tmp1;
            }
            f *= Math.pow(1.0 + (i + 1) * temp, 10.0 / tmp3);
        }
        tmp1 = 10.0 / nx / nx;
        f = f * tmp1 - tmp1;

        return f;

    }


    DerivativeStructure katsuura_func_diff_ds(double[] x,int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Katsuura  */ {
        int i, j;
        double  tmp1, tmp3;
        tmp3 = Math.pow(1.0 * nx, 1.2);

        sr_func(x, z, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }


       // f = 1.0;
        DerivativeStructure f = diffs[0].getField().getOne();
        for (i = 0; i < nx; i++) {
            //temp = 0.0;
            DerivativeStructure temp = diffs[0].getField().getZero();
            for (j = 1; j <= 32; j++) {
                tmp1 = Math.pow(2.0, j);
                //tmp2 = tmp1 * z[i];
                DerivativeStructure tmp2 =  diffs[i].multiply(tmp1);
               // temp += Math.abs(tmp2 - Math.floor(tmp2 + 0.5)) / tmp1;
                temp = temp.add(tmp2.subtract(tmp2.add(0.5).floor()).abs().divide(tmp1));
            }
            //f *= Math.pow(1.0 + (i + 1) * temp, 10.0 / tmp3);
            f = f.multiply(temp.multiply((i + 1)).add(1.0).pow(10.0/tmp3));
        }
        tmp1 = 10.0 / nx / nx;
        //f = f * tmp1 - tmp1;
        f = f.multiply(tmp1).subtract(tmp1);
        return f;

    }

    double happycat_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag)
	/*HappyCat, probided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...,-1]*/ {
        int i;
        double alpha, r2, sum_z;
        alpha = 1.0 / 8.0;

        sr_func(x, z, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/

        r2 = 0.0;
        sum_z = 0.0;
        f = 0.0;
        for (i = 0; i < nx; i++) {
            z[i] = z[i] - 1.0; //shift to orgin
            r2 += z[i] * z[i];
            sum_z += z[i];

        }
        f = Math.pow(Math.abs(r2 - nx), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;

        return f;
    }

    DerivativeStructure happycat_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag)
	/*HappyCat, probided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...,-1]*/ {
        int i;
        double alpha;//, r2, sum_z;
        alpha = 1.0 / 8.0;

        sr_func(x, z, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure r2 = diffs[0].getField().getZero();
        DerivativeStructure sum_z = diffs[0].getField().getZero();
//        r2 = 0.0;
//        sum_z = 0.0;
        //f = 0.0;
        for (i = 0; i < nx; i++) {
//            z[i] = z[i] - 1.0; //shift to orgin
//            r2 += z[i] * z[i];
//            sum_z += z[i];
            diffs[i] = diffs[i].subtract(1.0);
            r2 = r2.add(diffs[i].pow(2));
            sum_z = sum_z.add(diffs[i]);

        }
        //f = Math.pow(Math.abs(r2 - nx), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;

        DerivativeStructure f = r2.subtract(nx).abs().pow(2*alpha).add(r2.multiply(0.5).add(sum_z).divide(nx + 0.5));

        return f;
    }

    double hgbat_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag)
	/*HGBat, provided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...-1]*/ {
        int i;
        double alpha, r2, sum_z;
        alpha = 1.0 / 4.0;

        sr_func(x, z, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */

        r2 = 0.0;
        sum_z = 0.0;
        for (i = 0; i < nx; i++) {
            z[i] = z[i] - 1.0;//shift to orgin
            r2 += z[i] * z[i];
            sum_z += z[i];
        }
        f = Math.pow(Math.abs(Math.pow(r2, 2.0) - Math.pow(sum_z, 2.0)), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;
        return f;

    }

    double []  hgbat_func_diff(double[] x,int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled)
	/*HGBat, provided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...-1]*/ {
        int i;
        double alpha;
        alpha = 1.0 / 4.0;

        sr_func(x, z, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */

        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure r2 = diffs[0].getField().getZero();
        DerivativeStructure sum_z = diffs[0].getField().getZero();
       // r2 = 0.0;
       // sum_z = 0.0;
        for (i = 0; i < nx; i++) {
//            z[i] = z[i] - 1.0;//shift to orgin
//            r2 += z[i] * z[i];
//            sum_z += z[i];
            diffs[i] = diffs[i].subtract(1.0);
            r2 = r2.add(diffs[i].pow(2));
            sum_z = sum_z.add(diffs[i]);

        }
        //f = Math.pow(Math.abs(Math.pow(r2, 2.0) - Math.pow(sum_z, 2.0)), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;
        DerivativeStructure f = r2.pow(2).subtract(sum_z.pow(2)).abs().pow(2*alpha).add(r2.multiply(0.5).add(sum_z).divide(nx).add(0.5));
        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = f.getPartialDerivative(indexs);
            }

            return g;
        }else{
            g[0] = f.getValue();

            return g;
        }

    }


    DerivativeStructure hgbat_func_diff_ds(double[] x,int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled)
	/*HGBat, provided by Hans-Georg Beyer (HGB)*/
	/*original global optimum: [-1,-1,...-1]*/ {
        int i;
        double alpha;
        alpha = 1.0 / 4.0;

        sr_func(x, z, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */

        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure r2 = diffs[0].getField().getZero();
        DerivativeStructure sum_z = diffs[0].getField().getZero();
        // r2 = 0.0;
        // sum_z = 0.0;
        for (i = 0; i < nx; i++) {
//            z[i] = z[i] - 1.0;//shift to orgin
//            r2 += z[i] * z[i];
//            sum_z += z[i];
            diffs[i] = diffs[i].subtract(1.0);
            r2 = r2.add(diffs[i].pow(2));
            sum_z = sum_z.add(diffs[i]);

        }
        //f = Math.pow(Math.abs(Math.pow(r2, 2.0) - Math.pow(sum_z, 2.0)), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;
        DerivativeStructure f = r2.pow(2).subtract(sum_z.pow(2)).abs().pow(2*alpha).add(r2.multiply(0.5).add(sum_z).divide(nx).add(0.5));
        return f;

    }

    double grie_rosen_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank-Rosenbrock  */ {
        int i;
        double temp, tmp1, tmp2;

        sr_func(x, z, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */


        f = 0.0;

        z[0] += 1.0; //shift to orgin
        for (i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0; //shift to orgin
            tmp1 = z[i] * z[i] - z[i + 1];
            tmp2 = z[i] - 1.0;
            temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
            f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;
        }
        tmp1 = z[nx - 1] * z[nx - 1] - z[0];
        tmp2 = z[nx - 1] - 1.0;
        temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        ;
        f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;

        return f;
    }

    DerivativeStructure grie_rosen_func_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank-Rosenbrock  */ {
        int i;
    //    double temp, tmp1, tmp2;

        sr_func(x, z, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure f = diffs[0].getField().getZero();
        //DerivativeStructure sum_z = diffs[0].getField().getZero();

        //f = 0.0;


       // z[0] += 1.0; //shift to orgin
        DerivativeStructure tmp1, tmp2, temp;
        diffs[0] = diffs[0].add(1.0);
        for (i = 0; i < nx - 1; i++) {
            diffs[i+1] = diffs[i+1].add(1.0);
            tmp1 = diffs[i].pow(2).subtract(diffs[i+1]);
            tmp2 = diffs[i].subtract(1.0);
            temp = tmp1.pow(2).multiply(100.0).add(tmp2.pow(2));
            f = f.add(temp.pow(2).divide(4000.0).subtract(temp.cos()).add(1.0));
//            z[i + 1] += 1.0; //shift to orgin
//            tmp1 = z[i] * z[i] - z[i + 1];
//            tmp2 = z[i] - 1.0;
//            temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
//            f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;
        }
        tmp1 = diffs[nx-1].pow(2).subtract(diffs[0]);
        tmp2 = diffs[nx-1].subtract(1.0);
        temp = tmp1.pow(2).multiply(100.0).add(tmp2.pow(2));
        f = f.add(temp.pow(2).divide(4000.0).subtract(temp.cos()).add(1.0));
//        tmp1 = z[nx - 1] * z[nx - 1] - z[0];
//        tmp2 = z[nx - 1] - 1.0;
//        temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
//
//        f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;


        return f;
    }

    double escaffer6_func(double[] x, double f, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Expanded Scaffer��s F6  */ {
        int i;
        double temp1, temp2;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */


        f = 0.0;
        for (i = 0; i < nx - 1; i++) {
            temp1 = Math.sin(Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]));
            temp1 = temp1 * temp1;
            temp2 = 1.0 + 0.001 * (z[i] * z[i] + z[i + 1] * z[i + 1]);
            f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        }
        temp1 = Math.sin(Math.sqrt(z[nx - 1] * z[nx - 1] + z[0] * z[0]));
        temp1 = temp1 * temp1;
        temp2 = 1.0 + 0.001 * (z[nx - 1] * z[nx - 1] + z[0] * z[0]);
        f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);

        return f;
    }

    double []escaffer6_func_diff(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Expanded Scaffer��s F6  */ {
        int i;
    //    double temp1, temp2;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure f = diffs[0].getField().getZero();

      //  f = 0.0;
        for (i = 0; i < nx - 1; i++) {
//            temp1 = Math.sin(Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]));
//            temp1 = temp1 * temp1;
//            temp2 = 1.0 + 0.001 * (z[i] * z[i] + z[i + 1] * z[i + 1]);
//            f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
            DerivativeStructure temp1 = diffs[i].pow(2).add(diffs[i+1].pow(2)).sqrt().sin();
            temp1 = temp1.pow(2);
            DerivativeStructure temp2 = diffs[i].pow(2).add(diffs[i+1].pow(2)).multiply(0.001).add(1.0);
            f = f.add(temp1.subtract(0.5).divide(temp2.pow(2)).add(0.5));
        }
//        temp1 = Math.sin(Math.sqrt(z[nx - 1] * z[nx - 1] + z[0] * z[0]));
//        temp1 = temp1 * temp1;
//        temp2 = 1.0 + 0.001 * (z[nx - 1] * z[nx - 1] + z[0] * z[0]);
//        f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        DerivativeStructure temp1 = diffs[0].pow(2).add(diffs[nx -1].pow(2)).sqrt().sin();
        temp1 = temp1.pow(2);
        DerivativeStructure temp2 = diffs[0].pow(2).add(diffs[nx-1].pow(2)).multiply(0.001).add(1.0);
        f = f.add(temp1.subtract(0.5).divide(temp2.pow(2)).add(0.5));


        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = f.getPartialDerivative(indexs);
            }

            return g;
        }else{
            g[0] = f.getValue();

            return g;
        }
    }


    DerivativeStructure escaffer6_func_diff_ds(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag, boolean diff_enabled) /* Expanded Scaffer��s F6  */ {
        int i;
        //    double temp1, temp2;

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        double [] g = new double[nx];
        DerivativeStructure[] diffs = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs[i] = new DerivativeStructure(nx, 2, i, z[i]);
        }

        DerivativeStructure f = diffs[0].getField().getZero();

        //  f = 0.0;
        for (i = 0; i < nx - 1; i++) {
//            temp1 = Math.sin(Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]));
//            temp1 = temp1 * temp1;
//            temp2 = 1.0 + 0.001 * (z[i] * z[i] + z[i + 1] * z[i + 1]);
//            f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
            DerivativeStructure temp1 = diffs[i].pow(2).add(diffs[i+1].pow(2)).sqrt().sin();
            temp1 = temp1.pow(2);
            DerivativeStructure temp2 = diffs[i].pow(2).add(diffs[i+1].pow(2)).multiply(0.001).add(1.0);
            f = f.add(temp1.subtract(0.5).divide(temp2.pow(2)).add(0.5));
        }
//        temp1 = Math.sin(Math.sqrt(z[nx - 1] * z[nx - 1] + z[0] * z[0]));
//        temp1 = temp1 * temp1;
//        temp2 = 1.0 + 0.001 * (z[nx - 1] * z[nx - 1] + z[0] * z[0]);
//        f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        DerivativeStructure temp1 = diffs[0].pow(2).add(diffs[nx -1].pow(2)).sqrt().sin();
        temp1 = temp1.pow(2);
        DerivativeStructure temp2 = diffs[0].pow(2).add(diffs[nx-1].pow(2)).multiply(0.001).add(1.0);
        f = f.add(temp1.subtract(0.5).divide(temp2.pow(2)).add(0.5));


//        if(diff_enabled) {
//            for (i = 0; i < nx; i++) {
//                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//                indexs[i] = 1;
//                g[i] = f.getPartialDerivative(indexs);
//            }
//
//            return g;
//        }else{
//            g[0] = f.getValue();
//
//            return g;
//        }
        return f;
    }

    double hf01(double[] x, double f, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 1 */ {
        int i, tmp, cf_num = 3;
        double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 2] + G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ellips_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;
    }

    double [] hf01_diff(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag, boolean diff_enabled) /* Hybrid Function 1 */ {
        int i, tmp, cf_num = 3;
        //double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};
        double []g = new double[nx];

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
           // g[i] = 0;
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = schwefel_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g1 = schwefel_func_diff(ty, G_nx[i], tO, tM,0,0,diff_enabled);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
       // fit[i] = rastrigin_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g2 = rastrigin_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 2] + G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = ellips_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g3 = ellips_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);


        for (i = 0; i < nx; i++) {
           g[i] = g1[i] + g2[i] + g3[i];
        }
        return g;
    }

    DerivativeStructure hf01_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag, boolean diff_enabled) /* Hybrid Function 1 */ {
        int i, tmp, cf_num = 3;
        //double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};
        double []g = new double[nx];

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
            // g[i] = 0;
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = schwefel_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        DerivativeStructure g1 = schwefel_func_diff_ds(ty, G_nx[i], tO, tM,0,0,diff_enabled);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        // fit[i] = rastrigin_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
       // double [] g2 = rastrigin_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g2 = rastrigin_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 2] + G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = ellips_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        //double [] g3 = ellips_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g3 = ellips_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);

//        for (i = 0; i < nx; i++) {
//            g[i] = g1[i] + g2[i] + g3[i];
//        }
        return g1.add(g2).add(g3);
    }


    double hf02(double[] x, double f, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 2 */ {
        int i, tmp, cf_num = 4;
        double[] fit = new double[4];
        int[] G_nx = new int[4];
        int[] G = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = griewank_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = weierstrass_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rosenbrock_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = escaffer6_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }


    double [] hf02_diff(double[] x,  int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag, boolean diff_enabled) /* Hybrid Function 2 */ {
        int i, tmp, cf_num = 4;
        //double[] fit = new double[4];
        int[] G_nx = new int[4];
        int[] G = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = griewank_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g1 = griewank_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = weierstrass_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g2 = weierstrass_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        // fit[i] = rosenbrock_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g3 = rosenbrock_func_diff(ty,  G_nx[i], tO, tM, 0, 0, diff_enabled);

        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = escaffer6_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g4 = escaffer6_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        double [] g = new double[nx];
        for (i = 0; i < cf_num; i++) {
            //f += fit[i];
            g[i] = g1[i] + g2[i] + g3[i] + g4[i];
        }
        return g;

    }
    DerivativeStructure hf02_dff_ds(double[] x,  int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag, boolean diff_enabled) /* Hybrid Function 2 */ {
        int i, tmp, cf_num = 4;
        //double[] fit = new double[4];
        int[] G_nx = new int[4];
        int[] G = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = griewank_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        //double [] g1 = griewank_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g1 = griewank_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = weierstrass_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
       // double [] g2 = weierstrass_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g2 = weierstrass_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        // fit[i] = rosenbrock_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        //double [] g3 = rosenbrock_func_diff(ty,  G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g3 = rosenbrock_func_diff_ds(ty,  G_nx[i], tO, tM, 0, 0, diff_enabled);

        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = escaffer6_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        //double [] g4 = escaffer6_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g4 = escaffer6_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        double [] g = new double[nx];
//        for (i = 0; i < cf_num; i++) {
//            //f += fit[i];
//            g[i] = g1[i] + g2[i] + g3[i] + g4[i];
//        }
        return g1.add(g2).add(g3).add(g4);

    }

    double hf03(double[] x, double f, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 3 */ {
        int i, tmp, cf_num = 5;
        double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }


        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */


        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = escaffer6_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }

        fit[i] = rosenbrock_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ellips_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);

        //for(i=0;i<cf_num;i++){
        //	System.out.println("fithf05["+i+"]"+"="+fit[i]);
        //}

        f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }

    double [] hf03_diff(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag, boolean diff_enabled) /* Hybrid Function 3 */ {
        int i, tmp, cf_num = 5;
       // double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }


        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */


        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = escaffer6_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g1 = escaffer6_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = hgbat_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g2 = hgbat_func_diff(ty, G_nx[i], tO, tM, 0, 0,diff_enabled);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }

        //fit[i] = rosenbrock_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g3 = rosenbrock_func_diff(ty, G_nx[i], tO, tM, 0, 0,diff_enabled);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = schwefel_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g4 = schwefel_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = ellips_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        double [] g5 = ellips_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);

        //for(i=0;i<cf_num;i++){
        //	System.out.println("fithf05["+i+"]"+"="+fit[i]);
        //}

        double [] g = new double[nx];
        for (i = 0; i < cf_num; i++) {
            //f += fit[i];
            g[i] = g1[i] + g2[i] + g3[i] + g4[i] + g5[i];
        }
        return g;

    }

    DerivativeStructure hf03_diff_ds(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag, boolean diff_enabled) /* Hybrid Function 3 */ {
        int i, tmp, cf_num = 5;
        // double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }


        sr_func(x, z, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */


        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }


        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = escaffer6_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
       // double [] g1 = escaffer6_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g1 = escaffer6_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = hgbat_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
       // double [] g2 = hgbat_func_diff(ty, G_nx[i], tO, tM, 0, 0,diff_enabled);
        DerivativeStructure g2 = hgbat_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0,diff_enabled);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }

        //fit[i] = rosenbrock_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        //double [] g3 = rosenbrock_func_diff(ty, G_nx[i], tO, tM, 0, 0,diff_enabled);
        DerivativeStructure g3 = rosenbrock_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0,diff_enabled);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = schwefel_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
       // double [] g4 = schwefel_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g4 = schwefel_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        //fit[i] = ellips_func(ty, fit[i], G_nx[i], tO, tM, 0, 0);
        //double [] g5 = ellips_func_diff(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);
        DerivativeStructure g5 = ellips_func_diff_ds(ty, G_nx[i], tO, tM, 0, 0, diff_enabled);

        //for(i=0;i<cf_num;i++){
        //	System.out.println("fithf05["+i+"]"+"="+fit[i]);
        //}

        double [] g = new double[nx];
//        for (i = 0; i < cf_num; i++) {
//            //f += fit[i];
//            g[i] = g1[i] + g2[i] + g3[i] + g4[i] + g5[i];
//        }
        return g1.add(g2).add(g3).add(g4).add(g5);

    }

    double cf01(double[] x, double f, int nx, double[] Os, double[] Mr, double[] bias, int r_flag) /* Composition Function 1 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {20, 20, 20};


        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];


        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, 0);
        //System.out.println(fit[i]);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        //System.out.println(fit[i]);

        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = hgbat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        //System.out.println(fit[i]);

        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }

    double [] cf01_diff(double[] x, int nx, double[] Os, double[] Mr, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 1 */ {
        int i, j, cf_num = 3;
       // double[] fit = new double[3];
        double[] delta = {20, 20, 20};


        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];

        DerivativeStructure [] g = new DerivativeStructure[cf_num];
        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
      //  fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, 0);
        //double [] g1 = schwefel_func_diff(x,  nx, tOs, tMr, 1, 0, diff_enabled);
        g[i]  = schwefel_func_diff_ds(x,  nx, tOs, tMr, 1, 0, diff_enabled);
        //System.out.println(fit[i]);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
      //  fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        //double [] g2 = rastrigin_func_diff(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = rastrigin_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        //System.out.println(fit[i]);

        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
       // fit[i] = hgbat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        //double [] g3 = hgbat_func_diff(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = hgbat_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        //System.out.println(fit[i]);

        return cf_cal_diff(x, nx, Os, delta, bias, g, cf_num, diff_enabled);
    }

    double cf02(double[] x, double f, int nx, double[] Os, double[] Mr, int[] SS, double[] bias, int r_flag) /* Composition Function 2 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];
        int[] tSS = new int[nx];


        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        fit[i] = hf01(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);


        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        fit[i] = hf02(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);


        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        fit[i] = hf03(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);


        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }

    double [] cf02_diff(double[] x, int nx, double[] Os, double[] Mr, int[] SS, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 2 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];
        int[] tSS = new int[nx];

        DerivativeStructure [] g = new DerivativeStructure[cf_num];
        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        //fit[i] = hf01(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);
        g[i] =  hf01_diff_ds(x, nx, tOs, tMr, tSS, 1, r_flag,diff_enabled);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        //fit[i] = hf02(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);
        g[i]= hf02_dff_ds(x,  nx, tOs, tMr, tSS, 1, r_flag,diff_enabled);


        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        //fit[i] = hf03(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);
        g[i] = hf03_diff_ds(x,  nx, tOs, tMr, tSS, 1, r_flag,diff_enabled);



        return cf_cal_diff(x,  nx, Os, delta, bias, g, cf_num, diff_enabled);
    }

    double cf03(double[] x, double f, int nx, double[] Os, double[] Mr, double[] bias, int r_flag) /* Composition Function 3 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 10, 10, 20, 20};


        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];

        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];

        }
        fit[i] = hgbat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1000;
        //System.out.println(fit[i]);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];

        }
        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        //System.out.println(fit[i]);

        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        //System.out.println(fit[i]);

        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = weierstrass_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 400;
        //System.out.println(fit[i]);

        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = ellips_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;
        //System.out.println(fit[i]);

        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }

    double [] cf03_diff(double[] x,  int nx, double[] Os, double[] Mr, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 3 */ {
        int i, j, cf_num = 5;
      //  double[] fit = new double[5];
        double[] delta = {10, 10, 10, 20, 20};

        DerivativeStructure g[] = new DerivativeStructure[cf_num];

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];

        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];

        }
//        fit[i] = hgbat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1000;
        //System.out.println(fit[i]);
        g[i] = hgbat_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1000).multiply(10000);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];

        }
//        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+3;
        //System.out.println(fit[i]);
        g[i] = rastrigin_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+3).multiply(10000);
        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 4e+3;
        //System.out.println(fit[i]);
        g[i] = schwefel_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(4e+3).multiply(10000);
        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = weierstrass_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 400;
        //System.out.println(fit[i]);
        g[i] = weierstrass_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(400).multiply(10000);
        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = ellips_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+10;
        g[i] = ellips_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+10).multiply(10000);
        //System.out.println(fit[i]);

        return cf_cal_diff(x, nx, Os, delta, bias, g, cf_num, diff_enabled);
    }

    double cf04(double[] x, double f, int nx, double[] Os, double[] Mr, double[] bias, int r_flag) /* Composition Function 4 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 20, 20, 30, 30};

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];

        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = ellips_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;
        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = fit[i] * 10;
        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = happycat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;

        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }

    double [] cf04_diff(double[] x, int nx, double[] Os, double[] Mr, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 4 */ {
        int i, j, cf_num = 5;
      //  double[] fit = new double[5];
        double[] delta = {10, 20, 20, 30, 30};
DerivativeStructure [] g = new DerivativeStructure[cf_num];
        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];

        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 4e+3;
        g[i] = schwefel_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(4e+3).multiply(10000);
        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+3;
        g[i] = rastrigin_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+3).multiply(10000);
        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = ellips_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+10;
        g[i] = ellips_func_diff_ds(x,  nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+10).multiply(10000);
        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = fit[i] * 10;
        g[i] = escaffer6_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].multiply(10);
        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = happycat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+3;
        g[i] = happycat_func_diff_ds(x, nx, tOs, tMr, 1, r_flag);
        g[i] = g[i].divide(1e+3).multiply(10000);

        return cf_cal_diff(x,  nx, Os, delta, bias, g, cf_num, diff_enabled);
    }

    double cf05(double[] x, double f, int nx, double[] Os, double[] Mr, int[] SS, double[] bias, int r_flag) /* Composition Function 5 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 10, 10, 20, 20};


        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];
        int[] tSS = new int[nx];

        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        fit[i] = hf03(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        fit[i] = hf01(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);

        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = fit[i] * 10;

        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }

    double [] cf05_diff(double[] x, int nx, double[] Os, double[] Mr, int[] SS, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 5 */ {
        int i, j, cf_num = 5;
   //     double[] fit = new double[5];
        double[] delta = {10, 10, 10, 20, 20};
        DerivativeStructure [] g = new DerivativeStructure[cf_num];

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];
        int[] tSS = new int[nx];

        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        //fit[i] = hf03(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);
        g[i] = hf01_diff_ds(x, nx, tOs, tMr, tSS, 1, r_flag, diff_enabled);
        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+3;
        g[i] = rastrigin_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+3).multiply(10000);
        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
        for (j = 0; j < nx; j++) {
            tSS[j] = SS[i * nx + j];
        }
        //fit[i] = hf01(x, fit[i], nx, tOs, tMr, tSS, 1, r_flag);
        g[i] = hf01_diff_ds(x, nx, tOs, tMr, tSS, 1, r_flag, diff_enabled);
        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 4e+3;
        g[i] = schwefel_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(4e+3).multiply(10000);
        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }
//        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = fit[i] * 10;
        g[i] = escaffer6_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].multiply(10);

        return cf_cal_diff(x, nx, Os, delta, bias, g, cf_num, diff_enabled);
    }

    double cf06(double[] x, double f, int nx, double[] Os, double[] Mr, double[] bias, int r_flag) /* Composition Function 6 */ {
        int i, j, cf_num = 7;
        double[] fit = new double[7];
        double[] delta = {10, 20, 30, 40, 50, 50, 50};


        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];


        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = happycat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = grie_rosen_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;

        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;

        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = fit[i] * 10;

        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = ellips_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        i = 5;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = bent_cigar_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        i = 6;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;


        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }

    double [] cf06_diff(double[] x, int nx, double[] Os, double[] Mr, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 6 */ {
        int i, j, cf_num = 7;
      //  double[] fit = new double[7];
        double[] delta = {10, 20, 30, 40, 50, 50, 50};
        DerivativeStructure [] g = new DerivativeStructure[cf_num];

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];


        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = happycat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+3;
        g[i] = happycat_func_diff_ds(x,  nx, tOs, tMr, 1, r_flag);
        g[i] = g[i].divide(1e+3).multiply(10000);
        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = grie_rosen_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 4e+3;
        g[i] = grie_rosen_func_diff_ds(x, nx, tOs, tMr, 1, r_flag);
        g[i] = g[i].divide(4e+3).multiply(10000);
        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 4e+3;
        g[i] = schwefel_func_diff_ds(x,  nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(4e+3).multiply(10000);
        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = fit[i] * 10;
        g[i] = escaffer6_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].multiply(10);
        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = ellips_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+10;
        g[i] = ellips_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+10).multiply(10000);
        i = 5;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = bent_cigar_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+10;
        g[i] = bent_cigar_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+10).multiply(10000);
        i = 6;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 10000 * fit[i] / 1e+3;
        g[i] = rastrigin_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+3).multiply(10000);

        return cf_cal_diff(x, nx, Os, delta, bias, g, cf_num,diff_enabled);
    }

    double cf07(double[] x, double f, int nx, double[] Os, double[] Mr, double[] bias, int r_flag) /* Composition Function 7 */ {
        int i, j, cf_num = 10;
        double[] fit = new double[10];
        double[] delta = {10, 10, 20, 20, 30, 30, 40, 40, 50, 50};


        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];


        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 1e+3;

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = weierstrass_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 400;

        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = happycat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 1e+3;

        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 4e+3;

        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = rosenbrock_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 1e+5;

        i = 5;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = hgbat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 1000;

        i = 6;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = katsuura_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 1e+7;

        i = 7;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = fit[i] * 10;

        i = 8;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = grie_rosen_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 4e+3;

        i = 9;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

        fit[i] = ackley_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
        fit[i] = 100 * fit[i] / 1e+5;


        return cf_cal(x, f, nx, Os, delta, bias, fit, cf_num);
    }


    double [] cf07_diff(double[] x, int nx, double[] Os, double[] Mr, double[] bias, int r_flag, boolean diff_enabled) /* Composition Function 7 */ {
        int i, j, cf_num = 10;
    //    double[] fit = new double[10];
        double[] delta = {10, 10, 20, 20, 30, 30, 40, 40, 50, 50};
        DerivativeStructure [] g = new DerivativeStructure[cf_num];

        double[] tOs = new double[nx];
        double[] tMr = new double[cf_num * nx * nx];


        i = 0;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = rastrigin_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 1e+3;

        g[i] = rastrigin_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+3).multiply(100);

        i = 1;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = weierstrass_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 400;

        g[i] = weierstrass_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(400).multiply(100);

        i = 2;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = happycat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 1e+3;

        g[i] = happycat_func_diff_ds(x, nx, tOs, tMr, 1, r_flag);
        g[i] = g[i].divide(1e+3).multiply(100);

        i = 3;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = schwefel_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 4e+3;


        g[i] = schwefel_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(4e+3).multiply(100);

        i = 4;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = rosenbrock_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 1e+5;

        g[i] = rosenbrock_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+5).multiply(100);

        i = 5;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = hgbat_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 1000;

        g[i] = hgbat_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1000).multiply(100);

        i = 6;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = katsuura_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 1e+7;

        g[i] = katsuura_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+7).multiply(100);

        i = 7;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = escaffer6_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = fit[i] * 10;

        g[i] = escaffer6_func_diff_ds(x, nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].multiply(10);

        i = 8;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = grie_rosen_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 4e+3;

        g[i] = grie_rosen_func_diff_ds(x,  nx, tOs, tMr, 1, r_flag);
        g[i] = g[i].divide(4e+3).multiply(100);

        i = 9;
        for (j = 0; j < nx; j++) {
            tOs[j] = Os[i * nx + j];
        }
        for (j = 0; j < nx * nx; j++) {
            tMr[j] = Mr[i * nx * nx + j];
        }

//        fit[i] = ackley_func(x, fit[i], nx, tOs, tMr, 1, r_flag);
//        fit[i] = 100 * fit[i] / 1e+5;

        g[i] = ackley_func_diff_ds(x,  nx, tOs, tMr, 1, r_flag, diff_enabled);
        g[i] = g[i].divide(1e+5).multiply(100);

        return cf_cal_diff(x, nx, Os, delta, bias, g, cf_num, diff_enabled);
    }




    void shiftfunc(double[] x, double[] xshift, int nx, double[] Os) {
        int i;
        for (i = 0; i < nx; i++) {
            xshift[i] = x[i] - Os[i];
        }
    }


    void rotatefunc(double[] x, double[] xrot, int nx, double[] Mr) {
        int i, j;
        for (i = 0; i < nx; i++) {
            xrot[i] = 0;
            for (j = 0; j < nx; j++) {
                xrot[i] = xrot[i] + x[j] * Mr[i * nx + j];
            }
        }
    }

    void sr_func(double[] x, double[] sr_x, int nx, double[] Os, double[] Mr, double sh_rate, int s_flag, int r_flag) {
        int i, j;
        if (s_flag == 1) {
            if (r_flag == 1) {
                shiftfunc(x, y, nx, Os);
                for (i = 0; i < nx; i++)//shrink to the orginal search range
                {
                    y[i] = y[i] * sh_rate;
                }
                rotatefunc(y, sr_x, nx, Mr);
            } else {
                shiftfunc(x, sr_x, nx, Os);
                for (i = 0; i < nx; i++)//shrink to the orginal search range
                {
                    sr_x[i] = sr_x[i] * sh_rate;
                }
            }
        } else {

            if (r_flag == 1) {
                for (i = 0; i < nx; i++)//shrink to the orginal search range
                {
                    y[i] = x[i] * sh_rate;
                }
                rotatefunc(y, sr_x, nx, Mr);
            } else

            {
                for (j = 0; j < nx; j++)//shrink to the orginal search range
                {
                    sr_x[j] = x[j] * sh_rate;
                }
            }
        }


    }

    void asyfunc(double[] x, double[] xasy, int nx, double beta) {
        int i;
        for (i = 0; i < nx; i++) {
            if (x[i] > 0)
                xasy[i] = Math.pow(x[i], 1.0 + beta * i / (nx - 1) * Math.pow(x[i], 0.5));
        }
    }


    void oszfunc(double[] x, double[] xosz, int nx) {
        int i, sx;
        double c1, c2, xx = 0;
        for (i = 0; i < nx; i++) {
            if (i == 0 || i == nx - 1) {
                if (x[i] != 0)
                    xx = Math.log(Math.abs(x[i]));//xx=log(fabs(x[i]));
                if (x[i] > 0) {
                    c1 = 10;
                    c2 = 7.9;
                } else {
                    c1 = 5.5;
                    c2 = 3.1;
                }
                if (x[i] > 0)
                    sx = 1;
                else if (x[i] == 0)
                    sx = 0;
                else
                    sx = -1;
                xosz[i] = sx * Math.exp(xx + 0.049 * (Math.sin(c1 * xx) + Math.sin(c2 * xx)));
            } else
                xosz[i] = x[i];
        }
    }


    double cf_cal(double[] x, double f, int nx, double[] Os, double[] delta, double[] bias, double[] fit, int cf_num) {
        int i, j;

        double[] w;
        double w_max = 0, w_sum = 0;
        w = new double[cf_num];
        for (i = 0; i < cf_num; i++) {
            fit[i] += bias[i];
            w[i] = 0;
            for (j = 0; j < nx; j++) {
                w[i] += Math.pow(x[j] - Os[i * nx + j], 2.0);
            }
            if (w[i] != 0)
                w[i] = Math.pow(1.0 / w[i], 0.5) * Math.exp(-w[i] / 2.0 / nx / Math.pow(delta[i], 2.0));
            else
                w[i] = INF;
            if (w[i] > w_max)
                w_max = w[i];
        }

        for (i = 0; i < cf_num; i++) {
            w_sum = w_sum + w[i];
        }
        if (w_max == 0) {
            for (i = 0; i < cf_num; i++)
                w[i] = 1;
            w_sum = cf_num;
        }
        f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f = f + w[i] / w_sum * fit[i];
        }

        return f;

    }

    double [] cf_cal_diff(double[] x, int nx, double[] Os, double[] delta, double[] bias, DerivativeStructure [] fit,  int cf_num, boolean diff_enabled) {
        int i, j;

       // double[] w;
        double w_max = 0;//, w_sum = 0;
       // w = new double[cf_num];
        DerivativeStructure [] w = new DerivativeStructure[cf_num];
        double [] g = new double[nx];
        DerivativeStructure[] diffs_x = new DerivativeStructure[nx];
        for (i = 0; i < nx; i++) {
            diffs_x[i] = new DerivativeStructure(nx, 2, i, x[i]);
        }


//        g1 = g1.add(bias[0]);
//        g2 = g2.add(bias[1]);
//        g3 = g3.add(bias[2]);

        for (i = 0; i < cf_num; i++) {
            fit[i] = fit[i].add(bias[i]);
            //fit[i] += bias[i];
            //w[i] = 0;
            w[i] = diffs_x[0].getField().getZero();
            for (j = 0; j < nx; j++) {
                //w[i] += Math.pow(x[j] - Os[i * nx + j], 2.0);
                w[i] = w[i].add(diffs_x[j].subtract(Os[i * nx + j]).pow(2));
            }
            if (!w[i].equals(diffs_x[0].getField().getZero()))
                //w[i] = Math.pow(1.0 / w[i], 0.5) * Math.exp(-w[i] / 2.0 / nx / Math.pow(delta[i], 2.0));
                w[i] = w[i].pow(-1).pow(0.5).multiply(w[i].negate().divide(2.0).divide(nx).divide(Math.pow(delta[i], 2.0)).exp());
            else
                //w[i] = INF;
                w[i] = diffs_x[0].getField().getOne().multiply(INF);
            if (w[i].getValue() > w_max)
                w_max = w[i].getValue();
        }

        DerivativeStructure w_sum = diffs_x[0].getField().getZero();
        for (i = 0; i < cf_num; i++) {
            //w_sum = w_sum + w[i];
            w_sum = w_sum.add(w[i]);
        }
        if (w_max == 0) {
            for (i = 0; i < cf_num; i++)
                w[i] = diffs_x[0].getField().getOne();
            w_sum = diffs_x[0].getField().getOne().multiply(cf_num);
        }
        DerivativeStructure f = diffs_x[0].getField().getZero();
       // f = 0.0;
        for (i = 0; i < cf_num; i++) {
            //f = f + w[i] / w_sum * fit[i];
//        f = f.add(w[i].divide(w_sum.multiply(g1)));
//        f = f.add(w[i].divide(w_sum.multiply(g2)));
//        f = f.add(w[i].divide(w_sum.multiply(g3)));
            f = f.add(w[i].divide(w_sum.multiply(fit[i])));
       }
        if(diff_enabled) {
            for (i = 0; i < nx; i++) {
                int[] indexs = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                indexs[i] = 1;
                g[i] = f.getPartialDerivative(indexs);
            }

            return g;
        }else{
            g[0] = f.getValue();

            return g;
        }

    }


}

