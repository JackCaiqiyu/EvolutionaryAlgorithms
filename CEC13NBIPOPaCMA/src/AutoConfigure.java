import com.benchmark.Rand;

import javax.xml.crypto.dom.DOMCryptoContext;

/**
 * Created by framg on 18/06/2016.
 */
public class AutoConfigure {
    int DIM;
    int F;

    public AutoConfigure(int DIM, int F){
        this.DIM = DIM;
        this.F = F;
    }

    public int [] configure(){
        Configuration.withModelOptimization = true;
        Configuration.hyper_lambda = 20;
        Configuration.iSTEPminForHyperOptimization = 1;
        Configuration.maxStepts = 20;
        Configuration.alpha = 0.20;
        Configuration.iterstart = 10;
        Configuration.rand = new Rand();
        Configuration.DIM = DIM;
        Configuration.F = F;
        Configuration.max_evals = 10000 * DIM;


        int [] configuration = new int[3];
        double fmin = Double.POSITIVE_INFINITY;

        for(int i=0; i<=1; i++){
            for(int j=0; j<=1; j++){
                for(int k=0; k<=1; k++){
                    if(i==0){
                        Configuration.BIPOP = false;
                    }else{
                        Configuration.BIPOP = true;
                    }
                    if(j==0){
                        Configuration.newRestartRules = false;
                    }else{
                        Configuration.newRestartRules = true;
                    }
                    if(k==0){
                        Configuration.CMAactive = false;
                    }else{
                        Configuration.CMAactive = true;
                    }

                    NBIPOPaCMA nbipoPaCMA = new NBIPOPaCMA();
                    double f = nbipoPaCMA.execute();
                    if(fmin > f){
                        fmin = f;
                        configuration[0] = i;
                        configuration[1] = j;
                        configuration[2] = k;
                    }
                }
            }
        }

        return configuration;
    }

}
