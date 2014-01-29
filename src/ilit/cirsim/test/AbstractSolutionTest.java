package ilit.cirsim.test;

import ilit.cirsim.simulator.LinearSolver;
import ilit.cirsim.simulator.StampInjector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.MnaEquationsSystem;

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
    protected LinearSolver linearSolver;
    protected StampInjector stampInjector;

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

    protected void initModules()
    {
        CircuitGraph circuitGraph = new CircuitGraph();
        circuit = new CircuitProxy(circuitGraph);
        equations = new MnaEquationsSystem(circuit);
        stampInjector = new StampInjector(circuit, equations);
        linearSolver = new LinearSolver();
    }

    protected void placeStamps()
    {
        equations.prepareSystemSize();
        stampInjector.placeLinearStamps();
    }

    protected void initComponent(Component component, Node anode, Node cathode)
    {
        circuit.insertComponent(component, anode, cathode, false);
    }

    protected void solve()
    {
        linearSolver.solve(equations);
    }
}
