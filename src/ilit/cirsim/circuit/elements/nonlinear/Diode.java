package ilit.cirsim.circuit.elements.nonlinear;

import ilit.cirsim.circuit.elements.base.Piecewise;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.MnaEquationsSystem;

/**
 * Diode model is piecewise linear model consisting of two linear segments.
 * Segment chosen based on node voltage difference.
 */
public class Diode extends Resistor implements Piecewise
{
    /** Diode allows very high forward current */
    private static final double FORWARD_RESISTANCE = 1e-2;
    /** Diode allows very low backward current */
    private static final double REVERSE_RESISTANCE = 1e5;

    private boolean isModelDefined = false;
    private static final boolean FORWARD_MODE = true;
    private static final boolean REVERSE_MODE = false;
    private boolean previousMode;
    private boolean currentMode;

    public Diode()
    {
        super(FORWARD_RESISTANCE);
    }

    public boolean isNonlinear()
    {
        return true;
    }

    /** Switch linear model to probing model to define initial direction*/
    @Override
    public void setProbeModel()
    {
        resistance = FORWARD_RESISTANCE;
        previousMode = FORWARD_MODE;
        currentMode = FORWARD_MODE;
    }

    /** Swap linear model depending on current direction */
    @Override
    public boolean updateModel(MnaEquationsSystem equations)
    {
        boolean modelChanged;

        double anodeVoltage = equations.getSolution(anode);
        double cathodeVoltage = equations.getSolution(cathode);
        if (anodeVoltage > cathodeVoltage)
        {
            /** Diode is in forward(permitting) mode */
            resistance = FORWARD_RESISTANCE;
            currentMode = FORWARD_MODE;
            modelChanged = previousMode != currentMode;
        }
        else
        {
            /** Diode is in reverse(blocking) mode */
            resistance = REVERSE_RESISTANCE;
            currentMode = REVERSE_MODE;
            modelChanged = previousMode != currentMode;
        }
        previousMode = currentMode;
        isModelDefined = true;

        return modelChanged;
    }

    @Override
    public boolean isModelDefined()
    {
        return isModelDefined;
    }
}
