package ilit.cirsim.simulator.stamps;

import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.Resistor;

public class ResistorStamp extends AbstractStamp
{
    public static ResistorStamp instance = new ResistorStamp();

    public void setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        FlexCompRowMatrix matrix = equationsSystem.getMatrix();
        Resistor resistor = (Resistor)component;

        double conductance = 1 / resistor.getResistance();

        int node1Id = resistor.node1.getId();
        int node2Id = resistor.node2.getId();

        if (resistor.node1.isGround())
        {
            int i = allocateMatrixIndex(node2Id);

            matrix.add(i, i, conductance);
        }
        else if (resistor.node2.isGround())
        {
            int i = allocateMatrixIndex(node1Id);

            matrix.add(i, i, conductance);
        }
        else
        {
            int i1 = allocateMatrixIndex(node1Id);
            int i2 = allocateMatrixIndex(node2Id);

            matrix.add(i1, i1, conductance);
            matrix.add(i2, i1, -conductance);
            matrix.add(i2, i2, conductance);
            matrix.add(i1, i2, -conductance);
        }
    }
}
