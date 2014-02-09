package ilit.cirsim.circuit.elements.sources;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.stamps.CurrentSourceStamp;
import ilit.cirsim.view.elements.CurrentSourceView;

public class CurrentSource extends Component
{
    protected double current;

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

    public void placeStamp(MnaEquationsSystem equations)
    {
        superPlaceStamp(CurrentSourceStamp.instance, equations, this);
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
