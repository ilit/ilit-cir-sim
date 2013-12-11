package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
    private CircuitProxy circuit;

    /**
     * Compressed matrix. Zero values are not stored in memory.
     * Flexible - automatically perform internal memory management
     * (sparsity pattern is unknown).
     */
    private FlexCompRowMatrix matrix; /** Modified Nodal Analysis matrix */
    private SparseVector sideVector; /** Right Hand Side */

    @Inject
    public MnaEquationsSystem(CircuitProxy circuit)
    {
        this.circuit = circuit;

        createEmptySystem();
    }

    public FlexCompRowMatrix getMatrix()
    {
        return matrix;
    }

    public SparseVector getSideVector()
    {
        return sideVector;
    }

    private void createEmptySystem()
    {
        int initialMatrixSize = defineMatrixSize();
        matrix = new FlexCompRowMatrix(initialMatrixSize, initialMatrixSize);
        sideVector = new SparseVector(initialMatrixSize);
    }

    private int defineMatrixSize()
    {
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
}