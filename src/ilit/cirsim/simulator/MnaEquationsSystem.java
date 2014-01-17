package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import ilit.cirsim.circuit.CircuitProxy;

/**
 * Modified Nodal Analysis equations system.
 * Matrix and side vector.
 */
@Singleton
public class MnaEquationsSystem
{
    /** Holds circuit topology\composition */
    private final CircuitProxy circuit;

    private Matrix matrix; /** Modified Nodal Analysis matrix */
    private SparseVector sideVector; /** And its back up */
    private DenseVector xVector;      /** X */

    /** Back ups to have a clean linear system before each Newton loop iteration */
    private Matrix matrixBk;
    private SparseVector sideVectorBk;

    @Inject
    public MnaEquationsSystem(CircuitProxy circuit)
    {
        this.circuit = circuit;
    }

    public void createEmptySystem()
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

    private int defineMatrixSize()
    {
        if (circuit == null)
            throw new Error("equationsSystem.defineMatrixSize(): circuit == null");

        /**
         * All elements whose currents are to be eliminated
         * will be referred to as being in group 1,
         * while all other elements will be referred to as group 2.
         */
        return nodeVariables() + currentVariables();
    }

    private int nodeVariables()
    {
        return circuit.bearingNodesAmount();
    }

    private int currentVariables()
    {
        return circuit.getG2Components().size();
    }

    /**
     * Used in Newton loop to have a clean system of only
     * linear elements at each Newton loop iteration start
     */
    public void cloneBackUp()
    {
        matrixBk = matrix.copy();
        sideVectorBk = sideVector.copy();
    }

    public void restoreFromBackUp()
    {
        matrix = matrixBk.copy();
        sideVector = sideVectorBk.copy();
    }
}
