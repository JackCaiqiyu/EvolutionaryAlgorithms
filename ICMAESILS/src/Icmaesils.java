/*
    Copyright 2013 Tianjun Liao and Thomas Stuetzle

    This file is part of iCMAESILS.

    iCMAESILS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    iCMAESILS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with iCMAESILS.  If not, see <http://www.gnu.org/licenses/>.
*/


import java.util.Vector;

public class Icmaesils {

    double const* cmaes_SetMean(cmaes_t*, const double*xmean);


    boolean improve = true;
    double tunec;
    int relaynum;

    Vector<Double> mtsls1solfitness;
    Vector<Double> icmaessolfitness;
    Vector<Double> icmaes_train_reward;
    Vector<Double> mtsls1_train_reward;

    //void test_func(long double*, long double*, int, int, int);

    double [] OShift, M,y,z,x_bound;
    int ini_flag = 0, n_flag, func_flag;
    double [] f, x;
    Vector<Vector<Double>> matrix;
    double initevalscout = 0;
    double initbestvalue = FLT_MAX;
    Vector<Double> initkeepbestx;

    public Icmaesils(){

        this->config = config;
        tunea = config -> gettunea();
        tuneb = config -> gettuneb();
        tunec = config -> gettunec();
        tuned = config -> gettuned();
        tunee = config -> gettunee();
        tunef = config -> gettunef();
        tuneg = config -> gettuneg();
        globleseed = config -> getRNGSeed();
        x = (long double*)malloc(1 * config -> getProblemDimension() * sizeof(long double));
        f = (long double*)malloc(sizeof(long double)*1);

    }

    public int runICMAESILS(){
        /*
                unsigned dim = config -> getProblemDimension();
        mtsls1solfitness.resize(dim + 2);
        icmaessolfitness.resize(dim + 2);
        icmaes_train_reward.resize(dim + 2);
        mtsls1_train_reward.resize(dim + 2);

        initialization();
        for (int i = 0; i < dim; i++) {
            mtsls1solfitness[i] = initkeepbestx[i];
        }

        // competition phase

        for (relaynum = 0; relaynum < 1; relaynum++) {
            icmaes(10000 * dim * config -> getlearn_perbudget(), mtsls1solfitness);
        }
        icmaes_train_reward = icmaessolfitness;
        for (int i = 0; i < dim; i++) {
            icmaessolfitness[i] = mtsls1solfitness[i];
        }
        ILSmtsls1(10000 * dim * config -> getlearn_perbudget(), config -> getmtsls1per_ratedim() * dim, icmaessolfitness);
        mtsls1_train_reward = mtsls1solfitness;

        // deployment phase
        if (icmaes_train_reward[dim] > mtsls1_train_reward[dim]) {
            ILSmtsls1(10000 * dim * (1 - config -> getlearn_perbudget() * 2), config -> getmtsls1per_ratedim() * dim, mtsls1_train_reward);
        } else {
            icmaes_train_reward[dim + 1] = mtsls1_train_reward[dim + 1];
            icmaes(10000 * dim * (1 - config -> getlearn_perbudget() * 2), icmaes_train_reward);
        }

        free(x);
        free(f);
        free(y);
        free(z);
        free(M);
        free(OShift);
        free(x_bound);
         */
        return 0;
    }

