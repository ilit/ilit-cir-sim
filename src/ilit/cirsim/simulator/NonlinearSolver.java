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
        /** Linear stamps should have been placed already */
        /**
         * set initial candidate solution
         *     DC sweep is used to overcome convergence problems of initial DC analysis
         *
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
