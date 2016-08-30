import lbfgsb.FunctionValues;

/**
 * Created by framg on 06/06/2016.
 */
public class FminconFunction extends Matlab.FminconFunction {
    @Override
    public double f(double[] x) {
        return TestFunction.testFuction(x);
    }

    @Override
    public double[] g(double[] x) {
        return TestFunction.gradient(x);
    }

    @Override
    public double lb() {
        return TestFunction.lb();
    }

    @Override
    public double ub() {
        return TestFunction.ub();
    }

    @Override
    public FunctionValues getValues(double[] x) {
        return new FunctionValues(f(x), g(x));
    }
}
