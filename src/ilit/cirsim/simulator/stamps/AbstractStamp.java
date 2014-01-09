package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;

public abstract class AbstractStamp implements IStampStrategy
{
    protected int allocateMatrixIndex(int id)
    {
        return IdToMatrixIndexRelations.instance.getIndex(id);
    }

    protected int allocateMatrixIndex(Component component)
    {
        return IdToMatrixIndexRelations.instance.getIndex(component);
    }

    protected int allocateMatrixIndex(Node node)
    {
        return IdToMatrixIndexRelations.instance.getIndex(node);
    }
}
