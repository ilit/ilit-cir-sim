package ilit.cirsim.test;

import ilit.cirsim.simulator.StampInjector;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
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
public class AbstractStampTest
{
    protected FlexCompRowMatrix matrix;
    protected SparseVector sideVector;
    protected CircuitProxy circuit;
    protected MnaEquationsSystem equations;

    @BeforeMethod
    public void setUp() throws Exception
    {
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        UniqueIDManager.instance.reset();
        IdToMatrixIndexRelations.instance.reset();

        matrix = null;
        sideVector = null;
        circuit = null;
    }

    protected void initCircuit()
    {
        CircuitGraph circuitGraph = new CircuitGraph();
        circuit = new CircuitProxy(circuitGraph);
    }

    protected void placeStamps()
    {
        equations = new MnaEquationsSystem(circuit);
        equations.createEmptySystem();
        matrix = equations.getMatrix();
        sideVector = equations.getSideVector();
        StampInjector stampInjector = new StampInjector(circuit, equations);
        stampInjector.placeStamps();
    }

    protected void initComponent(Component component, Node node1, Node node2)
    {
        circuit.insertComponent(component, node1, node2, false);
    }
}
