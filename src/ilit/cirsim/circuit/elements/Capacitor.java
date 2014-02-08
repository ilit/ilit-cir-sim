package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IDynamic;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.TransientAnalysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
        double lastVDiff = cathodeVoltage - anodeVoltage;

        double lastCurrent = equations.getSolution(this);

        /**
         * Calculate voltage for next step solving
         * using Forward Euler method.
         */
        double increment = (timeStep / capacitance) * lastCurrent;
        voltage = lastVDiff + increment;
    }
}
