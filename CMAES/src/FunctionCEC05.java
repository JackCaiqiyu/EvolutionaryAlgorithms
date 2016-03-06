public class FunctionCEC05 extends AbstractObjectiveFunction{
    private int nFun;
    private int dim;

    public FunctionCEC05(int nFun, int dim){
        this.nFun = nFun;
        this.dim = dim;
    }

    @Override
    public double valueOf(double[] x) {
        benchmark benchmark = new benchmark();
        test_func aTestFunc = benchmark.testFunctionFactory(nFun, dim);

        return aTestFunc.f(x);

    }
}
