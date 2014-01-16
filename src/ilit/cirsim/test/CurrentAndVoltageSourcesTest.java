package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TODO test circuit having both current source and voltage source
 */
public class CurrentAndVoltageSourcesTest extends AbstractStampTest
{
    private static final int ROUNDING_SCALE = 5;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @DataProvider(name = "testValues")
    public Object[][] pushData() {
        return new Object[][] {
                /**
                 * RL1, Current, Voltage, ItsCheckCurrent,
                 */
                { 100d, -0.01d, 5d, -0.04d},
                { 100d, -0.02d, 5d, -0.03d},
                { 100d, -0.06d, 5d,  0.01d},
        };
    }
    /**
     * Simulation reference current direction is from + to -.
     * So normal positive current is negative in the X vector.
     */

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double R, double I, double V, double checkCurrent
    )
    {
        initEmptyCircuit();

        /** Instantiate all components */
        Resistor resistor = new Load(R);
        VoltageSource voltageSource = new VoltageSource(V);
        CurrentSource currentSource = new CurrentSource(I);

        Ground gr = new Ground();
        Node sourcesCathode = new Node();

        /** Describe topology */
        initComponent(voltageSource, gr, sourcesCathode);
        initComponent(currentSource, gr, sourcesCathode);
        initComponent(resistor, gr, sourcesCathode);

        /** Populate equations system */
        placeStamps();

        /** Solve */
        solve();

        /** Check */
        int vIndex = IdToMatrixIndexRelations.instance.getIndex(voltageSource);
        DenseVector X = equations.getXVector();
        double vSourceCurrent = X.get(vIndex);
        double approxVSourceCurrent = Precision.round(vSourceCurrent, ROUNDING_SCALE);

        Assert.assertEquals(approxVSourceCurrent, checkCurrent);
    }
}
