/**
 * Created by framg on 21/04/2016.
 */
public class RankSVMLearn {
    private static int kt_RBF = 0;
    private static int kt_Polynomial = 1;


    private double CalculateTrainingKernelMatrix(double[] xvals, int npoints, int nx, double[] K,
                                               double TwoSigmaPow2, double sigma_A, double sigma_Pow, int kernel, double kernelParam1, double kernelParam2)
    {
        double distPow2,tmp;
        double []x1,x2;
        double avrdist = 0;
        for (int i=0; i<npoints; i++)
            for (int j=i; j<npoints; j++)
            {
                if (kernel == kt_RBF)
                {
                    if (i == j)	distPow2 = 0;
                    else
                    {
                        x1 = new double[nx];
                        x2 = new double[nx];
                        for(int n=0; n<nx; n++){
                            x1[n] = xvals[(i*nx) + n];
                            x2[n] = xvals[(j*nx) + n];
                        }
                        //x1 = Util.copyArray(xvals[i*nx]);
                        //x2 = Util.copyArray(xvals[j*nx]);
                        distPow2 = 0;
                        for (int k=0; k<nx; k++)
                        {
                            tmp = x1[k] - x2[k];
                            distPow2 += tmp * tmp;
                        }
                    }
                    K[i*npoints +j] = K[j*npoints +i] =distPow2;
                    avrdist += Math.sqrt(distPow2);
                }
                else {
                    double [] x1tmp = new double [nx];
                    double [] x2tmp = new double[nx];
                    for(int n=0; n<nx; n++){
                        x1tmp[n] = xvals[(i * nx) + n];
                        x2tmp[n] = xvals[(j * nx) + n];
                    }

                    K[i * npoints + j] = K[j * npoints + i] = MyKernel(kernel, x1tmp ,x2tmp , nx, kernelParam1, kernelParam2);
                }
            }

        avrdist /= ((npoints-1)*npoints/2);
        double sigma = sigma_A * Math.pow(avrdist , sigma_Pow);
        TwoSigmaPow2 = 2.0*sigma*sigma;

        double avrK = 0;
        for (int i=0; i<npoints; i++)
            for (int j=i; j<npoints; j++)
            {
                if (kernel == kt_RBF)
                {
                    if (i == j)		K[i*npoints +j] = K[j*npoints +i] = 1;
                    else			K[i*npoints +j] = K[j*npoints +i] = Math.exp( -K[i*npoints +j] / TwoSigmaPow2);
                }
            }

        return TwoSigmaPow2;
    }

    private void OptimizeL(int ntrain, double[] p_Ci, double epsilon, int niter,
                   double[] p_Kij,double[] p_dKij,double[] p_alpha,double[] p_sumAlphaDKij,double[] p_div_dKij)
    {
        int nAlpha = ntrain-1;
        double old_alpha, new_alpha, delta_alpha, sumAlpha, dL;
        int i,i1,j;

        for (i=0; i<nAlpha; i++)
            for (j=0; j<nAlpha; j++)
                p_dKij[i*nAlpha + j] = p_Kij[i*ntrain + j] - p_Kij[i*ntrain + (j+1)] - p_Kij[(i+1)*ntrain + j] + p_Kij[(i+1)*ntrain + (j+1)];

        for (i=0; i<nAlpha;i++)
        {
            //	p_alpha[i] = p_Ci[i];// * rand()/(float)RAND_MAX;//p_Ci[i] * (0.95 + 0.05*rand()/(float)RAND_MAX);	// p_Ci[i] * rand()/(float)RAND_MAX;
            //	p_alpha[i] = p_Ci[i] * rand()/(float)RAND_MAX;
            p_alpha[i] = p_Ci[i] * (0.95 + 0.05* Configuration.rand.getFloat());
        }

        for (i=0; i<nAlpha; i++)
        {
            sumAlpha = 0;
            for (j=0; j<nAlpha;j++)
                sumAlpha += p_alpha[j] * p_dKij[i*nAlpha + j];
            p_sumAlphaDKij[i] = (epsilon - sumAlpha) / p_dKij[i*nAlpha + i];
        }

        for (i=0; i<nAlpha; i++)
            for (j=0; j<nAlpha; j++)
                p_div_dKij[i*nAlpha + j] = p_dKij[i*nAlpha + j] / p_dKij[j*nAlpha + j];


        for (i=0; i<niter; i++)
        {
            i1 = i%nAlpha;	//	int i1 = rand()%nAlpha;
            old_alpha = p_alpha[i1];
            new_alpha = old_alpha + p_sumAlphaDKij[i1];
            if (new_alpha > p_Ci[i1])		new_alpha = p_Ci[i1];
            if (new_alpha < 0)				new_alpha = 0;
            delta_alpha = new_alpha - old_alpha;


            dL = delta_alpha * p_dKij[i1*nAlpha + i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + epsilon);

            if (dL > 0)
            {
                for (j=0; j<nAlpha; j++)
                    p_sumAlphaDKij[j] -= delta_alpha * p_div_dKij[i1*nAlpha + j];

                p_alpha[i1] = new_alpha;
            }

        }
    }


