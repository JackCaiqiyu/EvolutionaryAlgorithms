import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by framg on 09/04/2016.
 */
public class LSHADEND {
    Archive archive;
    int iter;
    int memory_size;
    double [] memory_sf, memory_cr;
    int memory_pos;
    double [][] pop;
    double [] fitness;
    double p_best_rate;
    int nfes = 0;
    int min_memory_size;
    int m2;
    int min_pop_size, max_pop_size;
    double arc_rate;
    List <Double> result;
    double PND;
    int ND_fes;
    int loc;
    int pop_size;
    double best_value;
    double Ter_Err = 10e-8;

    public LSHADEND(){
        result = new ArrayList<>();
        ND_fes= (2 * Configuration.max_nfes)/3;
        m2 = 100;
        iter = 0;
        p_best_rate = 0.11;
        arc_rate = 1.4;
        memory_size = 100;
        PND = 1;
        min_memory_size = 5;
        pop_size = 18*Configuration.D;
        max_pop_size = pop_size;
        min_pop_size = 4;
        pop = new double[pop_size][Configuration.D];
        for(int i=0; i<pop_size; i++) {
            for (int j = 0; j < Configuration.D; j++) {
                pop[i][j]= Configuration.benchmark.lbound() + (Configuration.benchmark.ubound() - Configuration.benchmark.lbound()) * Configuration.rand.getDouble();
            }
        }
        fitness = new double[pop_size];
        for(int i=0; i<pop_size; i++){
            fitness[i] = Configuration.benchmark.f(pop[i]);
        }
     //   assert (Configuration.com.benchmark.cec.cec05.benchmark.f(pop[0]) != fitness[0]);
        double [] valBest =  Util.sortNewArray(fitness);
        best_value = Math.abs(Configuration.benchmark.bias() - valBest[0]);
        nfes = pop_size;

        Configuration.records.newRecord(best_value, nfes);

        int [] indexBest = Util.sortOnlyIndexs(fitness);
        loc = indexBest[0];

        memory_sf = new double[memory_size];
        Util.assignArray(memory_sf, 0.5);
        memory_cr = new double[memory_size];
        Util.assignArray(memory_cr, 0.5);
        memory_pos = 0;
        archive = new Archive();
        archive.NP = (int)Math.ceil(arc_rate * pop_size);
        archive.pop = new double[0][Configuration.D];
        archive.funvalues = new double[0];
    }


