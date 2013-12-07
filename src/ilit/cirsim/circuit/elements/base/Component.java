package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;

public abstract class Component implements
        IRenderable, IIdentifiable, IStampable, IMnaGroup
{
    public Node node1;
    public Node node2;

    protected int id;

    public Component()
    {
        id = UniqueIDManager.instance.getNewID();
    }

    public String getComponentClassName()
    {
        return getClass().getSimpleName();
    }

    public int getId()
    {
        return id;
    }

    public boolean isGrounded()
    {
        return node1.isGround() || node2.isGround();
    }
}
