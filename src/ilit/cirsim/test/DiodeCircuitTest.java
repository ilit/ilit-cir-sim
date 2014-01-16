package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.LinearSolver;
import ilit.cirsim.simulator.NonlinearSolver;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DiodeCircuitTest extends AbstractStampTest
{
    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * RL1, Current, Voltage, ItsCheckCurrent,
                 */
                { 100d, 5d, -0.04d},
                { 100d, 5d, -0.03d},
                { 100d, 5d,  0.01d},
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double R, double V, double checkCurrent
    )
    {
        initEmptyCircuit();

        /** Instantiate all components */
        Resistor resistor = new Load(R);
        Diode diode = new Diode();
        VoltageSource voltageSource = new VoltageSource(V);

        Ground gr = new Ground();
        Node sourcesCathode = new Node();
        /**
         * In a diode, cathode is the negative terminal at the pointed end of the arrow symbol,
         * where current flows out of the device.
         * Note: electrode naming for diodes is always based on the direction of
         * the forward current (that of the arrow, in which the current flows "most easily")
         */
        Node diodeCathode = new Node();

        /**
         * Topology
         *   - +    +▶|-
         * g--V--1---D---2--R--g
         */
        initComponent(voltageSource, gr, sourcesCathode);
        initComponent(diode, sourcesCathode, diodeCathode);
        initComponent(resistor, diodeCathode, gr);

        /** Populate equations system */
        placeStamps();

        /** Solve */
        NonlinearSolver solver = new NonlinearSolver(equations, new LinearSolver(equations));
        solver.solve();

        /** Check */
        int vIndex = IdToMatrixIndexRelations.instance.getIndex(voltageSource);
        DenseVector X = equations.getXVector();
        double vSourceCurrent = X.get(vIndex);
        double approxVSourceCurrent = Precision.round(vSourceCurrent, ROUNDING_SCALE);

        Assert.assertEquals(approxVSourceCurrent, checkCurrent);
    }
}