    public void execute(){

        int n = Configuration.D;
        double [] mu_sf, mu_cr;

        int [] sorted_index;
        double [] cr ;
        int [] mem_rand_index ;
        double [][] popold;
        popold = Util.copyMatrix(pop);
        mu_cr = new double[pop_size];
        mu_sf = new double[pop_size];
        //while ( Configuration.max_nfes > nfes){
        while (best_value > Ter_Err && Configuration.max_nfes > nfes){

            if(Configuration.rand.getDouble() < PND && nfes <ND_fes ){
                popold = ND(loc, popold, fitness);
                pop = Util.copyMatrix(popold);
                sorted_index = Util.sortOnlyIndexs(fitness);
                //temp_fit = Util.sortNewArray(fitness);
            }else{

                pop = Util.copyMatrix(popold);
                sorted_index = Util.sortOnlyIndexs(fitness);
                mem_rand_index = new int[pop_size];
                for(int i=0; i<pop_size; i++) {
                    mem_rand_index[i] = (int)Math.ceil((memory_size-1) * Configuration.rand.getDouble());
                }
               // mu_cr = new double[pop_size];
               // mu_sf = new double[pop_size];

                for(int i=0; i<pop_size; i++){
                    mu_sf[i] = memory_sf[mem_rand_index[i]];
                    mu_cr[i] = memory_cr[mem_rand_index[i]];
                }
                cr = new double[mu_cr.length];
                if(Configuration.rand.getDouble() < 0.8){
                    for(int i=0; i<cr.length; i++){
                        cr[i] = Configuration.rand.gaussian(mu_cr[i], 0.1);

                        if(cr[i] > 1){
                            cr[i] = 1;
                        }
                        if(cr[i] < 0){
                            cr[i] = 0;
                        }
                    }

                }else{
                    for(int i=0; i<cr.length; i++) {
                        do {
                            cr[i] = mu_cr[i] + (0.1 * Math.tan(Math.PI * (Configuration.rand.getDouble() - 0.5)));
                        }while (cr[i] <= 0);
                        if(cr[i] > 1){
                            cr[i] = 1;
                        }
                    }

                }

                double [] sf = new double[pop_size];
                for(int i=0; i<pop_size; i++){
                    do {
                        sf[i] = mu_sf[i] + (0.1 * (Math.tan(Math.PI *(Configuration.rand.getDouble() - 0.5))));
                    }while(sf[i] <= 0);

                    if(sf[i] > 1){
                        sf[i] = 1;
                    }
                }


                int [] r0 = new int[pop_size];
                for(int i=0; i<pop_size; i++){
                    r0[i] = i;
                }

                double [][] popAll = Util.add(pop, archive.pop);
                int[][] r = gnR1R2(pop_size, popAll.length, r0);


                int pNP = (int)Math.max(Math.round(p_best_rate * pop_size), 2);

                int [] randindex = new int[pop_size];
                for(int i=0; i<pop_size; i++) {
                    randindex[i] = (int)Math.ceil(Configuration.rand.getDouble() * pNP) -1;
                    if(randindex[i] < 0)
                        randindex[i] = 0;
                }

                double [][] pbest = new double[randindex.length][];
                for(int i=0; i<randindex.length; i++){
                    pbest[i] =Util.copyArray( pop[sorted_index[randindex[i]]]);
                }

                double [][] popR1 = Util.sortByIndexs(pop, r[0]);
                double [][] popAllR2 = Util.sortByIndexs(popAll, r[1]);
                double [][] vipart1 = Util.addMatrix(Util.subMatrix(pbest, pop), Util.subMatrix(popR1, popAllR2));
                double [][] vipart2 = Util.mulMatrix(vipart1, Util.duplicateArray(sf, n));
                double [][] vi = Util.addMatrix(pop, vipart2);
                vi = boundConstraint(vi, pop);

                boolean [][] mask = new boolean[pop_size][n];
                for(int i=0; i<pop_size; i++){
                    for(int j=0; j<n; j++){
                        mask[i][j] = Configuration.rand.getDouble() > cr[i];
                    }
                }
                int [] rows = new int[pop_size];
                int [] cols = new int[pop_size];

                for(int i=0; i<pop_size; i++){
                    rows[i] = i;
                    cols[i] = (int)Math.floor(Configuration.rand.getDouble() * (n-1));
                }

                setMaskJrand(rows, cols, mask);

                double [][] ui = Util.copyMatrix(vi);
                for(int i=0; i<pop_size; i++){
                    for(int j=0; j<n; j++){
                        if(mask[i][j] == true){
                            ui[i][j] = pop[i][j];
                        }
                    }
                }

                double [] children_fitness = new double[pop_size];
                for(int i=0; i<pop_size; i++){
                    children_fitness[i] = Configuration.benchmark.f(ui[i]);
                }

                nfes += pop_size;

                double [] dif = new double[pop_size];
                for(int i=0; i < dif.length; i++){
                    dif[i] = Math.abs(fitness[i] - children_fitness[i]);
                }

                int sizeI = 0;
                for(int i=0; i<pop_size; i++) {
                    if (fitness[i] > children_fitness[i]) {
                        sizeI++;
                    }
                }

                double [] goodCR = new double[sizeI];
                double [] goodF = new double[sizeI];
                double [] dif_val = new double[sizeI];
                double [] archive_fitness = new double[sizeI];
                double [][] archive_pop = new double[sizeI][n];
                int index = 0;

                for(int i=0; i<pop_size; i++){
                    if(fitness[i] > children_fitness[i]){
                        goodCR[index] = cr[i];
                        goodF[index] = sf[i];
                        dif_val[index] = dif[i];
                        archive_fitness[index] =  fitness[i];
                        archive_pop[index] = Util.copyArray(popold[i]);
                        index++;
                    }
                }
                updateArchive(archive_pop, archive_fitness);

                int [] I = new int[pop_size];
                for(int i=0; i<pop_size; i++){
                    if(fitness[i] > children_fitness[i]){
                        fitness[i] = children_fitness[i];
                        I[i] = 2;
                    }else{
                        I[i] = 1;
                    }

                }
                popold = Util.copyMatrix(pop);
                for(int i=0; i<pop_size; i++){
                    if(I[i] == 2){
                        popold[i] = Util.copyArray(ui[i]);
                    }
                }
                int num_success_params = sizeI;
                if (num_success_params > 0){
                    double sum_dif = Util.summatory(dif_val);
                    dif_val = Util.divides(dif_val, sum_dif);

                    memory_sf[memory_pos] = Util.multiplies_1xn_nx1(dif_val, Util.power2(goodF)) / Util.multiplies_1xn_nx1(dif_val, goodF);
                    if (Util.max(goodCR) == 0 || memory_cr[memory_pos] == -1){
                        memory_cr[memory_pos] = -1;
                    }else{
                        memory_cr[memory_pos] = Util.multiplies_1xn_nx1(dif_val, Util.power2(goodCR)) / Util.multiplies_1xn_nx1(dif_val, goodCR);
                    }
                    memory_pos++;
                    if(memory_pos >= memory_size){
                        memory_pos = 0;
                    }
                }

                int plan_pop_size2 = (int)Math.round((((new BigDecimal(min_memory_size - 100)).divide(new BigDecimal(Configuration.max_nfes)).doubleValue()) * nfes) + 100);
                if (m2 > plan_pop_size2){
                    int reduction_ind_num2 = m2 - plan_pop_size2;
                    if(m2 - reduction_ind_num2 < min_memory_size){
                        reduction_ind_num2 = m2 - min_memory_size;
                    }
                    m2 = m2-reduction_ind_num2;
                    memory_size=m2;


                }

                int plan_pop_size = (int)Math.ceil(((new BigDecimal(min_pop_size - max_pop_size).divide(new BigDecimal(Configuration.max_nfes)).doubleValue()) * nfes) + max_pop_size);
                if(pop_size > plan_pop_size){
                    int reduction_ind_num = pop_size - plan_pop_size;
                    if (pop_size - reduction_ind_num < min_pop_size){
                        reduction_ind_num = pop_size - min_pop_size;
                    }
                    pop_size = pop_size - reduction_ind_num;



                    for(int i=0; i<reduction_ind_num; i++){
                        int [] indBest = Util.sortOnlyIndexs(fitness);
                        int worst_ind = indBest[indBest.length-1];
                        popold = Util.eraseMember(popold, worst_ind);
                        pop = Util.eraseMember(pop, worst_ind);
                        fitness = Util.eraseMember(fitness, worst_ind);
                    }
                    archive.NP = (int)Math.round(arc_rate * pop_size);
                    if(archive.pop.length > archive.NP){
                        int [] randpos = Configuration.rand.randperm(archive.pop.length);
                        randpos = Util.cutArray(randpos, archive.NP);
                        archive.pop = Util.sortByIndexs(archive.pop, randpos);
                    }

                    double [][] poptmp = Util.copyMatrix(pop);
                    pop = new double[pop_size][Configuration.D];
                    for(int i=0; i<pop_size; i++) {
                        pop[i]= Util.copyArray(poptmp[i]);
                    }
                    double []fitnessTmp = Util.copyArray(fitness);
                    fitness = new double[pop_size];
                    for(int i=0; i<pop_size; i++){
                        fitness[i] = fitnessTmp[i];
                    }

                }

            }
            iter++;
            int [] indexBest = Util.sortOnlyIndexs(fitness);
            double [] valBest =  Util.sortNewArray(fitness);
            loc = indexBest[0];
            best_value = Math.abs(Configuration.benchmark.bias() - valBest[0]);
      //      System.out.println("value " + best_value + " nfes: " +  nfes);

            Configuration.records.newRecord(best_value, nfes);

            result.add(best_value);



        }
        /*for(int i=0; i<10; i++){

            System.out.println("x" + i + ": " + pop[loc][i]);
        }*/
        Configuration.records.endRun(best_value, nfes, Configuration.max_nfes);
        System.out.println("value " + best_value + " nfes: " +  nfes);
      //  System.out.println("value " + Configuration.com.benchmark.cec.cec05.benchmark.f(pop[loc]));
    }

