public class Test {

    public static void main(String [] args) {

        for(int F=1; F<=25; F++) {
            for (int DIM = 10; DIM <= 50; DIM += 20) {
                Configuration.records = new Records();
                for(int run =0; run<25; run++) {
                    Configuration.dim = DIM;
                    Configuration.I_fno = F;
                    Configuration.max_eval = 10000 * DIM;
                    Rand rand = new Rand(seeds.getSeed(Configuration.I_fno));
                    Configuration.rand = rand;
                    GAMPC gampc = new GAMPC();
                    gampc.execute();
                }
                Configuration.records.write(DIM, F, "GA_MPC", false);
            }
        }


    }

}
