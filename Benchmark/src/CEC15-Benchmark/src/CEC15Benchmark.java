import input_data.DataReader;
import math.Functions;

/**
 * Created by framg on 02/05/2016.
 */
public class CEC15Benchmark extends AllBenchmarks {
    private double[]                  OShift, M, x_bound;
    private int[]                     SS;

    private boolean ini_flag;


    CEC15Benchmark(int DIM, int FUN) {
        super(DIM, FUN, 15);
        ini_flag = true;
    }


    public static int nProblems(){
        return 15;
    }

    @Override
    public double f(double[] x) {
        double f = 0.0;
        int func_num = FUN;
        int nx = DIM;


        if ((func_num < 1) || (func_num > 15)) {
            System.err.println("Error: functiont cant be lower than 1 and greater than 15.");
            System.exit(0);
            return 0;
        }




        // cf_num not correct for D2
        int cf_num = 10, i, j;


        if (ini_flag) /* initiailization */ {
            ini_flag = false;


          //  x_bound = new double[nx];

            //for (i = 0; i < nx; i++) {
           //     x_bound[i] = 100.0;
           // }

            //
            // if (!((nx == 2) || (nx == 10) || (nx == 20) || (nx == 30) || (nx == 50) || (nx == 100))) {
            if (!((nx == 10) || (nx == 30))) {
                System.out.println("\nError: Expensive Test functions are only defined for D=10, 30.");
                System.exit(0);
                return 0;
            }

//          if ((nx == 2) && (((func_num >= 17) && (func_num <= 22)) || ((func_num >= 29) && (func_num <= 30)))) {
//              System.out.println("\nError: hf01,hf02,hf03,hf04,hf05,hf06,cf07&cf08 are NOT defined for D=2.\n");
//
//              return null;
//          }

            /* Load Matrix M**************************************************** */
            M = DataReader.readRotation(func_num, nx, cf_num);

            /* Load shift_data************************************************** */
            OShift = DataReader.readShiftData(func_num, nx, cf_num);

            /* Load Shuffle_data****************************************** */
            SS        = DataReader.readShuffleData(func_num, nx);
        }




            switch (func_num) {

//          case 1 :
//              f[i] = Functions.ellips_func(x, nx, OShift, M, 1, 1);
//              f = 100.0;
//
//              break;
                case 1 :
                    f = Functions.bent_cigar_func(x, nx, OShift, M, 1, 1);

                    break;

                case 2 :
                    f = Functions.discus_func(x, nx, OShift, M, 1, 1);

                    break;

//          case 4 :
//              f[i] = Functions.rosenbrock_func(x, nx, OShift, M, 1, 1);
//              break;
//
//          case 5 :
//              f[i] = Functions.ackley_func(x, nx, OShift, M, 1, 1);
//              f = 500.0;
//
//              break;
                case 3 :
                    f = Functions.weierstrass_func(x, nx, OShift, M, 1, 1);

                    break;

//          case 7 :
//              f[i] = Functions.griewank_func(x, nx, OShift, M, 1, 1);
//              f = 700.0;
//
//              break;
//
//          case 8 :
//              f[i] = Functions.rastrigin_func(x, nx, OShift, M, 1, 0);
//              f = 800.0;
//
//              break;
//
//          case 9 :
//              f[i] = Functions.rastrigin_func(x, nx, OShift, M, 1, 1);
//              f = 900.0;
//
//              break;
                case 4 :
                    f = Functions.schwefel_func(x, nx, OShift, M, 1, 0);
                    break;

//          case 11 :
//              f[i] = Functions.schwefel_func(x, nx, OShift, M, 1, 1);
//              f = 1100.0;
//
//              break;
                case 5 :
                    f = Functions.katsuura_func(x, nx, OShift, M, 1, 1);
                    break;

                case 6 :
                    f = Functions.happycat_func(x, nx, OShift, M, 1, 1);
                    break;

                case 7 :
                    f = Functions.hgbat_func(x, nx, OShift, M, 1, 1);
                    break;

                case 8 :
                    f = Functions.grie_rosen_func(x, nx, OShift, M, 1, 1);
                    break;

                case 9 :
                    f = Functions.escaffer6_func(x, nx, OShift, M, 1, 1);
                    break;

                case 10 :
                    f = Functions.hf01(x, nx, OShift, M, SS, 1, 1);
                    break;

//          case 18 :
//              f[i] = Functions.hf02(x, nx, OShift, M, SS, 1, 1);
//              f = 1800.0;
//
//              break;
                case 11 :
                    f = Functions.hf03(x, nx, OShift, M, SS, 1, 1);
                    break;

//          case 20 :
//              f[i] = Functions.hf04(x, nx, OShift, M, SS, 1, 1);
//              f = 2000.0;
//
//              break;
//
//          case 21 :
//              f[i] = Functions.hf05(x, nx, OShift, M, SS, 1, 1);
//              f = 2100.0;
//
//              break;
                case 12 :
                    f = Functions.hf06(x, nx, OShift, M, SS, 1, 1);
                    break;

                case 13 :
                    f = Functions.cf01(x, nx, OShift, M, 1);
                    break;

//          case 24 :
//              f[i] = Functions.cf02(x, nx, OShift, M, 1);
//              f = 2400.0;
//
//              break;
                case 14 :
                    f = Functions.cf03(x, nx, OShift, M, 1);
                    break;

//          case 26 :
//              f[i] = Functions.cf04(x, nx, OShift, M, 1);
//              f = 2600.0;
//
//              break;
                case 15 :
                    f = Functions.cf05(x, nx, OShift, M, 1);
                    break;

//          case 28 :
//              f[i] = Functions.cf06(x, nx, OShift, M, 1);
//              f = 2800.0;
//
//              break;
//
//          case 29 :
//              f[i] = Functions.cf07(x, nx, OShift, M, SS, 1);
//              f = 2900.0;
//
//              break;
//
//          case 30 :
//              f[i] = Functions.cf08(x, nx, OShift, M, SS, 1);
//              f = 3000.0;
//
//              break;
                default :

                     System.out.println("\nError: There are only 15 test functions in this test suite!");
                    System.exit(0);
                    // f[i] = 0.0;
                    break;
            }

            // Apply f^*


        return f+bias();
    }

    @Override
    public double bias() {
        switch (FUN){
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
}
