package ilit.cirsim.simulator.stamps;

import ilit.cirsim.simulator.IdToMatrixIndexRelations;

public abstract class AbstractStamp implements IStampStrategy
{
    protected int allocateMatrixIndex(int id)
    {
        return IdToMatrixIndexRelations.instance.getIndex(id);
    }
}
