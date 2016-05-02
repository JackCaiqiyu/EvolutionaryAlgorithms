import sun.security.provider.SHA;

/**
 * Created by framg on 17/03/2016.
 */


public class MVMO {
    Xt xt;
    Ps ps;
    Proc proc;
    Paramater parameter;
    Table table;

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
    int fs_factor_start;
    int fs_factor_end;
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
    int value_ini;


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
    int [] izz;
    int [] no_in;
    int [] no_inin;

    double local_search0_percentage;
    double delta_Shape_dyn;
    double delta_Shape_dyn0;
    double delta_Shape_dyn1;
    double ffx;
    double oox;



    public void initialize() {
        parameter.scaling = ps.x_max - ps.x_min; //TODO check realmente x_min es un array
        indepent_runs = parameter.n_par * 2;

        double[][] xx = new double[parameter.n_par][ps.D];
        double[][] x_norm = new double[parameter.n_par][ps.D];

        for (int i = 0; i < xx.length; i++)
            for (int j = 0; j < xx[i].length; j++)
                xx[i][j] = 0;

        for (int i = 0; i < x_norm.length; i++)
            for (int j = 0; j < x_norm[i].length; j++)
                x_norm[i][j] = 0;

        for (int iijj = 0; iijj < parameter.n_par; iijj++) {
            for (int jjkk = 0; jjkk < ps.D; jjkk++) {
                xx[iijj][jjkk] = ps.x_min[jjkk] + Configuration.rand.getFloat() * (ps.x_max[jjkk] - ps.x_min[jjkk]);
            }

            for (int i = 0; i < ps.D; i++) {
                x_norm[iijj][i] = (xx[iijj][i] - ps.x_min). / parameter.scaling; //TODO scaling es otro array?? todos mentirosos
            }
        }

        for (int i = 0; i < x_norm.length; i++)
            for (int j = 0; j < x_norm[i].length; j++)
                x_normalized[i][j] = x_norm[i][j];

        n_eval = proc.n_eval;
        n_par = parameter.n_par;
        D = ps.D;
        n_to_save = parameter.n_tosave;
        fs_factor_start = parameter.fs_factor_start;
        fs_factor_end = parameter.fs_factor_end;
        Shape_dyn = new double[n_par][D];
        Util.assignMatrix(Shape_dyn, 1);
        IX = new int[n_par];
        Util.assignArray(IX, 1);
        shape = new double[n_par][D];
        Util.assignMatrix(shape, 0);
        izm = new int[n_par];
        Util.assignArray(izm, 0);

        for (int i = 0; i < n_par; i++) {
            IX[i] = i;
            izm[i] = Math.round(Configuration.rand.getFloat() * (ps.D - 1)) + 1;
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
                    meann[i][j] = Configuration.rand.getFloat();
        } else {
            Util.assignMatrix(meann, value_ini);
        }
        meann_app = Util.copyMatrix(meann);


        // izz = new int[n_par];
        //Util.assignArray(izz, 0);
        considered = new boolean[n_par][ps.D];
        for (int i = 0; i < considered.length; i++)
            for (int j = 0; j < considered[i].length; j++)
                considered[i][j] = true;


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
            n_eval = 100000;
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
    }

