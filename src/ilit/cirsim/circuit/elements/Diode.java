package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Resistor;

public class Diode extends Resistor implements Piecewise
{
    /** Reverse bias saturation current */
    private static final double PROBE_RESISTANCE = 10.0;

    public Diode()
    {
        super(PROBE_RESISTANCE);
    }

    public boolean isNonlinear()
    {
        return true;
    }

    /** Switch linear model to probing model */
    @Override
    public void setProbeStamp()
    {
        resistance = PROBE_RESISTANCE;
    }
}
