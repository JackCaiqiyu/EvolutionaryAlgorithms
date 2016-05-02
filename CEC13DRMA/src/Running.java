/**
 * Created by framg on 28/04/2016.
 */
public class Running {
    int m_neval;  /**< Current evaluation number */
    int m_generalEval;

    double m_generalBest;

    int m_maxeval;  /**< Maximum evaluation number */

    int m_maxmsecs; /**< Maximum time number */
    //clock_t m_timeInit;

    OptimeCriterion *m_checkOptime;    /**< Optimum criterion applied */

    boolean    m_optimized;  /**< Stores if the optimum has been achieved */

    Running *m_parent; /**< Stores a new Running to assign */
    list<Running*> m_children;

    double m_best;  /** @var Best fitness obtained */

    bool m_getConvergence;
    FILE *convergence_file;
}
