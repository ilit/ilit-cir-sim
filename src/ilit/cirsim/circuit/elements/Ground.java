package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.util.UniqueIDManager;

public class Ground extends Node
{
    public Ground()
    {
        id = UniqueIDManager.GROUND_NODE_ID;
    }
}
