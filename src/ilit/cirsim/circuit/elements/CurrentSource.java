package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.IGraphRenderable;
import ilit.cirsim.simulator.stamps.CurrentSourceStamp;
import ilit.cirsim.simulator.stamps.IStampStrategy;
import ilit.cirsim.simulator.stamps.VoltageSourceStamp;
import ilit.cirsim.view.elements.CurrentSourceView;
import ilit.cirsim.view.elements.VoltageSourceView;

public class CurrentSource extends Component
{
    private final double current;

    public CurrentSource(double I)
    {
        super();

        current = I;

        view = new CurrentSourceView(this);
    }

    public double getCurrent()
    {
        return current;
    }

    public IStampStrategy getStamp()
    {
        return CurrentSourceStamp.instance;
    }

    public boolean isGroupOne()
    {
        return true;
    }

    public boolean isNonlinear()
    {
        return false;
    }
}
