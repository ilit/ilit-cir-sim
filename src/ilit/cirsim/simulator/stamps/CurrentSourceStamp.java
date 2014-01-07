package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.CurrentSource;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public class CurrentSourceStamp extends AbstractStamp
{
    public static CurrentSourceStamp instance = new CurrentSourceStamp();

    FlexCompRowMatrix matrix;

    public void setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        /**
         * assert(current == 0);
             if(nminus == -1) {
                RHS[nplus] -= ElemValue[ElemIndex];
             }
             else if(nplus == -1) {
                RHS[nminus] += ElemValue[ElemIndex];
             }
             else{
                RHS[nplus] -= ElemValue[ElemIndex];
                RHS[nminus] += ElemValue[ElemIndex];
             }
         */
        matrix = equationsSystem.getMatrix();
        SparseVector sideVector = equationsSystem.getSideVector();

        CurrentSource currentSource = (CurrentSource)component;
        double current = currentSource.getCurrent();

        int node1Id = currentSource.node1.getId();
        int node2Id = currentSource.node2.getId();

        /** Nodes stamping */
        if (currentSource.node1.isGround())
        {
            /**
             * RHS[nplus] -= ElemValue[ElemIndex];
             */
            groundedStamp(node2Id, -current, sideVector);
        }
        else if (currentSource.node2.isGround())
        {
            /**
             * RHS[nminus] += ElemValue[ElemIndex];
             */
            groundedStamp(node1Id, current, sideVector);
        }
        else
        {
            int node1Index = allocateMatrixIndex(node1Id);
            int node2Index = allocateMatrixIndex(node2Id);

            /**
             * RHS[nplus] -= ElemValue[ElemIndex];
             * RHS[nminus] += ElemValue[ElemIndex];
             */
            sideVector.add(node1Index, -current);
            sideVector.add(node2Index, current);
        }
    }

    private void groundedStamp(int liveNodeId, double val, SparseVector sideVector)
    {
        int liveNodeIndex = allocateMatrixIndex(liveNodeId);

        sideVector.add(liveNodeIndex, val);
    }
}
