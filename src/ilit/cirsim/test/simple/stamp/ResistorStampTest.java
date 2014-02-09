package ilit.cirsim.test.simple.stamp;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ResistorStampTest extends AbstractSingleStampTest
{
    private static final double RESISTANCE = 100.0d;
    private static final double CONDUCTANCE = 1 / RESISTANCE;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test()
    public void resistorTest()
    {
        allocateComponent(new Load(RESISTANCE), new Node(), new Node());

        Assert.assertEquals(matrix.numColumns(), 2);
        Assert.assertEquals(matrix.get(0, 0), CONDUCTANCE);
        Assert.assertEquals(matrix.get(1, 0), -CONDUCTANCE);
        Assert.assertEquals(matrix.get(1, 1), CONDUCTANCE);
        Assert.assertEquals(matrix.get(0, 1), -CONDUCTANCE);
    }

    @Test()
    public void groundedResistorTest()
    {
        allocateComponent(new Load(RESISTANCE), new Ground(), new Node());

        Assert.assertEquals(matrix.numColumns(), 1);
        Assert.assertEquals(matrix.get(0, 0), CONDUCTANCE);
    }

    @Test()
    public void groundedResistorTest2()
    {
        allocateComponent(new Load(RESISTANCE), new Node(), new Ground());

        Assert.assertEquals(matrix.numColumns(), 1);
        Assert.assertEquals(matrix.get(0, 0), CONDUCTANCE);
    }
}
