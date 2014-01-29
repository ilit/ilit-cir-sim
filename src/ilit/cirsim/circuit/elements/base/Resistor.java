package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.simulator.stamps.IStampStrategy;
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

    public IStampStrategy getStamp()
    {
        return ResistorStamp.instance;
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