    private void OptimizeL2(int ntrain, double[] p_Ci, double epsilon, int niter,
                    double[] p_Kij,double[] p_dKij,double[] p_alpha,double[] p_sumAlphaDKij,double[] p_div_dKij)
    {
        int nAlpha = ntrain-1;
        double old_alpha, new_alpha, delta_alpha, sumAlpha, dL;
        int i,i1,j;

        double[] p_dKii = new double[ntrain];

        for (i=0; i<nAlpha; i++)
        {
            for (j=0; j<nAlpha; j++)
            {
                p_dKij[i*nAlpha + j] = p_Kij[i*ntrain + j] - p_Kij[i*ntrain + (j+1)] - p_Kij[(i+1)*ntrain + j] + p_Kij[(i+1)*ntrain + (j+1)];
            }
            p_dKii[i] = p_dKij[i*nAlpha + i];
        }

        for (i=0; i<nAlpha;i++)
        {
            //	p_alpha[i] = p_Ci[i];// * rand()/(float)RAND_MAX;//p_Ci[i] * (0.95 + 0.05*rand()/(float)RAND_MAX);	// p_Ci[i] * rand()/(float)RAND_MAX;
            //	p_alpha[i] = p_Ci[i] * rand()/(float)RAND_MAX;
            p_alpha[i] = p_Ci[i] ;//* (0.95 + 0.05*rand()/(float)RAND_MAX);
        }

        for (i=0; i<nAlpha; i++)
        {
            sumAlpha = 0;
            for (j=0; j<nAlpha;j++)
                sumAlpha += p_alpha[j] * p_dKij[i*nAlpha + j];
            p_sumAlphaDKij[i] = (epsilon - sumAlpha) / p_dKij[i*nAlpha + i];
        }

        for (i=0; i<nAlpha; i++)
            for (j=0; j<nAlpha; j++)
                p_div_dKij[i*nAlpha + j] = p_dKij[i*nAlpha + j] / p_dKij[j*nAlpha + j];


        double L = 0;
        double lastBestL = 0;
        double BestL = 0;

        int G = 0;
        int verb = 0;
        for (i=0; i<niter; i++)
        {
            if (G == 0)
            {
                i1 = i%nAlpha;	//	int i1 = rand()%nAlpha;
                old_alpha = p_alpha[i1];
                new_alpha = old_alpha + p_sumAlphaDKij[i1];
                if (new_alpha > p_Ci[i1])		new_alpha = p_Ci[i1];
                if (new_alpha < 0)				new_alpha = 0;
                delta_alpha = new_alpha - old_alpha;


                //dL = delta_alpha * p_dKij[i1*nAlpha + i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + 0);
                dL = delta_alpha * p_dKii[i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + 0);

                if (dL > 0)
                {
                    for (j=0; j<nAlpha; j++)
                        p_sumAlphaDKij[j] -= delta_alpha * p_div_dKij[i1*nAlpha + j];

                    p_alpha[i1] = new_alpha;
                }
                L = L + dL;
            }
            else
            {
                int jmax =0;
                double dLmax = -1e+30;
                for (int j1=0; j1<nAlpha; j1++)
                {

                    i1 = j1;	//	int i1 = rand()%nAlpha;
                    old_alpha = p_alpha[i1];
                    new_alpha = old_alpha + p_sumAlphaDKij[i1];
                    if (new_alpha > p_Ci[i1])		new_alpha = p_Ci[i1];
                    if (new_alpha < 0)				new_alpha = 0;
                    delta_alpha = new_alpha - old_alpha;


                    //dL = delta_alpha * p_dKij[i1*nAlpha + i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + 0);
                    dL = delta_alpha * p_dKii[i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + 0);
                    if (dL > dLmax)
                    {
                        dLmax = dL;
                        jmax = j1;
                    }
                }
                i1 = jmax;	//	int i1 = rand()%nAlpha;
                old_alpha = p_alpha[i1];
                new_alpha = old_alpha + p_sumAlphaDKij[i1];
                if (new_alpha > p_Ci[i1])		new_alpha = p_Ci[i1];
                if (new_alpha < 0)				new_alpha = 0;
                delta_alpha = new_alpha - old_alpha;


                //dL = delta_alpha * p_dKij[i1*nAlpha + i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + 0);
                dL = delta_alpha * p_dKii[i1] * ( p_sumAlphaDKij[i1] - 0.5*delta_alpha + 0);

                if (dL > 0)
                {
                    for (j=0; j<nAlpha; j++)
                        p_sumAlphaDKij[j] -= delta_alpha * p_div_dKij[i1*nAlpha + j];

                    p_alpha[i1] = new_alpha;
                }
                L = L + dL;

                if (dL/(p_Ci[0]*p_Ci[0]) < 1e-20)
                {
                    //	printf("%d %e\n",i,dL/(p_Ci[0]*p_Ci[0]));
                    i = niter;
                    verb = 0;
                }
            }


        }


    }