    public Vector<Double> icmaes(double maxfevalsforicmaes, Verctor<Double> mtsls1feedback){
        /*
         cmaes_t evo;
        double*const*pop;
        double*fitvals;
        double fbestever = 0;
        double*xbestever = NULL;
        double fmean;
        int i, irun, lambda = 0, countevals = 0;
        int maxevals;
        char const*stop;
        long double long_fitness;
        int dim = config -> getProblemDimension();
        double xinit[ dim],stddev[dim];
        maxevals = maxfevalsforicmaes;
        int countmaxevalsforicmaes;
        int first = 1;
        double icmaeskeepbestxk[ dim];
        double liaobestvalue;
        double liaoevalscount;
        if (relaynum == 0) {
            liaobestvalue = initbestvalue;
            liaoevalscount = initevalscout;
            countmaxevalsforicmaes = maxevals;
        } else {
            liaobestvalue = mtsls1feedback[dim];
            liaoevalscount = mtsls1feedback[dim + 1];
            countmaxevalsforicmaes = maxevals + mtsls1feedback[dim + 1];
        }

        double liaofirstforxinit;
        for (irun = 0; liaoevalscount < countmaxevalsforicmaes; ++irun) {
            if (irun == 0) {
                for (int j = 0; j < dim; j++) {
                    xinit[j] = mtsls1feedback[j];
                    icmaeskeepbestxk[j] = xinit[j];
                }
            } else {
                for (int j = 0; j < dim; j++) {
                    xinit[j] = (config -> getMaxInitRange() - config -> getMinInitRange()) * gsl_rng_uniform(RNG::R) + config -> getMinInitRange();
                }
            }
            for (int j = 0; j < dim; j++) {
                stddev[j] = tunec * (config -> getMaxInitRange() - config -> getMinInitRange());
            }
            for (int n = 0; n < dim; n++) {
                x[n] = xinit[n];
            }

            test_func(x, f, dim, 1, config -> getProblemID());
            liaofirstforxinit = f[0];
            liaoevalscount++;
            if (liaobestvalue - liaofirstforxinit > 1e-30) {
                if (liaoevalscount <= countmaxevalsforicmaes) {
                    liaobestvalue = liaofirstforxinit;
                    if (liaobestvalue <= 1e-8) {
                        printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                    } else {
                        printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                    }
                    for (int j = 0; j < dim; j++) {
                        icmaeskeepbestxk[j] = xinit[j];
                    }
                }
            }

            string filename = "initials.par";
            fitvals = cmaes_init( & evo, config -> getProblemDimension(), xinit, stddev, 0, lambda, filename.c_str());
            lambda = (int) cmaes_Get( & evo, "lambda");
            setbuf(stdout, NULL);


            while (!(stop = cmaes_TestForTermination( & evo)))
            {
                pop = cmaes_SamplePopulation( & evo);
                for (i = 0; i < cmaes_Get( & evo,"popsize");
                ++i){


                inbound(config -> getMinInitRange(), config -> getMaxInitRange(), pop[i], dim);
                for (int n = 0; n < dim; n++) {

                    x[n] = pop[i][n];
                }
                test_func(x, f, dim, 1, config -> getProblemID());
                long_fitness = f[0];
                liaoevalscount++;

                if (long_fitness <= FLT_MAX) {
                    fitvals[i] = long_fitness;
                } else {
                    fitvals[i] = FLT_MAX;
                }

                if (liaobestvalue - fitvals[i] > 1e-30) {
                    if (liaoevalscount <= countmaxevalsforicmaes) {
                        liaobestvalue = fitvals[i];
                        if (liaobestvalue <= 1e-8) {
                            printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                        } else {
                            printf("%.0f %le\n", liaoevalscount, liaobestvalue);
                        }
                        for (int j = 0; j < dim; j++) {
                            icmaeskeepbestxk[j] = pop[i][j];
                        }
                    }
                }

            }

                cmaes_UpdateDistribution( & evo, fitvals);
                fflush(stdout);
            }

            lambda = tuned * cmaes_Get( & evo, "lambda");
            if (lambda > 200) {
                lambda = 200;
            }
            cmaes_exit( & evo);
        }
        for (int j = 0; j < dim; j++) {
            icmaessolfitness[j] = icmaeskeepbestxk[j];
        }
        icmaessolfitness[dim] = liaobestvalue;
        icmaessolfitness[dim + 1] = countmaxevalsforicmaes;
        return icmaessolfitness;
         */
    }