    private int [][] gnR1R2(int NP1, int NP2, int [] r0){
        int [][] r = new int[2][r0.length];
        int NP0 = r0.length;
        int [] r1 = new int[NP0];
        int [] r2 = new int[NP0];

        for(int i=0; i<NP0; i++){
            r1[i] = (int)Math.floor(Configuration.rand.getDouble() * (NP1-1));
        }

        for (int i=0; i<99999999; i++){
            List<Integer> pos = new ArrayList<>();
            int sum = 0;
            for(int j=0; j<NP0; j++){
                if(r1[j] == r0[j]){
                    sum++;
                    pos.add(j);
                }
            }
            if(sum == 0){
                break;
            }else{
                for(Integer j : pos){
                    r1[j] = (int)Math.floor(Configuration.rand.getDouble() * (NP1-1));
                }
            }
            if( i > 1000){
                System.exit(0);
            }

        }

        for(int i=0; i<NP0; i++){
            r2[i] = (int)Math.floor(Configuration.rand.getDouble() * (NP2-1));
        }

        for (int i=0; i<99999999; i++){
            List<Integer> pos = new ArrayList<>();
            int sum = 0;
            for(int j=0; j<NP0; j++){
                if((r2[j] == r0[j])||(r2[j] == r1[j])){
                    sum++;
                    pos.add(j);
                }
            }
            if(sum == 0){
                break;
            }else{

                for(Integer j : pos){
                    r2[j] = (int)Math.floor(Configuration.rand.getDouble() * (NP2-1));
                }
            }
            if( i > 1000){
                System.exit(0);
            }

        }

        r[0] = Util.copyArray(r1);
        r[1] = Util.copyArray(r2);
        return r;
    }

