package ilit.cirsim.test.stamp;

import org.testng.Assert;
import org.testng.annotations.Test;
import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;

public class VoltageSourceStampTest extends AbstractSingleStampTest
{
    private static int CURRENT_INDEX = 0;
    private static int CATHODE_INDEX = 1; /** plus */
    private static int ANODE_INDEX = 2; /** minus */
    private static double VOLTAGE = 100.0d;

    @Test()
    public void dcVoltageTest()
    {
        initComponent(new VoltageSource(VOLTAGE), new Node(), new Node());

        Assert.assertEquals(matrix.numColumns(), 3);

        Assert.assertEquals(sideVector.get(CURRENT_INDEX), VOLTAGE);

        Assert.assertEquals(matrix.get(CATHODE_INDEX, CATHODE_INDEX), 0d);
        Assert.assertEquals(matrix.get(CATHODE_INDEX, ANODE_INDEX), 0d);
        Assert.assertEquals(matrix.get(ANODE_INDEX, CATHODE_INDEX), 0d);
        Assert.assertEquals(matrix.get(ANODE_INDEX, ANODE_INDEX), 0d);

        Assert.assertEquals(matrix.get(CURRENT_INDEX, CATHODE_INDEX), 1.0d);
        Assert.assertEquals(matrix.get(CATHODE_INDEX, CURRENT_INDEX), 1.0d);

        Assert.assertEquals(matrix.get(CURRENT_INDEX, ANODE_INDEX), -1.0d);
        Assert.assertEquals(matrix.get(ANODE_INDEX, CURRENT_INDEX), -1.0d);
    }

    @Test()
    public void groundedDcVoltageTest()
    {
        initComponent(new VoltageSource(VOLTAGE), new Ground(), new Node());

        Assert.assertEquals(matrix.numColumns(), 2);

        Assert.assertEquals(sideVector.get(CURRENT_INDEX), VOLTAGE);

        /** Untouched */
        Assert.assertEquals(matrix.get(CATHODE_INDEX, CATHODE_INDEX), 0d);

        Assert.assertEquals(matrix.get(CURRENT_INDEX, CATHODE_INDEX), 1.0d);
        Assert.assertEquals(matrix.get(CATHODE_INDEX, CURRENT_INDEX), 1.0d);
    }

    @Test()
    public void groundedDcVoltageTest2()
    {
        initComponent(new VoltageSource(VOLTAGE), new Node(), new Ground());

        Assert.assertEquals(matrix.numColumns(), 2);

        Assert.assertEquals(sideVector.get(CURRENT_INDEX), VOLTAGE);

        /** Untouched */
        Assert.assertEquals(matrix.get(CATHODE_INDEX, CATHODE_INDEX), 0d);

        Assert.assertEquals(matrix.get(CURRENT_INDEX, CATHODE_INDEX), -1.0d);
        Assert.assertEquals(matrix.get(CATHODE_INDEX, CURRENT_INDEX), -1.0d);
    }
}
