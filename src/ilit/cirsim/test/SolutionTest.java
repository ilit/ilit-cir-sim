package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.EquationsSolver;
import no.uib.cipr.matrix.DenseVector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 */
public class SolutionTest extends AbstractStampTest
{
    private static double VOLTAGE = 100.0d;

    private EquationsSolver solver;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
        solver = null;
    }

    @Test()
    public void groundedDcVoltageAndResistorTest()
    {
        initCircuit();

        /** Instantiate all components */
        Resistor resL1 = new Load(100);
        Resistor resL2 = new Load(110);
        Resistor resMid = new Load(30);
        Resistor resR1 = new Load(130);
        Resistor resR2 = new Load(140);

        Ground gr = new Ground();
        Node vPlus = new Node();
        Node nodeL = new Node();
        Node nodeR = new Node();

        /** Describe topology */
        initComponent(new VoltageSource(VOLTAGE), gr, vPlus);

        initComponent(resL1, vPlus, nodeL);
        initComponent(resR1, vPlus, nodeR);

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
        solver = new EquationsSolver(equations);
        solver.solve();

        /*
        Assert.assertEquals(X.get(0), 0d);
        Assert.assertEquals(X.get(1), 0d);
        Assert.assertEquals(X.get(2), 0d);
        Assert.assertEquals(X.get(3), 0d);
        */
    }
}