    private double [][] boundConstraint(double [][] vi, double[][] pop){
        for(int i=0; i<pop_size; i++){
            for(int j=0; j<Configuration.D; j++){
                if(Configuration.benchmark.lbound() > vi[i][j]){
                    vi[i][j] = (pop[i][j] + Configuration.benchmark.lbound())/2;
                }
                if(Configuration.benchmark.ubound() < vi[i][j]){
                    vi[i][j] = (pop[i][j] + Configuration.benchmark.ubound()) /2;
                }
            }
        }
        return vi;
    }


    private void setMaskJrand(int [] rows, int [] cols, boolean[][] mask){
        for(int i=0; i<rows.length; i++){
            mask[rows[i]][cols[i]] = false;
        }
    }

    private void updateArchive(double [][] pop_fun, double [] fitness_fun){
        //Archive archive_new = new Archive();
        if(archive.NP != 0) {
            double[][] popAll = Util.add(archive.pop, pop_fun);
            double[] funvalues = Util.add(archive.funvalues, fitness_fun);
            List<Integer> indexs = findUnique(popAll);
            if (indexs.size() < popAll.length) {
                double [][]popAlltmp = new double[indexs.size()][];
                double [] funvaluestmp = new double[indexs.size()];
                int i = 0;
                for(Integer index: indexs){
                    popAlltmp[i] = Util.copyArray(popAll[index]);
                    funvaluestmp[i] = funvalues[index];
                    i++;
                }
                popAll = Util.copyMatrix(popAlltmp);
                funvalues = Util.copyArray(funvaluestmp);

            }

            if (popAll.length <= archive.NP) {
                archive.pop = Util.copyMatrix(popAll);
                archive.funvalues = Util.copyArray(funvalues);
            } else {
                int[] randpos = Configuration.rand.randperm(popAll.length);
                randpos = Util.cutArray(randpos, archive.NP);
                //archive.pop = Util.copyMatrix(popAll);
                //archive.funvalues = Util.copyArray(funvalues);
                archive.pop = Util.sortByIndexs(popAll, randpos);
                archive.funvalues = Util.sortByIndexs(funvalues, randpos);
            }
        }
    }


    private List<Integer> findUnique(double [][] poptmp){
        List<Integer> listIndexs = new ArrayList<>();
        List<Integer> uniques = new ArrayList<>();
        boolean found;

        for(int i=poptmp.length-1; i>=0; i--){
            found = false;
            for(int j=i-1; j>=0; j--){
                if(equals(poptmp[i], poptmp[j])){
                    //listIndexs.add(i);
                    found = true;
                    break;
                }
            }
            if(!found) {
                uniques.add(i);
            }
        }



       /* double [] array = null;
        for(int i=0; i<poptmp.length; i++){
            found = false;
            for(int j=i+1; j<poptmp.length; j++){
                if(equals(poptmp[i], poptmp[j])){
                   found = true;
                    array = Util.copyArray(poptmp[j]);
                    break;
                }
            }

            if(found){
                boolean notInclude = false;
                for(Integer j: listIndexs){
                    if(equals(poptmp[j], array)){
                       notInclude = true;
                        break;
                    }
                }
                if(!notInclude) {
                    listIndexs.add(i);
                }
            }
        }*/
        return  uniques;
    }

