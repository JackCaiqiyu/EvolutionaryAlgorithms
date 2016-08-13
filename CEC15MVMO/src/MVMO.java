import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by framg on 17/03/2016.
 */


public class MVMO {
    Xt xt;
    Ps ps;
    static Proc proc;
    Paramater parameter;
    Table table;

    int r_select;
    double ff;
    double ff2;
    double vvqq;
    double delta_nrandomly;
    double ratio_gute_max;
    double ratio_gute_min;
    double border_gute0;
    int border_gute;
    int n_par;
    int n_randomly_ini;
    int n_randomly_last;
    boolean yes_n_randomly;
    boolean yes_fs_factor;
    int ipx;
    int n_randomly_X;
    int n_randomly;
    int n_eval;
    int ipp;
    double fs_factor0;
    double fs_factor_start;
    double fs_factor_end;
    int local_i;
    int local_max;
    double local_search0;
    int local_search_called;
    int indepent_runs;
    boolean changed_best;
    boolean firsttime;
    double amax;
    double bbb;
    double beta1;
    double beta10;
    double alpha;
    double ccc;
    int mappingST;
    int D;
    int max_eval_LS;
    int min_eval_LS;
    int n_to_save;
    int verybest;
    double shape_dyn_ini;
    double shape_ini;
    double value_ini;


    double [] A;
    int [] IX;
    boolean [][] considered;
    boolean [] local_search;
    double [][] x_normalized;
    double [][] meann_app;
    double [][]meann;
    boolean [] goodbad;
    double [][] shape;
    double [][] Shape_dyn;
    int [] izm;
    double [] values_noeq;
    //int [] izz;
    int [] no_in;
    int [] no_inin;

    double local_search0_percentage;
    double delta_Shape_dyn;
    double delta_Shape_dyn0;
    double delta_Shape_dyn1;
  //  double ffx;
 //   double oox;



    public void initialize() {
        alpha = 1;
        parameter.scaling = ps.x_max - ps.x_min;
        indepent_runs = parameter.n_par * 2;

        double[][] xx = new double[parameter.n_par][ps.D];
        double[][] x_norm = new double[parameter.n_par][ps.D];

        for (int iijj = 0; iijj < parameter.n_par; iijj++) {
            for (int jjkk = 0; jjkk < ps.D; jjkk++) {
                xx[iijj][jjkk] = ps.x_min + Configuration.rand.getDouble() * (ps.x_max - ps.x_min);
            }

            for (int i = 0; i < ps.D; i++) {
                x_norm[iijj][i] = (xx[iijj][i] - ps.x_min) / parameter.scaling;
            }
        }

        x_normalized = Util.copyMatrix(x_norm);

        n_eval = proc.n_eval;
        n_par = parameter.n_par;
        D = ps.D;
        n_to_save = parameter.n_tosave;
        fs_factor_start = parameter.fs_factor_start;
        fs_factor_end = parameter.fs_factor_end;
        Shape_dyn = new double[n_par][D];
        IX = new int[n_par];
        shape = new double[n_par][D];
        izm = new int[n_par];

        for (int i = 0; i < n_par; i++) {
            IX[i] = i;
            izm[i] = (int)Math.round(Configuration.rand.getDouble() * (ps.D - 1));
            for (int k = 0; k < ps.D; k++) {
                Shape_dyn[i][k] = shape_dyn_ini;
                shape[i][k] = shape_ini;
            }
        }

        delta_Shape_dyn = parameter.delta_Shape_dyn;
        local_search0_percentage = parameter.local_prob;
        ratio_gute_min = parameter.ratio_gute_min;
        ratio_gute_max = parameter.ratio_gute_max;
        n_randomly_ini = parameter.n_random_ini;
        n_randomly_last = parameter.n_random_last;
        local_i = 0;


        table.bests = new double[parameter.n_tosave][ps.D][n_par];
        table.fitness = new double[parameter.n_tosave][n_par];
        table.objective = new double[parameter.n_tosave][n_par];
        table.feasibility = new boolean[parameter.n_tosave][n_par];


        local_search = new boolean[n_par];
        for (int i = 0; i < local_search.length; i++)
            local_search[i] = false;

        meann = Util.copyMatrix(x_normalized);
        if (value_ini == 2) {
            for (int i = 0; i < meann.length; i++)
                for (int j = 0; j < meann[i].length; j++)
                    meann[i][j] = Configuration.rand.getDouble();
        } else {
            Util.assignMatrix(meann, value_ini);
        }
        meann_app = Util.copyMatrix(meann);


        // izz = new int[n_par];
        //Util.assignArray(izz, 0);
        considered = new boolean[n_par][ps.D];
        for (int i = 0; i < n_par; i++)
            for (int j = 0; j <ps.D; j++)
                considered[i][j] = true;

        values_noeq = new double[n_to_save];
        Util.assignArray(values_noeq, 0);

        if (n_randomly_last < 1) {
            n_randomly_last = 1;
        }

        if (n_randomly_last > ps.D) {
            n_randomly_last = ps.D;
        }

        if (n_randomly_ini > ps.D) {
            n_randomly_ini = ps.D;
        }

        if (n_eval <= 0) {
            n_eval = 10000 * D;
        }

        if (fs_factor_start <= 0) {
            fs_factor_start = 1;
        }

        if (fs_factor_end <= 0) {
            fs_factor_end = 1;
        }


        fs_factor0 = fs_factor_start;

        yes_n_randomly = true;
        if (n_randomly_ini == n_randomly_last) {
            n_randomly = n_randomly_last;
            yes_n_randomly = false;
        }

        if (n_to_save <= 1) {
            n_to_save = 2;
        }

        if (delta_Shape_dyn <= 0) {
            delta_Shape_dyn = 0.20;
        }

        delta_Shape_dyn0 = delta_Shape_dyn;
        delta_Shape_dyn1 = 1.0 + delta_Shape_dyn0;

        yes_fs_factor = true;
        if (fs_factor_start == fs_factor_end){
            yes_fs_factor = false;
        }


        no_in = new int[n_par];
        Util.assignArray(no_in, 0);

        no_inin = new int[n_par];
        Util.assignArray(no_inin, 0);
        amax=0.0;

        local_search0=local_search0_percentage/100;
        goodbad = new boolean[n_par];
        for(int i=0; i<goodbad.length; i++){
            goodbad[i] = false;
        }
        firsttime=true;


        delta_nrandomly=n_randomly_ini-n_randomly_last;
        A = new double[n_par];
        Util.assignArray(A, 0);
        local_search_called=0;

    }


