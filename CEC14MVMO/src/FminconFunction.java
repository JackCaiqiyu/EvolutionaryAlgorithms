/**
 * Created by framg on 06/06/2016.
 */
public class FminconFunction extends Matlab.FminconFunction {
    @Override
    public double f(double[] x) {
        return TestFunction.testFuction(x);
    }
}
