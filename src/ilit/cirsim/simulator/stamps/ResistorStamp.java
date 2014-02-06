package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.util.StampMemento;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.Matrix;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.Resistor;

public class ResistorStamp extends Stamp
{
    public static final ResistorStamp instance = new ResistorStamp();

    public StampMemento setStamp(MnaEquationsSystem equationsSystem, Component component)
    {
        Matrix matrix = equationsSystem.getMatrix();
        if (matrix == null)
           throw new Error("equationsSystem.getMatrix() == null");

        StampMemento memento = new StampMemento();

        Resistor resistor = (Resistor)component;

        double conductance = 1 / resistor.getResistance();

        if (component.anode.isGround())
        {
            int i = getIndex(component.cathode);

            matrixAddSave(i, i, conductance, matrix, memento);
        }
        else if (component.cathode.isGround())
        {
            int i = getIndex(component.anode);

            matrixAddSave(i, i, conductance, matrix, memento);
        }
        else
        {
            int i1 = getIndex(component.anode);
            int i2 = getIndex(component.cathode);

            matrixAddSave(i1, i1,  conductance, matrix, memento);
            matrixAddSave(i2, i1, -conductance, matrix, memento);
            matrixAddSave(i2, i2,  conductance, matrix, memento);
            matrixAddSave(i1, i2, -conductance, matrix, memento);
        }

        return memento;
    }
}