    public MVMO(){
        parameter = new Paramater();
        xt = new Xt();
        ps = new Ps();
        proc = new Proc();
        table = new Table();

        ps.x_min = Configuration.benchmark.lbound();
        ps.x_max = Configuration.benchmark.ubound();
        ps.D = Configuration.DIM;

        proc.n_eval = Configuration.max_fes;


        parameter.n_par= Configuration.n_par;
        parameter.n_tosave= Configuration.n_tosave;
        parameter.fs_factor_start= (int)Configuration.fs_factor_start;
        parameter.fs_factor_end= Configuration.fs_factor_end;
        parameter.delta_Shape_dyn= Configuration.delta_Shape_dyn;
        parameter.local_prob= Configuration.local_prob;
        min_eval_LS = (int)Math.round(Configuration.min_eval_LS);
        max_eval_LS = (int)Math.round(Configuration.max_eval_LS);
        parameter.ratio_gute_max=Configuration.ratio_gute_max;
        parameter.ratio_gute_min=Configuration.ratio_gute_min;
        parameter.n_random_ini = Configuration.n_random_ini;
        parameter.n_random_last= Configuration.n_random_last;
        local_max= Configuration.local_max;
        shape_ini=Configuration.shape_ini;
        shape_dyn_ini=Configuration.shape_dyn_ini;
        value_ini=Configuration.value_ini;
        r_select=Configuration.r_select;
        mappingST=Configuration.mappingST;


        initialize();
    }

