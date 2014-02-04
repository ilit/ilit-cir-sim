package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MementoResistorsTest extends AbstractSolutionTest
{
    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * Resistor Left; Resistor Right; Resistor common;
                 * Left and right are in parallel.
                 *
                 * RL,   RR1,  RR2  Current with\without RR
                 */
                { 100d, 100d, 200d, -0.1d, -0.075d },
                { 100d, 1e5d, 1e-2d, -0.05005d, -500.05d }
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double resLeft, double resRight1, double resRight2,
            double currentWM, double currentWoM
    )
    {
        initModules();

        /** Instantiate all components */
        Resistor leftResistor = new Load(resLeft);
        Resistor rightResistor = new Load(resRight1);
        VoltageSource voltageSource = new VoltageSource(5);

        Ground gr = new Ground();
        Node sourceCathode = new Node();

        /** Describe topology */
        initComponent(voltageSource, gr, sourceCathode);

        initComponent(leftResistor, sourceCathode, gr);
        initComponent(rightResistor, sourceCathode, gr);

        equations.prepareSystemSize();

        solve();
        Assert.assertEquals(getSolution(voltageSource), currentWM);

        rightResistor.removeStamp(equations);
        rightResistor.setResistance(resRight2);
        solve();
        Assert.assertEquals(getSolution(voltageSource), currentWoM);

        rightResistor.removeStamp(equations);
        rightResistor.setResistance(resRight1);
        solve();
        Assert.assertEquals(getSolution(voltageSource), currentWM);
    }

    private double getSolution(VoltageSource voltageSource)
    {
        double sourceCurrent = equations.getSolution(voltageSource);
        return Precision.round(sourceCurrent, ROUNDING_SCALE);
    }
}
