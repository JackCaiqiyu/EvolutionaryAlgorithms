/**
 * Created by framg on 28/04/2016.
 */
public class SelectRegNAM extends ISelectReg {
    @Override
    public void select(double[][] pop) {
        int max = Configuration.POP_SIZE;
        int ndim = Configuration.DIM;

        Region mom = new Region();
        Region elem = new Region();
        int pos_mom, pos_elem, pos_best=0;
        double dist, best_dist;
        boolean [] checkGen = new boolean[ndim];

        m_domain.getSearchDomain(checkGen, ndim);
        //fill(checkGen, checkGen+ndim, true);
        Util.assignArray(checkGen, true);

        int []sample = new int[max];
        // Obtengo un elemento aleatoriamente
        initSample(sample, max);
        int [] returns =  getSample(sample, max);
        max = returns[0];
        pos_mom = returns[1];
        mom = pop->getInd(pos_mom);
        for (unsigned i = 0; i < m_num; ++i) {
            pos_elem = m_random->getSample(sample, &max);
            elem = pop->getInd(pos_elem);
            dist = distreal(mom->sol(), elem->sol(), checkGen);

            if (i == 0 || (dist > best_dist) ) {
                pos_best = pos_elem;
                best_dist = dist;
            }
        } // De recorrer la población

        *pmom = pos_mom;
        *pdad = pos_best;
        delete[] sample;
        delete[] checkGen;
    }

    private void initSample(int[] sample, int max) {
        int i;

        for (i = 0; i < max; i++) {
            sample[i] = i;
        }
    }

    private int [] getSample(int[] sample, int pmax) {
        int max = pmax;
        int r, pos;

        assert(max >= 0);
        r = Configuration.rand.getInt(0, max-1);
        pos = sample[r];
        sample[r] = sample[max-1];
        --max;

        int [] returns = new int[2];
        returns[0] = max;
        returns[1] = pos;
        return returns;
    }
}
