package ilit.cirsim.test.simple.currentsource;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.sources.CurrentSource;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TwoCurrentsManyResistorsSolutionTest extends AbstractSolutionTest
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
                 * Resistor Left; Resistor Right; Resistor middle; Resistor grounded
                 * Current Voltage Left Right and Middle
                 *
                 *  Ra,   Rb,   Rc,   Rd    Re  CL, CM, Node5V
                 */
                { 100d, 100d, 100d, 100d, 100d, -1d, -1d,  100d},
                { 100d, 100d, 100d, 100d, 100d, -1d, -2d,  100d},
                {1000d,1000d, 100d, 100d, 100d, -1d, -2d,  100d},
                {1000d,1000d, 100d, 100d,1000d, -1d, -2d, 1000d},
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double ra, double rb, double rc, double rd, double re,
            double leftCurrent, double middleCurrent,
            double node5voltageCheck
    )
    {
        initModules();

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

        equations.prepareSystemSize();

        /** Solve */
        solve();

        double node5voltage = Precision.round(equations.getSolutionNodeVoltage(node5), ROUNDING_SCALE);

        Assert.assertEquals(node5voltage, node5voltageCheck);
    }
}
