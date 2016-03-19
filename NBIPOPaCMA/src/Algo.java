
public class Algo {
    public  int iterstart;
    public  int iter;
    public  double sav_fitness;
    public  double sav_out;
    public  int  sav_counteval;
    public  double [][] coeffs;
    public  double coeffs_mean;
    public int  realfunc;
    public double[] model_err;
    public double[] model_avrerr;
    public boolean withModelOptimization;
    public boolean withDisp;
    public double err;
    public int nevals;
    public int iSZ;
    public int aSZ;
    public double Fmin;
    public boolean CMAactive;
    public boolean withSurr;
    public boolean withFileDisp;
    public int maxArchSize;
    public double [][] ARX;
    public double [][] ARF;
    public  double [][] ARneval;

    public Algo() {
    }

    public Algo(Algo algo) {
    }
}
