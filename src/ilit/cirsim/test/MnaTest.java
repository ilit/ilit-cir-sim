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
    public void groundedDcVoltageAndResistorTest()
    {
        initCircuit();

        Node node = new Node();
        Ground gr = new Ground();

        initComponent(new VoltageSource(VOLTAGE), node, gr);
        initComponent(new Load(RESISTANCE), node, gr);

        placeStamps();

        Assert.assertEquals(matrix.numColumns(), 2);
        Assert.assertEquals(sideVector.size(), 2);

        Assert.assertEquals(sideVector.get(0), 0d);
        Assert.assertEquals(sideVector.get(1), VOLTAGE);

        Assert.assertEquals(matrix.get(0, 0), 1 / RESISTANCE);
        Assert.assertEquals(matrix.get(1, 0), 1d);
        Assert.assertEquals(matrix.get(0, 1), 1d);
        Assert.assertEquals(matrix.get(1, 1), 0d);

        /**
         * 0.01 1 |   0
         *    1 0 | 100
         */
    }
}
