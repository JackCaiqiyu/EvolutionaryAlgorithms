import com.benchmark.Rand;
import com.benchmark.Records;
import com.benchmark.cec.cec15.CEC15Benchmark;

/**
 * Created by framg on 26/03/2016.
 */
public class Test {




    public static void main(String [] args) {

       /* double [][] A = new double[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                A[i][j] = i + j;
            }
        }*/
      //  double [] [] A = {{1, 2,     3},{3,     1,     2}, {2,     3,     1}, {2, 2, 2}};
      //  double [] H = {1,2,3};
       /* double [][] A = {{3.0 ,    -2.0 ,     -0.9 ,    2*Math.ulp(1.0)},
                {-2.0  ,    4.0  ,     1.0   , -Math.ulp(1.0)},
                {-Math.ulp(1.0)/4   , Math.ulp(1.0)/2 ,   -1.0  ,   0},
        {-0.5  ,   -0.5  ,     0.1 ,    1.0}};
        RealMatrix n = new Array2DRowRealMatrix(A);
        EigenDecomposition eigenDecomposition = new EigenDecomposition(n);
        System.out.println(eigenDecomposition.getV().toString());
*/
      //  RealMatrix n1 = new Array2DRowRealMatrix(A);
      //  RealMatrix n2 = new Array2DRowRealMatrix(H);
      //  double [] C =n1.multiply(n2).transpose().getData()[0];
//        double mock_double = 0;
//        for(int i=0; i<80; i++){
//            mock_double = ((mock_double + 0.18) * 2)% 1;
//            System.out.println(mock_double);
//        }
//
//        System.exit(0);


        Configuration.records = new Records();
       // int F = 5;
        for(int F=1; F<=1; F++) {
            for (int DIM = 10; DIM <= 10; DIM += 20) {
                Configuration.benchmark = new CEC15Benchmark(DIM, F);
                Configuration.records.startRecord();
                for(int run =0; run<1; run++) {
                    Configuration.D = DIM;
                    Configuration.nF = F;
                    Configuration.maxfunevals = 10000 * DIM;
//                    Configuration.NP = 190;
//                    Configuration.F = 0.5;
//                    Configuration.CR = 0.5;
//                    Configuration.ER = 1;
//                    Configuration.p = 0.11;
//                    Configuration.H = 6;
//                    Configuration.Q = 64;
//                    Configuration.Ar = 3;
//                    Configuration.cw = 0.3;
//                    Configuration.cwinit = 0.3;
//                    Configuration.erw = 0.2;
//                    Configuration.CRmin = 0.05;
//                    Configuration.CRmax = 0.3;
//                    Configuration.NPmin = 4;
//                    Configuration.crw = 0.1;
//                    Configuration.fw = 0.1;
//                    Configuration.EarlyStop = "auto";
//                    Configuration.ConstraintHandling ="Interpolation";
//                    Configuration.noise = false;
//                    Configuration.Xinitial = null;


                    Configuration.NP = 180;
                    Configuration.F = 0.5;
                    Configuration.CR = 0.5;
                    Configuration.ER = 0;
                    Configuration.p = 0.11;
                    Configuration.H = 6;
                    Configuration.Q = 200;
                    Configuration.Ar = 3;
                    Configuration.cw = 0;
                    Configuration.cwinit = 0;
                    Configuration.erw = 0;
                    Configuration.CRmin = 0;
                    Configuration.CRmax = 1;
                    Configuration.NPmin = 4;
                    Configuration.crw = 0.1;
                    Configuration.fw = 0.1;

                    Configuration.EarlyStop = "auto";
                    Configuration.ConstraintHandling ="Interpolation";
                    Configuration.noise = false;
                    Configuration.Xinitial = null;



                    Rand rand = new Rand();
                    Configuration.rand = rand;
                   // com.benchmark.cec.cec05.benchmark com.benchmark.cec.cec05.benchmark = new com.benchmark.cec.cec05.benchmark();
                   // com.benchmark.cec.cec05.test_func com.benchmark.cec.cec05.test_func = com.benchmark.cec.cec05.benchmark.testFunctionFactory(Configuration.nF, Configuration.D);
                   // Configuration.com.benchmark.cec.cec05.benchmark = com.benchmark.cec.cec05.test_func;
                    System.out.println("Fun: "+ F + "Run: " + run + "DIM: " + DIM);
                    //System.out.print("RUN " + run + ":");



                    LSHADE_SPS algorithm = new LSHADE_SPS();
                    algorithm.execute();

                }
               // Configuration.records.endRecord(F, DIM);
                //Configuration.records.write(DIM, F, "LSHADE_SPS", false);
            }
        }
      //  Configuration.records.exportExcel("LSHADE_SPS");
    }
}
