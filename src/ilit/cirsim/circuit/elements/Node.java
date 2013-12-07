package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IIdentifiable;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;

public class Node implements IIdentifiable
{
    protected int id;

    public Node()
    {
        super();

        id = UniqueIDManager.instance.getNewID();
    }

    public int getId()
    {
        return id;
    }

    public boolean isGround()
    {
        return id == UniqueIDManager.GROUND_NODE_ID;
    }
}
