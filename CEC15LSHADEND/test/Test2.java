import java.io.File;

/**
 * Created by framg on 07/05/2016.
 */
public class Test2 {


    public static boolean setCurrentDirectory(String directory_name)
    {
        boolean result = false;  // Boolean indicating whether directory was set
        File directory;       // Desired current working directory

        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs())
        {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }

        return result;
    }

    @org.junit.Test
    public void LSHADENDTest(){
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        // testVi();
       // setCurrentDirectory("C:\\Users\\framg\\proyectos\\EvolutionaryAlgorithms\\");
        Configuration.records = new Records();
        int F = 5;
        //CEC15Benchmark benchmarkLearning = new CEC15Benchmark(10, 5);
        // double []x = {44.064898688431610, -57.965198017032080, -71.398834348380120, 46.018242218020870, 81.862397730987340, 82.203847931393650, 76.751101636120180, 67.399556442702450, 81.479228448199450, 85.516188519508690};
        // System.out.println(benchmarkLearning.f(x));
        // for(int F=8; F<=8; F++) {
        for (int DIM = 10; DIM <= 10; DIM += 20) {
            Configuration.benchmark = new CEC15Benchmark(DIM, F);
            Configuration.records.startRecord();
            for(int run =0; run<1; run++) {
                Configuration.D = DIM;
                Configuration.nF = F;
                Configuration.max_nfes = 10000 * DIM;
                //Rand rand = new Rand(seeds.getSeed(Configuration.nF));
                Configuration.rand = new Rand(true);
                //benchmark benchmark = new benchmark();
                //test_func test_func = benchmark.testFunctionFactory(Configuration.nF, Configuration.D);
                //Configuration.benchmark = test_func;
                System.out.println("Fun: "+ F + "Run: " + run + "DIM: " + DIM);
                LSHADEND algorithm = new LSHADEND();
                algorithm.execute();
            }
            Configuration.records.endRecord(F,DIM);
        }
        //   }

        Configuration.records.exportExcel("LSHADEND");
    }


}
