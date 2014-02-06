package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.util.UniqueIDManager;

public class Ground extends Node
{
    public Ground()
    {
        super(UniqueIDManager.GROUND_NODE_ID);
        id = UniqueIDManager.GROUND_NODE_ID;
    }
}
