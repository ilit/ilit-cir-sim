package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.stamps.ResistorStamp;
import ilit.cirsim.simulator.stamps.VoltageSourceStamp;
import ilit.cirsim.view.elements.VoltageSourceView;

public class VoltageSource extends Component
{
    protected double voltage;

    public VoltageSource(double V)
    {
        super();

        voltage = V;

        view = new VoltageSourceView(this);
    }

    public double getVoltage()
    {
        return voltage;
    }

    public void setVoltage(double voltage)
    {
        this.voltage = voltage;
    }

    public void placeStamp(MnaEquationsSystem equations)
    {
        superPlaceStamp(VoltageSourceStamp.instance, equations, this);
    }

    public boolean isGroupOne()
    {
        return false;
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
