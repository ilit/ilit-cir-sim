package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.util.StampMemento;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * The contribution of every element to the matrix equation is described by means
 * of a template, which is called an element stamp.
 */
abstract public class Stamp
{
    int getIndex(Component component)
    {
        return IdToMatrixIndexRelations.instance.getIndex(component);
    }

    int getIndex(Node node)
    {
        return IdToMatrixIndexRelations.instance.getIndex(node);
    }

    void matrixAddSave(int i1, int i2, double value,
                       Matrix matrix, StampMemento memento)
    {
        matrix.add(i1, i2, value);
        memento.matrixAdd(i1, i2, value);
    }

    void rhsAddSave(int i, double value,
                    SparseVector rhs, StampMemento memento)
    {
        rhs.add(i, value);
        memento.rhsAdd(i, value);
    }

    abstract public StampMemento setStamp(MnaEquationsSystem equationsSystem, Component component);
}
