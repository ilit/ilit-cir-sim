package ilit.cirsim.test;

import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.dynamic.Transformer;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.PiecewiseLinearModeling;
import ilit.cirsim.simulator.SolverWrapper;
import org.testng.annotations.AfterMethod;

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
    private PiecewiseLinearModeling piecewiseLinearModeling;

    @AfterMethod
    public void tearDown() throws Exception
    {
        UniqueIDManager.instance.reset();
        IdToMatrixIndexRelations.instance.reset();

        circuit = null;
        solver = null;
        equations = null;
        piecewiseLinearModeling = null;
    }

    protected void initModules()
    {
        circuit = new CircuitProxy(new CircuitGraph());
        equations = new MnaEquationsSystem(circuit);
        piecewiseLinearModeling = new PiecewiseLinearModeling(equations, circuit);
        solver = new SolverWrapper(circuit, equations);
    }

    protected void initComponent(Component component, Node anode, Node cathode)
    {
        circuit.insertComponent(component, anode, cathode, false);
    }

    protected void initTransformer(Transformer transformer, Node n0, Node n1, Node n2, Node n3)
    {
        circuit.insertTransformer(transformer, n0, n1, n2, n3);
    }

    protected void solve()
    {
        solver.solve();
    }

    protected void solve(double timeStep)
    {
        solver.solve(timeStep);
    }
}
