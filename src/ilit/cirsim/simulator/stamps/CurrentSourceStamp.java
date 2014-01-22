package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.CurrentSource;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CurrentSourceStamp extends AbstractStamp
{
    /**
     * In a device which provides power, the cathode is positive
     * and the anode is negative.
     */

    public static final CurrentSourceStamp instance = new CurrentSourceStamp();

    private Matrix matrix;

    public void setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        matrix = equationsSystem.getMatrix();
        SparseVector sideVector = equationsSystem.getSideVector();

        CurrentSource currentSource = (CurrentSource)component;
        double current = currentSource.getCurrent();

        /** Nodes stamping */
        if (currentSource.anode.isGround())
        {
            /** RHS[nplus] -= ElemValue[ElemIndex]; */
            groundedStamp(currentSource.cathode, -current, sideVector);
        }
        else if (currentSource.cathode.isGround())
        {
            /** RHS[nminus] += ElemValue[ElemIndex]; */
            groundedStamp(currentSource.anode, current, sideVector);
        }
        else
        {
            int anodeIndex = getIndex(currentSource.anode);
            int cathodeIndex = getIndex(currentSource.cathode);

            /**
             * RHS[nplus] -= ElemValue[ElemIndex];
             * RHS[nminus] += ElemValue[ElemIndex];
             */
            sideVector.add(cathodeIndex, -current);
            sideVector.add(anodeIndex, current);
        }
    }

    private void groundedStamp(Node liveNode, double val, SparseVector sideVector)
    {
        int liveNodeIndex = getIndex(liveNode);

        sideVector.add(liveNodeIndex, val);
    }
}
