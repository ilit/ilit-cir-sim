package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.*;

@Singleton
public class EquationsSolver
{
    private MnaEquationsSystem equations;

    @Inject
    public EquationsSolver(MnaEquationsSystem equations)
    {
        this.equations = equations;
    }

    public void solve()
    {
        CompRowMatrix A = (CompRowMatrix)equations.getMatrix().copy();
        SparseVector b = equations.getSideVector();
        DenseVector x = equations.getXVector();

        IterativeSolver solver = new BiCGstab(x);

        /** Create a Cholesky preconditioner */
        Preconditioner M = new ICC(A);

        /** Set up the preconditioner, and attach it */
        M.setMatrix(A);
        solver.setPreconditioner(M);

        /** Add a convergence monitor */
        solver.getIterationMonitor().setIterationReporter(new OutputIterationReporter());

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
