package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.stamps.IStampStrategy;

public class Diode extends Component
{
    @Override
    public IStampStrategy getStamp()
    {
        return null;
    }

    public boolean isNonlinear()
    {
        return true;
    }

    @Override
    public boolean isGroupOne()
    {
        return true;
    }
}
