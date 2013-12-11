package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.simulator.EquationsSolver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 */
public class SolutionTest extends AbstractStampPlacerTest
{
    private static double VOLTAGE = 100.0d;
    private static double RESISTANCE = 100.0d;

    private EquationsSolver solver;

    @BeforeMethod
    public void setUp() throws Exception
    {
        super.setUp();
        solver = new EquationsSolver()
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test()
    public void dcVoltageTest()
    {
        initCircuit();

        Node node = new Node();
        Ground gr = new Ground();

        initComponent(new VoltageSource(VOLTAGE), node, gr);
        initComponent(new Load(RESISTANCE), node, gr);

        initAnalysis();
    }
}
