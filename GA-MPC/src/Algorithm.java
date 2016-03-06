import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {
    Rand rand = new Rand();
    int current_eval = 0;
    int max_eval = Configuration.max_eval;
    int previous_eval;
    int iter = 0;
    //List<Float> archive = new ArrayList<>();
    float[][] archive;
    int[] best;
    int[] consecutive;
    float beta;
    int best_size = 0;
    int arch_size[];
    float [][] offspring_individuals;
    float [][] x;


    public Algorithm(){
        archive = new float[Configuration.popSize][Configuration.dim];
        x = new float[Configuration.popSize][Configuration.dim];
    }



    public void execute(){

        benchmark benchmark = new benchmark();
        test_func aTestFunc = benchmark.testFunctionFactory(Configuration.I_fno, Configuration.dim);
       // double result = aTestFunc.f(x);

        while(current_eval < max_eval){
            previous_eval = current_eval;
            current_eval = current_eval + Configuration.popSize;
            iter = iter + 1;

             /*
            %%%%% Create an archive pool = 0.5 * PopSize
            for i=1:PopSize/2
            archive (i,:) = x(i,:);
            end
            */
            for (int i=0; i<Configuration.popSize/2; i++){
                archive[i] = x[i]; //TODO initializate archive and x
                //arch_size++; //TODO copiar x en archive y guardar tamños
            }

           /*%%% Selection Pool Popsize * 3
            for i=1:PopSize*3

            TcSize =randi([2,3]); %% select the TC size 2 or 3, randomly.
            for  tc=1: TcSize %%% select the individual that follow the tournament
            randnum(tc) = randi(PopSize);
            end
            best(i) = min(randnum); %%% as we sorted the array before, the better is the one with minimum randnum
                    end*/

            best_size = 0;
            for(int i=0; i<Configuration.popSize*3; i++){
                int TcSize = rand.getInt(2,3);
                int[] randnum = new int[TcSize];
                for(int tc=0; tc<TcSize; tc++){
                    randnum[tc] = rand.getInt(0, Configuration.popSize);
                }
                best[i] = min(randnum, TcSize); //TODO implementar funcion min, obtiene el que tenga mejor fit de los 2 o 3 individuos
                best_size++;
            }

            //%%%% Crossover Operator
            //for i=1:3:PopSize
            for(int i=0; i<Configuration.popSize; i+=3){ //TODO comprobar que el bucle este bien construido
                /*
                if I_fno > 16 && I_fno <= 20
                beta = normrnd(0.5,0.3);  %%% generate beta = Gaussian number, with mean=0.5 and standard deviation=0.3.%%%%
                else
                beta = normrnd(0.7,0.1);  %%% generate beta = Gaussian number, with mean=0.7 and standard deviation=0.1.%%%%
                end
                */
                beta = (float) rand.gaussian(0.5f, 0.3f); //TODO porque se aplican diferentes valores para diferentes funciones?
                /*
                consecutive(1) = best(i);
                consecutive(2) = best(i+1);
                consecutive(3) = best(i+2);
                 */
                consecutive[0] = best[0];
                consecutive[1] = best[1];
                consecutive[2] = best[2];

                /*
                %%%% sort the selected three parents
                consecutive= sort(consecutive);
                 */
                consecutive = sort(consecutive); //TODO funcion sort para ordenar. Mayor a menor? o menor a mayor?

                /*
                %%% Check the similarity between all selected individuals
                if (consecutive(1)== consecutive(2))

                    while (consecutive(2)== consecutive(1)) ||(consecutive(2)== consecutive(3))
                        consecutive(2)=randi(PopSize);
                    end
                    %%%% sort the selected three parents
                    consecutive= sort(consecutive);
                end

                if (consecutive(1)== consecutive(3))

                    while (consecutive(3)== consecutive(1)) ||(consecutive(3)== consecutive(2))
                        consecutive(3)= randi(PopSize);%
                    end
                    %%%% sort the selected three parents
                    consecutive= sort(consecutive);
                end

                if (consecutive(2)== consecutive(3))

                    while (consecutive(3)== consecutive(1)) ||(consecutive(3)== consecutive(2))
                        consecutive(3)= randi(PopSize);
                    end
                    %%%% sort the selected three parents
                    consecutive= sort(consecutive);
                end
                 */

                if(consecutive[0] == consecutive[1]){
                    while ((consecutive[1] == consecutive[0]) || (consecutive[1] == consecutive[2])){
                        consecutive[1] = rand.getInt(0, Configuration.popSize);
                    }
                    consecutive = sort(consecutive); //TODO funcion sort para ordenar. Mayor a menor? o menor a mayor?
                }

                if(consecutive[0] == consecutive[2]){
                    while ((consecutive[2] == consecutive[0]) || (consecutive[2] == consecutive[1])){
                        consecutive[2] = rand.getInt(0, Configuration.popSize);
                    }
                    consecutive = sort(consecutive); //TODO funcion sort para ordenar. Mayor a menor? o menor a mayor?
                }

                if(consecutive[1] == consecutive[2]){
                    while ((consecutive[2] == consecutive[0]) || (consecutive[2] == consecutive[1])){
                        consecutive[2] = rand.getInt(0, Configuration.popSize);
                    }
                    consecutive = sort(consecutive); //TODO funcion sort para ordenar. Mayor a menor? o menor a mayor?
                }

                /*
                if (rand<1)
                    for j=1: n
                        offspring_individuals(i,j) = x(consecutive(1), j) + beta * (x(consecutive(2), j) - x(consecutive(3), j));
                        offspring_individuals(i+1,j) = x(consecutive(2), j) + beta * (x(consecutive(3), j) - x(consecutive(1), j));
                        offspring_individuals(i+2,j) = x(consecutive(3), j) + beta * (x(consecutive(1), j) - x(consecutive(2), j));
                    end
                end
                 */
                if(rand.getFloat() < 1){ //TODO comprobar que sea bool y no float, no estoy seguro
                    for(int j=0; j<Configuration.n; j++){
                        offspring_individuals[i][j] = x[consecutive[0]][j] + beta * (x[consecutive[1]][j] - x[consecutive[2]][j]);
                        offspring_individuals[i+1][j] = x[consecutive[1]][j] + beta * (x[consecutive[2]][j] - x[consecutive[0]][j]);
                        offspring_individuals[i+2][j] = x[consecutive[2]][j] + beta * (x[consecutive[0]][j] - x[consecutive[1]][j]);
                    }//TODO inicializar offspring_indivifuals y x
                }

            }
            /*
             for i=1:PopSize
            [offspring_individuals n xmax xmin] = han_boun (offspring_individuals, n, xmax, xmin, I_fno,i);
            end
             */
            for(int i=0; i<Configuration.popSize; i++){
                offspring_individuals =  han_boun(offspring_individuals, n, xmax, xmin, I_fno, i); //TODO implementar funcion
            }

            /*
            %%%%%%%%%%%%% Randomized Operator
            arch_size = size(archive);
            for i=1:PopSize
                for j=1: n
                    p=0.1;%%% the used randomized operator probablity

                    %%%%%%%%%%%% In a latter study: to get better results for  SEPERABLE
                    %%%%%%%%%%%% FUNCTIONS IT IS RECOMMENDED TO USE THE FOLLOWING VALUES
                    %%%%%%%%%%%%
                    %% if mod(i,3)==1
                    %%  p=0.01;
                    %% else
                    %%   p=0.1;
                    %%end


                    if (rand<p)
                        pos= randi(arch_size(1));%%%% select an individual from the archive pool
                        offspring_individuals(i,j)=  archive(pos,j); %%% exchange variables
                    end
                end
            end
            %%%%%%%%%%%%% End ... the Randomized Operator
             */
            for(int i=0; i<Configuration.popSize; i++){
                for(int j=0; j<Configuration.n; j++){
                    if(rand.getFloat() < Configuration.p){
                        int pos; // %%%% select an individual from the archive pool
                        pos = rand.getInt(0, arch_size[1]);  //TODO arch_size es matriz????
                        offspring_individuals[i][j] = archive[pos][j];
                    }
                }
            }
            /*
                %%%%%%%%% Group both elite and x

                for i=1:arch_size(1)+PopSize
                    if i<=arch_size(1)
                        all_individuals (i,:) = archive(i,:);
                    else
                        current = i-arch_size(1);
                        all_individuals (i,:)= offspring_individuals(current,:);
                    end
                end
             */

            for (int i=0; i<arch_size[1] + Configuration.popSize; i++){
                if(i <= arch_size[1]){
                    //TODO copiar contenido de archive en all_individuals
                }else{
                    int current = i - arch_size[1];
                    //TODO copiar contenido de offsping_individuals en all_indiviuals
                }
            }

            /*
            %%%%% Calculated the fitness values for the neww offspring
        for i=1:arch_size(1)+PopSize
            if i <= arch_size(1)
                fitx_all(i)= fitx(i);
            else

                if I_fno <9
                    fitx_all(i)=bench_func(all_individuals(i,:),I_fno);
                end
                if I_fno == 9
                    [fitx_all(i) penalty(i) rate_d(i)]= cost_fn(all_individuals(i,:));
                end
                if I_fno == 10
                    [y sllreturn bwfn]= antennafunccircular(all_individuals(i,:), null, phi_desired, distance);
                    fitx_all(i)=y;
                end
                if I_fno==11
                    [fitx_all(i) Count]= MY_FUNCTION_10_1(all_individuals(i,:));
                end
                if I_fno==12
                    [fitx_all(i) Count]= MY_FUNCTION_10_2(all_individuals(i,:));
                end
                if I_fno==13
                    [fitx_all(i) Count ]= MY_FUNCTION_11_1(all_individuals(i,:));
                end
                if I_fno==14
                    [fitx_all(i) Count]= MY_FUNCTION_11_2(all_individuals(i,:));
                end
                if I_fno==15
                    [fitx_all(i) Count]= MY_FUNCTION_11_3(all_individuals(i,:));
                end
                if I_fno==16
                    [fitx_all(i) Count]= MY_FUNCTION_11_4(all_individuals(i,:));
                end
                if I_fno==17
                    [fitx_all(i) Count ]= MY_FUNCTION_11_5(all_individuals(i,:));
                end
                if I_fno==18
                    [fitx_all(i) Count]= MY_FUNCTION_12_1(all_individuals(i,:));
                end
                if I_fno==19
                    [fitx_all(i) Count]= MY_FUNCTION_12_2(all_individuals(i,:));
                end
                if I_fno==20
                    [fitx_all(i) Count ]= MY_FUNCTION_12_3(all_individuals(i,:));
                end
                if I_fno == 21
                    fitx_all(i) =messengerfull(all_individuals(i,:),a.MGADSMproblem);
                end
                if I_fno == 22
                    fitx_all(i) =cassini2(all_individuals(i,:),a.MGADSMproblem);
                end
            end

            %%%% ONLY For problems Tersoff Potential Si(b) and  Si(c), the fitness value
            %%%% may be equal to NAN, so it is converted to zero
            if isnan(fitx_all(i)) ==1
                fitx_all(i)=0;
            end
        end
             */
            for(int i=0; i<arch_size[0] + Configuration.popSize; ++i){
                if(i <= arch_size[i]){
                    fitx_all[i] = x_all[i];
                }else{
                    fitx_all[i] = aTestFunc.f(all_individuals);
                }

                //TODO ultima comprobacion si es igual a NAN
            }


        }

    }

    private int min(int [] array, int tam){
        int num = Utility.infinity;


        for(int i=0; i<tam; i++){
            if(array[i] < num){
                num = array[i];
            }
        }


        return num;
    }


}
