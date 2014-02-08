package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IDynamic;
import ilit.cirsim.simulator.MnaEquationsSystem;

/** Voltage source is companion model for Capacitor using Forward Euler method */
public class Capacitor extends VoltageSource implements IDynamic
{
    private static final double INITIAL_VOLTAGE = 0;

    private final double capacitance;

    public Capacitor(double capacitance)
    {
        super(INITIAL_VOLTAGE);

        this.capacitance = capacitance;
    }

    public boolean isDynamic()
    {
        return true;
    }

    public void updateCompanionModel(MnaEquationsSystem equations, double timeStep)
    {
        double anodeVoltage = equations.getSolution(anode);
        double cathodeVoltage = equations.getSolution(cathode);
        double lastVoltageDrop = cathodeVoltage - anodeVoltage;

        double lastCurrent = equations.getSolution(this);

        /**
         * Calculate voltage for next step
         * using Forward Euler method.
         */
        voltage = lastVoltageDrop + (timeStep / capacitance) * lastCurrent;
    }
}
