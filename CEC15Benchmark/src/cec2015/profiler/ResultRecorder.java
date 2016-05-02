
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package cec2015.profiler;

//~--- non-JDK imports --------------------------------------------------------

import cec2015.math.Statistics;
import io.IOUtils;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author QIN
 */
public class ResultRecorder {
    private final ArrayList<OptimResults> results = new ArrayList<OptimResults>();
    private final RecordRule              rule    = new RecordRule();
    private String                        filePrefix;

    /**
     * Create a recorder for results of multiple function evaluation
     *
     * @param resultfilePrefix
     */
    public ResultRecorder(String resultfilePrefix) {
        filePrefix = resultfilePrefix;
    }

    private String makeupFilename(int func_num, int dim, int runtime) {
        return filePrefix + "-f" + func_num + "-d" + dim + "-r" + runtime;
    }

    private String makeupResultHistory(int func_num, int dim) {
        return filePrefix + "-hist-f" + func_num + "-d" + dim + ".txt";
    }

    private String makeupStatFilename(int dim) {
        return filePrefix + "-stat-d" + dim + ".txt";
    }

    public void setFilenamePrefix(String prx) {
        filePrefix = prx;

        for (OptimResults result : results) {
            result.setFilename(makeupFilename(result.getFuncNum(), result.getDim(), result.getRuntime()));
        }
    }

    protected OptimResults getResults(int func_num, int nx, int runtime) {
        for (OptimResults result : results) {
            if ((result.getFuncNum() == func_num) && ((result.getDim() == nx) && (result.getRuntime() == runtime))) {
                return result;
            }
        }

        OptimResults or = new OptimResults(makeupFilename(func_num, nx, runtime), func_num, nx, runtime);

        results.add(or);

        return or;
    }

    public void addRecordRule(int dim, int[] rp) {
        rule.addRule(dim, rp);
    }

    public void flush() {
        for (OptimResults result : results) {
            result.flush();
        }

        int[] funs = getFunNumArray();
        int[] dims = getDimArray();

        for (int d : dims) {
            
            String stat = getStatistics(d);
            if (stat!=null)
                IOUtils.flush(makeupStatFilename(d),stat);

            for (int f : funs) {
                String hist = getHistory(f, d);
                if (hist != null)
                    IOUtils.flush(makeupResultHistory(f, d), hist);
            }
        }
    }

    void record(int func_num, int nx, int runtime, int count, double[] x, double fx) {

        // lazy initialization
        OptimResults or = getResults(func_num, nx, runtime);

        if (rule.isRecord(nx, count)) {
            or.record(count, x, fx);
        }
    }

    private int[] getFunNumArray() {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for (OptimResults result : results) {
            if (!ret.contains(result.getFuncNum())) {
                ret.add(result.getFuncNum());
            }
        }

        int[] funs = new int[ret.size()];

        for (int i = 0; i < ret.size(); i++) {
            funs[i] = ret.get(i);
        }

        Arrays.sort(funs);

        return funs;
    }

    private int[] getDimArray() {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for (OptimResults result : results) {
            if (!ret.contains(result.getDim())) {
                ret.add(result.getDim());
            }
        }

        int[] funs = new int[ret.size()];

        for (int i = 0; i < ret.size(); i++) {
            funs[i] = ret.get(i);
        }

        Arrays.sort(funs);

        return funs;
    }

    private int[] getRuntimeArray() {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for (OptimResults result : results) {
            if (!ret.contains(result.getRuntime())) {
                ret.add(result.getRuntime());
            }
        }

        int[] funs = new int[ret.size()];

        for (int i = 0; i < ret.size(); i++) {
            funs[i] = ret.get(i);
        }

        Arrays.sort(funs);

        return funs;
    }

    private int getIndex(int value, int[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) {
                return i;
            }
        }

        return -1;
    }
    
    private boolean isRecorded(int f, int d) {
         for (OptimResults result : results) {
            if ((result.getFuncNum() == f) && (result.getDim() == d)) {
                if(result.getRecordedY()!=null)
                    return true;                
            }
         }
         return false;
    }

    private double[][] getResultTable(int funNum, int dim) {
        
        if (!isRecorded(funNum, dim))
            return null;
        
        int[]      runtimes = getRuntimeArray();       
        
        double[][] ret      = new double[runtimes.length][];

        for (OptimResults result : results) {
            if ((result.getFuncNum() == funNum) && (result.getDim() == dim)) {
                int index = getIndex(result.getRuntime(), runtimes);

                if (index != -1) {
                    ret[index] = result.getRecordedY();
                }
            }
        }

        return ret;
    }

    private String getHistory(int fun, int d) {
        double[][] ret      = getResultTable(fun, d);
        if (ret==null)
            return null;
        
        int[] runtimes = getRuntimeArray();
        
        
        

        // int[][] ec = get
        String retStr = "% Run\n";

        for (int i = 0; i < runtimes.length; i++) {
            retStr += (runtimes[i] + "\t");
            if (ret[i]==null)
                continue;
            for (int j = 0; j < ret[i].length - 1; j++) {
                retStr += (ret[i][j] + "\t");
            }

            retStr += (ret[i][ret[i].length - 1] + "\n");
        }

        return retStr;
    }

    private String getStatistics(int dim) {
        int[]  funs   = getFunNumArray();
        
        
        String retStr = "%%\t\tStatistics result of " + dim + "D\n% Func.\tBest\tWorst\tMedian\tMean\tStd\n";

        for (int f : funs) {
            double[] stat = getStatistics(f, dim);
            if (stat==null)
                continue;
            retStr += (f + "\t");

            for (int i = 0; i < stat.length - 1; i++) {
                retStr += (stat[i] + "\t");
            }

            retStr += (stat[stat.length - 1] + "\n");
        }

        return retStr;
    }

    // Best      Worst   Median  Mean    Std  Count
    private double[] getStatistics(int funNum, int dim) {
        if (!isRecorded(funNum, dim))
            return null;
        double[][] table    = getResultTable(funNum, dim);     
        
        
        double[]   stat     = new double[5];
        int        runtimes = table.length;
        int n = table.length;
        for(double[] tt: table) {
            if (tt==null)
                n--;
        }
        double[]   result   = new double[n];

        for (int i = 0; i < result.length; i++) {
            if (table[i] !=null)
                result[i] = table[i][table[i].length - 1];
            //else
                //result[i] = Double.NaN;
        }

        Arrays.sort(result, 0, result.length);
        stat[0] = Statistics.calculateMin(result);
        stat[1] = Statistics.calculateMax(result);
        stat[2] = Statistics.calculateMedian(result);
        stat[3] = Statistics.calculateMean(result);
        stat[4] = Statistics.calculateStandardDeviation(result);
        //stat[5] = result.length;

        return stat;
    }

//  public int[] getRuns() {
//      
//      
//      int[] ret = new int[runs.size()];
//
//      for (int i = 0; i < ret.length; i++) {
//          ret[i] = runs.get(i);
//      }
//
//      Arrays.sort(ret);
//
//      return ret;
//  }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