    public double execute(){
        while(true){
            ff = BigDecimal.valueOf(proc.i_eval).divide(BigDecimal.valueOf(n_eval), 64, RoundingMode.HALF_UP).doubleValue(); //proc.i_eval / n_eval;
            ff2 = ff *ff;
            vvqq=Math.pow(10.0 , -(13.3+ff*35.0));

            border_gute0 = ratio_gute_max - ff *(ratio_gute_max - ratio_gute_min);
            border_gute = (int)Math.round (n_par * border_gute0);
            if(border_gute < 3 || n_par - border_gute < 1){
                border_gute = n_par;
            }

            if(yes_n_randomly){
                n_randomly_X = (int)Math.round(n_randomly_ini - ff * delta_nrandomly);
                n_randomly =(int) Math.round(n_randomly_last+Configuration.rand.getDouble()*(n_randomly_X-n_randomly_last));
            }

            if(yes_fs_factor){
                fs_factor0=fs_factor_start+ff2*(fs_factor_end-fs_factor_start);
            }
            ipx = 0;
            while(ipx < n_par){
//                if(proc.i_eval == 225){
//                    System.out.print("DEBUG");
//                }


                ipp = IX[ipx];
              // double [] xxxx = {0.8977    ,0.4658   , 0.5636  ,  0.8131 ,  0.6043   , 0.6939  ,  0.8927   , 0.9000    ,0.9000   , 0.9000};
                ipx++;

                for(int i=0; i<ps.D; i++){
                    considered[ipp][i] = false;
                }

                x_normalized[ipp] = Matrix.addByElement(Matrix.multipliesByElement(x_normalized[ipp], parameter.scaling), ps.x_min);
                if(local_search[ipp] && local_i < local_max && local_search0 > 0) {
                    Matlab.fminconOutput outfmin = LocalSearchMVMOSH(x_normalized[ipp], proc.n_eval - proc.i_eval);
                    x_normalized[ipp] = Util.copyArray(outfmin.x);
                    local_search[ipp] = false;
                    local_i++;
                    local_search_called++;
                    xt.fitness = outfmin.fit;
                    xt.objective = Configuration.PPL;

                    if (xt.fitness == xt.objective) {
                        xt.feasibility = true;
                    }else{
                        xt.feasibility = false;
                    }
                }else{
                    double ffx = TestFunction.testFuction(x_normalized[ipp]);
                    xt.fitness=ffx;
                    xt.objective=Configuration.PPL;
                    if (xt.fitness == xt.objective) {
                        xt.feasibility = true;
                    }else{
                        xt.feasibility = false;
                    }
                }

                if(proc.finish){
                    if(Configuration.isRecordsEnable) {
                        Configuration.records.endRun(Math.abs(MVMO.proc.best_value- Configuration.benchmark.bias()), proc.i_eval, Configuration.max_fes);
                    }
                    return proc.best_value;
                }

                x_normalized[ipp] = Util.divideArray(Util.subArray(x_normalized[ipp], ps.x_min), parameter.scaling);
                Fill_solution_archive();
                meann_app[ipp] = Util.copyArray(meann[ipp]);
                if(proc.i_eval > indepent_runs){
                    if (changed_best || firsttime) {
                        for(int i=0; i<n_par; i++){
                            A[i] = table.fitness[0][i];
                        }
                        if (firsttime) {
                            amax = 0.;
                            firsttime = false;
                        }
                        for (int ia = 0; ia < n_par; ia++) {
                            if (!table.feasibility[0][ia]){
                                A[ia] = A[ia] + amax;
                            }
                        }
                        IX = Util.sortOnlyIndexs(A);
                    }
                    verybest=IX[0];
                    for(int i=border_gute; i<n_par; i++){
                        goodbad[IX[i]] = false;
                    }

                    for(int i=0; i<border_gute; i++){
                        goodbad[IX[i]] = true;
                    }

                    if(!goodbad[ipp]){
                        int bestp, onep1, worstp;

                        int [] returnObject = must();
                        bestp = returnObject[0];
                        onep1 = returnObject[1];
                        worstp = returnObject[2];

                        bbb=1.1+(Configuration.rand.getDouble()-0.5)*2.0;
                        beta1 = alpha*3.0*bbb*((1.0+2.5*ff2)*Configuration.rand.getDouble() - (1.0-ff2)*0.30);
                       // beta1 = BigDecimal.valueOf(alpha).multiply(BigDecimal.valueOf(3.0).multiply(BigDecimal.valueOf(bbb).multiply(BigDecimal.valueOf(Configuration.rand.getDouble()).subtract(BigDecimal.valueOf((1.0-ff2)).multiply(BigDecimal.valueOf(0.30)))))).doubleValue();
                        beta10=0.0;

                        while (beta1 != beta10 ){
                            beta10 = beta1;
                            for(int jx=0; jx<ps.D; jx++){
                                ccc=table.bests[0][jx][bestp]-table.bests[0][jx][worstp];
                                if (Math.abs(ccc) >  1.0e-15) {
                                    x_normalized[ipp][jx] = table.bests[0] [jx] [onep1] + beta1 * ccc;
                                }else{
                                    x_normalized[ipp][jx] = table.bests[0] [jx] [onep1];
                                }
                                if (table.bests[0][jx][bestp] < 0.85 && table.bests[0][jx][bestp] > 0.15 && Configuration.rand.getDouble() > 0.15){
                                    while (x_normalized[ipp][jx] > 1.0 ||  x_normalized[ipp][jx]<0.0){

                                        returnObject = must();
                                        bestp = returnObject[0];
                                        onep1 = returnObject[1];
                                        worstp = returnObject[2];

                                        ccc = table.bests[0] [jx] [bestp] - table.bests[0] [jx][worstp];
                                        if (Math.abs(ccc) > 1.0e-15) {
                                            bbb = 1.1 + (Configuration.rand.getDouble() - 0.5)*2.0;
                                            beta1 = alpha * 3.0 * bbb * ((1.0 + 2.5 * ff2)*Configuration.rand.getDouble() - (1.0- ff2)*0.3);
                                            x_normalized[ipp] [jx] = table.bests[0] [jx] [onep1] + beta1 * ccc;
                                        }else {
                                            x_normalized[ipp] [jx] = table.bests[0] [jx] [onep1];
                                        }
                                    }
                                }else if(table.bests[0][jx][bestp] > 0.85 || table.bests[0][jx][bestp] < 0.15 && Configuration.rand.getDouble() < 0.15 ){
                                    while (x_normalized[ipp][jx] > 1.0 ||  x_normalized[ipp][jx] < 0.0) {

                                        returnObject = must();
                                        bestp = returnObject[0];
                                        onep1 = returnObject[1];
                                        worstp = returnObject[2];
                                        ccc = table.bests[0] [jx][bestp] - table.bests[0] [jx] [worstp];
                                        if (Math.abs(ccc) > 1.e-15) {
                                            bbb = 1.1 + (Configuration.rand.getDouble() - 0.5)*2.0;
                                            beta1 = alpha * 3.0 * bbb * ((1.0 + 2.5 * ff2)* Configuration.rand.getDouble() - (1.0 - ff2)*0.30);
                                            x_normalized[ipp][jx] = table.bests[0] [jx] [onep1] + beta1 * ccc;
                                        }else {
                                            x_normalized[ipp][jx] = table.bests[0] [jx] [onep1];
                                        }
                                    }
                                }else{
                                    if (x_normalized[ipp][jx] > 1.0) {
                                        x_normalized[ipp][jx] = 1.0;
                                    }else if (x_normalized[ipp][jx] < 0.0) {
                                        x_normalized[ipp] [jx] = 0.0;
                                    }
                                }
                            }
                        }
                        for(int i=0; i<ps.D; i++)
                            meann_app[ipp][i] =x_normalized[ipp][i];
                    }else{
                        if (Configuration.rand.getDouble() > (1.0-ff)*0.2){
                            for(int i=0; i<ps.D; i++)
                                x_normalized[ipp][i]=0.999 * table.bests[0][i][ipp]+0.0010 * table.bests[0][i][verybest];
                        }else{
                            for(int i=0; i<ps.D; i++){
                                x_normalized[ipp][i]=0.999 * table.bests[1][i][ipp]+0.0010 * table.bests[0][i][verybest];
                            }
                        }
                        int irandom=(int)Math.round(Configuration.rand.getDouble()*(border_gute-1));
                        for (int jxx=0; jxx < ps.D; jxx++){
                            meann_app[ipp][jxx] = meann[irandom][jxx];
                        }
                    }
                }else{
                    for(int i=0; i<ps.D; i++)
                        x_normalized[ipp][i]=table.bests[0][i][ipp];
                }

                if (Configuration.rand.getDouble() < local_search0 &&  proc.i_eval > min_eval_LS &&  proc.i_eval < max_eval_LS && goodbad[ipp]) {
                    local_search[ipp] = true;
                }else {
                    local_search[ipp] = false;
                }
                if (local_search[ipp] == false) {
                    VariableSelect1();
                }

                for(int ivar=0; ivar < D; ivar++){
                    if (considered[ipp][ivar]) {
                        if (goodbad[ipp]) {
                            x_normalized[ipp][ivar] = Configuration.rand.getDouble();
                        }


                        if (shape[ipp][ivar] > 0.0) {
                            double sss2;
                            double mmm1;
                            double mmm2;
                            double grosser;
                            double kleiner;
                            double sss1 = shape[ipp][ivar];
                            double fs_factor1 = fs_factor0 * (1.0 + ff2 * Configuration.rand.getDouble());
                            double delta_ddd_x = delta_Shape_dyn0 * (Configuration.rand.getDouble() - 0.5)*2.0 + delta_Shape_dyn1;
                            if (sss1 > Shape_dyn[ipp] [ivar]) {
                                Shape_dyn[ipp] [ivar] = Shape_dyn[ipp] [ivar] * delta_ddd_x;
                            }else {
                                Shape_dyn[ipp] [ivar] = Shape_dyn[ipp] [ivar] / delta_ddd_x;
                            }
                            if (Shape_dyn[ipp] [ivar] > sss1) {
                                grosser = Shape_dyn[ipp] [ivar];
                                kleiner = sss1;
                            }else {
                                kleiner = Shape_dyn[ipp][ivar];
                                grosser = sss1;
                            }
                            if (mappingST == 1) {
                                mmm1 = meann_app[ipp][ivar];
                                mmm2 = 0.00;
                            }else if (mappingST==2) {
                                mmm1 = Configuration.rand.getDouble();
                                mmm2 = 0.5;
                            }else {
                                mmm1 = meann_app[ipp][ivar];
                                mmm2 = 0.50;
                            }
                            if (mmm1 > mmm2) {
                                sss1 = grosser;
                                sss2 = kleiner;
                            }else {
                                sss1 = kleiner;
                                sss2 = grosser;
                            }
                            sss1 = sss1 * fs_factor1;
                            sss2 = sss2 * fs_factor1;
                            x_normalized[ipp][ivar] = h_function(meann_app[ipp] [ivar], sss1, sss2, x_normalized[ipp] [ivar]);

                        }
                    }
                }

            }
        }
    }


