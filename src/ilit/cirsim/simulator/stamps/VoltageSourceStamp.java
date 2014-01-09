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

        int anode = voltageSource.anode.getId();
        int cathode = voltageSource.cathode.getId();
        int componentsCurrentIndex = allocateMatrixIndex(voltageSource);

        /** Nodes stamping */
        if (voltageSource.anode.isGround())
        {
            /**
             * MNA[nplus][current] += 1.0;
             * MNA[current][nplus] += 1.0;
             * RHS[current] += ElemValue;
             */
            groundedStamp(cathode, componentsCurrentIndex, -1.0d);
        }
        else if (voltageSource.cathode.isGround())
        {
            groundedStamp(anode, componentsCurrentIndex, 1.0d);
        }
        else
        {
            int anodeIndex = allocateMatrixIndex(anode);
            int cathodeIndex = allocateMatrixIndex(cathode);

            /**
             * MNA[nplus][current] += 1.0;
             * MNA[current][nplus] += 1.0;
             * MNA[nminus][current] -= 1.0;
             * MNA[current][nminus] -= 1.0;
             * RHS[current] += ElemValue;
             */
            nodeStamp(anodeIndex, componentsCurrentIndex, 1.0d);
            nodeStamp(cathodeIndex, componentsCurrentIndex, -1.0d);
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
