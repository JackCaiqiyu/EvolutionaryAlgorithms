import com.benchmark.AllBenchmarks;

/**
 * Created by framg on 06/06/2016.
 */
public class TestFunction {

    public static double testFuction(double [] x){
        double f = Configuration.benchmark.f(x);

        if(f < AllBenchmarks.objective()){
            f = 0;
        }
        MVMO.proc.i_eval++;

        if(MVMO.proc.i_eval <= MVMO.proc.n_eval && MVMO.proc.i_eval >= 1){
            if(MVMO.proc.best_value > f){
                MVMO.proc.best_value = f;
            }
        }
        if(MVMO.proc.i_eval >= MVMO.proc.n_eval || MVMO.proc.best_value < AllBenchmarks.objective()){
            MVMO.proc.finish=true;
        }

        return f;
    }


}
