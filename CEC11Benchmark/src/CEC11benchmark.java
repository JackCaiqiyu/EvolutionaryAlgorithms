/**
 * Created by framg on 02/05/2016.
 */
public class CEC11benchmark {

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

   /* static double function2(double [] x){
        //The bifunctional catalyst blend optimal control problem
                tol=1.0e-01;// tol is a matter of concern. decreasing it make algo fast.
                tspan=[0 0.78];// check for the range
        yo =[1 0 0 0 0 0 0];
        u=x;//u should be passed here.
                options = odeset('RelTol',tol);
        [T,Y] = ode45(@(t,y) diffsolv(t,y,u),tspan,yo,options);
        w=size(Y);
        f=Y(w(1),w(2))*1e+003;
    }*/

}
