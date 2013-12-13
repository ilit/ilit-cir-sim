package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.EquationsSolver;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 */
public class ManyVoltagesManyResistorsSolutionTest extends AbstractStampTest
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
                 * Source Voltage Left Right and Middle; Source Evaluated Currents;
                 *
                 *  Ra,   Rb,   Rc,   Rd    Re  VL, VM,  CurrentL, CurrentM
                 */
                { 100d, 100d, 100d, 100d, 100d, 5d, 5d,  -0.0125d, -0.0125d},
        };
    }

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double ra, double rb, double rc, double rd, double re,
            double vl, double vm,
            double cl, double cm
    )
    {
        initCircuit();

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
         *
         *   3<(V)-4
         *   |     |
         *   Rb    Rc
         *   |     |
         *   2--Rd-5--Re-g
         *   |
         *   Ra
         *   1
         *  (V)
         *
         */

        initComponent(VSL, node1, g);
        initComponent(VSM, node3, node4); /** Directional. Positive is on the left */

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

        int vlId = VSL.getId();
        int vmId = VSM.getId();
        int vli = IdToMatrixIndexRelations.instance.getIndex(vlId);
        int vmi = IdToMatrixIndexRelations.instance.getIndex(vmId);
        DenseVector X = equations.getXVector();
        double sourceCurrentLeft  = Precision.round(X.get(vli), ROUNDING_SCALE);
        double sourceCurrentMid   = Precision.round(X.get(vmi), ROUNDING_SCALE);

        Assert.assertEquals(sourceCurrentLeft, cl);
        Assert.assertEquals(sourceCurrentMid, cm);
    }
}
