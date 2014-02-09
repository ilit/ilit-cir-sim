package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.sources.CurrentSource;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.util.StampMemento;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CurrentSourceStamp extends Stamp
{
    /**
     * Using independent current source in group one.
     * In a device which provides power, the cathode is positive
     * and the anode is negative.
     */
    public static final CurrentSourceStamp instance = new CurrentSourceStamp();

    public StampMemento setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        SparseVector rhs = equationsSystem.getSideVector();

        StampMemento memento = new StampMemento();

        CurrentSource currentSource = (CurrentSource)component;

        double current = currentSource.getCurrent();

        /** Nodes stamping */
        if (currentSource.anode.isGround())
        {
            /** RHS[nplus] -= ElemValue[ElemIndex]; */
            int liveNodeIndex = getIndex(currentSource.cathode);
            rhsAddSave(liveNodeIndex, -current, rhs, memento);
        }
        else if (currentSource.cathode.isGround())
        {
            /** RHS[nminus] += ElemValue[ElemIndex]; */
            int liveNodeIndex = getIndex(currentSource.anode);
            rhsAddSave(liveNodeIndex, current, rhs, memento);
        }
        else
        {
            int anodeIndex = getIndex(currentSource.anode);
            int cathodeIndex = getIndex(currentSource.cathode);

            /**
             * RHS[nplus] -= ElemValue[ElemIndex];
             * RHS[nminus] += ElemValue[ElemIndex];
             */
            rhsAddSave(cathodeIndex, -current, rhs, memento);
            rhsAddSave(anodeIndex, current, rhs, memento);
        }

        return memento;
    }
}
