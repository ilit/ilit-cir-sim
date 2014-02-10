package ilit.cirsim.test.plotting;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.dynamic.Capacitor;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;

public class CapacitorPlot extends AbstractSolutionTest
{
    private static final double TIME_STEP = 5e-5;
    private static final double CAPACITANCE = 1e-5; /** 10 micro Farads */
    private static final double SOURCE_VOLTAGE = 5;
    private static final double RESISTANCE = 100;
    private static final int NUMBER_OF_STEPS = 100;

    private static final String WINDOWS_TITLE = "Capacitor plot";
    private static final String LINE_LEGEND = "Voltage source current";
    private static final String GRAPH_TITLE = "Current decreases while capacitor charges";
    private static final String X_TITLE = "Time";
    private static final String Y_TITLE = "Current";

    private final Plot plot = new Plot(WINDOWS_TITLE, LINE_LEGEND, GRAPH_TITLE, X_TITLE, Y_TITLE);

    public static void main(String[] args)
    {
        CapacitorPlot capacitorPlot = new CapacitorPlot();
        capacitorPlot.capTest();
    }

    void capTest()
    {
        initModules();

        /** Instantiate all components */
        Resistor resistor = new Load(RESISTANCE);
        VoltageSource voltageSource = new VoltageSource(SOURCE_VOLTAGE);
        Capacitor capacitor = new Capacitor(CAPACITANCE);

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();

        /**
         * Topology:
         *     -   +    + -
         * g---(Vdc)--1--R--2--C---g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(resistor, node1, node2);
        initComponent(capacitor, node2, gr);

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
