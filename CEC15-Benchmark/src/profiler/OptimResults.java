
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package profiler;

//~--- JDK imports ------------------------------------------------------------

import io.IOUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author QIN
 */
public class OptimResults {
    private final ArrayList<double[]> xdata        = new ArrayList<double[]>();
    private final ArrayList<Double>   ydata        = new ArrayList<Double>();
    private final ArrayList<Integer>  recordnumber = new ArrayList<Integer>();
    private String                    filename;
    private final int                 func_num, dim, runtime;

    OptimResults(String resultfilename, int func_num, int dim, int runtime) {
        filename      = resultfilename;
        this.func_num = func_num;
        this.dim      = dim;
        this.runtime  = runtime;
    }

    void setFilename(String fn) {
        filename = fn;
    }

    public int getFuncNum() {
        return func_num;
    }

    public int getDim() {
        return dim;
    }

    public int getRuntime() {
        return runtime;
    }

    int[] getRecordPoints() {
        if (recordnumber.isEmpty())
            return null;
        int[] ret = new int[recordnumber.size()];

        for (int i = 0; i < recordnumber.size(); i++) {
            ret[i] = recordnumber.get(i);
        }

        return ret;
    }

    double[] getRecordedY() {
        if (ydata.isEmpty())
            return null;
        double[] ret = new double[ydata.size()];

        for (int i = 0; i < ydata.size(); i++) {
            ret[i] = ydata.get(i);
        }

        return ret;
    }

    void record(int count, double[] x, double fx) {
        xdata.add(x);
        ydata.add(fx);
        recordnumber.add(count);
    }

    void flush() {
        
        
        IOUtils.flush(filename+"-x.txt", reportX());
        IOUtils.flush(filename+"-fx.txt", reportY());

    }

    private String reportX() {
        String rep = "%count   \tx\n";

        for (int i = 0; i < recordnumber.size(); i++) {
            int rn = recordnumber.get(i);

            // double fx = ydata.get(i).doubleValue();
            double[] x = xdata.get(i);

            rep = rep + "  " + rn + "\t";

            for (int j = 0; j < x.length; j++) {
                rep = rep + x[j] + "\t";
            }

            rep = rep + "\n";
        }

        return rep;
    }

    private String reportY() {
        String rep = "%count   \ty\n";

        for (int i = 0; i < recordnumber.size(); i++) {
            int    rn = recordnumber.get(i);
            double fx = ydata.get(i);

            // double[] x = xdata.get(i);
            rep = rep + "  " + rn + "\t" + fx + "\n";
        }

        return rep;
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
