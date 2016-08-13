package CMAES;

/**
 * Created by framg on 15/06/2016.
 */
public class Options {
    public int max_evals;
    public boolean max_iter;
    public  boolean StopOnStagnation;
    public  boolean TolFun;
    public  boolean TolHistFun;
    public  boolean TolX;
    public  boolean TolUpX;
    public static boolean ccov1;
    public static boolean ccovmu;
    public static boolean ccum;
    public  boolean cmaActve;
    public  int restarts;

    public Options(){

    }

}
