package ilit.cirsim.test.currentsource;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.EquationsSolver;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.test.AbstractStampTest;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TwoCurrentsManyResistorsSolutionTest extends AbstractStampTest
{
    private static int ROUNDING_SCALE = 5;

    private EquationsSolver solver;

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
                 * Resistor Left; Resistor Right; Resistor middle; Resistor grounded
                 * Current Voltage Left Right and Middle
                 *
                 *  Ra,   Rb,   Rc,   Rd    Re  CL, CM, Node5V
                 */
                { 100d, 100d, 100d, 100d, 100d, 1d, 1d,  100d},
                { 100d, 100d, 100d, 100d, 100d, 1d, 2d,  100d},
                {1000d,1000d, 100d, 100d, 100d, 1d, 2d,  100d},
                {1000d,1000d, 100d, 100d,1000d, 1d, 2d, 1000d},
        };
    }

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double ra, double rb, double rc, double rd, double re,
            double leftCurrent, double middleCurrent,
            double node5voltageCheck
    )
    {
        initCircuit();

        /** Instantiate all components */
        Resistor Ra = new Load(ra);
        Resistor Rb = new Load(rb);
        Resistor Rc = new Load(rc);
        Resistor Rd = new Load(rd);
        Resistor Re = new Load(re);
        CurrentSource leftISource = new CurrentSource(leftCurrent);
        CurrentSource midISource = new CurrentSource(middleCurrent);

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        Node node5 = new Node();
        Ground g = new Ground();

        /** Describe topology
         *
         *   3<(I)-4
         *   |     |
         *   Rb    Rc
         *   |     |
         *   2--Rd-5--Re-g
         *   |
         *   Ra
         *   1
         *  (I)
         *
         */

        initComponent(leftISource, g, node1);
        initComponent(midISource, node4, node3);

        initComponent(Ra, node1, node2);
        initComponent(Rb, node2, node3);
        initComponent(Rc, node4, node5);
        initComponent(Rd, node2, node5);
        initComponent(Re, node5, g);

        /** Populate equations system */
        placeStamps();

        /** Solve */
        solver = new EquationsSolver(equations);
        solver.solve();

        int node5index = IdToMatrixIndexRelations.instance.getIndex(node5);
        DenseVector X = equations.getXVector();
        double node5voltage = Precision.round(X.get(node5index), ROUNDING_SCALE);

        Assert.assertEquals(node5voltage, node5voltageCheck);
    }
}