    public Matlab.fminconOutput LocalSearchMVMOSH(double [] x, int FEsAllowed){
        FminconFunction fminconFuction = new FminconFunction();
        Matlab.fminconOutput output =  Matlab.fmincon(fminconFuction, x, FEsAllowed );

        for(int i=0; i<output.x.length; i++){
            if(Double.isNaN(output.x[i])){
                output.x = Util.copyArray(x);
                break;
            }
        }

        return output;
    }


//    public double LSearch(){
//        double f = Configuration.benchmark.f(x_normalized[ipp]);
//        proc.i_eval++;
//        return f;
//    }


    //Evacuated h-function
    public double h_function(double x_bar,double s1,double s2, double x_p) {

        double H = x_bar * (1.0 - Math.exp(-x_p * s1))+ (1.00 - x_bar) *Math.exp(-(1.0 - x_p)* s2);
        double H0 = (1.0 - x_bar) * Math.exp(-s2);
        double H1 = x_bar * Math.exp(-s1);
        return H + H1 * x_p + H0 * (x_p - 1.0);

    }

    public double [] mv_noneq(int nnnnnn, double [] values, double vmean, double vshape, double vvqq){
        int iz = 0;
        //double [] values_noeq = new double[ps.D];
        values_noeq[iz] = values[0];

        for(int ii_jj=1; ii_jj<nnnnnn; ii_jj++){
            int izz = iz;
            boolean gleich = false;
            for(int kk_ii=0; kk_ii<izz; kk_ii++){
                if(Math.abs(values_noeq[kk_ii] - values[ii_jj]) < vvqq){
                    gleich = true;
                    break;
                }
            }
            if(!gleich){
                iz++;
                values_noeq[iz] = values[ii_jj];
            }
        }
        if(iz > 0){
            vmean = 0.8 * vmean+0.2* Util.summatory(Util.range(values_noeq, 0, iz))/iz;
            for(int i=0; i<iz; i++){
                values_noeq[i] = values_noeq[i] - vmean;
            }
        }
        if(iz > 0){
            double vv = Util.summatory(Util.range(values_noeq, 0, iz))/iz;
            if(vv > 1e-50){
                vshape = -Math.log(vv);
            }
        }

        double [] output = new double[2];
        output[0] = vmean;
        output[1] = vshape;

        return output;
    }

