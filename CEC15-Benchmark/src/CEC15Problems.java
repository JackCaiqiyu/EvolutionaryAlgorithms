
/*
CEC14 Test Function Suite for Single Objective Optimization
BO Zheng (email: zheng.b1988@gmail.com)
Dec. 19th 2013
Modified in Aug. 8th 2014
 */

//~--- non-JDK imports --------------------------------------------------------

import cec2015.math.Functions;

import cec2015.profiler.FEvCount;
import cec2015.profiler.ResultRecorder;

import input_data.DataReader;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;

public class CEC15Problems {
    private final ArrayList<FEvCount> funcEvalCounters = new ArrayList<FEvCount>();
    private final ArrayList<Integer>  runs             = new ArrayList<Integer>();
    private final ResultRecorder      rr;    // = new ResultRecorder("test");
    private double[]                  OShift, M, y, z, x_bound;
    private int                       ini_flag, n_flag, func_flag;
    private int[]                     SS;
    private int                       currentRun;

    /*
     * Constructor:
     * Default: Start run count with 1, no result file output.
     * Result recording: Start run count with 1, specifiy output director and fill prefix like "PSO/result"
     */
    public CEC15Problems() {
        this(1, null);
    }

    public CEC15Problems(String resultFilePrefix) {
        this(1, new ResultRecorder(resultFilePrefix));
    }

    /*
     * Private constructor function:
     */
    private CEC15Problems(int run, ResultRecorder r) {
        rr         = r;
        currentRun = run;
        runs.add(run);

        float rp[] = {
            0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.09f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f,
            0.8f, 0.9f, 1.0f
        };

        for (int dim = 10; dim <= 30; dim += 20) {
            int rp10_30[] = new int[rp.length];

            for (int i = 0; i < rp.length; i++) {
                rp10_30[i] = (int) Math.round(50 * dim * rp[i]);
            }

            addRecordRule(dim, rp10_30);
        }
    }

    /*
     * 3 Function evaluation recording functions:
     *   CurrentBestX,
     *   CurrentBest function value,
     *   current evaluation count
     * If the 3rd parameter is ommited, it will get information for current run.
     */
    public double[] getCurrentBestX(int func_num, int n, int run) {
        if (rr != null) {
            return getFuncEvalCounter(run).getCurrentBestX(func_num, n);
        } else {
            return null;
        }
    }

    public int getEvalCount(int func_num, int n, int run) {
        if (rr != null) {
            return getFuncEvalCounter(run).getCount(func_num, n);
        } else {
            return 0;
        }
    }

    public double getCurrentBest(int func_num, int n, int run) {
        if (rr != null) {
            return getFuncEvalCounter(run).getCurrentBest(func_num, n);
        } else {
            return Double.NaN;
        }
    }

    public double[] getCurrentBestX(int func_num, int n) {
        return getCurrentBestX(func_num, n, getNumberOfRun());
    }

    public int getEvalCount(int func_num, int n) {
        return getEvalCount(func_num, n, getNumberOfRun());
    }

    public double getCurrentBest(int func_num, int n) {
        return getCurrentBest(func_num, n, getNumberOfRun());
    }

    /*
     * Recording rules are applied dimension-wise
     * Likely, for 10D problems, the total function evaluations should be 50D=500.
     * We may want to record at the following evaluation:
     *   50, 100, 150, 200, 250, 300, 350, 400, 450, 500
     *
     * int[] rp = {50, 100, 150, 200, 250, 300, 350, 400, 450, 500}
     * prob.addRecordRule(10, rp);
     *
     */
    private void addRecordRule(int dim, int[] recordPoints) {
        if (rr != null) {
            rr.addRecordRule(dim, recordPoints);
        }
    }

    /*
     * Dealing with different runs of the optimization codes.
     * If we demand the partitipants to run the optimization code 20 times to collect statistics data,
     * we have to record current-best results for each run.
     *
     * There are two ways to make sure we get the right recording.
     *
     *
     * 1. Use default run, start with 1, and call nextRun increase the current run count.
     *
     * CEC15Problems prob = new CEC15Problems("PSOresult/result");
     * for (int run = 0; run < 20; run++) {
     *   //optimization code
     *
     *   prob.nextRun();
     * }
     *
     * 2. Or we can explicitly set run count by setCurrentRun.
     *
     * CEC15Problems prob = new CEC15Problems("PSOresult/result");
     * for (int run = 1; run <= 20; run++) {
     *   prob.setCurrentRun(run);
     *   //optimization code
     *
     * }
     */
//    public int nextRun() {
//        setCurrentRun(getCurrentRun() + 1);
//
//        return getCurrentRun();
//    }

    public void setNumberOfRun(int run) {
        if (runs.contains(run)) {
            currentRun = run;
        } else {
            runs.add(run);
            currentRun = run;
        }
    }

    /*
     * Helper functions for running time recording.
     */
    public int getNumberOfRun() {
        return currentRun;
    }

    public int[] getRuns() {
        int[] ret = new int[runs.size()];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = runs.get(i);
        }

        Arrays.sort(ret);

