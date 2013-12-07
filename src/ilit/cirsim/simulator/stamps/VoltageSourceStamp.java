package ilit.cirsim.simulator.stamps;

import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.base.Component;

public class VoltageSourceStamp extends AbstractStamp
{
    public static VoltageSourceStamp instance = new VoltageSourceStamp();

    FlexCompRowMatrix matrix;

    public void setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        matrix = equationsSystem.getMatrix();
        SparseVector sideVector = equationsSystem.getSideVector();

        VoltageSource voltageSource = (VoltageSource)component;
        double voltage = voltageSource.getDcVoltage();

        int node1Id = voltageSource.node1.getId();
        int node2Id = voltageSource.node2.getId();
        int componentsCurrentIndex = allocateMatrixIndex(voltageSource.getId());

        /** Nodes stamping */
        if (voltageSource.node1.isGround())
        {
            /**
             * MNA[nplus][current] += 1.0;
             * MNA[current][nplus] += 1.0;
             * RHS[current] += ElemValue;
             */
            groundedStamp(node2Id, componentsCurrentIndex, -1.0d);
        }
        else if (voltageSource.node2.isGround())
        {
            groundedStamp(node1Id, componentsCurrentIndex, 1.0d);
        }
        else
        {
            int node1Index = allocateMatrixIndex(node1Id);
            int node2Index = allocateMatrixIndex(node2Id);

            /**
             * MNA[nplus][current] += 1.0;
             * MNA[current][nplus] += 1.0;
             * MNA[nminus][current] -= 1.0;
             * MNA[current][nminus] -= 1.0;
             * RHS[current] += ElemValue;
             */
            nodeStamp(node1Index, componentsCurrentIndex, 1.0d);
            nodeStamp(node2Index, componentsCurrentIndex, -1.0d);
        }

        /** Side vector stamping */
        sideVector.add(componentsCurrentIndex, voltage);
    }

    private void groundedStamp(int liveNodeId, int componentsCurrentIndex, double val)
    {
        int liveNodeIndex = allocateMatrixIndex(liveNodeId);

        nodeStamp(liveNodeIndex, componentsCurrentIndex, val);
    }

    private void nodeStamp(int nodeIndex, int componentIndex, double val)
    {
        matrix.add(nodeIndex, componentIndex, val);
        matrix.add(componentIndex, nodeIndex, val);
    }
}
