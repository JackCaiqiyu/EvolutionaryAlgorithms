package CMAESold;

/**
 * Created by framg on 17/04/2016.
 */
public class Boundary_transformation {
    private static final double DBL_MAX = 999999999;
    private static double default_lower;
    private static double default_upper;

    public static double [] lower_bounds; /* array of size len_of_bounds */
    public static double [] upper_bounds; /* array of size len_of_bounds */
    public static int len_of_bounds; /* in case, last value is recycled */
    public static double [] al; /* "add"-on to lower boundary preimage, same length as bounds */
    public static double [] au; /* add-on to upper boundary preimage, same length as bounds */


    public static void cmaes_boundary_transformation_init(double lower_bound, double upper_bound, int len_of_bound)
    {
        double [] ub, lb;
        default_lower = DBL_MAX / -1e2;
        default_upper = DBL_MAX / 1e2;

        lower_bounds = new double[Cmaes.Configuration.DIM];
        upper_bounds = new double[Cmaes.Configuration.DIM];
        for(int i = 0; i< Cmaes.Configuration.DIM; i++){
            lower_bounds[i] = lower_bound;
            upper_bounds[i] = upper_bound;
        }
        len_of_bounds = len_of_bound;

      //  if (lower_bounds == null && len_of_bounds <= 1) /* convenience default */
       //     t->lower_bounds = default_lower;
       // if (upper_bounds == NULL && len_of_bounds <= 1)
       //     t->upper_bounds = default_upper;
       // if (len_of_bounds == 0) {
       //     t->lower_bounds = default_lower;
        //    t->upper_bounds = default_upper;
        //    t->len_of_bounds = 1;
       // }

        //if (t->lower_bounds == NULL || t->upper_bounds == NULL)
         //   _FatalError("init: input upper_bounds or lower_bounds was NULL and len_of_bounds > 1");

    /* compute boundaries in pre-image space, al and au */
        al = new double[len_of_bounds];
        au = new double[len_of_bounds];

        lb = Util.copyArray(lower_bounds);
        ub = Util.copyArray(upper_bounds);

        for(int i = 0; i < len_of_bounds; ++i) {
           // if (lb[i] == ub[i])
           //     _FatalError("in _init: lower and upper bounds must be different in all variables");
        /* between lb+al and ub-au transformation is the identity */
            al[i] = Math.min((ub[i] - lb[i]) / 2., (1. + Math.abs(lb[i])) / 20.);
            au[i] = Math.min((ub[i] - lb[i]) / 2., (1. + Math.abs(ub[i])) / 20.);
        }
    }

    public static void cmaes_boundary_transformation(double[] x, double [] y, int len)
    {
        double lb, ub, ali, aui;

        cmaes_boundary_transformation_shift_into_feasible_preimage(x, y, len);
        for(int i = 0; i < len; ++i) {
            lb = lower_bounds[_index(i)];
            ub = upper_bounds[_index(i)];
            ali = al[_index( i)];
            aui = au[_index(i)];
            if (y[i] < lb + ali)
                y[i] = lb + (y[i] - (lb - ali)) * (y[i] - (lb - ali)) / 4. / ali;
            else if (y[i] > ub - aui)
                y[i] = ub - (y[i] - (ub + aui)) * (y[i] - (ub + aui)) / 4. / aui;
        }
    }


    public static void cmaes_boundary_transformation_shift_into_feasible_preimage( double[]x, double []y, int len)
    {
        double lb, ub, ali, aui, r, xlow, xup;
        int i;

        for(i = 0; i < len; ++i) {
            lb = lower_bounds[_index(i)];
            ub = upper_bounds[_index(i)];
            ali = al[_index(i)];
            aui = au[_index(i)];
            xlow = lb - 2 * ali - (ub - lb) / 2.0;
            xup = ub + 2 * aui + (ub - lb) / 2.0;
            r = 2 * (ub - lb + ali + aui); /* == xup - xlow == period of the transformation */

            y[i] = x[i];

            if (y[i] < xlow) { /* shift up */
                y[i] += r * (1 + (int)((xlow - y[i]) / r));
            }
            if (y[i] > xup) { /* shift down */
                y[i] -= r * (1 + (int)((y[i] - xup) / r));
            /* printf(" \n%f\n", fmod(y[i] - ub - au, r)); */
            }
            if (y[i] < lb - ali) /* mirror */
                y[i] += 2 * (lb - ali - y[i]);
            if (y[i] > ub + aui)
                y[i] -= 2 * (y[i] - ub - aui);

          /*  if ((y[i] < lb - al - 1e-15) || (y[i] > ub + au + 1e-15)) {
                printf("BUG in cmaes_boundary_transformation_shift_into_feasible_preimage: lb=%f, ub=%f, al=%f au=%f, y=%f\n",
                        lb, ub, al, au, y[i]);
                _FatalError("BUG");
            }*/
        }
    }

    public static void cmaes_boundary_transformation_inverse(double [] x, double [] y, int len)
    {
        double lb, ub, ali, aui;
        int i;

        for (i = 0; i < len; ++i) {
            lb = lower_bounds[_index(i)];
            ub = upper_bounds[_index(i)];
            ali = al[_index(i)];
            aui = au[_index(i)];
            y[i] = x[i];
            if (y[i] < lb + ali)
                y[i] = (lb - ali) + 2 * Math.sqrt(ali * (y[i] - lb));
            else if (y[i] > ub - aui)
                y[i] = (ub + aui) - 2 * Math.sqrt(aui * (ub - y[i]));
        }

        double [] z = new double[len];
        for (i = 0; i < len; ++i)
                z[i] = y[i];
        cmaes_boundary_transformation(z, y, len);
        //for (i = 0; i < len; ++i)
            //if (Math.abs(y[i] - x[i]) > 1e-14)
            //        printf("  difference for index %ld should be zero, is %f ", i, y[i] - x[i]);
        for (i = 0; i < len; ++i)
            y[i] = z[i];


    }

    static int _index(int i)
    {
        return i < len_of_bounds ? i : len_of_bounds - 1;
    }


}
