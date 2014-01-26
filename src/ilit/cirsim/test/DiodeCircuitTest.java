package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.*;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DiodeCircuitTest extends AbstractSolutionTest
{
    protected static final int ROUNDING_SCALE = 3;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * RL1, Current, Voltage, ItsCheckCurrent, Diode is against source
                 */
                { 5d, 5d,  -1d, false}, /** Diode does not affect forward current */
                { 5d, 5d,  0d, true},   /** Diode stops backward current */
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void diodeTest(double R, double V, double checkCurrent, boolean isDiodeAgaintSource)
    {
        initModules();

        if (equations == null)
            throw new Error("equations == null");
        if (linearSolver == null)
            throw new Error("linearSolver == null");
        if (stampInjector == null)
            throw new Error("stampInjector == null");
        if (circuit == null)
            throw new Error("circuit == null");

        Assert.assertEquals(0, circuit.getG1LinearComponents().size());
        Assert.assertEquals(0, circuit.getG1NonlinearComponents().size());
        Assert.assertEquals(0, circuit.getG2LinearComponents().size());

        /** Instantiate all components */
        Resistor resistor = new Load(R);
        Diode diode = new Diode();
        VoltageSource voltageSource = new VoltageSource(V);

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
        if (isDiodeAgaintSource)
            initComponent(diode, node2, node1);
        else
            initComponent(diode, node1, node2);
        initComponent(resistor, node2, gr);

        Assert.assertEquals(1, circuit.getG1NonlinearComponents().size());
        Assert.assertEquals(1, circuit.getG1LinearComponents().size());
        Assert.assertEquals(1, circuit.getG2LinearComponents().size());

        /** Instantiate solver */
        PiecewiseLinearSolver piecewiseLinearSolver =
                new PiecewiseLinearSolver(equations, circuit, stampInjector);
        SolverFacade solver = new SolverFacade(
                linearSolver,
                piecewiseLinearSolver,
                circuit,
                stampInjector,
                equations);
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
