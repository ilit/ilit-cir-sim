package ilit.cirsim.simulator.stamps;

import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.Matrix;
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

        if (component.anode.isGround())
        {
            int i = getIndex(component.cathode);

            matrix.add(i, i, conductance);
        }
        else if (component.cathode.isGround())
        {
            int i = getIndex(component.anode);

            matrix.add(i, i, conductance);
        }
        else
        {
            int i1 = getIndex(component.anode);
            int i2 = getIndex(component.cathode);

            matrix.add(i1, i1, conductance);
            matrix.add(i2, i1, -conductance);
            matrix.add(i2, i2, conductance);
            matrix.add(i1, i2, -conductance);
        }
    }
}