    void normalizeX(double[] x_Encoded, int ntrain, int nx, double[] X_min, double[] X_max)
    {
        int i,j;
        double []xcur;
        for (j=0; j<nx; j++)
        {
            X_min[j] = x_Encoded[j];
            X_max[j] = x_Encoded[j];
        }
        for (i=0; i<ntrain; i++)
        {
            xcur = new double[nx];
            for(int n=0; n<nx; n++) {
                xcur[n] = x_Encoded[(i*nx)+n];
            }
            for(j=0; j<nx; j++)
            {
                if (xcur[j] < X_min[j])	X_min[j] = xcur[j];
                if (xcur[j] > X_max[j])	X_max[j] = xcur[j];
            }
        }
        for (i=0; i<ntrain; i++)
        {
            xcur = new double[nx];
            for(int n=0; n<nx; n++) {
                xcur[n] = x_Encoded[(i*nx)+n];
            }
            for(j=0; j<nx; j++)
            {
                xcur[j]  = (xcur[j] - X_min[j] ) / (X_max[j] - X_min[j]);
            }
        }
    }

    void Encoding(double [] x, double [] x_Encoded, double [] invsqrtC, double [] xmean, int npoints, int nx)	//x'(i) = C^(-0.5) * ( x(i) - xmean(i) )
    {
        double [] xcur, xnew;
        double sum;
        int jrow;
        double[] dx = new double[nx];
        for (int i=0; i<npoints; i++)
        {
            //xcur = x[i*nx];
            //xnew = x_Encoded[i*nx];
            xcur = new double[nx];
            xnew = new double[nx];
            for(int n=0; n<nx; n++){
                xcur[n] = x[(i*nx) + n];
                xnew[n] = x_Encoded[(i*nx) + n];
            }
            for (int j=0; j<nx; j++)
                dx[j] = xcur[j] - xmean[j];
            for (int j=0; j<nx; j++)
            {
                sum = 0;
                jrow = j*nx;
                for (int k=0; k<nx; k++)
                    sum += invsqrtC[jrow + k] * dx[k];
                xnew[j] = sum;
            }
        }
    }


