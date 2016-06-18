/**
 * Created by framg on 16/06/2016.
 */
public class Options {
    int ParentNumber;
    String RecombinationWeights;
    CMA cma;
    Noise noise;



    public static class CMA{
        double ccum;
        double cs;
        boolean on;
        double ccov1;
        double ccovmu;
        double damps;
    }

    public static class Noise{
        int reevals;
        double alphasigma;
        double epsilon;
        double theta;
        double cum;
        double cutoff;
    }
}