    public void execute(){
        while(true){
            ff = proc.i_eval / n_eval;
            ff2 = ff *ff;
            vvqq=Math.pow(10.0 , -(13.3+ff*35.0));

            border_gute0 = ratio_gute_max - ff *(ratio_gute_max - ratio_gute_min);
            border_gute = (int)Math.round (n_par * border_gute0);
            if(border_gute < 3 || n_par - border_gute < 1){
                border_gute = n_par;
            }

            if(yes_n_randomly){
                n_randomly_X = (int)Math.round(n_randomly_ini - ff * delta_nrandomly);
                n_randomly = Math.round(n_randomly_last+Configuration.rand.getFloat()*(n_randomly_X-n_randomly_last));
            }

            if(yes_fs_factor){
                fs_factor0=fs_factor_start+ff2*(fs_factor_end-fs_factor_start);
            }
            ipx = 0;
            while(ipx < n_par){
                ipp = IX[ipx];
                ipx++;

                for(int i=0; i<ps.D; i++){
                    considered[ipp][i] = false;
                }

                x_normalized[ipp] = Util.copyArray(Util.multiplyArray(x_normalized[ipp], ps.x_min + parameter.scaling));
                if(local_search[ipp] && local_i < local_max && local_search0 > 0) {
                    //TODO tiene pinta de ser el record
                    /*
                                    [msgstr, msgid] = lastwarn ;
                                    TFrcond = strcmp('MATLAB:nearlySingularMatrix',msgid); % Only informative from 'fmincon' function
                                    if TFrcond~=0
                                        rcond_value0=str2num(msgstr(81:end-1));
                                    end
                     */
                    //TODO implementar [ffx,oox,~,x_normalized(ipp,:),FEVALS] = LocalSearchMVMOSH(fhd,iii,jjj,kkk,args,x_normalized(ipp,:),proc.n_eval-proc.i_eval); %Local search
                    local_search[ipp] = false;
                    local_i++;
                    local_search_called++;
                    xt.fitness = ffx;
                    xt.objective = oox;

                    if (xt.fitness == xt.objective) {
                        xt.feasibility = true;
                    }else{
                        xt.feasibility = false;
                    }
                    //TODO mas warnings de las narices
                    /*
                    [~, msgid1] = lastwarn;
                    TFrcond1 = strcmp('MATLAB:nearlySingularMatrix',msgid1);
                     */
                }else{
                    //TODO eval de la funcion objetivo? [ffx,oox,~,x_normalized(ipp,:)]=feval(fhd,iii,jjj,kkk,args,x_normalized(ipp,:));
                    xt.fitness=ffx;
                    xt.objective=oox;
                    if (xt.fitness == xt.objective) {
                        xt.feasibility = true;
                    }else{
                        xt.feasibility = false;
                    }
                }

                //TODO aqui condicion de parada break


                x_normalized[ipp] = Util.divideArray(Util.subArray(x_normalized[ipp], ps.x_min), parameter.scaling);

                Fill_solution_archive();
                meann_app[ipp] = Util.copyArray(meann[ipp]);
                if(proc.i_eval > indepent_runs){
                    if (changed_best || firsttime) {
                        for(int I=0; I<n_par; I++){
                            A[I] = table.fitness[0][I];
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

                        bbb=1.1+(Configuration.rand.getFloat()-0.5)*2.0;
                        beta1 = alpha*3.0*bbb*((1.0+2.5*ff2)*Configuration.rand.getFloat() - (1.0-ff2)*0.30);
                        beta10=0.0;

                        if(beta1 != beta10 ){ //TODO pensar mejor sistema de comparar dos doubles
                            beta10 = beta1;
                            for(int jx=0; jx<ps.D; jx++){
                                ccc=table.bests[0][jx][bestp]-table.bests[0][jx][worstp];
                                if (Math.abs(ccc) >  1.d-15) { //TODO que es 1.d?????????
                                    x_normalized[ipp][jx] = table.bests[0] [jx] [onep1] + beta1 * ccc;
                                }else{
                                    x_normalized[ipp][jx] = table.bests[0] [jx] [onep1];
                                }
                                if (table.bests[0][jx][bestp] < 0.85 && table.bests[0][jx][bestp] > 0.15 && Configuration.rand.getFloat() > 0.15){
                                    while (x_normalized[ipp][jx] > 1.0 ||  x_normalized[ipp][jx]<0.0){

                                        returnObject = must();
                                        bestp = returnObject[0];
                                        onep1 = returnObject[1];
                                        worstp = returnObject[2];

                                        ccc = table.bests[0] [jx] [bestp] - table.bests[0] [jx][worstp];
                                        if (Math.abs(ccc) > 1.d - 15) {
                                            bbb = 1.1 + (Configuration.rand.getFloat() - 0.5)*2.0;
                                            beta1 = alpha * 3.0 * bbb * ((1.0 + 2.5 * ff2)*Configuration.rand.getFloat() - (1.0- ff2)*0.3);
                                            x_normalized[ipp] [jx] = table.bests[0] [jx] [onep1] + beta1 * ccc;
                                        }else {
                                            x_normalized[ipp] [jx] = table.bests[0] [jx] [onep1];
                                        }
                                    }
                                }else if(table.bests[0][jx][bestp] > 0.85 || table.bests[0][jx][bestp] < 0.15 && Configuration.rand.getFloat() < 0.15 ){
                                    while (x_normalized[ipp][jx] > 1.0 ||  x_normalized[ipp][jx] < 0.0) {

                                        returnObject = must();
                                        bestp = returnObject[0];
                                        onep1 = returnObject[1];
                                        worstp = returnObject[2];
                                        ccc = table.bests[0] [jx][bestp] - table.bests[0] [jx] [worstp];
                                        if (Math.abs(ccc) > 1.d - 15) {
                                            bbb = 1.1 + (Configuration.rand.getFloat() - 0.5)*2.0;
                                            beta1 = alpha * 3.0 * bbb * ((1.0 + 2.5 * ff2)* Configuration.rand.getFloat() - (1.0 - ff2)*0.30);
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
                        if (Configuration.rand.getFloat() > (1.0-ff)*0.2){
                            for(int i=0; i<ps.D; i++)
                                x_normalized[ipp][i]=0.999 * table.bests[0][i][ipp]+0.0010 * table.bests[0][i][verybest];
                        }else{
                            for(int i=0; i<ps.D; i++){
                                x_normalized[ipp][i]=0.999 * table.bests[1][i][ipp]+0.001 * table.bests[0][i][verybest];
                            }
                        }
                        int irandom=Math.round(Configuration.rand.getFloat()*(border_gute-1))+ 1;
                        for (int jxx=0; jxx < ps.D; jxx++){
                            meann_app[ipp][jxx] = meann[irandom][jxx];
                        }
                    }
                }else{
                    for(int i=0; i<ps.D; i++)
                        x_normalized[ipp][i]=table.bests[0][i][ipp];
                }

                if (Configuration.rand.getFloat() < local_search0 &&  proc.i_eval > min_eval_LS &&  proc.i_eval < max_eval_LS && goodbad[ipp]) {
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
                            x_normalized[ipp][ivar] = Configuration.rand.getFloat();
                        }


                        if (shape[ipp][ivar] > 0.0) {
                            double sss2;
                            double mmm1;
                            double mmm2;
                            double grosser;
                            double kleiner;
                            double sss1 = shape[ipp][ivar];
                            double fs_factor1 = fs_factor0 * (1.0 + ff2 * Configuration.rand.getFloat());
                            double delta_ddd_x = delta_Shape_dyn0 * (Configuration.rand.getFloat() - 0.5)*2.0 + delta_Shape_dyn1;
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
                                mmm1 = Configuration.rand.getFloat();
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

    /*function [ffx,oox,ggx,xn_out,FEVALS] = LocalSearchMVMOSH(testcase,iii,jjj,kkk,args,xx_yy,FEsAllowed)
    global PPL GGL
    if FEsAllowed <= 0, return, end
            options=optimset('Display','off','algorithm','interior-point','UseParallel','never','MaxFunEvals',FEsAllowed,'FinDiffType','central' ) ;
    [Xsqp, FUN , ~ , output]=...
    fmincon(@(xx_yy)LSearch(xx_yy,testcase,iii,jjj,kkk,args),xx_yy,[],[],[],[],ps.x_min,ps.x_max,[],options);


    FEVALS=output.funcCount  ;
    for nvar=1:size(xx_yy,2)
    if isnan(Xsqp(1,nvar))
    Xsqp=xx_yy;
    break;
    end
            end

    xn_out = Xsqp;
    ffx=FUN;
    oox=PPL;
    ggx=GGL;
    end*/
    public double LocalSearchMVMOSH(int n){
        if(n <= 0){
            //TODO terminar
        }

    }

    /*
        function J=LSearch(xx_yy2,testcase,iii,jjj,kkk,args)
        global PPL GGL
        [J,PPL,GGL,~] = feval(testcase,iii,jjj,kkk,args,xx_yy2);

    end
     */

    public double LSearch(){
        double f = Configuration.benchmark.f(x_normalized[ipp]);
        proc.i_eval++;
        return f;
    }


    //Evacuated h-function
    public double h_function(double x_bar,double s1,double s2, double x_p) {

        double H = x_bar * (1.0 - Math.exp(-x_p * s1))+ (1.00 - x_bar) *Math.exp(-(1.0 - x_p)* s2);
        double H0 = (1.0 - x_bar) * Math.exp(-s2);
        double H1 = x_bar * Math.exp(-s1);
        return H + H1 * x_p + H0 * (x_p - 1.0);

    }

    public void VariableSelect1() {
        int n_var = ps.D;
        int inn = -1;
        izm[ipp]=izm[ipp]-1;
        if (izm[ipp]< 1) {
            izm[ipp] = n_var;
        }
        considered[ipp][izm[ipp]]=true;
        if (n_randomly > 1) {
            for (int ii = 0; ii < n_randomly - 1; ii++) {
                boolean isrepeat = false;
                while (!isrepeat) {
                    inn = Math.round(Configuration.rand.getFloat() * (n_var - 1)) + 1;
                    if (!considered[ipp][inn]) {
                        isrepeat = true;
                    }
                }
                considered[ipp] [inn] = true;
            }
        }
    }

    public void Fill_solution_archive(){
        no_in[ipp] = no_in[ipp]+1;
        boolean changed = false;
        changed_best=false;
        int i_position = 0;

        if(no_in[ipp] == 1){
            for(int i=0; i<n_to_save; i++) {
               // for(int j=0; j<table.fitness[i].length; j++) {
                    table.fitness[i][ipp] = 1.e222;
              //  }
            }

            for(int i=0; i<n_to_save; i++) {
               // for(int j=0; j<table.feasibility[i].length; j++) {
                    table.feasibility[i][ipp] = false;
            //    }
            }

            for(int i=0; i<table.bests[0].length; i++){
                table.bests[0][i][ipp] = x_normalized[ipp][i];
            }

           // for(int i=0; i<table.fitness[0].length; i++){
                table.fitness[0][ipp] = xt.fitness;
         //   }

         //   for(int i=0; i<table.objective[0].length; i++){
                table.objective[0][ipp] = xt.objective;
         //   }

         //   for(int i=0; i<table.feasibility[0].length; i++){
                table.feasibility[0][ipp] = xt.feasibility;
        //    }

            no_inin[ipp]++;
            changed_best = true;

        }else{

            for(int ij=0; ij<n_to_save; ij++){
                if(xt.fitness < table.fitness[ij][ipp] && xt.feasibility == table.feasibility[0][ij] || (table.feasibility[ij][ipp] != xt.feasibility) ){
                    i_position = ij;
                    changed = true;
                    if(ij < n_to_save){
                        no_inin[ipp] = no_inin[ipp] + 1;
                    }
                    return;
                }
            }

        }


        if (changed) {
            int nnnnnn = n_to_save;
            if (i_position == 1) {
                changed_best = true;
            }
            if (no_inin[ipp] < n_to_save) {
                nnnnnn = no_inin[ipp];
            }
            //TODO que carajo es esto?
           // isdx = nnnnnn:-1:i_position + 1;
            for(int i=0; i<table.bests[isdx].length; i++) {
                table.bests[isdx][i][ipp] = table.bests[isdx - 1][i][ipp];
            }
            table.fitness[isdx][ipp]=table.fitness[isdx - 1][ipp];
            table.objective[isdx][ipp]=table.objective[isdx - 1][ipp];
            table.feasibility[isdx][ipp]=table.feasibility[isdx - 1][ipp];

            for(int i=0; i<table.bests[i_position].length; i++) {
                table.bests[i_position][i][ipp] = x_normalized[ipp][i];
            }
            table.fitness[i_position][ipp]=xt.fitness;
            table.objective[i_position][ipp]=xt.objective;
            table.feasibility[i_position][ipp]=xt.feasibility;

            if ((no_inin[ipp] >= n_to_save)) {
                for (int ivvar = 1; ivvar < D; ivvar++) {
                    [meann(ipp, ivvar), shape(ipp, ivvar)]=mv_noneq(nnnnnn, table.bests(1:nnnnnn, ivvar, ipp), meann(ipp, ivvar), shape(ipp, ivvar), vvqq); //TODO implementar


                }
            }
        }



    }
    //0: bestp, 1: onep1, 2: worstp
    public int [] must(){
        int [] returnObject = new int[3];

        int iup = (int) (Math.round(5.0 * (1.0 - ff2)) + 1);
        int ilow = 1;
        int bestp = Math.round(Configuration.rand.getFloat() * (iup - ilow)) + ilow;
        int worstp = -1;
        iup = 15;
        ilow = 0;
        while ((worstp <= bestp) || (worstp > n_par)){
            worstp=Math.round(Configuration.rand.getFloat()*(iup-ilow))+ ilow;
            worstp = Math.round(border_gute + (worstp -3));
        }
        iup = worstp -1;
        ilow = bestp + 1;
        int onep1 = Math.round(Configuration.rand.getFloat() * (iup - ilow)) + ilow;
        returnObject[1] = IX[onep1];
        returnObject[0] = IX[bestp];
        returnObject[2] = IX[worstp];

        return returnObject;
    }


}
