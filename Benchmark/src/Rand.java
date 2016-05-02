import java.util.Random;

public class Rand {
    public Random random;

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


        return random.nextInt(max - min + 1) + min;
    }

    public float getFloat(){
        return random.nextFloat();
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
        double r, x, y;
        do {
            x = uniform(-1.0, 1.0);
            y = uniform(-1.0, 1.0);
            r = x*x + y*y;
        } while (r >= 1 || r == 0);
        return x * Math.sqrt(-2 * Math.log(r) / r);

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
        return mu + sigma * gaussian();
    }


    public double cauchyrnd(double a, double b){
        return a + b * Math.tan(Math.PI*(getFloat()-0.5));
    }


    public double [] cauchyrnd(int dim, double a, double b){
        double [] probs = new double[dim];

        for(int i=0; i<dim; i++){
            probs[i] = a + b * Math.tan(Math.PI*(getFloat()-0.5));
        }

        return probs;
    }



    public int [] randperm(int n){
        int [] rand = new int[n];
        int pos;
        int aux;
        for(int i=0; i<n; i++){
            rand[i] = i;
        }

        for(int i=0; i<n; i++){
            pos = getInt(0, n-1);
            aux = rand[i];
            rand[i] = rand[pos];
            rand[pos] = aux;
        }
        return rand;
    }

}
