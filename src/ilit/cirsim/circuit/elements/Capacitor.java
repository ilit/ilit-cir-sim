package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IDynamic;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.TransientAnalysis;

public class Capacitor extends VoltageSource implements IDynamic
{
    private static final double INITIAL_VOLTAGE = 0;

    private final double capacitance;

    protected Capacitor(double capacitance)
    {
        super(INITIAL_VOLTAGE);

        this.capacitance = capacitance;
    }

    public double getCapacitance()
    {
        return capacitance;
    }

    public boolean isDynamic()
    {
        return true;
    }

    public void updateCompanionModel(MnaEquationsSystem equations)
    {
        double anodeVoltage = equations.getSolution(anode);
        double cathodeVoltage = equations.getSolution(cathode);
        double lastVDiff = anodeVoltage - cathodeVoltage;

        double lastCurrent = equations.getSolution(this);

        voltage = lastVDiff + (TransientAnalysis.TIME_STEP / C(lastVDiff)) * lastCurrent;
    }
}
