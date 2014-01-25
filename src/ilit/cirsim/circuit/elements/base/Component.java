package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;

public abstract class Component implements
        IRenderable, IIdentifiable, IStampable, IGroup
{
    private static final String ERROR_VIEW_UNDEF = "View undefined";
    /**
     * In a device which consumes power, the cathode is negative,
     * and in a device which provides power, the cathode is positive.
     */
    public Node cathode;
    /**
     * In a device which consumes power, the anode is positive,
     * and in a device which provides power, the anode is negative.
     */
    public Node anode;

    private final int id;
    protected IGraphRenderable view;

    protected Component()
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

    public IGraphRenderable getView()
    {
        if (view == null)
            throw new Error(ERROR_VIEW_UNDEF);

        return view;
    }
}
