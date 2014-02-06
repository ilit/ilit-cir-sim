package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.util.StampMemento;
import ilit.cirsim.circuit.elements.util.StampMementoTriplet;
import ilit.cirsim.circuit.elements.util.StampMementoTuple;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.stamps.Stamp;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public abstract class Component implements
        IRenderable, IIdentifiable, IStampable, IGroup
{
    private static final String ERROR_VIEW_UNDEF = "View is null for component ";
    private static final String ERROR_NO_MEMENTO = "Stamp memento is null for component ";
    private static final String DOUBLE_STAMP_ERROR = "Stamp is already placed for component ";
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

    protected StampMemento stampMemento;

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
            throw new Error(ERROR_VIEW_UNDEF + id);

        return view;
    }

    protected void superPlaceStamp(Stamp stamp, MnaEquationsSystem equations,
                                   Component component)
    /** Named super to keep interface obligations intact for children classes */
    {
        if (stampIsPlaced())
            throw new Error(DOUBLE_STAMP_ERROR + id);

        stampMemento = stamp.setStamp(equations, component);

        if (stampMemento == null)
            throw new Error(ERROR_NO_MEMENTO + id);
    }

    /** Removes stamp from equations by subtracting its impact */
    public void removeStamp(MnaEquationsSystem equations)
    {
        /** Nothing to remove. Skip silently. */
        if (!stampIsPlaced())
            return;

        Matrix matrix = equations.getMatrix();

        for (StampMementoTriplet memento : stampMemento.getMatrixLines())
            matrix.add(memento.i1, memento.i2, -memento.value);

        SparseVector rhs = equations.getSideVector();
        for (StampMementoTuple memento : stampMemento.getRhsLines())
            rhs.add(memento.index, -memento.value);

        /** Memento content is fully restored to system and is now obsolete */
        stampMemento = null;
    }

    public boolean stampIsPlaced()
    {
        return stampMemento != null;
    }
}
