import javafx.util.Pair;

import java.util.List;

/**
 * Created by framg on 28/04/2016.
 */
public class DiscreteDomain {

    int [] m_mins; /// min values vector
    int [] m_maxs; /// max values vector
    int m_dim; /// dimensionality
    boolean m_isbound; /// if true must be checked the bounds
    int m_search_ini; // The initial dimension search
    int m_search_fin; // The final dimension search

    boolean[] m_check_dim; // The dimension search


    public DiscreteDomain(int dim)  {
        m_mins = new int [dim];
        m_maxs = new int [dim];
        m_dim = dim;
        m_isbound = true;
        m_search_ini = 0;
        m_search_fin = dim-1;
        m_check_dim = new boolean[dim];

        Util.assignArray(m_check_dim, true);
    }

    public DiscreteDomain(int dim, int division){
        m_mins = new int [dim];
        m_maxs = new int [dim];
        m_dim = dim;
        m_isbound = true;
        m_search_ini = 0;
        m_search_fin = dim-1;
        m_check_dim = new boolean[dim];

        setValues(division);

    }

    void checkGen(int gen) {
        if (gen >= m_dim) {
            //char msg[100];
            //sprintf(msg, "position %d is not valide for a gen", gen);
            //throw new string(msg);
            System.err.println("position is not valide for a gen");
            System.exit(0);
        }

    }

    int [] getValues(int gen, boolean check) {
        if (check)
            checkGen(gen);

        int [] p_range = new int[2];
        p_range[0] = m_mins[gen];
        p_range[1] = m_maxs[gen];

        return p_range;
    }

    void setValues(int gen, int min, int max,boolean check) {
        if (check)
            checkGen(gen);

        assert(min <= max);

        m_mins[gen] = min;
        m_maxs[gen] = max;
    }

    void setValues(int division) {
        assert(division > 0);

        for(int i=0;i<m_dim;i++)
        {
            m_mins[i] = 0;
            m_maxs[i] = division-1;
        }
    }

    int clip(int gen, int value, boolean check) {
        int min, max;

        if (check)
            checkGen(gen);

        if (!m_isbound) {
            return value;
        }

        min = m_mins[gen];
        max = m_maxs[gen];

        if (value < min)
            return min;
        else if (value > max)
            return max;
        else
            return value;
    }

    boolean canBeChanged(int dim) {
        assert(dim < m_dim);

        return m_check_dim[dim];

       /* if ((dim >= m_search_ini) && (dim <= m_search_fin))
            return true;
        else
            return false;
            */
    }

    void clip(int [] crom) {
        assert(crom.length == m_dim);


        if (!m_isbound) {
            return;
        }

        for (int i = 0; i < m_dim; ++i) {
            crom[i] = clip(i, crom[i], false);
        }
    }



    boolean check(int [] crom) {
        boolean follow = true;
        assert(crom.length != m_dim);

        for (int i = 0; i < m_dim && follow; ++i) {
            if (crom[i] < m_mins[i])
                follow = false;
            else if (crom[i] > m_maxs[i])
                follow = true;
        }

        return follow;
    }

    void setDomainCenter(int [] center, double scale) {
        int i;
        int min, max, range, value;

        for (i = 0; i < m_dim; i++) {
            int [] p_range = getValues(i, true);
            min = p_range[0];
            max = p_range[1];
            value = center[i];
            range = (int)Math.round((max-min)*scale/2);

            if (value - range > min) {
                min = value - range;
            }
            if (value + range < max) {
                max = value + range;
            }

            setValues(i, min, max, true);
        }

    }


    void getInitRandom(int [] crom) {
        List<Integer> new_crom = Util.arrayToList(crom);
        for (int i = 0; i < m_dim; ++i) {
            new_crom.add(Configuration.rand.getInt(m_mins[i], m_maxs[i])); //TODO check si max esta incluido o no
        }
    }

    void getInit(int [] crom) {
        getInitRandom(crom);
    }


    void setSearchDomain(boolean [] searchDim, int dim) {
        assert(dim == m_dim);
        for(int i=0; i<dim; i++)
            m_check_dim[i] = searchDim[i];
    }

    void getSearchDomain(boolean [] searchDim, int dim) {
        assert(dim == m_dim);
        for(int i=0; i<dim; i++)
            searchDim[i] = m_check_dim[i];
    }

}
