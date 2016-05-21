package com.benchmark.cec.cec05;

import com.benchmark.AllBenchmarks;

import java.net.URL;

/**
 * Created by framg on 03/05/2016.
 */
public class CEC05Benchmark extends AllBenchmarks {
    test_func test_func;
    public static String files = "supportData/";


    public CEC05Benchmark(int DIM, int FUN) {
        super(DIM, FUN, 25);
        //URL url = getClass().getClassLoader().getResource("com/benchmark/cec/cec05/supportData/fbias_data.txt");
        //files = url.getPath();
       // files = "com/benchmark/cec/cec05/supportData/";
//        System.out.println("Working Directory = " +
//                System.getProperty("user.dir"));
        benchmark benchmark = new benchmark();
        test_func = benchmark.testFunctionFactory(FUN, DIM);
    }

    public static int nProblems(){
        return 25;
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
