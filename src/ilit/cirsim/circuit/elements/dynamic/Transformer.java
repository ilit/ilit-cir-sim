package ilit.cirsim.circuit.elements.dynamic;

import ilit.cirsim.circuit.elements.base.IDynamic;
import ilit.cirsim.circuit.elements.sources.CurrentSource;
import ilit.cirsim.simulator.MnaEquationsSystem;

/**
 * Composite component.
 * Consists of two current sources.
 */
public class Transformer implements IDynamic
{
    public CurrentSource primaryCoil;
    public CurrentSource secondaryCoil;

    private static final double COUPLING_COEFFICIENT = 0.999;

    private double primaryInductance;
    private double coilRatio;

    public Transformer(double primaryInductance, double coilRatio)
    {
        this.primaryInductance = primaryInductance;
        this.coilRatio = coilRatio;

        primaryCoil = new CurrentSource(0);
        secondaryCoil = new CurrentSource(0);
    }

    public void updateCompanionModel(MnaEquationsSystem equations, double timeStep)
    {
        double primaryVoltage   = equations.getSolutionVoltageDrop(primaryCoil);
        double secondaryVoltage = equations.getSolutionVoltageDrop(secondaryCoil);

        double l1 = primaryInductance;
        double l2 = primaryInductance * coilRatio * coilRatio;
        double m = COUPLING_COEFFICIENT * Math.sqrt(l1 * l2);

        double deti = 1 / (l1 * l2 - m * m);

        double a1 = l2 * deti;
        double a2 = -m * deti;
        double a3 = -m * deti;
        double a4 = l1 * deti;

        // TODO something is a little bit wrong. Coil ratio works only in one direction
        primaryCoil.current   += a1 * timeStep * primaryVoltage + a2 * timeStep * secondaryVoltage;
        secondaryCoil.current += a3 * timeStep * primaryVoltage + a4 * timeStep * secondaryVoltage;
    }
}