        return ret;
    }

    /*
     * Write results to real files.
     */
    public void writeResultToFiles() {
        if (rr != null) {
            rr.flush();
        }
    }

    /*
     * @params: x : sample points nx: dimension mx: sample size func_num: function number
     */
    public double[] eval(double[] x, int nx, int mx, int func_num) {
        if ((func_num < 1) || (func_num > 15)) {
            return null;
        }




        // cf_num not correct for D2
        int cf_num = 10, i, j;

        // for func_num = 23-28 cf_num = 8
        if (ini_flag == 1) {
            if ((n_flag != nx) || (func_flag != func_num)) /* check if nx or func_num are changed, reinitialization */ {
                ini_flag = 0;
            }
        }

        if (ini_flag == 0) /* initiailization */ {
            y       = new double[nx];
            z       = new double[nx];
            x_bound = new double[nx];

            for (i = 0; i < nx; i++) {
                x_bound[i] = 100.0;
            }

            //
            // if (!((nx == 2) || (nx == 10) || (nx == 20) || (nx == 30) || (nx == 50) || (nx == 100))) {
            if (!((nx == 10) || (nx == 30))) {
                System.out.println("\nError: Expensive Test functions are only defined for D=10, 30.");
                return null;
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
            n_flag    = nx;
            func_flag = func_num;
            ini_flag  = 1;
        }
        double[] f = new double[mx];
        Arrays.fill(f, func_num * 100.0);
        double[] t;    // = new double[nx];

        for (i = 0; i < mx; i++) {
            t    = Arrays.copyOfRange(x, i * nx, (i + 1) * nx);

            switch (func_num) {

//          case 1 :
//              f[i] = Functions.ellips_func(t, nx, OShift, M, 1, 1);
//              f[i] += 100.0;
//
//              break;
            case 1 :
                f[i] += Functions.bent_cigar_func(t, nx, OShift, M, 1, 1);

                break;

            case 2 :
                f[i] += Functions.discus_func(t, nx, OShift, M, 1, 1);

                break;

//          case 4 :
//              f[i] = Functions.rosenbrock_func(t, nx, OShift, M, 1, 1);
//              break;
//
//          case 5 :
//              f[i] = Functions.ackley_func(t, nx, OShift, M, 1, 1);
//              f[i] += 500.0;
//
//              break;
            case 3 :
                f[i] += Functions.weierstrass_func(t, nx, OShift, M, 1, 1);

                break;

//          case 7 :
//              f[i] = Functions.griewank_func(t, nx, OShift, M, 1, 1);
//              f[i] += 700.0;
//
//              break;
//
//          case 8 :
//              f[i] = Functions.rastrigin_func(t, nx, OShift, M, 1, 0);
//              f[i] += 800.0;
//
//              break;
//
//          case 9 :
//              f[i] = Functions.rastrigin_func(t, nx, OShift, M, 1, 1);
//              f[i] += 900.0;
//
//              break;
            case 4 :
                f[i] += Functions.schwefel_func(t, nx, OShift, M, 1, 0);
                break;

//          case 11 :
//              f[i] = Functions.schwefel_func(t, nx, OShift, M, 1, 1);
//              f[i] += 1100.0;
//
//              break;
            case 5 :
                f[i] += Functions.katsuura_func(t, nx, OShift, M, 1, 1);
                break;

            case 6 :
                f[i] += Functions.happycat_func(t, nx, OShift, M, 1, 1);
                break;

            case 7 :
                f[i] += Functions.hgbat_func(t, nx, OShift, M, 1, 1);
                break;

            case 8 :
                f[i] += Functions.grie_rosen_func(t, nx, OShift, M, 1, 1);
                break;

            case 9 :
                f[i] += Functions.escaffer6_func(t, nx, OShift, M, 1, 1);
                break;

            case 10 :
                f[i] += Functions.hf01(t, nx, OShift, M, SS, 1, 1);
                break;

//          case 18 :
//              f[i] = Functions.hf02(t, nx, OShift, M, SS, 1, 1);
//              f[i] += 1800.0;
//
//              break;
            case 11 :
                f[i] += Functions.hf03(t, nx, OShift, M, SS, 1, 1);
                break;

//          case 20 :
//              f[i] = Functions.hf04(t, nx, OShift, M, SS, 1, 1);
//              f[i] += 2000.0;
//
//              break;
//
//          case 21 :
//              f[i] = Functions.hf05(t, nx, OShift, M, SS, 1, 1);
//              f[i] += 2100.0;
//
//              break;
            case 12 :
                f[i] += Functions.hf06(t, nx, OShift, M, SS, 1, 1);
                break;

            case 13 :
                f[i] += Functions.cf01(t, nx, OShift, M, 1);
                break;

//          case 24 :
//              f[i] = Functions.cf02(t, nx, OShift, M, 1);
//              f[i] += 2400.0;
//
//              break;
            case 14 :
                f[i] += Functions.cf03(t, nx, OShift, M, 1);
                break;

//          case 26 :
//              f[i] = Functions.cf04(t, nx, OShift, M, 1);
//              f[i] += 2600.0;
//
//              break;
            case 15 :
                f[i] += Functions.cf05(t, nx, OShift, M, 1);
                break;

//          case 28 :
//              f[i] = Functions.cf06(t, nx, OShift, M, 1);
//              f[i] += 2800.0;
//
//              break;
//
//          case 29 :
//              f[i] = Functions.cf07(t, nx, OShift, M, SS, 1);
//              f[i] += 2900.0;
//
//              break;
//
//          case 30 :
//              f[i] = Functions.cf08(t, nx, OShift, M, SS, 1);
//              f[i] += 3000.0;
//
//              break;
            default :

                // System.out.println("\nError: There are only 15 test functions in this test suite!");
                // f[i] = 0.0;
                break;
            }

            // Apply f^*
        }

        if (getFuncEvalCounter(getNumberOfRun()) != null) {
            getFuncEvalCounter(getNumberOfRun()).eval(func_num, nx, x, f);
        }

        return f;
    }

    private void makeStatistics() {}

    private FEvCount getFuncEvalCounter(int run) {
        if (rr == null) {
            return null;
        }

        for (FEvCount counter : funcEvalCounters) {
            if (counter.getCurrentRun() == run) {
                return counter;
            }
        }

        FEvCount fc = new FEvCount(30, 100, run, rr);

        funcEvalCounters.add(fc);

        return fc;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
