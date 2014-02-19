package ilit.cirsim.test.plotting;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.dynamic.Transformer;
import ilit.cirsim.circuit.elements.sources.AlternatingVoltageSource;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;

public class TransformerPlot extends AbstractSolutionTest
{
    private static final double TIME_STEP = 5e-5;
    private static final int NUMBER_OF_STEPS = 1000;

    private static final double PRIMARY_INDUCTANCE = 100; /** Henry */
    private static final double COIL_RATIO = 0.5;

    private static final double SOURCE_VOLTAGE = 240;     /** Volts */
    private static final double SOURCE_FREQUENCY = 40;    /** Hz */

    private static final double RESISTANCE = 100;         /** Ohm */

    private static final String WINDOWS_TITLE = "Transformer plot";
    private static final String X_TITLE = "Time";

    private static final String SOURCE_LINE_LEGEND = "Current";
    private static final String SOURCE_GRAPH_TITLE = "Voltage source current";
    private static final String SOURCE_Y_TITLE = "Current";

    private static final String COIL_VOLTAGE_LINE_LEGEND = "Voltage";
    private static final String PRIM_COIL_GRAPH_TITLE = "Voltage on primary coil";
    private static final String SEC_COIL_GRAPH_TITLE = "Voltage on secondary coil";
    private static final String COIL_VOLTAGE_Y_TITLE = "Voltage";

    private final Plot currentPlot =
        new Plot(WINDOWS_TITLE,
                SOURCE_LINE_LEGEND,
                SOURCE_GRAPH_TITLE,
                X_TITLE, SOURCE_Y_TITLE);

    private final Plot primaryVoltagePlot =
        new Plot(WINDOWS_TITLE,
                COIL_VOLTAGE_LINE_LEGEND,
                PRIM_COIL_GRAPH_TITLE,
                X_TITLE, COIL_VOLTAGE_Y_TITLE);

    private final Plot secondaryVoltagePlot =
            new Plot(WINDOWS_TITLE,
                    COIL_VOLTAGE_LINE_LEGEND,
                    SEC_COIL_GRAPH_TITLE,
                    X_TITLE, COIL_VOLTAGE_Y_TITLE);

    public static void main(String[] args)
    {
        TransformerPlot transformerPlot = new TransformerPlot();
        transformerPlot.capTest();
    }

    void capTest()
    {
        initModules();

        /** Instantiate all components */
        Resistor Ra = new Load(RESISTANCE);
        Resistor Rb = new Load(RESISTANCE);
        AlternatingVoltageSource voltageSource =
                new AlternatingVoltageSource(SOURCE_VOLTAGE, SOURCE_FREQUENCY);
        Transformer transformer = new Transformer(PRIMARY_INDUCTANCE, COIL_RATIO);

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();

        /**
         * Topology:
         *
         *  1--Ra--2--$|$--3--Rb
         *  |         $|$      |
         * Vac        $|$      g
         *  |      g--$|$--g
         *  g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(Ra, node1, node2);
        initComponent(Rb, node3, gr);
        initTransformer(transformer, node2, node3, gr, gr);

        equations.prepareSystemSize();


        solve(0); /** Start point with time = 0 */
        currentPlot.add(0, -getApproxVSourceCurrent(voltageSource));
        primaryVoltagePlot.add(0, equations.getSolutionVoltageDrop(transformer.primaryCoil));
        secondaryVoltagePlot.add(0, equations.getSolutionVoltageDrop(transformer.secondaryCoil));

        for (int step = 1; step < NUMBER_OF_STEPS; step++)
        {
            solve(TIME_STEP);
            currentPlot.add(step, -getApproxVSourceCurrent(voltageSource));
            primaryVoltagePlot.add(step, equations.getSolutionVoltageDrop(transformer.primaryCoil));
            secondaryVoltagePlot.add(step, equations.getSolutionVoltageDrop(transformer.secondaryCoil));
        }
        currentPlot.start();
        primaryVoltagePlot.start();
        secondaryVoltagePlot.start();
    }

    private double getApproxVSourceCurrent(VoltageSource voltageSource)
    {
        double vSourceCurrent = equations.getSolutionCurrent(voltageSource);
        return Precision.round(vSourceCurrent, ROUNDING_SCALE);
    }
}
