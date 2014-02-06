package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Piecewise;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.MnaEquationsSystem;

public class Diode extends Resistor implements Piecewise
{
    /** Resistance for testing current direction */
    private static final double PROBE_RESISTANCE = 1;
    /** Diode allows very high forward current */
    public static final double FORWARD_RESISTANCE = 1e-2;
    /** Diode allows very low backward current */
    public static final double REVERSE_RESISTANCE = 1e5;

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

    /** Swap linear model depending on current direction */
    @Override
    public void updateModel(MnaEquationsSystem equations)
    {
        double anodeVoltage = equations.getSolution(anode);
        double cathodeVoltage = equations.getSolution(cathode);
        if (anodeVoltage > cathodeVoltage)
        {
            /** Diode is in forward mode */
            resistance = FORWARD_RESISTANCE;
        }
        else
        {
            /** Diode is in reverse mode */
            resistance = REVERSE_RESISTANCE;
        }
    }
}
