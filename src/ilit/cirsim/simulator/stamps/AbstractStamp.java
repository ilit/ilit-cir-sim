package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;

abstract class AbstractStamp implements IStampStrategy
{
    protected int getIndex(Component component)
    {
        return IdToMatrixIndexRelations.instance.getIndex(component);
    }

    protected int getIndex(Node node)
    {
        return IdToMatrixIndexRelations.instance.getIndex(node);
    }
}
