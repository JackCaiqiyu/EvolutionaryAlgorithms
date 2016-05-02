/**
 * Created by framg on 28/04/2016.
 */
public class Region {
    int [] m_sol; /**< Solution represented by the individual */
    double m_id; /**< Identity id */

    double m_perf; /**< Fitness */
    boolean m_evaluated; /**< Store if the individual has been evaluated */

    public int[] getM_sol() {
        return m_sol;
    }

    public void setM_sol(int[] m_sol) {
        this.m_sol = m_sol;
    }

    public double getM_id() {
        return m_id;
    }

    public void setM_id(double m_id) {
        this.m_id = m_id;
    }

    public double getM_perf() {
        return m_perf;
    }

    public void setM_perf(double m_perf) {
        this.m_perf = m_perf;
    }

    public boolean isM_evaluated() {
        return m_evaluated;
    }

    public void setM_evaluated(boolean m_evaluated) {
        this.m_evaluated = m_evaluated;
    }
}
