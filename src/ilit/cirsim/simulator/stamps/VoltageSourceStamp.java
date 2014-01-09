package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.Node;
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

        int componentsCurrentIndex = allocateMatrixIndex(voltageSource);

        /** Nodes stamping */
        if (voltageSource.anode.isGround())
        {
            /**
             * MNA[nplus][current] += 1.0;
             * MNA[current][nplus] += 1.0;
             * RHS[current] += ElemValue;
             */
            groundedStamp(voltageSource.cathode, componentsCurrentIndex, 1.0d);
        }
        else if (voltageSource.cathode.isGround())
        {
            groundedStamp(voltageSource.anode, componentsCurrentIndex, -1.0d);
        }
        else
        {
            int cathodeIndex = allocateMatrixIndex(voltageSource.cathode);
            int anodeIndex = allocateMatrixIndex(voltageSource.anode);

            /**
             * MNA[nplus][current] += 1.0;
             * MNA[current][nplus] += 1.0;
             * MNA[nminus][current] -= 1.0;
             * MNA[current][nminus] -= 1.0;
             * RHS[current] += ElemValue;
             */
            nodeStamp(cathodeIndex, componentsCurrentIndex, 1.0d);
            nodeStamp(anodeIndex, componentsCurrentIndex, -1.0d);
        }

        /** Side vector stamping - RHS */
        sideVector.add(componentsCurrentIndex, voltage);
    }

    private void groundedStamp(Node liveNode, int componentsCurrentIndex, double val)
    {
        int liveNodeIndex = allocateMatrixIndex(liveNode);

        nodeStamp(liveNodeIndex, componentsCurrentIndex, val);
    }

    private void nodeStamp(int nodeIndex, int componentIndex, double val)
    {
        matrix.add(nodeIndex, componentIndex, val);
        matrix.add(componentIndex, nodeIndex, val);
    }
}
