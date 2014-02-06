package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IIdentifiable;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;

public class Node implements IIdentifiable
{
    private static final String GROUND_ID_ERROR =
            "Only ground node ID allowed to be manually passed to constructor";

    protected int id;

    public Node()
    {
        id = UniqueIDManager.instance.getNewID();
    }

    /** This constructor was made to allow Ground class omit default Node constructor */
    public Node(int id)
    {
        if (id != UniqueIDManager.GROUND_NODE_ID)
            throw new Error(GROUND_ID_ERROR);
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
