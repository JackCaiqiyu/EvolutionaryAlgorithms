package com.benchmark;

/**
 * Created by framg on 26/03/2016.
 */
public abstract class AllBenchmarks {
    protected int DIM;
    protected int FUN;
    protected int n_FUN;

    public AllBenchmarks(int DIM, int FUN, int n_FUN){
        this.DIM = DIM;
        this.FUN = FUN;
        this.n_FUN = n_FUN;
    }

    public static double objective(){
        return 10e-8;
    }

    public static int runs(){
        return 50;
    }

    public abstract double f(double[] x);
    public abstract double bias();
    public abstract double lbound();
    public abstract double ubound();


}
