package ilit.cirsim.test.voltagesource;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.LinearEquationsSolver;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.test.AbstractStampTest;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OneVoltageManyResistorsSolutionTest extends AbstractStampTest
{
    private static final int ROUNDING_SCALE = 5;

    private LinearEquationsSolver solver;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
        solver = null;
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * Resistor Left; Resistor Right; Resistor middle;
                 * Source Voltage; Source Evaluated Current;
                 *
                 * RL1,  RL2, Rmid,  RR1,  RR2, Voltage, Current
                 */
                { 100d, 100d,  30d, 100d, 300d,  100d, -0.79032d },
                { 100d, 100d, 100d, 100d, 100d,    5d,    -0.05d },
                {  50d, 100d, 100d, 100d, 100d,    5d, -0.05909d },
                {  50d, 150d,   1d, 100d, 100d,    5d, -0.05353d },
                {  50d, 150d,   1d, 100d, 100d,  500d, -5.35311d }
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double rl1, double rl2, double rm, double rr1, double rr2,
            double voltage, double current
    )
    {
        initEmptyCircuit();

        /** Instantiate all components */
        Resistor resL1 = new Load(rl1);
        Resistor resL2 = new Load(rl2);
        Resistor resMid = new Load(rm);
        Resistor resR1 = new Load(rr1);
        Resistor resR2 = new Load(rr2);
        VoltageSource voltageSource = new VoltageSource(voltage);

        Ground gr = new Ground();
        Node sourceCathode = new Node();
        Node nodeL = new Node();
        Node nodeR = new Node();

        /** Describe topology */
        initComponent(voltageSource, gr, sourceCathode);

        initComponent(resL1, sourceCathode, nodeL);
        initComponent(resR1, sourceCathode, nodeR);

        initComponent(resL2, nodeL, gr);
        initComponent(resR2, nodeR, gr);

        initComponent(resMid, nodeR, nodeL);

        /** Populate equations system */
        placeStamps();


        DenseVector X = equations.getXVector();
        Assert.assertEquals(X.size(), 4);
        Assert.assertEquals(X.get(0), 0d);
        Assert.assertEquals(X.get(1), 0d);
        Assert.assertEquals(X.get(2), 0d);
        Assert.assertEquals(X.get(3), 0d);

        /** Solve */
        solve();

        int vIndex = IdToMatrixIndexRelations.instance.getIndex(voltageSource);
        double sourceCurrent = X.get(vIndex);
        double approxSourceCurrent = Precision.round(sourceCurrent, ROUNDING_SCALE);

        Assert.assertEquals(approxSourceCurrent, current);
    }
}
