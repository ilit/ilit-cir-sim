package ilit.cirsim.test.simple.currentsource;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.sources.CurrentSource;
import ilit.cirsim.test.AbstractSolutionTest;
import no.uib.cipr.matrix.DenseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OneCurrentManyResistorsSolutionTest extends AbstractSolutionTest
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
                 * Resistor Left; Resistor Right; Resistor middle;
                 * Source Voltage; Source Evaluated Current;
                 *
                 * RL1,  RL2, Rmid,  RR1,  RR2, Current, Voltage at source
                 */
                { 100d, 100d,  30d, 100d, 300d,  -1d, 126.53061d },
                { 100d, 100d, 100d, 100d, 100d,  -1d, 100d },
                { 100d, 100d, 100d, 100d, 100d,  -2d, 200d },
        };
    }

    @Test(dataProvider = "testValues")
    public void groundedDcVoltageAndResistorTest(
            double rl1, double rl2, double rm, double rr1, double rr2,
            double current, double checkVoltage
    )
    {
        initModules();

        /** Instantiate all components */
        Resistor resL1 = new Load(rl1);
        Resistor resL2 = new Load(rl2);
        Resistor resMid = new Load(rm);
        Resistor resR1 = new Load(rr1);
        Resistor resR2 = new Load(rr2);
        CurrentSource currentSource = new CurrentSource(current);

        Ground gr = new Ground();
        Node sourceCathode = new Node();
        Node nodeL = new Node();
        Node nodeR = new Node();

        /** Describe topology */
        initComponent(currentSource, gr, sourceCathode);

        initComponent(resL1, sourceCathode, nodeL);
        initComponent(resR1, sourceCathode, nodeR);

        initComponent(resL2, nodeL, gr);
        initComponent(resR2, nodeR, gr);

        initComponent(resMid, nodeR, nodeL);

        equations.prepareSystemSize();

        DenseVector X = equations.getXVector();
        Assert.assertEquals(X.size(), 3);
        Assert.assertEquals(X.get(0), 0d);
        Assert.assertEquals(X.get(1), 0d);
        Assert.assertEquals(X.get(2), 0d);

        /** Solve */
        solve();

        double sourceVoltage = equations.getSolutionNodeVoltage(sourceCathode);
        double approxSourceVoltage = Precision.round(sourceVoltage, ROUNDING_SCALE);

        Assert.assertEquals(approxSourceVoltage, checkVoltage);
    }
}
