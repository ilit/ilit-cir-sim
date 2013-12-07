package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.IGraphRenderable;
import ilit.cirsim.simulator.stamps.IStampStrategy;
import ilit.cirsim.simulator.stamps.VoltageSourceStamp;
import ilit.cirsim.view.elements.VoltageSourceView;

public class VoltageSource extends Component
{
    public static final int AC_MAGNITUDE = 1;
    public static final int AC_OFFSET = 0;

    public boolean isDc;

    /** DC */
    public double dcV;

    /** AC */
    public double offset = 0;
    public double amplitude;
    public double frequency;

    /** DC constructor */
    public VoltageSource(double V)
    {
        super();

        isDc = true;
        dcV = V;

        commonConstructor();
    }

    /** AC constructor */
    public VoltageSource(double amplitude, double frequency)
    {
        super();

        isDc = false;
        this.amplitude = amplitude;
        this.frequency = frequency;

        commonConstructor();
    }

    private void commonConstructor()
    {

    }

    public IGraphRenderable getView()
    {
        return VoltageSourceView.instance;
    }

    public double getDcVoltage()
    {
        return dcV;
    }

    public IStampStrategy getStamp()
    {
        return VoltageSourceStamp.instance;
    }

    public boolean isGroupOne()
    {
        return false;
    }
}