    public Vector<Double> ILSmtsls1(int maxmtsls1fevals, int maxiter, Vector<Double> icmaesfeedback){
        /*
         int dim = config -> getProblemDimension();
        cmaes_t evols;
        double liaobestmtsls1value;
        double liaoevalsmtsls1count;

        liaobestmtsls1value = icmaesfeedback[dim];
        liaoevalsmtsls1count = icmaesfeedback[dim + 1];
        maxmtsls1fevals = maxmtsls1fevals + icmaesfeedback[dim + 1];

        double lsmin = config -> getMinInitRange();
        double lsmax = config -> getMaxInitRange();
        double xk[ dim];
        double mtsls1keepbestxk[ dim];

        for (int i = 0; i < dim; i++) {

            xk[i] = icmaesfeedback[i];
            mtsls1keepbestxk[i] = xk[i];
        }
        double liaofirstforxinitmtsls1;

        for (int n = 0; n < dim; n++) {

            x[n] = xk[n];
        }
        test_func(x, f, dim, 1, config -> getProblemID());

        liaofirstforxinitmtsls1 = f[0];
        liaoevalsmtsls1count++;
        if (liaobestmtsls1value - liaofirstforxinitmtsls1 > 1e-30) {
            liaobestmtsls1value = liaofirstforxinitmtsls1;
            printf("%.0f %le\n", liaoevalsmtsls1count, liaofirstforxinitmtsls1);
        }

        double s;
        s = config -> getmtsls1_initstep_rate() * (lsmax - lsmin);

        do {

            int iterun = 0;
            vector<int> conbef;
            bool convergence = false;
            double acceptbefore = liaobestmtsls1value;
            do {
                if (liaoevalsmtsls1count < maxmtsls1fevals) {
                    if (improve == false) {

                        s = s / (long double)(2);

                        if (s < 1e-20) {

                            conbef.push_back(liaobestmtsls1value);
                            if (conbef.size() >= 2) {
                                if (abs(conbef.back() - conbef[conbef.size() - 1]) < 1e-20) {
                                    convergence = true;
                                }
                            }
                            s = ((0.6 - 0.3) * gsl_rng_uniform(RNG::R) + 0.3) * (lsmax - lsmin);
                        }
                    }
                    improve = false;
                    for (int i = 0; i < dim; i++) {
                        long double before1;
                        for (int n = 0; n < dim; n++) {
                            x[n] = xk[n];
                        }
                        test_func(x, f, dim, 1, config -> getProblemID());
                        before1 = f[0] + addPenalty(lsmin, lsmax, xk, dim, liaoevalsmtsls1count);

                        liaoevalsmtsls1count++;
                        if (liaobestmtsls1value - before1 > 1e-30) {
                            if (liaoevalsmtsls1count <= maxmtsls1fevals) {
                                liaobestmtsls1value = before1;
                                if (liaobestmtsls1value <= 1e-8) {
                                    printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                } else {
                                    printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                }

                                for (int i = 0; i < dim; i++) {
                                    mtsls1keepbestxk[i] = xk[i];
                                }
                            }
                        }

                        xk[i] = xk[i] - s;
                        long double after1;
                        for (int n = 0; n < dim; n++) {
                            x[n] = xk[n];
                        }
                        test_func(x, f, dim, 1, config -> getProblemID());
                        after1 = f[0] + addPenalty(lsmin, lsmax, xk, dim, liaoevalsmtsls1count);

                        liaoevalsmtsls1count++;
                        if (liaobestmtsls1value - after1 > 1e-30) {
                            if (liaoevalsmtsls1count <= maxmtsls1fevals) {
                                liaobestmtsls1value = after1;

                                if (liaobestmtsls1value <= 1e-8) {
                                    printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                } else {
                                    printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                }
                                for (int i = 0; i < dim; i++) {
                                    mtsls1keepbestxk[i] = xk[i];
                                }
                            }
                        }

                        if (abs(after1 - before1) <= 1e-20) {
                            xk[i] = xk[i] + s;

                        } else {
                            if (after1 - before1 > 1e-20) {
                                xk[i] = xk[i] + s;
                                long double before2 = before1;
                                xk[i] = xk[i] + 0.5 * s;
                                long double after2;

                                for (int n = 0; n < dim; n++) {

                                    x[n] = xk[n];
                                }
                                test_func(x, f, dim, 1, config -> getProblemID());
                                after2 = f[0] + addPenalty(lsmin, lsmax, xk, dim, liaoevalsmtsls1count);

                                liaoevalsmtsls1count++;
                                if (liaobestmtsls1value - after2 > 1e-30) {
                                    if (liaoevalsmtsls1count <= maxmtsls1fevals) {
                                        liaobestmtsls1value = after2;
                                        if (liaobestmtsls1value <= 1e-8) {
                                            printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                        } else {
                                            printf("%.0f %le\n", liaoevalsmtsls1count, liaobestmtsls1value);
                                        }
                                        for (int i = 0; i < dim; i++) {
                                            mtsls1keepbestxk[i] = xk[i];
                                        }
                                    }
                                }
                                if (after2 >= before2 || (0 < before2 - after2 <= 1e-20)) {
                                    xk[i] = xk[i] - 0.5 * s;

                                } else {

                                    improve = true;
                                }
                            } else {

                                improve = true;
                            }

                        }
                    }
                    iterun++;
                } else {
                    break;
                }
            } while (iterun < maxiter && convergence == false);
            if (liaoevalsmtsls1count < maxmtsls1fevals) {
                if (acceptbefore - liaobestmtsls1value < 1e-20) {
                    for (int i = 0; i < dim; i++) {
                        double srand = (lsmax - lsmin) * gsl_rng_uniform(RNG::R) + lsmin;
                        xk[i] = srand + ((1 - config -> getmtsls1_iterbias_choice()) * gsl_rng_uniform(RNG::R) + config -> getmtsls1_iterbias_choice()) * (mtsls1keepbestxk[i] - srand);
                    }
                }

                s = config -> getmtsls1_initstep_rate() * (lsmax - lsmin);

            } else {
                break;
            }

        } while (liaoevalsmtsls1count < maxmtsls1fevals);
        for (int i = 0; i < dim; i++) {
            mtsls1solfitness[i] = mtsls1keepbestxk[i];
        }
        mtsls1solfitness[dim] = liaobestmtsls1value;
        mtsls1solfitness[dim + 1] = maxmtsls1fevals;
        return mtsls1solfitness;
         */
    }

