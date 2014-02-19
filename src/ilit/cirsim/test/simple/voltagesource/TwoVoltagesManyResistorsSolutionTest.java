package ilit.cirsim.test.simple.voltagesource;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TwoVoltagesManyResistorsSolutionTest extends AbstractSolutionTest
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
                 * Source Voltage Left Right and Middle; Source Evaluated Currents;
                 *
                 *  Ra,   Rb,   Rc,   Rd    Re  VL, VM,  CurrentL, CurrentM
                 */
                { 100d, 100d, 100d, 100d, 100d, 5d, 5d,  -0.0125d, -0.0125d},
                { 100d, 100d, 900d, 100d, 100d, 5d, 5d,  -0.01562d, -0.00313d},
                { 100d, 100d, 900d, 100d, 100d, 5d, 25d, -0.00938d, -0.02188d}
        };
    }

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double ra, double rb, double rc, double rd, double re,
            double vl, double vm,
            double cl, double cm
    )
    {
        initModules();

        /** Instantiate all components */
        Resistor Ra = new Load(ra);
        Resistor Rb = new Load(rb);
        Resistor Rc = new Load(rc);
        Resistor Rd = new Load(rd);
        Resistor Re = new Load(re);
        VoltageSource VSL = new VoltageSource(vl);
        VoltageSource VSM = new VoltageSource(vm);

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();
        Node node4 = new Node();
        Node node5 = new Node();
        Ground g = new Ground();

        /** Describe topology
         *    +
         *   3-(V)-4
         *   |     |
         *   Rb    Rc
         *   |     |
         *   2--Rd-5--Re-g
         *   |
         *   Ra
         *   1 +
         *  (V)
         *
         */

        initComponent(VSL, g, node1);
        initComponent(VSM, node4, node3);

        initComponent(Ra, node1, node2);
        initComponent(Rb, node2, node3);
        initComponent(Rc, node4, node5);
        initComponent(Rd, node2, node5);
        initComponent(Re, node5, g);

        equations.prepareSystemSize();

        /** Solve */
        solve();

        double sourceCurrentLeft  = Precision.round(equations.getSolutionCurrent(VSL), ROUNDING_SCALE);
        double sourceCurrentMid   = Precision.round(equations.getSolutionCurrent(VSM), ROUNDING_SCALE);

        Assert.assertEquals(sourceCurrentLeft, cl);
        Assert.assertEquals(sourceCurrentMid, cm);
    }
}
