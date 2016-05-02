/**
 * Created by framg on 27/04/2016.
 */
public class DRMA {
    /**
     * Constructor.
     *
     * @param random random number generator
     */
    public DRMA()
    {
        this.m_select = null;
        this.m_replace = null;
        this.m_mutation = null;
    }
    public void dispose()
    {
        if (m_select)
        {
            if (m_select != null)
                m_select.dispose();
        }

        if (m_replace)
        {
            if (m_replace != null)
                m_replace.dispose();
        }

        if (m_mutation)
        {
            m_mutation = null;
        }
    }
    public final int init()
    {

        setNbOfUpdate(nbOfUpdate);
        setNbOfDivision(initialNbOfDivision);
        resetNbOfDivisions();
        // Primero inicio la población
        m_pop.reset(m_domain);
        // Inicio los distintos elementos: Running, Replace, ...

        if (m_debug)
        {
            for (int j = 0;j < m_pop.size();j++)
            {
                tRegionPtr reg = m_pop.getInd(j);
                System.out.printf("%d : ",j);
                for (int i = 0;i < m_domain.getDimension();i++)
                {
                    System.out.printf("%d ", reg.gen(i));
                }
                System.out.print("\n");
            }
        }



        reset();

        // Compruebo los elementos (mutation no lo compruebo al ser opcional
        if (m_select == null)
        {
            throw new ConfigException("select");
        }
        if (m_replace == null)
        {
            throw new ConfigException("replace");
        }
        if (m_cross == null && m_crossreal == null)
        {
            throw new ConfigException("cross");
        }
        m_pop.eval(m_eval_region);
        return m_running.numEval();
    }


    public final int realApply(double [] sol, tangible.RefObject<tFitness> fitness)
    {
        int n_eval = 0;

        int pos_mom;
        int pos_dad;
        Region mom = new Region();
        Region dad = new Region();
        Region best = new Region();
        int [] crom = new int [Configuration.DIM];
        double [] cromreal = new double[Configuration.DIM];
        double best_fit;
        int better;
        int countEA = 0;
        int countEAEvals = n_eval;
        int countLSEvals = 0;
        int countImprovingEA = 0;
        boolean hasImprovedBest = false;
        boolean changed;
        int inPop;

        int totalLS = (int)(Configuration.MAX_EVALS * m_eamultiplier);
        int totalEA = (int)(Configuration.MAX_EVALS * (1 - m_eamultiplier));

        int numberOfLS = (int)(totalLS / m_intensity);
        int nbOfEAEvals = (int)(totalEA / numberOfLS);

        int [] stat = new int[numberOfDivision];
       // ArrayList<Integer> stat = new ArrayList<Integer>(numberOfDivision);

        fill(stat.iterator(), stat.end(), 0); //TODO implementar

        int initMax = m_running.numEval();


        int count = 0;
        // While is not finish
        while (!m_running.isFinish())
        {

            count++;

            //select parents
            m_select.select(m_pop, pos_mom, pos_dad);
            mom = m_pop.getInd(pos_mom);
            dad = m_pop.getInd(pos_dad);


            a//ssert mom != dad;

            //perform crossover
            cross(pos_mom, pos_dad, cromreal);
            boolean reMutate;

            if (m_mutationreal)
            {
                m_mutationreal.apply(cromreal);
            }

            if (m_mutationreal)
            {
                do
                {
                    m_problem.getDomain().clip(cromreal);
                    crom = getCorrespondingRegion(cromreal);
                    inPop = m_pop.isInPopulation(crom);
                    if (inPop >= 0)
                    {
                        tRegionPtr sameRegion = m_pop.getInd(inPop);
                        if (!sameRegion.isOptimised())
                        {
                            reMutate = false;
                        }
                        else
                        {
                            changed = false;
                            while (!changed)
                            {
                                changed = m_mutationreal.apply(cromreal);
                            }
                            reMutate = true;
                        }
                        //reMutate = false;
                    }
                    else
                    {
                        reMutate = false;
                    }

                } while (reMutate);
            }



            //if the new region generated is already in the pop do : (nothing here)
            if (inPop >= 0)
            {
                //create new individual
                tRegionPtr newind = new tRegionPtr();
                tIndividualRealPtr newposition = new tIndividualReal(cromreal);
                //evaluate it
                m_new_eval.eval(newposition);
                countEAEvals++;
                newind = m_pop.getInstance(crom);
                newind.setBestPosition(newposition);
                newind.setPerf(newposition.perf());
                //if the new solution is better than the one in the region
                if (newind.isBetter(m_pop.getInd(inPop)))
                {
                    //replace it
                    m_pop.replace((int)inPop,newind);
                }
                //otherwise discard the new solution
                else
                {
                    newind = null;
                }
            }
            //if the new region generated is not already in the pop do:
            else
            {
                //create new individual
                tRegionPtr newind = new tRegionPtr();
                tIndividualRealPtr newposition = new tIndividualReal(cromreal);
                //evaluate it
                m_new_eval.eval(newposition);
                countEAEvals++;
                newind = m_pop.getInstance(crom);
                newind.setBestPosition(newposition);
                newind.setPerf(newposition.perf());
                //check if it is better than the best
                if (newind.isBetter(m_pop.getInd(m_pop.getBest())))
                {
                    hasImprovedBest = true;
                }
                // get the individual for replacement (here the worst)
                int candreplace = m_replace.getCandidate(m_pop, newind);
                //if it is better, replace
                if (m_replace.mustBeReplace(m_pop.getInd(candreplace), newind))
                {
                    m_pop.replace(candreplace, newind);

                }
                //else discard it
                else
                {
                    newind = null;
                }
            }
            //check if an update of the number of divisions has to be performed
            if (nbOfUpdate > 0 && updateNb < nbOfUpdate)
            {
                //if the number of eval of the next update is passed
                if (m_running.numEval() > nextUpdate)
                {
                    //update number of divisions
                    updateNbOfDivision();
                }
            }
            //if the max number of EA is reached apply LS
            if (countEAEvals % (nbOfEAEvals) == 0)
            {
                countEA++;
                if (hasImprovedBest)
                {
                    countImprovingEA++;
                    hasImprovedBest = false;
                }
                countLSEvals += applyLocalSearch();
            }

            better = m_pop.getBest();
            best_fit = m_pop.getInd(better).perf();


        } // running while loop

        // retrieve best
        better = m_pop.getBest();
        best = m_pop.getInd(better);
        tChromosomeReal bestsol = best.getBestPosition().sol();
        copy(bestsol.begin(), bestsol.end(), sol.begin());

        fitness.argValue = best.perf();
        int neval = m_running.numEval() - initMax;

        m_running.reset();
        return neval;
    }

