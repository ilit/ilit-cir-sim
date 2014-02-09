package ilit.cirsim.simulator;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * Modified Nodal Analysis equations system.
 * Matrix and side vector.
 */
public class MnaEquationsSystem
{
    private static final double GROUND_VOLTAGE = 0;

    /** Holds circuit topology\composition */
    private final CircuitProxy circuit;

    private Matrix matrix; /** Modified Nodal Analysis matrix */
    private SparseVector sideVector; /** And its back up */
    private DenseVector xVector;      /** X */

    public MnaEquationsSystem(CircuitProxy circuit)
    {
        this.circuit = circuit;
    }

    /** Warning! Depends on circuit being filled! */
    public void prepareSystemSize()
    {
        int initialMatrixSize = defineMatrixSize();

        /**
         * Compressed matrix. Zero values are not stored in memory.
         * Flexible - automatically perform internal memory management
         * (sparsity pattern is unknown).
         */
        matrix = new FlexCompRowMatrix(initialMatrixSize, initialMatrixSize);
        sideVector = new SparseVector(initialMatrixSize);
        xVector = new DenseVector(initialMatrixSize);
    }

    public Matrix getMatrix()
    {
        if (matrix == null)
            throw new Error("equationsSystem.getMatrix() == null");

        return matrix;
    }

    public SparseVector getSideVector()
    {
        if (sideVector == null)
            throw new Error("equationsSystem.getSideVector() == null");

        return sideVector;
    }

    public DenseVector getXVector()
    {
        if (xVector == null)
            throw new Error("equationsSystem.getXVector() == null");

        return xVector;
    }

    public double getSolution(Node node)
    {
        if (node.isGround())
            return GROUND_VOLTAGE;

        int index = IdToMatrixIndexRelations.instance.getIndex(node);

        return xVector.get(index);
    }

    public double getSolution(Component component)
    {
        int index = IdToMatrixIndexRelations.instance.getIndex(component);

        return xVector.get(index);
    }

    private int defineMatrixSize()
    {
        if (circuit == null)
            throw new Error("equationsSystem.defineMatrixSize(): circuit == null");

        /**
         * All elements whose currents are to be eliminated
         * will be referred to as being in group 1,
         * while all other elements will be referred to as group 2.
         *
         * Models I use:
         * Group one: resistors\diodes and current sources\inductors.
         * Group two: voltage sources\capacitors.
         */
        return nodeVariables() + currentVariables();
    }

    private int nodeVariables()
    {
        return circuit.bearingNodesAmount();
    }

    private int currentVariables()
    {
        int vars = 0;

        /** Voltage sources are group two components */
        for (Component component: circuit.getRegularComponents())
            if (!component.isGroupOne())
                vars++;

        /** Alternating voltage sources are group two components */
        for (Component component: circuit.getAlternatingSources())
            if (!component.isGroupOne())
                vars++;

        /** Capacitors are group two components like voltage source */
        for (Component component: circuit.getDynamicComponents())
            if (!component.isGroupOne())
                vars++;

        return vars;
    }
}
