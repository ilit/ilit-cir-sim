package ilit.cirsim.test.simple.stamp;

import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;

public abstract class AbstractSingleStampTest
{
    private CircuitProxy circuit;
    private MnaEquationsSystem equations;
    Matrix matrix;
    SparseVector sideVector;

    public void tearDown() throws Exception
    {
        UniqueIDManager.instance.reset();
        IdToMatrixIndexRelations.instance.reset();
    }

    void allocateComponent(Component component, Node anode, Node cathode)
    {
        CircuitGraph circuitGraph = new CircuitGraph();
        circuit = new CircuitProxy(circuitGraph);
        circuit.insertComponent(component, anode, cathode, false);
        equations = new MnaEquationsSystem(circuit);
        equations.prepareSystemSize();
        component.placeStamp(equations);
        matrix = equations.getMatrix();
        sideVector = equations.getSideVector();
    }
}