    public void LearningRankN(double[] x_training, double[] x_trainingEncoded,
                       int nx, int ntrain, int niter, int kernel, double epsilon, double[] p_Ci, double[] p_Cinv,
                       double sigma_A, double sigma_Pow, double[] xmean, int doEncoding, double[] optAlphas, double pTwoSigmaPow2, double kernelParam1, double kernelParam2,
                       int normalize, double[] X_min, double[] X_max) //TODO devolver pTwoSigmaPow2

    {

        //0. Init Temp Data
        int nAlpha = ntrain-1;
        double[] p_Kij = new double[ntrain*ntrain];
        double[] p_dKij = new double[nAlpha*nAlpha];
        double[] p_alpha = new double[nAlpha];
        double[] p_sumAlphaDKij = new double[nAlpha];
        double[] p_div_dKij = new double[nAlpha*nAlpha];
        double[] p_dx = new double[nx];
        double[] Kvals = new double[ntrain];

        double ttotal = 0;
        //1. Transform training points to the new coordinate system and
        //then compute Euclidean distance instead of 'EXPENSIVE' Mahalanobis distance
        if (doEncoding == 1)
        {	//p_Cinv is the C^-0.5 ;)
            Encoding(x_training, x_trainingEncoded, p_Cinv, xmean, ntrain, nx);
        }

        if (normalize == 1)
        {
            normalizeX(x_trainingEncoded, ntrain, nx, X_min, X_max);
        }

        //2. Calculate the distance between points, then calculate sigma(gamma) and Kernel Matrix
        double TwoSigmaPow2 = 0;
        TwoSigmaPow2 = CalculateTrainingKernelMatrix(x_trainingEncoded, ntrain, nx, p_Kij, TwoSigmaPow2, sigma_A, sigma_Pow, kernel, kernelParam1, kernelParam2); //TODO TwoSigmaPow2 devolver


        //3. Optimize alpha parameters
        OptimizeL2(ntrain, p_Ci, epsilon, niter, p_Kij, p_dKij, p_alpha, p_sumAlphaDKij, p_div_dKij);


        pTwoSigmaPow2 = TwoSigmaPow2;
        for (int i=0; i<nAlpha; i++)
        {
            optAlphas[i] = p_alpha[i];
        }

    }

    private double MyKernel(int kernel, double [] x1, double [] x2,int nx, double param1, double param2)
    {
        if (kernel == kt_RBF)			return Kernel_RBF(x1,x2,nx,param1);
        if (kernel == kt_Polynomial)		return Kernel_Polynomial(x1,x2,nx,param1,param2);
        return -1;
    }

    private double Kernel_RBF(double []  x1, double [] x2,int nx, double TwoSigmaPow2)
    {
        return Math.exp(- DistPow2_Euclidean(x1, x2, nx) / TwoSigmaPow2);
    }

    private double Kernel_Polynomial(double []  x1, double [] x2,int nx, double c, double d)
    {
        double KK = 0;
        for (int k=0; k<nx; k++)
            KK += x1[k] * x2[k];
        return Math.pow(KK + c,d);
    }

    private  double DistPow2_Euclidean(double [] x1, double [] x2,int nx)
    {
        double tmp;
        double dist = 0;
        for (int i=0; i<nx; i++)
        {
            tmp = x1[i] - x2[i];
            dist += tmp * tmp;
        }
        return dist;
    }
}
