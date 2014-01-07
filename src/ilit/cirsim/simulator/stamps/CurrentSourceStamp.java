package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.CurrentSource;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CurrentSourceStamp extends AbstractStamp
{
    /**
     * In a device which provides power, the cathode is positive
     * and the anode is negative.
     */

    public static CurrentSourceStamp instance = new CurrentSourceStamp();

    FlexCompRowMatrix matrix;

    public void setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        matrix = equationsSystem.getMatrix();
        SparseVector sideVector = equationsSystem.getSideVector();

        CurrentSource currentSource = (CurrentSource)component;
        double current = currentSource.getCurrent();

        int nodePlus = currentSource.cathode.getId();
        int nodeMinus = currentSource.anode.getId();

        /** Nodes stamping */
        if (currentSource.anode.isGround())
        {
            /** RHS[nplus] -= ElemValue[ElemIndex]; */
            groundedStamp(nodePlus, -current, sideVector);
        }
        else if (currentSource.cathode.isGround())
        {
            /** RHS[nminus] += ElemValue[ElemIndex]; */
            groundedStamp(nodeMinus, current, sideVector);
        }
        else
        {
            int nodeMinusIndex = allocateMatrixIndex(nodeMinus);
            int nodePlusIndex = allocateMatrixIndex(nodePlus);

            /**
             * RHS[nplus] -= ElemValue[ElemIndex];
             * RHS[nminus] += ElemValue[ElemIndex];
             */
            sideVector.add(nodePlusIndex, -current);
            sideVector.add(nodeMinusIndex, current);
        }
    }

    private void groundedStamp(int liveNodeId, double val, SparseVector sideVector)
    {
        int liveNodeIndex = allocateMatrixIndex(liveNodeId);

        sideVector.add(liveNodeIndex, val);
    }
}
