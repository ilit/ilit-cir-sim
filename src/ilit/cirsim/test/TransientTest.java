package ilit.cirsim.test;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransientTest
{
    private static final double INITIAL_TIME = 0;
    private static final double FINAL_TIME = 10;

    //private final DynamicSolver dynamicSolver;

    protected CircuitProxy circuit;
    protected MnaEquationsSystem equations;
    protected LinearSolver linearSolver;
    protected StampInjector stampInjector;

    @BeforeMethod
    public void before()
    {
        //dynamicSolver = new DynamicSolver();
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        UniqueIDManager.instance.reset();
        IdToMatrixIndexRelations.instance.reset();

        circuit = null;
        linearSolver = null;
        equations = null;
        stampInjector = null;
    }

    //@Test
    public void oneCapacitorTest()
    {
        // Init items
        for (double time = INITIAL_TIME;
             time <= TransientAnalysis.TIME_STEP;
             time += FINAL_TIME)
        {
            //dynamicSolver.analize(INITIAL_TIME, TIME_STEP, FINAL_TIME);
        }
    }
}
