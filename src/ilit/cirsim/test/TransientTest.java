package ilit.cirsim.test;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.simulator.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TransientTest extends AbstractSolutionTest
{
    private static final double INITIAL_TIME = 0;
    private static final double FINAL_TIME = 0.5;

    private SolverWrapper solver;

    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    // TODO @Test
    public void oneCapacitorTest()
    {
        initModules();

        /** Instantiate all components */
        Resistor resistor = new Load(100);
        VoltageSource voltageSource = new VoltageSource(100);
        Capacitor capacitor = new Capacitor(10e-6);

        Ground gr = new Ground();
        Node node1 = new Node();
        Node node2 = new Node();

        /**
         * Describe topology
         *       +
         * g--(V)-1-R-2-C--g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(resistor, node1, node2);
        initComponent(capacitor, node2, gr);

        equations.prepareSystemSize();

        for (double time = INITIAL_TIME;
             time <= TransientAnalysis.TIME_STEP;
             time += FINAL_TIME)
        {
            solve();
        }
    }
}
