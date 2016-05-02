/**
 * Created by framg on 17/04/2016.
 */
public class Test {
    public static void main(String [] args) {


        for(int F=1; F<=25; F++) {
            for (int DIM = 10; DIM <= 50; DIM += 20) {
                Configuration.records = new Records();
                for(int run =0; run<1; run++) {
                    Configuration.DIM = DIM;
                    Configuration.F = F;

                    Configuration.tunea = 9.687;
                    Configuration.tuneb = 1.614;
                    Configuration.tunec = 0.6825;
                    Configuration.tuned = 3.245;
                    Configuration.tunee = -9.023;
                    Configuration.tunef = -10.82;
                    Configuration.tuneg = -16.26;
                    Configuration.getlearn_perbudget = 0.15;
                    Configuration.getmtsls1_iterbias_choice = 0.01910;
                    Configuration.getmtsls1_initstep_rate = 0.6703;
                    Configuration.getmtsls1per_ratedim = 1;


                    Configuration.seed = seeds.getSeed(Configuration.F);
                    Rand rand = new Rand(seeds.getSeed(Configuration.F));
                    Configuration.rand = rand;
                    benchmark benchmark = new benchmark();
                    test_func test_func = benchmark.testFunctionFactory(Configuration.F, Configuration.DIM);
                    Configuration.benchmark = test_func;
                    System.out.println("Fun: "+ F + "Run: " + run + "DIM: " + DIM);
                    ICMAESILS algorithm = new ICMAESILS();
                    algorithm.execute();
                }
                Configuration.records.write(DIM, F, "ICMAESILS", false);
            }
        }
    }


}
