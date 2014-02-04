package ilit.cirsim.test.diode;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.PiecewiseLinearModeling;
import ilit.cirsim.simulator.SolverWrapper;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DiodeGridCircuitTest extends AbstractSolutionTest
{
    private static final int ROUNDING_SCALE = 1;
    private static final double SIDE_RESISTANCE = 100;
    private static final double BOT_MID_RESISTANCE = 50;
    private static final double VOLTAGE = 500;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * Resistance of top mid resistor, node1 voltage, node2 voltage.
                 */
                { 200d, 307.7d, 307.7d }, /** Diodes equalize node 1,2 and 3 voltages */
                {  50d, 250d, 250d },     /** Diodes stop backward current */
                {  20d, 249.9d, 142.9d },
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void diodeTest(double topMidResistance, double checkNode1V, double checkNode2V)
    {
        initModules();

        /** Instantiate all components */
        Diode Da = new Diode();
        Diode Db = new Diode();
        VoltageSource voltageSource = new VoltageSource(VOLTAGE);
        Resistor Ra = new Load(SIDE_RESISTANCE);
        Resistor Rb = new Load(topMidResistance);
        Resistor Rc = new Load(SIDE_RESISTANCE);

        Resistor Rd = new Load(SIDE_RESISTANCE);
        Resistor Re = new Load(BOT_MID_RESISTANCE);
        Resistor Rf = new Load(SIDE_RESISTANCE);

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();

        /**
         * Topology
         *
         * g     g     g
         * |     |     |
         * Ra    Rb    Rc
         * |     |     |
         * 1--Da-2--Db-3
         * |     |     |
         * Rd    Re    Rf
         *  \    |    /
         *   \___4___/
         *       |
         *       V-g
         */
        initComponent(Ra, gr, node1);
        initComponent(Rb, gr, node2);
        initComponent(Rc, gr, node3);

        initComponent(Rd, node1, node4);
        initComponent(Re, node2, node4);
        initComponent(Rf, node3, node4);

        initComponent(Da, node2, node1);
        initComponent(Db, node2, node3);

        Assert.assertEquals(0, IdToMatrixIndexRelations.instance.getIndex(node1));
        Assert.assertEquals(1, IdToMatrixIndexRelations.instance.getIndex(node2));
        Assert.assertEquals(2, IdToMatrixIndexRelations.instance.getIndex(node3));
        Assert.assertEquals(3, IdToMatrixIndexRelations.instance.getIndex(node4));

        initComponent(voltageSource, gr, node4);

        Assert.assertEquals(4, IdToMatrixIndexRelations.instance.getIndex(voltageSource));

        Assert.assertEquals(4, IdToMatrixIndexRelations.instance.getIndex(voltageSource));

        /** Instantiate solver */
        PiecewiseLinearModeling piecewiseLinearModeling =
                new PiecewiseLinearModeling(equations, circuit);
        SolverWrapper solver = new SolverWrapper(
                circuit,
                equations,
                piecewiseLinearModeling);
        solver.prepareSystem();

        int numberOfNodes = 4;
        int numberOfGroupTwoElms = 1;
        int expectedSize = numberOfNodes + numberOfGroupTwoElms;
        Assert.assertEquals(expectedSize, equations.getMatrix().numColumns());

        Assert.assertEquals(2, circuit.getNonlinearComponents().size());
        Assert.assertEquals(7, circuit.getRegularComponents().size());

        /** Solve */
        solver.solve();

        /** Check */
        double node1V = equations.getSolution(node1);
        double node2V = equations.getSolution(node2);
        node1V = Precision.round(node1V, ROUNDING_SCALE);
        node2V = Precision.round(node2V, ROUNDING_SCALE);
        Assert.assertEquals(checkNode1V, node1V);
        Assert.assertEquals(checkNode2V, node2V);
    }
}
