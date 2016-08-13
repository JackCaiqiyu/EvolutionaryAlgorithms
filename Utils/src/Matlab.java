
import lbfgsb.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;

/**
 * Created by framg on 15/05/2016.
 */
public final class Matlab {


    private Matlab(){

    }


    static double [][] eigV(double [][] m){
        RealMatrix CRealMatrix = new Array2DRowRealMatrix(m);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(CRealMatrix);
        return eigenDecomposition.getV().getData();
    }

    static double [][] eigD(double [][] m){
        RealMatrix CRealMatrix = new Array2DRowRealMatrix(m);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(CRealMatrix);
        return eigenDecomposition.getD().getData();
    }

    static double [][] cov (double [][] m){
        Covariance covariance = new Covariance(m);
        return covariance.getCovarianceMatrix().getData();
    }

    //Identity matrix
    static double [][] eye (int dim){
        return MatrixUtils.createRealIdentityMatrix(dim).getData();
    }

    static double [][] triu(double [][] A, int k){
        double [][] m = Util.copyMatrix(A);

        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                if( i < j + k){
                    m[i][j] = 0;
                }
            }
        }
        return m;
    }


    static double [] diag (double [][] m){
        double [] a = new double[m.length];
        for(int i=0; i<m.length; i++){
            for(int j=0; j<m[0].length; j++){
                if(i == j){
                    a[i] = m[i][j];
                }
            }
        }
        return a;
    }

    static double[][] diag (double [] a){
        RealMatrix matrix = MatrixUtils.createRealDiagonalMatrix(a);
        return matrix.getData();
    }

    static double det(double [][] m){
        RealMatrix matrix = new Array2DRowRealMatrix(m);
        return new LUDecomposition(matrix).getDeterminant();
    }

    static fminconOutput fmincon(FminconFunction fun, double [] x, int FEsAllowed){
//        fminconOutput out = new fminconOutput();
//        out.fit = fun.f(x);
//        out.x = Util.copyArray(x);
//        out.evals = 1;
//        return out;

        fminconOutput out = new fminconOutput();
        Minimizer alg = new Minimizer();
        ArrayList<Bound> bounds = new ArrayList<>();
        for(int i=0; i<x.length; i++) {
            bounds.add(new Bound(fun.lb(), fun.ub()));
        }
        alg.setBounds(bounds);
        alg.getStopConditions().setMaxIterations(FEsAllowed);
        System.out.println(fun.f(x));
        try {
            Result result = alg.run(fun, x);
            out.fit = result.functionValue;
            out.x = Util.copyArray(result.point);
            out.evals = result.iterationsInfo.functionEvaluations;
            System.out.println(result);
        } catch (LBFGSBException e) {
            e.printStackTrace();
        }
        return out;
    }


    public static class fminconOutput {
        public double [] x;
        public double fit;
        public int evals;
        public double oox;
    }

    public static abstract class FminconFunction implements DifferentiableFunction {
        public abstract double f(double [] x);
        public abstract double [] g(double [] x);
        public abstract double lb();
        public abstract double ub();
    }
}
