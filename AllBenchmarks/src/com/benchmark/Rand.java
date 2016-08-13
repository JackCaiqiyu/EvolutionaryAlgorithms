package com.benchmark;

import java.util.Random;

public class Rand {
    private Random random;

    private boolean isMock = false;
    private float mock_float = 0;
    private double mock_double = 0;
    private int mock_int;


    public Rand(boolean isMock){
        this.isMock = true;
    }

    public Rand(){
        random = new Random();
    }

    public Rand(long seed){
        random = new Random();
        random.setSeed(seed);
    }


    public int getInt(int bound){
        int i;
        do{
            i = random.nextInt(bound);
        }while (i>=bound || i < 0);
        return i;
    }

    public int getInt(int min, int max){
        if(isMock == false) {

            return random.nextInt(max - min + 1) + min;
        }else{
            return Math.round((getFloat()*max) - min);
        }
    }

    public float getFloat(){
        if(isMock == false) {
            return random.nextFloat();
        }else{
            mock_float += 0.3;
            //double algo = ((mock_double + 0.1) / 3) * 2;
            if(mock_float > 1){
                mock_float = 0;
            }
            return mock_float;
        }

    }

    private double Beta(int a, int b){
        double x = Gamma1(a);
        double y = Gamma1(b);
        return x/(x+y);
    }

    private double Gamma1(int n){
        double sum = 0;
        for (int i = 0; i < n; i++){
            sum += -Math.log(random.nextDouble());
        }
        return sum;
    }

    public double getDouble(){
        if(isMock == false) {
            return random.nextDouble();
            //return Beta(111111, 22222);
        }else{
            mock_double += 0.017;
            //mock_double = ((mock_double + 0.18) * 2)% 1;
            if(mock_double > 1){
                mock_double = 0;
           }
//            if(mock_double == 0){
//                mock_double = 0.1;
//            }
            return mock_double;
        }

    }

    public float getFloat(float min, float max){
        return random.nextFloat() * (max - min) + min;
    }
    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public double uniform() {
        return random.nextDouble();
    }

    public double randn() {
        return random.nextGaussian();
    }

    /**
     * Returns a random real number uniformly in [a, b).
     *
     * @param  a the left endpoint
     * @param  b the right endpoint
     * @return a random real number uniformly in [a, b)
     * @throws IllegalArgumentException unless <tt>a < b</tt>
     */
    public double uniform(double a, double b) {
        if (!(a < b)) throw new IllegalArgumentException("Invalid range");
        return a + uniform() * (b-a);
    }

    /**
     * Returns a random real number from a standard Gaussian distribution.
     *
     * @return a random real number from a standard Gaussian distribution
     *         (mean 0 and standard deviation 1).
     */
    public double gaussian() {
        // use the polar form of the Box-Muller transform
        if(!isMock) {
            double r, x, y;
            do {
                x = uniform(-1.0, 1.0);
                y = uniform(-1.0, 1.0);
                r = x * x + y * y;
            } while (r >= 1 || r == 0);
            return x * Math.sqrt(-2 * Math.log(r) / r);
        }else{
            return getDouble();
        }
        // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
        // is an independent random gaussian
    }

    /**
     * Returns a random real number from a Gaussian distribution with mean &mu;
     * and standard deviation &sigma;.
     *
     * @param  mu the mean
     * @param  sigma the standard deviation
     * @return a real number distributed according to the Gaussian distribution
     *         with mean <tt>mu</tt> and standard deviation <tt>sigma</tt>
     */
    public double gaussian(double mu, double sigma) {
        if(!isMock){
        return mu + sigma * gaussian();
        }else{
            return getDouble();
    }

}


    public double cauchyrnd(double a, double b){
        return a + b * Math.tan(Math.PI*(getFloat()-0.5));
    }


    public double [] cauchyrnd(int dim, double a, double b){
        double [] probs = new double[dim];

        if(!isMock) {
            for (int i = 0; i < dim; i++) {
                probs[i] = a + b * Math.tan(Math.PI * (getFloat() - 0.5));
            }
        }else{
            for (int i = 0; i < dim; i++) {
                probs[i] = getDouble();
            }
        }
        return probs;
    }



    public int [] randperm(int n){
        int [] rand = new int[n];
        if(!isMock) {
            int pos;
            int aux;
            for (int i = 0; i < n; i++) {
                rand[i] = i;
            }

            for (int i = 0; i < n; i++) {
                pos = getInt(0, n - 1);
                aux = rand[i];
                rand[i] = rand[pos];
                rand[pos] = aux;
            }
        }else{
            int pos;
            int aux;
            for (int i = 0; i < n; i++) {
                rand[i] = i;
            }

            for (int i = 0; i < n; i++) {
                pos = (int)Math.round(getDouble() * (n-1));
                aux = rand[i];
                rand[i] = rand[pos];
                rand[pos] = aux;
            }
        }

        return rand;
    }

}
