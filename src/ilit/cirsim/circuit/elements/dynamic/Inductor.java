package ilit.cirsim.circuit.elements.dynamic;

import ilit.cirsim.circuit.elements.base.IDynamic;
import ilit.cirsim.circuit.elements.sources.CurrentSource;
import ilit.cirsim.simulator.MnaEquationsSystem;

/** Current source is companion model for Inductor using Forward Euler method */
public class Inductor extends CurrentSource implements IDynamic
{
    private static final double INITIAL_CURRENT = 0;

    private final double inductance;

    public Inductor(double inductance)
    {
        super(INITIAL_CURRENT);

        this.inductance = inductance;
    }

    public boolean isDynamic()
    {
        return true;
    }

    public void updateCompanionModel(MnaEquationsSystem equations, double timeStep)
    {
        double lastVoltageDrop = equations.getSolutionVoltageDrop(this);

        /**
         * Calculate current for next step
         * using Forward Euler method.
         */
        current = current + (timeStep / inductance) * lastVoltageDrop;
    }
}
