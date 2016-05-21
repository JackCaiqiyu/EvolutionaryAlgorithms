import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

/**
 * Created by framg on 15/05/2016.
 */
public class Matlab {
    static double [][] eigV(double [][] m){
        RealMatrix CRealMatrix = new Array2DRowRealMatrix(m);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(CRealMatrix);
        return eigenDecomposition.getV().getData();
    }

    static double [][] cov (double [][] m){
        Covariance covariance = new Covariance(m);
        return covariance.getCovarianceMatrix().getData();
    }


}
