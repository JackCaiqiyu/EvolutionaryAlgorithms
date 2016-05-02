/**
 * Created by framg on 03/05/2016.
 */
public class CEC05Benchmark extends AllBenchmarks{
    test_func test_func;

    CEC05Benchmark(int DIM, int FUN) {
        super(DIM, FUN, 25);
        benchmark benchmark = new benchmark();
        test_func = benchmark.testFunctionFactory(FUN, DIM);
    }

    @Override
    public double f(double[] x) {
        return test_func.f(x);
    }

    @Override
    public double bias() {
        return bias.getBias(FUN);
    }

    @Override
    public double lbound() {
        return Bounds.getLowerBound(FUN);
    }

    @Override
    public double ubound() {
        return Bounds.getUpperBound(FUN);
    }
}
