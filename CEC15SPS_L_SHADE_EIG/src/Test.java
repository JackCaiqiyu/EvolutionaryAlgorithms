/**
 * Created by framg on 26/03/2016.
 */
public class Test {




    public static void main(String [] args) {


        Configuration.records = new Records();
       // int F = 5;
        for(int F=1; F<=15; F++) {
            for (int DIM = 10; DIM <= 10; DIM += 20) {
                Configuration.benchmark = new CEC15Benchmark(DIM, F);
                Configuration.records.startRecord();
                for(int run =0; run<1; run++) {
                    Configuration.D = DIM;
                    Configuration.nF = F;
                    Configuration.maxfunevals = 10000 * DIM;
                    Configuration.NP = 19*DIM;//540;
                    Configuration.CR = 0.5;
                    Configuration.EarlyStop = "fitness";
                    Configuration.ConstraintHandling = "Interpolation";
                    Rand rand = new Rand(seeds.getSeed(Configuration.nF));
                    Configuration.rand = rand;
                   // benchmark benchmark = new benchmark();
                   // test_func test_func = benchmark.testFunctionFactory(Configuration.nF, Configuration.D);
                   // Configuration.benchmark = test_func;
                    System.out.println("Fun: "+ F + "Run: " + run + "DIM: " + DIM);
                    LSHADE_SPS algorithm = new LSHADE_SPS();
                    algorithm.execute();

                }
                Configuration.records.endRecord(F, DIM);
                //Configuration.records.write(DIM, F, "LSHADE_SPS", false);
            }
        }
        Configuration.records.exportExcel("LSHADE_SPS");
    }
}