    public void VariableSelect1() {
        int n_var = ps.D;




        int inn= -1;
        izm[ipp]=izm[ipp]-1;
        if (izm[ipp]< 0) {
            izm[ipp] = n_var -1;
        }
        considered[ipp][izm[ipp]]=true;
        if (n_randomly > 1) {
            for (int ii = 0; ii < n_randomly - 1; ii++) {
                boolean isrepeat = false;
                while (!isrepeat) {
                    inn = (int)Math.round(Configuration.rand.getDouble() * (n_var - 1));
                    if (!considered[ipp][inn]) {
                        isrepeat = true;
                    }
                }
                considered[ipp] [inn] = true;
            }
        }
    }

    public void Fill_solution_archive(){
        no_in[ipp]++;
        boolean changed = false;
        changed_best=false;
        int i_position = 0;

        if(no_in[ipp] == 1){
            for(int i=0; i<n_to_save; i++) {
                table.fitness[i][ipp] = 1.e200;
                table.feasibility[i][ipp] = false;
            }

            for(int i=0; i<table.bests[0].length; i++){
                table.bests[0][i][ipp] = x_normalized[ipp][i];
            }
                table.fitness[0][ipp] = xt.fitness;
                table.objective[0][ipp] = xt.objective;
                table.feasibility[0][ipp] = xt.feasibility;


            no_inin[ipp]++;
            changed_best = true;

        }else{

            for(int ij=0; ij<n_to_save; ij++){
                boolean part2 = true;
                for(int i=0; i<n_par; i++){
                    if(xt.feasibility != table.feasibility[ij][i]){
                        part2 = false;
                        break;
                    }
                }
                if(xt.fitness < table.fitness[ij][ipp] && part2 || (table.feasibility[ij][ipp] == false && xt.feasibility == true) ){
                    i_position = ij;
                    changed = true;
                    if(ij < n_to_save){
                        no_inin[ipp] = no_inin[ipp] + 1;
                    }
                    break;
                }
            }

        }


        if (changed) {
            int nnnnnn = n_to_save;
            if (i_position == 0) {
                changed_best = true;
            }
            if (no_inin[ipp] < n_to_save) {
                nnnnnn = no_inin[ipp];
            }
            // isdx = nnnnnn:-1:i_position + 1;
            for(int isdx=nnnnnn-1; isdx>i_position; isdx--){
                if(isdx  <= 0 || isdx >= n_par){
                    System.out.print("DEBUG");
                }
                table.fitness[isdx][ipp]=table.fitness[isdx - 1][ipp];
                table.objective[isdx][ipp]=table.objective[isdx - 1][ipp];
                table.feasibility[isdx][ipp]=table.feasibility[isdx - 1][ipp];
                for(int i=0; i<ps.D; i++) {
                    table.bests[isdx][i][ipp] = table.bests[isdx - 1][i][ipp];
                }
            }



            for(int i=0; i<ps.D; i++) {
                table.bests[i_position][i][ipp] = x_normalized[ipp][i];
            }
            table.fitness[i_position][ipp]=xt.fitness;
            table.objective[i_position][ipp]=xt.objective;
            table.feasibility[i_position][ipp]=xt.feasibility;

            if ((no_inin[ipp] >= n_to_save)) {
                for (int ivvar = 1; ivvar < D; ivvar++) {
                    //[meann(ipp, ivvar), shape(ipp, ivvar)]=mv_noneq(nnnnnn, table.bests(1:nnnnnn, ivvar, ipp), meann(ipp, ivvar), shape(ipp, ivvar), vvqq);
                    double [] input = new double[nnnnnn];
                    for(int i=0; i<nnnnnn; i++){
                        input[i] = table.bests[i][ivvar][ipp];
                    }
                    double [] output = mv_noneq(nnnnnn, input, meann[ipp][ivvar], shape[ipp][ivvar], vvqq);
                    meann[ipp][ivvar] = output[0];
                    shape[ipp][ivvar] = output[1];
                }
            }
        }



    }
    //0: bestp, 1: onep1, 2: worstp
    public int [] must(){
        int [] returnObject = new int[3];

        int iup = (int) (Math.round(5.0 * (1.0 - ff2)));
        int ilow = 0;
        int bestp = (int)Math.round(Configuration.rand.getDouble() * (iup - ilow)) + ilow;
        int worstp = -1;
        iup = 14;
        ilow = -1;
        while ((worstp <= bestp) || (worstp >= n_par)){
            worstp=(int)Math.round(Configuration.rand.getDouble()*(iup-ilow))+ ilow;
            worstp = Math.round(border_gute + (worstp -3));
        }
        iup = worstp -1;
        ilow = bestp + 1;
        int onep1 = (int)Math.round(Configuration.rand.getDouble() * (iup - ilow)) + ilow;
        if(worstp >= n_par){
            System.out.println("DEBUG");
        }
        returnObject[0] = IX[bestp];
        returnObject[1] = IX[onep1];
        returnObject[2] = IX[worstp];

        return returnObject;
    }

}