    private boolean equals(double[] a, double []b){
        int n = 0;
        for(int i=0; i<a.length; i++){
            if(a[i] == b[i]){
                n++;
            }
        }
        if(n == a.length){
            return  true;
        }else{
            return false;
        }
    }


    private double [][] ND(int loc, double [][] popold, double [] valParents ){
        double [][] z = new double[20 + 1][];
        double [][] popold1 = Util.copyMatrix(popold);
        double [] mode = Util.copyArray(popold[loc]);
        double [] mode_org = Util.copyArray(popold[loc]);
        double f_loc = valParents[loc];
        double mu = Math.exp(-1);

        for(int i=0; i<20; i++){
            z[i] = Util.addition(Util.multiplyArray(mode, mu), Util.multiplyArray(griewankd2(mode, f_loc), 1-mu));
            mode = Util.copyArray(z[i]);
        }

        z[20] = Util.copyArray(mode_org);
        double [] fod = new double[21];
        for(int i=0; i<z.length; i++) {
            fod[i] = Configuration.benchmark.f(z[i]);
        }
        nfes += 21;
        int r = Util.minOnlyIndex(fod);

        if(iter < 2){
            PND = 1;
        }else{
            if (r == 20){
                PND = 0.01;
            }else{
                double diff = result.get(iter-2) - result.get(iter -1);
                PND = Math.max(0.01, diff/(result.get(iter-2)));
            }
        }
        popold[loc] = Util.copyArray(z[r]);
        popold = boundConstraint(popold, popold1);
        valParents[loc] = Configuration.benchmark.f(popold[loc]);
        nfes++;

        int [] sort_indexs = Util.sort(valParents);
        popold = Util.sortByIndexs(popold, sort_indexs);


        /*for(int i=0; i<popold.length; i++){
            double [] tmp = Util.copyArray(popold[i]);
            popold[i] = Util.copyArray(popold[sort_indexs[i]]);
            popold[sort_indexs[i]] = Util.copyArray(tmp);
        }*/

        sort_indexs = Util.sort(fod);
        z = Util.sortByIndexs(z, sort_indexs);
        //for(int i=0; i<z.length; i++){
           /* double [] tmp = Util.copyArray(z[i]);
            z[i] = Util.copyArray(z[sort_indexs[i]]);
            z[sort_indexs[i]] = Util.copyArray(tmp);*/
        //}

        for(int i=0; i<5; i++){
            if(valParents[i] > fod[i]){
                valParents[i] = fod[i];
                popold[i] = Util.copyArray(z[i]);
            }
        }

        return popold;

    }

    private double [] griewankd2(double[] y, double f){
        double [] grd = new double[Configuration.D];
        double [] x = plf(y);
        double delta = 1e-2;
        double xmin = Configuration.benchmark.lbound();
        double xmax = Configuration.benchmark.ubound();

        for(int i=0; i<Configuration.D; i++){
            double [] x_dum = Util.copyArray(x);
            x_dum[i] = x_dum[i] + delta;
            double f_dum = Configuration.benchmark.f(x_dum);
            nfes++;
            grd[i] = (f_dum - f) / delta;
        }

        double [] gy = Util.copyArray(grd);
        for(int i=0; i<Configuration.D; i++){
            if(gy[i]>xmax || gy[i]<xmin){
                gy[i] = (gy[i]/Util.summatory(Util.abs(gy))) * xmax;
            }
        }

        return plf(Util.subtraction(x, gy));


    }

    private double [] plf(double [] x){
        double [] lumta = new double[Configuration.D];
        double lmin = Configuration.benchmark.lbound();
        double lmax = Configuration.benchmark.ubound();

        for(int i=0; i<Configuration.D; i++){
            if(x[i] < lmin){
                lumta[i] = lmin;
            }else if (x[i] > lmax){
                lumta[i] = lmax;
            }else{
                lumta[i] = x[i];
            }
        }
        return  lumta;
    }




}
