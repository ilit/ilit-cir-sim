package ilit.cirsim.simulator;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.*;

class LinearSolver
{
    public void solve(MnaEquationsSystem equations)
    {
        CompRowMatrix A = new CompRowMatrix(equations.getMatrix(), true);
        SparseVector b = equations.getSideVector();

        /** Solution is stored here. ### Also used as initial guess ### */
        DenseVector x = equations.getXVector();

        /** Biconjugate stabilized solver */
        IterativeSolver solver = new BiCGstab(x);

        /**
         * Algebraic multigrid preconditioner. Uses the smoothed aggregation method
         * described by Vanek, Mandel and Brezina (1996).
         */
        Preconditioner M = new AMG();

        /** Set up the preconditioner, and attach it */
        M.setMatrix(A);
        solver.setPreconditioner(M);

        /** Add a convergence monitor */
        IterationMonitor monitor = new DefaultIterationMonitor();
        solver.setIterationMonitor(monitor);

        /** Start the solver, and check for problems */
        try
        {
            solver.solve(A, b, x);
        }
        catch (IterativeSolverNotConvergedException e)
        {
            System.err.println("Iterative solver failed to converge");
        }
    }
}
