import com.benchmark.AllBenchmarks;

public class NBIPOPaCMA {
    public NBIPOPaCMA() {

    }

    public double execute() {

        Configuration.myStat  = new Xcmaes.MyStat();
        Configuration.myStat.nevals = 0;
        Configuration.myStat.fmin = Double.POSITIVE_INFINITY;
        Configuration.myStat.bestx = null;

        int MAX_EVALS = Configuration.max_evals;
        int irest = 0;
//        Configuration.BIPOP = true;
//        Configuration.newRestartRules = false;
//        Configuration.CMAactive = false;

       // double fbest = Double.POSITIVE_INFINITY;
        while (Configuration.myStat.nevals < MAX_EVALS && Math.abs(Configuration.myStat.fmin - Configuration.benchmark.bias()) > AllBenchmarks.objective()) {
//            if(Configuration.BIPOP == true && Configuration.newRestartRules == false && Configuration.CMAactive == false){
//                System.out.println("DEBUG");
//            }


            Configuration.iGlobalRun++;
            int maxevals_available = MAX_EVALS - Configuration.myStat.nevals;
            Xcmaes xcmaes = new Xcmaes(maxevals_available);
            xcmaes.execute();
            irest ++;

        //    System.out.println(Math.abs(Configuration.myStat.fmin - Configuration.benchmark.bias()));
         //   System.out.println("BIPOP: " + Configuration.BIPOP);
          //  System.out.println("NEWSTARTRULES: " + Configuration.newRestartRules);
          //  System.out.println("CMAactive: " + Configuration.CMAactive);
           // System.out.println("DEBUG");
        }
        return Math.abs(Configuration.myStat.fmin - Configuration.benchmark.bias());
    }
}