    public boolean inbound(double min, double max, double [] ind, int num) {
        /*
                bool check = true;
        int i;
        for (i = 0; i < num; i++) {
            if (ind[i] > max) {
                ind[i] = max;

            } else if (ind[i] < min) {
                ind[i] = min;

            }
        }
        return check;
         */
    }

    double addPenalty(double minr, double maxr, double [] psol, int dim, double getfunevals) {
        /*
                int i;
        long double penalty = 0.0;
        for (i = 0; i < dim; i++) {
            if (psol[i] < minr) {

                penalty += fabs(psol[i] - minr) * fabs(psol[i] - minr);
            }
            if (psol[i] > maxr) {

                penalty += fabs(psol[i] - maxr) * fabs(psol[i] - maxr);
            }
        }

        return penalty * getfunevals;
         */
    }

    double printcosttime(){
        /*
        double etime;
        static struct timeval tp;
        gettimeofday( & tp, NULL);
        etime = (double) tp.tv_sec + (double) tp.tv_usec / 1000000.0;
        return etime - config -> getStartTime();
         */
    }

    double minmaxrand() {
        /*return double
        (config -> getMinInitRange() + (config -> getMaxInitRange() - config -> getMinInitRange()) * gsl_rng_uniform(RNG::R));
        */
    }

    void initialization() {
       /* initkeepbestx.resize(config -> getProblemDimension());
        unsigned int intipopsize = 1 * config -> getProblemDimension();
        matrix.resize(intipopsize);
        for (unsigned int i = 0;
        i<intipopsize ;
        i++)
        {
            matrix[i].resize(config -> getProblemDimension());
            for (unsigned a = 0; a < config -> getProblemDimension(); a++) {
                matrix[i][a] = minmaxrand();
                x[a] = matrix[i][a];
            }
            test_func(x, f, config -> getProblemDimension(), 1, config -> getProblemID());
            initevalscout++;
            matrix[i].push_back(f[0]);
            if (initbestvalue - matrix[i][config -> getProblemDimension()] > 1e-30) {
                initbestvalue = matrix[i][config -> getProblemDimension()];
                printf("%.0f %le\n", initevalscout, initbestvalue);
                for (int j = 0; j < config -> getProblemDimension(); j++) {
                    initkeepbestx[j] = matrix[i][j];
                }
            }
        }*/
    }




}