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

import java.util.ArrayList;

public class ThreeVoltagesManyResistorsSolutionTest extends AbstractSolutionTest
{
    private static final double RESISTANCE = 100.0d;

    private ArrayList<Resistor> resistors;
    private ArrayList<Node> nodes;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
        resistors = null;
        nodes = null;
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * Source Voltage Left Right and Middle; Source Evaluated Currents;
                 *
                 *  V1, V2, V3,     I1,        I2,      I3      node 8 voltage
                 */
                {   5d, 5d, 5d, -0.07809d, -0.05216d, -0.04259d, 1.35802d},
                {   5d, 6d, 7d, -0.08786d, -0.05907d, -0.05338d, 1.10957d},
        };
    }

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double v1val, double v2val, double v3val,
            double i1, double i2, double i3,
            double n8v
            )
    {
        initModules();

        /** Instantiate all components */
        /** Create resistors */
        resistors = new ArrayList<>();
        for (int i = 0x0; i <= 0xe; i++)
            resistors.add(new Load(RESISTANCE));

        /** Create sources */
        VoltageSource v1 = new VoltageSource(v1val);
        VoltageSource v2 = new VoltageSource(v2val);
        VoltageSource v3 = new VoltageSource(v3val);

        /** Create nodes */
        nodes = new ArrayList<>();
        Ground g = new Ground();
        nodes.add(g); /** node[0] is ground */
        for (int i = 0x0; i <= 0xe; i++)
            nodes.add(new Node());

        /** Describe topology
         *
         *         (V)
         *          |+
         *   1--R0--2--R1--3
         *   |      |      |
         *   R2     R3     R4
         *   |      |      |
         *   4--R5--5--R6--6
         *   |      |      |
         *  (V)     R7     R8
         *  +|      |      |
         *   7--R9--8--Ra--9
         *   |      |      |
         *   Rb     Rc    (V)
         *   |      |      |+
         *  10--Rd--g--Re--11
         *
         */

        initComponent(v1, g, nodes.get(2));
        initComponent(v2, nodes.get(4), nodes.get(7));
        initComponent(v3, nodes.get(9), nodes.get(11));

        initResistor(0x0, 1, 2); initResistor(0x1, 2, 3);
        initResistor(0x2, 1, 4); initResistor(0x3, 2, 5); initResistor(0x4, 3, 6);
        initResistor(0x5, 4, 5); initResistor(0x6, 5, 6);
        initResistor(0x7, 5, 8); initResistor(0x8, 6, 9);
        initResistor(0x9, 7, 8); initResistor(0xa, 8, 9);
        initResistor(0xb, 7,10); initResistor(0xc, 8, 0);
        initResistor(0xd,10, 0); initResistor(0xe, 11,0);

        equations.prepareSystemSize();

        /** Solve */
        solve();

        double sourceCurrent1 = Precision.round(equations.getSolution(v1), ROUNDING_SCALE);
        double sourceCurrent2 = Precision.round(equations.getSolution(v2), ROUNDING_SCALE);
        double sourceCurrent3 = Precision.round(equations.getSolution(v3), ROUNDING_SCALE);

        Assert.assertEquals(sourceCurrent1, i1);
        Assert.assertEquals(sourceCurrent2, i2);
        Assert.assertEquals(sourceCurrent3, i3);

        double node8Voltage = Precision.round(equations.getSolution(nodes.get(8)), ROUNDING_SCALE);

        Assert.assertEquals(node8Voltage, n8v);
    }

    private void initResistor(int res, int n1, int n2)
    {
        initComponent(resistors.get(res), nodes.get(n1), nodes.get(n2));
    }
}
