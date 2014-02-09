package ilit.cirsim.test.simple;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.dynamic.Capacitor;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.test.AbstractSolutionTest;
import no.uib.cipr.matrix.sparse.SparseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class CapacitorDCTest extends AbstractSolutionTest
{
    private static final double TIME_STEP = 5e-5;
    private static final double CAPACITANCE = 1e-5; /** 10 micro Farads */
    private static final double SOURCE_VOLTAGE = 5;
    private static final double RESISTANCE = 100;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test()
    public void capTest(
    )
    {
        initModules();

        /** Instantiate all components */
        Resistor resistor = new Load(RESISTANCE);
        VoltageSource voltageSource = new VoltageSource(SOURCE_VOLTAGE);
        Capacitor capacitor = new Capacitor(CAPACITANCE);

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();

        /**
         * Topology:
         *     -   +    + -
         * g---(Vdc)--1--R--2--C---g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(resistor, node1, node2);
        initComponent(capacitor, node2, gr);

        equations.prepareSystemSize();
        SparseVector rhs = equations.getSideVector();
        Assert.assertEquals(rhs.size(), 4);

        /** Solve and check */
        solve(TIME_STEP);
        Assert.assertEquals(getApproxVSourceCurrent(voltageSource), -0.05);
        solve(TIME_STEP);
        Assert.assertEquals(getApproxVSourceCurrent(voltageSource), -0.0475);
        solve(TIME_STEP);
        Assert.assertEquals(getApproxVSourceCurrent(voltageSource), -0.04513);
        /** Current decreases while capacitor charges */
    }

    private double getApproxVSourceCurrent(VoltageSource voltageSource)
    {
        double vSourceCurrent = equations.getSolution(voltageSource);
        return Precision.round(vSourceCurrent, ROUNDING_SCALE);
    }
}
