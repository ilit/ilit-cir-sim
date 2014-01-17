package ilit.cirsim.simulator.stamps;

import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.Resistor;

public class ResistorStamp extends AbstractStamp
{
    public static final ResistorStamp instance = new ResistorStamp();

    public void setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        Matrix matrix = equationsSystem.getMatrix();
        if (matrix == null)
           throw new Error("equationsSystem.getMatrix() == null");

        Resistor resistor = (Resistor)component;

        double conductance = 1 / resistor.getResistance();

        if (resistor.anode.isGround())
        {
            int i = allocateMatrixIndex(resistor.cathode);

            matrix.add(i, i, conductance);
        }
        else if (resistor.cathode.isGround())
        {
            int i = allocateMatrixIndex(resistor.anode);

            matrix.add(i, i, conductance);
        }
        else
        {
            int i1 = allocateMatrixIndex(resistor.anode);
            int i2 = allocateMatrixIndex(resistor.cathode);

            matrix.add(i1, i1, conductance);
            matrix.add(i2, i1, -conductance);
            matrix.add(i2, i2, conductance);
            matrix.add(i1, i2, -conductance);
        }
    }
}
