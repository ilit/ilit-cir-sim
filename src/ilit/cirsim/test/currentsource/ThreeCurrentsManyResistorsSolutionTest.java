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

import java.util.ArrayList;

public class ThreeCurrentsManyResistorsSolutionTest extends AbstractStampTest
{
    private static int ROUNDING_SCALE = 5;
    private static double RESISTANCE = 100.0d;


    private EquationsSolver solver;
    private ArrayList<Resistor> resistors;
    private ArrayList<Node> nodes;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
        solver = null;
        resistors = null;
        nodes = null;
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * Source Voltage Left Right and Middle; Source Evaluated Currents;
                 *
                 *  I1, I2, I3, node 8 voltage
                 *  TODO
                 */
                //{   1d, 1d, 1d, 1.35802d},
        };
    }

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double i1val, double i2val, double i3val,
            double node8voltage
            )
    {
        initCircuit();

        /** Instantiate all components */
        /** Create resistors */
        resistors = new ArrayList<>();
        for (int i = 0x0; i <= 0xe; i++)
            resistors.add(new Load(RESISTANCE));

        /** Create sources */
        CurrentSource i1 = new CurrentSource(i1val);
        CurrentSource i2 = new CurrentSource(i2val);
        CurrentSource i3 = new CurrentSource(i3val);

        /** Create nodes */
        nodes = new ArrayList<>();
        Ground g = new Ground();
        nodes.add(g); /** node[0] is ground */
        for (int i = 0x0; i <= 0xe; i++)
            nodes.add(new Node());

        /** Describe topology
         *
         *         (I)
         *          |+
         *   1--R0--2--R1--3
         *   |      |      |
         *   R2     R3     R4
         *   |      |      |
         *   4--R5--5--R6--6
         *   |      |      |
         *  (I)     R7     R8
         *  +|      |      |
         *   7--R9--8--Ra--9
         *   |      |      |
         *   Rb     Rc    (I)
         *   |      |      |+
         *  10--Rd--g--Re--11
         *
         */

        initComponent(i1, nodes.get(2), g); /** Directional. Positive is on the left */
        initComponent(i2, nodes.get(7), nodes.get(4));
        initComponent(i3, nodes.get(11), nodes.get(9));

        initResistor(0x0, 1, 2); initResistor(0x1, 2, 3);
        initResistor(0x2, 1, 4); initResistor(0x3, 2, 5); initResistor(0x4, 3, 6);
        initResistor(0x5, 4, 5); initResistor(0x6, 5, 6);
        initResistor(0x7, 5, 8); initResistor(0x8, 6, 9);
        initResistor(0x9, 7, 8); initResistor(0xa, 8, 9);
        initResistor(0xb, 7,10); initResistor(0xc, 8, 0);
        initResistor(0xd,10, 0); initResistor(0xe, 11,0);

        /** Populate equations system */
        placeStamps();

        /** Solve */
        solver = new EquationsSolver(equations);
        solver.solve();

        DenseVector X = equations.getXVector();

        int node8Id = nodes.get(8).getId();
        int node8Index = IdToMatrixIndexRelations.instance.getIndex(node8Id);
        double node8Voltage = Precision.round(X.get(node8Index), ROUNDING_SCALE);

        Assert.assertEquals(node8Voltage, node8voltage);
    }

    private void initResistor(int res, int n1, int n2)
    {
        initComponent(resistors.get(res), nodes.get(n1), nodes.get(n2));
    }
}
