package com.benchmark.cec.cec11;

/**
 * Created by framg on 02/05/2016.
 */
public class CEC11Benchmark {

    static double function1(double [] x){
        double theta=2* Math.PI/100;
        double f=0;
        for (int t=0; t<100; t++) {
            double y_t = x[0] * Math.sin(x[1] * t * theta + x[2] * Math.sin(x[3] * t * theta + x[4] * Math.sin(x[5] * t * theta)));
            double y_0_t = 1 * Math.sin(5 * t * theta - 1.5 * Math.sin(4.8 * t * theta + 2 * Math.sin(4.9 * t * theta)));
            f = f + Math.pow(y_t - y_0_t, 2);
        }
        return f;
    }

    static double function2(double [] x){
//
//        int p= x.length;
//        int n=p/3;
//
//        x=reshape(x,3,n)';
//        v=0;
//        a=ones(n,n);
//        b=repmat(2,n,n);
//        //double [][] r = new double[n][]
//        for i=1:(n-1)
//        for j=(i+1):n
//        r(i,j)=sqrt(sum((x(i,:)-x(j,:)).^2));
//        v=v+(a(i,j)/r(i,j)^12-b(i,j)/r(i,j)^6);
//        end
//                end
//        f=v;
        return 0;
    }

}
