package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.stamps.ResistorStamp;

public abstract class Resistor extends Component
{
    protected double resistance;

    protected Resistor(double initialResistance)
    {
        super();

        resistance = initialResistance;
    }

    public double getResistance()
    {
        return resistance;
    }

    public void setResistance(double resistance)
    {
        this.resistance = resistance;
        /** Don't forget to remove stamp */
    }

    public void placeStamp(MnaEquationsSystem equations)
    {
        superPlaceStamp(ResistorStamp.instance, equations, this);
    }

    public boolean isGroupOne()
    {
        return true;
    }

    public boolean isNonlinear()
    {
        return false;
    }

    public boolean isDynamic()
    {
        return false;
    }
}
