import com.benchmark.Rand;
import com.benchmark.cec.cec05.Bounds;

public class GAMPC {

    int current_eval = 0;
    int max_eval;
    int previous_eval;
    int iter = 0;
    float beta;
    int arch_size;

    float [][] offspring_individuals;
    float [][] all_individuals;
    float [][] x;
    float [] fitx;
    float [] fitx_all;
    float[][] archive;
    int[] best;

  //  com.benchmark.cec.cec05.benchmark com.benchmark.cec.cec05.benchmark = new com.benchmark.cec.cec05.benchmark();
   // com.benchmark.cec.cec05.test_func aTestFunc;

    public GAMPC(){
        archive = new float[Configuration.popSize/2][Configuration.dim];
        arch_size = Configuration.popSize/2;
        x = new float[Configuration.popSize][Configuration.dim];
        best = new int[Configuration.popSize*3];

        offspring_individuals = new float [Configuration.popSize][Configuration.dim];
        all_individuals= new float [Configuration.popSize + arch_size][Configuration.dim];
        fitx = new float[Configuration.popSize];
        fitx_all = new float[Configuration.popSize + arch_size];


        initialize();
    }

    private void initializePoblation(){
        for(int i=0; i<Configuration.popSize; i++){
            for(int j=0; j<Configuration.dim; j++){
                x[i][j] = (float) (Configuration.benchmark.lbound() + Configuration.rand.getFloat()* (Configuration.benchmark.ubound() - Configuration.benchmark.lbound()));
            }
        }

        for(int i=0; i<Configuration.popSize; i++){
            fitx[i] = (float) Configuration.benchmark.f(arrayFloatToDouble(x[i]));
        }
    }

    private void initialize(){
        current_eval = 0;
        previous_eval = 0;
        iter = 0;
        max_eval = Configuration.max_eval;
        initializePoblation();
       sortByFitnessPopulation();
    }

    public void execute(){
        while(current_eval < max_eval && fitx[0] - Configuration.benchmark.bias() > Configuration.Ter_Err){
            previous_eval = current_eval;
            current_eval = current_eval + Configuration.popSize;
            iter = iter + 1;


            for (int i=0; i<Configuration.popSize/2; i++){
                archive[i] = copyArray(x[i]);
            }

            /****************************STEP 3 *******************************/
            for(int i=0; i<Configuration.popSize*3; i++){
                int TcSize = Configuration.rand.getInt(2,3);
                int[] randnum = new int[TcSize];
                for(int tc=0; tc<TcSize; tc++){
                    randnum[tc] = Configuration.rand.getInt(Configuration.popSize);
                }
                best[i] = min(randnum, TcSize);
            }

            /******************STEP4*********************************/
            for(int i=0; i<Configuration.popSize; i+=3){
                int [] consecutive = new int [3];

                beta = (float) Configuration.rand.gaussian(0.7f, 0.1f);

                consecutive[0] = best[i];
                consecutive[1] = best[i+1];
                consecutive[2] = best[i+2];

                sort(consecutive, 3);
                if(consecutive[0] == consecutive[1]){
                    while ((consecutive[1] == consecutive[0]) || (consecutive[1] == consecutive[2])){
                        consecutive[1] = Configuration.rand.getInt(Configuration.popSize);
                    }
                    sort(consecutive, 3);
                }

                if(consecutive[0] == consecutive[2]){
                    while ((consecutive[2] == consecutive[0]) || (consecutive[2] == consecutive[1])){
                        consecutive[2] = Configuration.rand.getInt(Configuration.popSize);
                    }
                    sort(consecutive, 3);
                }

                if(consecutive[1] == consecutive[2]){
                    while ((consecutive[2] == consecutive[0]) || (consecutive[2] == consecutive[1])){
                        consecutive[2] = Configuration.rand.getInt(Configuration.popSize);
                    }
                    sort(consecutive, 3);
                }


                if(Configuration.rand.getFloat() < 1){
                    for(int j=0; j<Configuration.dim; j++){
                        offspring_individuals[i][j] = x[consecutive[0]][j] + beta * (x[consecutive[1]][j] - x[consecutive[2]][j]);
                        offspring_individuals[i+1][j] = x[consecutive[1]][j] + beta * (x[consecutive[2]][j] - x[consecutive[0]][j]);
                        offspring_individuals[i+2][j] = x[consecutive[2]][j] + beta * (x[consecutive[0]][j] - x[consecutive[1]][j]);
                    }
                }

            }
            /************************************************************/
            //Si algun valor de x se sale fuera de rango se reinicializa
            for(int i=0; i<Configuration.popSize; i++){
                han_boun(offspring_individuals, Configuration.dim,  i);
            }

            /********************************STEP5**********************************/
            for(int i=0; i<Configuration.popSize; i++){
                for(int j=0; j<Configuration.dim; j++){
                    if(Configuration.rand.getFloat() < Configuration.p){
                        int pos = Configuration.rand.getInt(arch_size);
                        offspring_individuals[i][j] = archive[pos][j];
                    }
                }
            }


            /***************************************************************************/

            for (int i=0; i<Configuration.popSize + arch_size; i++){
                if(i < arch_size){
                    all_individuals[i] = copyArray(archive[i]);
                }else{
                    int current = i - arch_size;
                    all_individuals[i] = copyArray(offspring_individuals[current]);
                }
            }

            for(int i=0; i<Configuration.popSize + arch_size ; ++i){
                if(i < arch_size){
                    fitx_all[i] = fitx[i];
                }else {
                    fitx_all[i] = (float) Configuration.benchmark.f(arrayFloatToDouble(all_individuals[i]));
                }
            }

            sortByFitnessPopulationAll();
            for(int i=0; i<Configuration.popSize; i++){

                x[i] = copyArray(all_individuals[i]);
                fitx[i] = fitx_all[i];
            }

         //   System.out.println("Best result: "+fitx[0]+" in eval: " +current_eval);
            Configuration.records.newRecord(fitx[0] - Configuration.benchmark.bias(), current_eval);



            /********************************STEP6************************************/
            for(int i=1; i<Configuration.popSize; i++){
                int similar =0;
                for (int j=0; j<Configuration.dim; j++){
                    if(Math.floor((double)x[i][j]) == Math.floor((double)x[i-1][j])){
                        similar++;
                    }
                }
                if(similar == Configuration.dim){
                    for(int j=0; j<Configuration.dim; j++){
                        x[i][j] = (float) (x[i][j] + Configuration.rand.gaussian(0.5 * (double)Configuration.rand.getFloat(), 0.25 * (double)Configuration.rand.getFloat()));
                    }
                }
            }



        }
        Configuration.records.endRun(fitx[0] - Configuration.benchmark.bias(), current_eval, max_eval );
    }

