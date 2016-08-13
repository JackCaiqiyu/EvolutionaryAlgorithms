import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framg on 16/06/2016.
 */
public class CMAES355 {
    double [] xstart ;
    double insigma ;
    double inopts ;
    double varargin ;
    double cmaVersion ;
    double definput ;
    double defopts ;
    boolean flg_future_setting ;
    double nargin ;
    double input ;
    double irun ;

    double flgresume ;
    int N;
    double numberofvariables ;
    double lambda0 ;
    int lambda_last ;
    double popsize ;
    int lambda ;
    double stopFitness ;
    double stopMaxFunEvals ;
    double stopMaxIter ;
    double stopFunEvals ;
    double   stopIter ;
    double   stopTolFun ;
    double   stopTolHistFun ;
    double   stopOnStagnation ;
    double   stopOnWarnings ;
    double  flgreadsignals ;
    double   flgWarnOnEqualFunctionValues ;
    boolean  flgEvalParallel ;
    double    stopOnEqualFunctionValues ;
    double   arrEqualFunvals ;
    double   flgActiveCMA ;
    boolean   noiseHandling ;
    double    noiseMinMaxEvals ;
    double    noiseAlphaEvals ;
    double    noiseCallback ;
    double noisecum;
    double    flgdisplay ;
    double    flgplotting ;
    double     verbosemodulo ;
    double   flgscience ;
    double    flgsaving ;
    double   strsaving ;
    double   flgsavingfinal ;
    double    savetime ;
    double     maxdx ;
    double    mindx ;
    double    lbounds ;
    double   ubounds ;
    double   stopTolX ;
    double   stopTolUpX ;
    double    B ;
    boolean    bnd ;
    double   startseed ;
    double   chiN;
    double   filenameprefix ;
    double   filenames ;
    List<Pair<Integer, Integer>> lambda_hist ;
    int    mu ;
    double  [] weights ;
    double   mueff ;
    double   cc ;
    double   cs ;
    double   ccov1 ;
    double   ccovmu ;
    boolean   flgDiagonalOnly ;
    double   ccov1_sep ;
    double   ccovmu_sep ;
    double   damps ;
    int    noiseReevals ;
    double   noiseAlpha ;
    double   noiseEpsilon ;
    double  noiseTheta ;
    double  noiseCutOff ;
    int  countiter ;
    Fitness   fitness ;
    double  [][] arx ;
    double   [][]arxvalid ;
    int   countevalNaN ;
    int   counteval ;
    double   tries ;
    double    noiseS ;
    double   noiseSS ;
    double   noiseN ;
    double   [] xold ;
    double []  xmean ;
    double   []zmean ;
    double   fmean ;
    double   [] ps ;
    double    [] pc ;
    Neg    neg ;
    double   []diagC ;
    double  []  diagD ;
    double   C ;
    double    sigma ;
    double    stopflag ;
    double   BD ;
    double   noiseX ;
    double   out ;
    double   time ;
    double   outiter ;
    double  iterplotted ;
    double   savemodulo ;

    double    arfitness ;
    double    Xnew_sorted ;
    double   invsqrtC ;

    double   stop ;



    Options opts;
   //State state;

    public CMAES355(){
        State state = new State();
      //  state.stopFitness = Util.inf;


    }

