package ilit.cirsim.simulator;

import com.google.inject.Inject;

/**
 * Nonlinear equations solver.
 * Uses Newton method.
 */
public class NonlinearSolver
{
    private final MnaEquationsSystem equations;
    private final LinearSolver linearSolver;

    @Inject
    public NonlinearSolver(MnaEquationsSystem equations, LinearSolver linearSolver)
    {
        this.equations = equations;
        this.linearSolver = linearSolver;
    }

    public void solve()
    {
        /**
         * set linear stamps
         * set initial candidate solution
         * Newton loop
         * {
         *     refresh nonlinear stamps
         *         remove old ones
         *         apply new ones
         *     solve linear
         *     check norms
         *     check convergence
         * }
         */
    }
}
