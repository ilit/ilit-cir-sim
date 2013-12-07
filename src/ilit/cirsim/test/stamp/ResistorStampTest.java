package ilit.cirsim.test.stamp;

import org.testng.Assert;
import org.testng.annotations.Test;
import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;

public class ResistorStampTest extends SingleStampTest
{
    private static double RESISTANCE = 100.0d;
    private static double CONDUCTANCE = 1 / RESISTANCE;

    @Test()
    public void resistorTest()
    {
        initComponent(new Load(RESISTANCE), new Node(), new Node());

        Assert.assertEquals(matrix.numColumns(), 2);
        Assert.assertEquals(matrix.get(0, 0), CONDUCTANCE);
        Assert.assertEquals(matrix.get(1, 0), -CONDUCTANCE);
        Assert.assertEquals(matrix.get(1, 1), CONDUCTANCE);
        Assert.assertEquals(matrix.get(0, 1), -CONDUCTANCE);
    }

    @Test()
    public void groundedResistorTest()
    {
        initComponent(new Load(RESISTANCE), new Ground(), new Node());

        Assert.assertEquals(matrix.numColumns(), 1);
        Assert.assertEquals(matrix.get(0, 0), CONDUCTANCE);
    }

    @Test()
    public void groundedResistorTest2()
    {
        initComponent(new Load(RESISTANCE), new Node(), new Ground());

        Assert.assertEquals(matrix.numColumns(), 1);
        Assert.assertEquals(matrix.get(0, 0), CONDUCTANCE);
    }
}