    public final int getDefaultPopsize()
    {
        return 20;
    }


    /**
     * Set the parents selection method
     *
     * @param sel criterion.
     */
    public final void setSelect(ISelectReg sel)
    {
        m_select = sel;
        m_select.setRandom(m_random);
        m_select.setDomain(m_domain);
        appendSignal(m_select);
    }
    /**
     * Set the Replacement Strategy
     * @param replace Replacement Strategy
     */
    public final void setReplacement(IReplaceReg replace)
    {
        m_replace = replace;
        appendSignal(m_replace);
    }
    /**
     * Set the mutation criterion
     *
     * @param mutation mutation method
     */
    public final void setMutation(IDMutation mutation)
    {

        m_mutation = new DMutation(mutation);
        m_mutation.setRandom(m_random);
        m_mutation.setDomain(m_domain);
        mutation.setDomain(m_domain);
        m_imutation = mutation;
    }

    public final void setProblem(Problem problem)
    {
        DCrossEAlgorithm.setProblem(problem);
    }

    public final void setProblem(ProblemParamPtr problem)
    {
        setProblem(problem.get());
    }

    public final void setCrossReal(ICrossBinaryPtr cross)
    {
        m_icrossreal = cross;
    }

    public final void setMutationReal(IMutation mutation)
    {
        m_imutationreal = mutation;

        if (m_problem)
        {
            mutation.setDomain(m_problem.getDomain());
            m_mutationreal = new Mutation(mutation);
            m_mutationreal.setRandom(m_random);
            m_mutationreal.setDomain(m_problem.getDomain());
        }

    }
    public final void checkForUniqueness()
    {
        for (int i = 0;i < m_pop.size() - 1;i++)
        {
            for (int j = i + 1;j < m_pop.size();j++)
            {
                if (m_pop.getInd(i).sol() == m_pop.getInd(j).sol())
                {
                    System.out.printf("%d and %d are the same\n",i,j);
                }
            }
        }
    }

    public final void reset()
    {
        DCrossEAlgorithm.reset();
        if (m_icrossreal)
        {
            m_icrossreal.setRandom(m_random);
            m_icrossreal.setDomain(m_problem.getDomain());
            m_icrossreal.setRunning(m_running);
            m_crossreal = new internal.CrossBinary(m_icrossreal);
            appendSignal(m_crossreal);
        }
    }

    /**
     * Crossover operator
     *
     * @param mom position of the 'mother' individual
     * @param dad position of the 'father' individual
     *
     * @param crom Chromosome, output
     *
     */
    private void cross(int pos_mom, int pos_dad, tChromosomeDiscrete crom)
    {
        tRegion mom;
        tRegion dad;
        tChromosomeReal cromreal = new tChromosomeReal();
        mom = m_pop.getInd(pos_mom);
        dad = m_pop.getInd(pos_dad);

        // Llamo al método correspondiente de cruce
        m_cross.invoke(mom, dad, crom);
    }
    private void cross(int pos_mom, int pos_dad, tChromosomeReal crom)
    {
        tRegion mom;
        tRegion dad;

        mom = m_pop.getInd(pos_mom);
        dad = m_pop.getInd(pos_dad);

        m_crossreal.invoke(mom.getBestPosition(), dad.getBestPosition(), crom);

    }

    private int m_initEval;

    private ISelectReg m_select;
    private IReplaceReg m_replace;
    private IDMutation m_imutation;
    private DMutation m_mutation;
    private internal.CrossBinaryPtr m_crossreal = new internal.CrossBinaryPtr(); //*< Crossover operator
    private ICrossBinaryPtr m_icrossreal = new ICrossBinaryPtr();
    private IMutation m_imutationreal;
    private Mutation m_mutationreal;
}
