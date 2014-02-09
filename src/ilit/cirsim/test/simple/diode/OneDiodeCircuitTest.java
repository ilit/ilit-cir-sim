package ilit.cirsim.test.simple.diode;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.nonlinear.Diode;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.simulator.PiecewiseLinearModeling;
import ilit.cirsim.simulator.SolverWrapper;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OneDiodeCircuitTest extends AbstractSolutionTest
{
    private static final int ROUNDING_SCALE = 3;
    private static final double RESISTANCE = 5;
    private static final double VOLTAGE = 5;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * Check current, Diode is against source
                 */
                { -0.998d, false}, /** Diode does not affect forward current */
                { 0d, true},   /** Diode stops backward current */
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void diodeTest(double checkCurrent, boolean isDiodeAgainstSource)
    {
        initModules();

        if (equations == null)
            throw new Error("equations == null");
        if (solver == null)
            throw new Error("linearSolver == null");
        if (circuit == null)
            throw new Error("circuit == null");

        Assert.assertEquals(0, circuit.getNonlinearComponents().size());
        Assert.assertEquals(0, circuit.getRegularComponents().size());
        Assert.assertEquals(0, circuit.getDynamicComponents().size());

        /** Instantiate all components */
        Resistor resistor = new Load(RESISTANCE);
        Diode diode = new Diode();
        VoltageSource voltageSource = new VoltageSource(VOLTAGE);

        Ground gr = new Ground();
        Node node1 = new Node();
        /**
         * In a diode, cathode is the negative terminal at the pointed end of the arrow symbol,
         * where current flows out of the device.
         * Note: electrode naming for diodes is always based on the direction of
         * the forward current (that of the arrow, in which the current flows "most easily")
         */
        Node node2 = new Node();

        /**
         * Topology
         *   - +    +▶|-
         * g--V--1---D---2--R--g
         */
        initComponent(voltageSource, gr, node1);
        if (isDiodeAgainstSource)
            initComponent(diode, node2, node1); /** Second test: Block */
        else
            initComponent(diode, node1, node2); /** First test: Normal flow */
        initComponent(resistor, node2, gr);

        Assert.assertEquals(1, circuit.getNonlinearComponents().size());
        Assert.assertEquals(2, circuit.getRegularComponents().size());

        /** Instantiate solver */
        SolverWrapper solver = new SolverWrapper(circuit, equations);
        solver.prepareSystem();

        Assert.assertEquals(3, equations.getXVector().size());
        Assert.assertEquals(3, equations.getSideVector().size());
        Assert.assertEquals(3, equations.getMatrix().numColumns());

        /** Solve */
        solver.solve();

        /** Check */
        double vSourceCurrent = equations.getSolution(voltageSource);
        double approxVSourceCurrent = Precision.round(vSourceCurrent, ROUNDING_SCALE);

        Assert.assertEquals(approxVSourceCurrent, checkCurrent);
    }
}
