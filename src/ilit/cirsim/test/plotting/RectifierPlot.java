package ilit.cirsim.test.plotting;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.dynamic.Capacitor;
import ilit.cirsim.circuit.elements.nonlinear.Diode;
import ilit.cirsim.circuit.elements.sources.AlternatingVoltageSource;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;

public class RectifierPlot extends AbstractSolutionTest
{
    private static final double TIME_STEP = 1e-5;
    private static final double CAPACITANCE = 2e-3;    /** 2mF */
    private static final double SOURCE_VOLTAGE = 5;    /** 5V */
    private static final double SOURCE_FREQUENCY = 40; /** 40Hz */
    private static final double RESISTANCE = 430;      /** 430Ohm */
    private static final int NUMBER_OF_STEPS = 10000;

    private static final String SRC_WINDOWS_TITLE = "Rectifier plot";
    private static final String SRC_LINE_LEGEND = "Voltage source current";
    private static final String SRC_GRAPH_TITLE = "Current spikes when capacitor charges";
    private static final String SRC_Y_TITLE = "Current";
    private static final String SRC_X_TITLE = "Time";
    private final Plot sourceCurrentPlot = new Plot(SRC_WINDOWS_TITLE, SRC_LINE_LEGEND, SRC_GRAPH_TITLE, SRC_X_TITLE, SRC_Y_TITLE);

    private static final String LOAD_WINDOWS_TITLE = "Rectifier plot";
    private static final String LOAD_LINE_LEGEND = "Load voltage";
    private static final String LOAD_GRAPH_TITLE = "Voltage is almost stable direct";
    private static final String LOAD_Y_TITLE = "Voltage";
    private static final String LOAD_X_TITLE = "Time";
    private final Plot loadVoltagePlot = new Plot(LOAD_WINDOWS_TITLE, LOAD_LINE_LEGEND, LOAD_GRAPH_TITLE, LOAD_X_TITLE, LOAD_Y_TITLE);

    public static void main(String[] args)
    {
        RectifierPlot rectifierPlot = new RectifierPlot();
        rectifierPlot.recTest();
    }

    void recTest()
    {
        initModules();

        /** Instantiate all components */
        AlternatingVoltageSource voltageSource = new AlternatingVoltageSource(SOURCE_VOLTAGE, SOURCE_FREQUENCY);
        Capacitor capacitor = new Capacitor(CAPACITANCE);
        Resistor resistor = new Load(RESISTANCE);
        Diode diodeA = new Diode();
        Diode diodeB = new Diode();
        Diode diodeC = new Diode();
        Diode diodeD = new Diode();

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();

        /**
         * Topology:
         *
         *  ---------1
         *  |      /   \
         *  |    ^Da   vDb
         *  |     |     |
         *  |+    2--C--3
         * (V)AC  2--R--3
         *  |     |     |
         *  g    vDc   ^Dd
         *         \   /
         *           g
         */
        initComponent(voltageSource, gr, node1);

        initComponent(resistor, node2, node3);
        initComponent(capacitor, node2, node3);

        initComponent(diodeA, node2, node1);
        initComponent(diodeB, node1, node3);
        initComponent(diodeC, node2, gr);
        initComponent(diodeD, gr, node3);

        equations.prepareSystemSize();

        solve(0); /** Start point with time = 0 */
        sourceCurrentPlot.add(0, -getApproxVSourceCurrent(voltageSource));
        loadVoltagePlot.add(0, getLoadVoltage(resistor));
        for (int step = 1; step < NUMBER_OF_STEPS; step++)
        {
            solve(TIME_STEP);
            sourceCurrentPlot.add(step, -getApproxVSourceCurrent(voltageSource));
            loadVoltagePlot.add(step, getLoadVoltage(resistor));
        }
        sourceCurrentPlot.start();
        loadVoltagePlot.start();
    }

    private double getLoadVoltage(Resistor resistor)
    {
        double cathodeVoltage = equations.getSolution(resistor.cathode);
        double anodeVoltage = equations.getSolution(resistor.anode);
        return Precision.round(cathodeVoltage - anodeVoltage, ROUNDING_SCALE);
    }

    private double getApproxVSourceCurrent(VoltageSource voltageSource)
    {
        double vSourceCurrent = equations.getSolution(voltageSource);
        return Precision.round(vSourceCurrent, ROUNDING_SCALE);
    }
}
