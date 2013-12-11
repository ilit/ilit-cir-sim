package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Modified Nodal Analysis matrix population with many element stamps
 */
public class MnaTest extends AbstractStampTest
{
    private static double VOLTAGE = 100.0d;
    private static double RESISTANCE = 100.0d;

    @Test()
    public void dcVoltageTest()
    {
        /**
         * TODO here
         */
        initCircuit();

        Node node = new Node();
        Ground gr = new Ground();

        initComponent(new VoltageSource(VOLTAGE), node, gr);
        initComponent(new Load(RESISTANCE), node, gr);

        placeStamps();

        Assert.assertEquals(matrix.numColumns(), 3);
    }
}
