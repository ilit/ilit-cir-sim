package ilit.cirsim.test;

import ilit.cirsim.simulator.*;
import org.testng.annotations.AfterMethod;
import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;

/**
 * Shared class for all tests involving:
 * ) matrix and side vector instantiation
 * ) stamp usage
 */
public class AbstractSolutionTest
{
    protected static final int ROUNDING_SCALE = 5;

    protected CircuitProxy circuit;
    protected MnaEquationsSystem equations;
    protected SolverWrapper solver;

    @AfterMethod
    public void tearDown() throws Exception
    {
        UniqueIDManager.instance.reset();
        IdToMatrixIndexRelations.instance.reset();

        circuit = null;
        solver = null;
        equations = null;
    }

    protected void initModules()
    {
        circuit = new CircuitProxy(new CircuitGraph());
        equations = new MnaEquationsSystem(circuit);
        solver = new SolverWrapper(circuit, equations, null);
    }

    protected void initComponent(Component component, Node anode, Node cathode)
    {
        circuit.insertComponent(component, anode, cathode, false);
    }

    protected void solve()
    {
        solver.solve();
    }
}