    private int min(int [] array, int tam){
        int pos = array[0];
        for(int i=1; i<tam; i++){
            if(array[i] <= pos){
                pos = array[i];
            }
        }
        return pos;
    }


    private void sort(int [] array, int tam){
        int tmp;
        for(int i=0; i<tam; i++){
            for(int j=i+1; j<tam; j++){
                if(array[i] > array[j]){
                    tmp = array[i];
                    array[i] = array[j];
                    array[j] = tmp;
                }
            }
        }
    }

    private void sortByFitnessPopulation(){
        float tmpfit;
        float [] tmpx;
        for(int i=0; i<Configuration.popSize; i++){
            for(int j=i+1; j<Configuration.popSize; j++){
                if(fitx[i] > fitx[j]){
                    tmpfit = fitx[j];
                    fitx[j] = fitx[i];
                    fitx[i] = tmpfit;

                    tmpx = copyArray(x[i]);
                    x[i] = copyArray(x[j]);
                    x[j] = copyArray(tmpx);
                }
            }
        }
    }

    private void sortByFitnessPopulationAll(){
        float tmpfit;
        float [] tmpx;
        for(int i=0; i<Configuration.popSize + arch_size; i++){
            for(int j=i+1; j<Configuration.popSize+ arch_size; j++){
                if(fitx_all[i] > fitx_all[j]){
                    tmpfit = fitx_all[j];
                    fitx_all[j] = fitx_all[i];
                    fitx_all[i] = tmpfit;

                    tmpx = copyArray(all_individuals[i]);
                    all_individuals[i] = copyArray(all_individuals[j]);
                    all_individuals[j] = copyArray(tmpx);
                }
            }
        }
    }


    private double[] arrayFloatToDouble(float [] floatArray){
        double[] doubleArray = new double[floatArray.length];
        for (int i = 0 ; i < floatArray.length; i++)
        {
            doubleArray[i] = (float) floatArray[i];
        }
        return doubleArray;
    }


    private float[] copyArray(float [] aOld){
        float [] aNew = new float [aOld.length];
        for(int i=0; i<aOld.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }

    public static void han_boun(float [][] x, int n, int i){
        /*
                for j=1: n
            if( x(i,j) <xmin (j))
                x(i,j)=   xmin (j) +rand*(xmax(j)-xmin(j));
            else if ( x(i,j)>xmax (j))
                    x(i,j)=   xmin (j) +rand*(xmax(j)-xmin(j));
                end
            end
        end
         */

        for(int j=0; j<n; j++){
            if(x[i][j] < Configuration.benchmark.lbound()){
                x[i][j] = (float) (Configuration.benchmark.lbound() + Configuration.rand.getFloat()* (Configuration.benchmark.ubound() - Configuration.benchmark.lbound()));
            }else if( x[i][j] > Configuration.benchmark.ubound()){
                x[i][j] = (float) (Configuration.benchmark.lbound() + Configuration.rand.getFloat() * (Configuration.benchmark.ubound() - Configuration.benchmark.lbound()));
            }
        }
    }


}
