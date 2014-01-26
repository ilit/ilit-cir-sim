package ilit.cirsim.test.stamp;

import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.StampInjector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import org.testng.annotations.AfterMethod;

public abstract class AbstractSingleStampTest
{
    protected CircuitProxy circuit;
    protected StampInjector stampInjector;
    protected MnaEquationsSystem equations;
    protected Matrix matrix;
    protected SparseVector sideVector;

    public void tearDown() throws Exception
    {
        UniqueIDManager.instance.reset();
        IdToMatrixIndexRelations.instance.reset();
    }

    protected void allocateComponent(Component component, Node anode, Node cathode)
    {
        CircuitGraph circuitGraph = new CircuitGraph();
        circuit = new CircuitProxy(circuitGraph);
        circuit.insertComponent(component, anode, cathode, false);
        equations = new MnaEquationsSystem(circuit);
        equations.prepareSystemSize();
        matrix = equations.getMatrix();
        sideVector = equations.getSideVector();
        stampInjector = new StampInjector(circuit, equations);
        stampInjector.placeLinearStamps();
    }
}
