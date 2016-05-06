import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by framg on 05/05/2016.
 */
public class CEC15BenchmarkLearning extends AllBenchmarks {
    double[] OShift, M, bias;
    boolean ini_flag ;
    int[] SS;
    int[] cf_nums = {0, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 5, 5, 5, 7, 10};

    int[] bShuffle = {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0};


    CEC15BenchmarkLearning(int DIM, int FUN) {
        super(DIM, FUN, 15);
        ini_flag = true;
    }

    @Override
    public double f(double[] x) {
        //int cf_num=10,i,j;
        int func_num = FUN;
        int nx = DIM;
        int cf_num, i, j;
        cf_num = cf_nums[func_num];


        if (ini_flag) /* initiailization*/ {
            ini_flag = false;
            if (!(nx == 2 || nx == 10 || nx == 20 || nx == 50 || nx == 100)) {
                System.out.println("\nError: Test functions are only defined for D=2,10,30,50,100.");
            }

            if (nx == 2 && ((func_num >= 6 && func_num <= 8) || (func_num == 10) || (func_num == 13))) {
                System.out.println("\nError: hf01,hf02,hf03,cf02&cf05 are NOT defined for D=2.\n");
            }

			/*Load Matrix M*****************************************************/
            File fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/input_data/M_" + func_num + "_D" + nx + ".txt");//* Load M data *
            Scanner input = null;
            try {
                input = new Scanner(fpt);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (!fpt.exists()) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }

            M = new double[cf_num * nx * nx];


            for (i = 0; i < cf_num * nx * nx; i++) {
                M[i] = Double.parseDouble(input.next());
            }

            input.close();


            if (cf_num > 1) {

                fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/input_data/bias_" + func_num + ".txt");//* Load bias data *
                try {
                    input = new Scanner(fpt);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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

            fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/input_data/shift_data_" + func_num + ".txt");
            try {
                input = new Scanner(fpt);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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


            input.close();





			/*Load Shuffle_data*******************************************/

            if (bShuffle[func_num] == 1) {
                fpt = new File("Benchmark/src/CEC15-BenchmarkLearning/src/input_data/shuffle_data_" + func_num + "_D" + nx + ".txt");
                try {
                    input = new Scanner(fpt);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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

        }
        testfunc15L tf = new testfunc15L(DIM);
        double f = 0;

        switch (func_num) {
            case 1:
                f = tf.ellips_func(x, f, nx, OShift, M, 1, 1);
                f += 100.0;
                break;
            case 2:
                f = tf.bent_cigar_func(x, f, nx, OShift, M, 1, 1);
                f += 200.0;
                break;
            case 3:
                f = tf.ackley_func(x, f, nx, OShift, M, 1, 1);
                f += 300.0;
                break;
            case 4:
                f = tf.rastrigin_func(x, f, nx, OShift, M, 1, 1);
                f += 400.0;
                break;
            case 5:
                f = tf.schwefel_func(x, f, nx, OShift, M, 1, 1);
                f += 500.0;
                break;
            case 6:
                f = tf.hf01(x, f, nx, OShift, M, SS, 1, 1);
                f += 600.0;
                break;
            case 7:
                f = tf.hf02(x, f, nx, OShift, M, SS, 1, 1);
                f += 700.0;
                break;
            case 8:
                f = tf.hf03(x, f, nx, OShift, M, SS, 1, 1);
                f += 800.0;
                break;
            case 9:
                f = tf.cf01(x, f, nx, OShift, M, bias, 1);
                f += 900.0;
                break;
            case 10:
                f = tf.cf02(x, f, nx, OShift, M, SS, bias, 1);
                f += 1000.0;
                break;
            case 11:
                f = tf.cf03(x, f, nx, OShift, M, bias, 1);
                f += 1100.0;
                break;
            case 12:
                f = tf.cf04(x, f, nx, OShift, M, bias, 1);
                f += 1200.0;
                break;
            case 13:
                f = tf.cf05(x, f, nx, OShift, M, SS, bias, 1);
                f += 1300.0;
                break;
            case 14:
                f = tf.cf06(x, f, nx, OShift, M, bias, 1);
                f += 1400.0;
                break;
            case 15:
                f = tf.cf07(x, f, nx, OShift, M, bias, 1);
                f += 1500.0;
                break;


            default:
                System.out.println("\nError: There are only 15 test functions in this test suite!");
                f = 0.0;
                break;
        }

        return f;


    }

    @Override
    public double bias() {
        switch (FUN) {
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


            default:
                System.out.println("\nError: There are only 15 test functions in this test suite!");

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
}
