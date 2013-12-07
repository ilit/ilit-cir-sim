package ilit.cirsim.test;

import org.testng.annotations.Test;

/**
 * Test Modified Nodal Analysis matrix population with many element stamps
 */
public class MnaTest extends AbstractAnalysisTest
{
    private static double VOLTAGE = 100.0d;
    private static double RESISTANCE = 100.0d;

    @Test()
    public void dcVoltageTest()
    {
        /**
         * TODO here
         */

        /*
        initCircuit();

        Node node = new Node();
        Ground gr = new Ground();

        initComponent(new VoltageSource(VOLTAGE), node, gr);
        initComponent(new Load(RESISTANCE), node, gr);

        initAnalysis();

        initComponent(new VoltageSource(VOLTAGE), new Node(), new Node());

        Assert.assertEquals(matrix.numColumns(), 3);

        Assert.assertEquals(sideVector.get(CURRENT_INDEX), VOLTAGE);

        Assert.assertEquals(matrix.get(NODE1_INDEX, NODE1_INDEX), 0d);
        Assert.assertEquals(matrix.get(NODE1_INDEX, NODE2_INDEX), 0d);
        Assert.assertEquals(matrix.get(NODE2_INDEX, NODE1_INDEX), 0d);
        Assert.assertEquals(matrix.get(NODE2_INDEX, NODE2_INDEX), 0d);

        Assert.assertEquals(matrix.get(CURRENT_INDEX, NODE1_INDEX), 1.0d);
        Assert.assertEquals(matrix.get(NODE1_INDEX, CURRENT_INDEX), 1.0d);

        Assert.assertEquals(matrix.get(CURRENT_INDEX, NODE2_INDEX), -1.0d);
        Assert.assertEquals(matrix.get(NODE2_INDEX, CURRENT_INDEX), -1.0d);
        */
    }
}
