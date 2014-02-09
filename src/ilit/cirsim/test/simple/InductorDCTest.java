package ilit.cirsim.test.simple;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.dynamic.Inductor;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import no.uib.cipr.matrix.sparse.SparseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class InductorDCTest extends AbstractSolutionTest
{
    private static final double TIME_STEP = 5e-4;
    private static final double INDUCTANCE = 1;  /** 1 Henry */
    private static final double SOURCE_VOLTAGE = 5;
    private static final double RESISTANCE = 100;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test()
    public void inductorTest(
    )
    {
        initModules();

        /** Instantiate all components */
        Resistor resistor = new Load(RESISTANCE);
        VoltageSource voltageSource = new VoltageSource(SOURCE_VOLTAGE);
        Inductor inductor = new Inductor(INDUCTANCE);

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();

        /**
         * Topology:
         *     -   +    + -
         * g---(Vdc)--1--R--2--L---g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(resistor, node1, node2);
        initComponent(inductor, node2, gr);

        equations.prepareSystemSize();
        SparseVector rhs = equations.getSideVector();
        Assert.assertEquals(rhs.size(), 3);

        /** Solve and check */
        solve(TIME_STEP);
        Assert.assertEquals(getApproxVSourceCurrent(voltageSource), 0.0);
        solve(TIME_STEP);
        Assert.assertEquals(getApproxVSourceCurrent(voltageSource), -0.0025);
        solve(TIME_STEP);
        Assert.assertEquals(getApproxVSourceCurrent(voltageSource), -0.00488);
        /** Current increases while inductor magnetic flux increases */
    }

    private double getApproxVSourceCurrent(VoltageSource voltageSource)
    {
        double vSourceCurrent = equations.getSolution(voltageSource);
        return Precision.round(vSourceCurrent, ROUNDING_SCALE);
    }
}
