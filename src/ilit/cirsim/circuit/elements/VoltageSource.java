package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.stamps.IStampStrategy;
import ilit.cirsim.simulator.stamps.VoltageSourceStamp;
import ilit.cirsim.view.elements.VoltageSourceView;

public class VoltageSource extends Component
{
    private final double voltage;

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

    public IStampStrategy getStamp()
    {
        return VoltageSourceStamp.instance;
    }

    public boolean isGroupOne()
    {
        return false;
    }

    public boolean isNonlinear()
    {
        return false;
    }
}
