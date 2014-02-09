package ilit.cirsim.test.plotting;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.sources.AlternatingVoltageSource;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;

public class AlternatingVoltageSourcePlot extends AbstractSolutionTest
{
    private static final double TIME_STEP = 6e-4;
    private static final double FREQUENCY = 40; /** 40Hz */
    private static final double PEAK_VOLTAGE = 240;
    private static final double RESISTANCE = 100;
    private static final int NUMBER_OF_STEPS = 100;

    private static final String WINDOWS_TITLE = "AC Voltage Source plot";
    private static final String LINE_LEGEND = "Voltage source current";
    private static final String GRAPH_TITLE = "Current is sinusoidal as the voltage";
    private static final String X_TITLE = "Time";
    private static final String Y_TITLE = "Current";

    private final Plot plot = new Plot(WINDOWS_TITLE, LINE_LEGEND, GRAPH_TITLE, X_TITLE, Y_TITLE);

    public static void main(String[] args)
    {
        AlternatingVoltageSourcePlot acVoltagePlot = new AlternatingVoltageSourcePlot();
        acVoltagePlot.acVTest();
    }

    void acVTest(
    )
    {
        initModules();

        /** Instantiate all components */
        Resistor resistor = new Load(RESISTANCE);
        AlternatingVoltageSource voltageSource =
                new AlternatingVoltageSource(PEAK_VOLTAGE, FREQUENCY);

        Ground gr = new Ground();
        Node node1 = new Node();

        /**
         * Topology:
         *     -   +
         * g---(Vdc)--1--R---g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(resistor, node1, gr);

        equations.prepareSystemSize();

        solve(0); /** Start point with time = 0 */
        plot.add(0, -getApproxVSourceCurrent(voltageSource));
        for (int step = 1; step < NUMBER_OF_STEPS; step++)
        {
            solve(TIME_STEP);
            plot.add(step, -getApproxVSourceCurrent(voltageSource));
        }
        plot.start();
    }

    private double getApproxVSourceCurrent(VoltageSource voltageSource)
    {
        double vSourceCurrent = equations.getSolution(voltageSource);
        return Precision.round(vSourceCurrent, ROUNDING_SCALE);
    }
}