    public void iteration(){
        if(countiter == 0 || lambda != lambda_last){
            if(countiter > 0 && Math.floor(Math.log10(lambda)) != Math.floor(Math.log10(lambda_last))){
                lambda_hist.add(new Pair<Integer, Integer>(countiter+1, lambda));
            }else{
                lambda_hist = new ArrayList<>();
                lambda_hist.add(new Pair<Integer, Integer>(countiter+1, lambda));
            }
            lambda_last = lambda;

            mu = opts.ParentNumber;
            if(opts.RecombinationWeights.equals("equal")){
                weights = new double[mu];
            }else if(opts.RecombinationWeights.equals("linear") ){
                weights = new double[mu];
                for(int i=0; i<mu; i++){
                    weights[i] = mu + 0.5-(i+1);
                }
            }else if(opts.RecombinationWeights.equals("superlinear")){
                weights = new double[mu];
                for(int i=0; i<mu; i++){
                    weights[i] = Math.log(mu + 0.5)-Math.log(i+1);
                }
            }else{
                System.out.println("Recombination not implemented");
                System.exit(-1);
            }
            mueff = Math.pow(Util.summatory(weights),2) / Math.pow(Util.summatory(Util.power2(weights)),2);
            for(int i=0; i<mu; i++){
                weights[i] = weights[i] / Util.summatory(weights);
            }

            if(mueff == lambda){
                System.out.println("Combination of values for PopSize, ParentNumber and "+
                " and RecombinationWeights is not reasonable");
                System.exit(-1);
            }

            cc = opts.cma.ccum;
            cs = opts.cma.cs;

            if(opts.cma.on){
                ccov1 = opts.cma.ccov1;
                ccovmu = opts.cma.ccovmu;
            }else{
                ccov1 = 0;
                ccovmu = 0;
            }

            if(flgDiagonalOnly){
                ccov1_sep = Math.min(1, ccov1 * (N+1.5) / 3);
                ccovmu_sep = Math.min(1- ccov1, ccovmu * (N+1.5) /3);
            }

            damps = opts.cma.damps;
            if(noiseHandling){
                noiseReevals = Math.min(opts.noise.reevals, lambda);
                noiseAlpha = opts.noise.alphasigma;
                noiseEpsilon = opts.noise.epsilon;
                noiseTheta = opts.noise.theta;
                noisecum = opts.noise.cum;
                noiseCutOff = opts.noise.cutoff;
            }else{
                noiseReevals = 0;
            }
        }

        countiter++;
        fitness.raw = new double[lambda + noiseReevals];

        for(int i=0; i<noiseReevals+lambda; i++){
            fitness.raw [i]= Double.NaN;
        }
        //TODO insertar evaluacion paralela (opcional)
        if(flgEvalParallel){

        }
        double [][] arz = new double[arx.length][N];
        for(int k=0; k<noiseReevals +lambda; k++){
            if(Double.isNaN(fitness.raw[k])){
                int tries = 0;

                while(Double.isNaN(fitness.raw[k])){
                    if(k < lambda){

                        for(int i=0; i<N; i++){
                            arz[k][i] = Configuration.rand.getDouble();
                        }

                        if(flgEvalParallel){

                        }else{
                            for(int i=0; i<arx.length; i++){
                                arx[k][i] = xmean[i] + sigma * BD * arz[k][i];
                            }
                        }

                    }else{
                        if(flgDiagonalOnly){

                        }else{
                            for(int i=0; i<arx.length; i++){
                                arx[k][i] = arx[k-lambda][i] + (noiseEpsilon * sigma) * (BD * Configuration.rand.getDouble());
                            }
                        }
                    }

                    if(!bnd){
                        for(int i=0; i<arxvalid.length; i++) {
                            arxvalid[k][i] = arx[k][i];
                        }
                    }else{

                    }
                    fitness.raw[k] = Configuration.benchmark.f(arxvalid[k]);
                    tries++;
                    if(Double.isNaN(fitness.raw[k])){
                        countevalNaN++;
                    }
                    if(tries%100 == 0){
                        System.err.println("NaN objective function values at evaluation");
                        System.exit(-1);
                    }
                }
                counteval++;


            }
        }

        fitness.sel = Util.copyArray(fitness.raw);

        if(bnd){
            //TODO comprobar que esta dentro del dominio (OPCIONAL)
        }

        if(noiseHandling){
            //TODO por defecto false (OPCIONAL)
        }

        fitness.idx = Util.sort(fitness.raw);
        fitness.idxsel = Util.sort(fitness.sel);
        for(int i=0; i<fitness.hist.length-1; i++){
            fitness.hist[i+1] = fitness.hist[i];
        }
        fitness.hist[0] = fitness.raw[0];
        if(fitness.hist.length < 120 + Math.ceil(30*N/lambda) ||
                countiter%5 == 0 && fitness.hist.length < 2e4){
            double [] tmp = new double[fitness.histbest.length + 1];
            tmp[0] = fitness.raw[0];
            for(int i=1; i<fitness.histbest.length+1; ++i){
                tmp[i] = fitness.histbest[i];
            }
            fitness.histbest = Util.copyArray(tmp);

            tmp = new double[fitness.histmedian.length + 1];
            tmp[0] = Util.median(fitness.raw);
            for(int i=1; i<fitness.histmedian.length+1; ++i){
                tmp[i] = fitness.histmedian[i];
            }
            fitness.histmedian = Util.copyArray(tmp);
        }else{
            for(int i=0; i<fitness.histbest.length-1; i++){
                fitness.histbest[i+1] = fitness.histbest[i];
            }
            for(int i=0; i<fitness.histmedian.length-1; i++){
                fitness.histmedian[i+1] = fitness.histmedian[i];
            }
            fitness.histbest[0] = fitness.raw[0];
            fitness.histmedian[0] = Util.median(fitness.histmedian);

        }
        for(int i=0; i<fitness.histsel.length-1; i++){
            fitness.histsel[i+1] = fitness.histsel[i];
        }
        fitness.histsel[0] = fitness.sel[0];

        xold = Util.copyArray(xmean);
        double [][] tmp = new double[arx.length][mu];
        for(int i=0; i<arx.length; i++){
            for(int j=0; j<mu; j++){
                tmp[i][j] = arx[i][fitness.idxsel[j]];
            }
        }
        xmean = Matrix.multiplies(tmp, weights);
        for(int i=0; i<arz.length; i++){
            for(int j=0; j<mu; j++){
                tmp[i][j] = arz[i][fitness.idxsel[j]];
            }
        }
        zmean = Matrix.multiplies(tmp, weights);

        if(mu==1){
            fmean = fitness.sel[0];
        }else{
            fmean = Double.NaN;
        }

        for(int i=0; i<ps.length; i++) {
            ps[i] = (1 - cs) * ps[i] + Math.sqrt(cs * (2 - cs) * mueff) * (B * zmean[i]);
        }
        boolean hsig = Util.norm(ps)/Math.sqrt(Math.pow(1-(1-cs),(2*countiter)))/chiN < 1.4 + 2/(N+1);
        if(flg_future_setting){
            hsig = Util.summatory(Util.power2(ps)) / (Math.pow(1-(1-cs), 2*countiter)) /chiN < (2 + 4/(N+1));
        }
        int hsigi;
        if(hsig){
            hsigi = 1;
        }else{
            hsigi = 0;
        }

        for(int i=0; i<pc.length; i++)
            pc[i] = (1-cc)*pc[i] + hsigi*(Math.sqrt(cc*(2-cc)*mueff)/sigma) * (xmean[i]-xold[i]);

        neg.ccov = 0;
        if(ccov1 + ccovmu > 0){
            if(flgDiagonalOnly){
               // for(int i=0; i<diagC.length;i++)
                   // diagC[i] = (1-ccov1_sep-ccovmu_sep+(1-hsigi)*ccov1_sep*cc*(2-cc)) * Matrix.add(diagC, Matrix.multipliesByElement(Util.power2(pc),ccov1_sep)) + ccovmu_sep * (diagC .* (arz(:,fitness.idxsel(1:mu)).^2 * weights));
            }
        }

    }

//    private Outputlocal_noisemeasurement local_noisemeasurement(double [] arf1, double [] arf2, lamreev, theta, cutlimit){
//        int lam = arf1.length;
//        if(arf1.length != arf2.length){
//            double [] tmp = new double[arf2.length + lam];
//            for(int i=0; i<tmp.length; i++){
//                if(i < arf2.length){
//                    tmp[i] = arf2[i];
//                }else{
//                    tmp[i] = arf1[i];
//                }
//            }
//            arf2 = Util.copyArray(tmp);
//        }
//
//        int [] idx = Util.sortOnlyIndexs(Util.add(arf1, arf2));
//        int [][] ranks = new int[2][lam];
//        for(int i=0; i<lam; i++){
//            ranks[0][i] = i;
//            ranks[1][i] = i;
//        }
//
//    }



    public static class Neg{
        double ccov;
    }

    public static class Fitness{
        double[] raw;
        double[] sel;
        int [] idx;
        int [] idxsel;
        double [] hist;
        double [] histbest;
        double [] histmedian;
        double [] histsel;
    }

}
