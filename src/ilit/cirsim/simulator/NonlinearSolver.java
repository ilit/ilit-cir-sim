package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;

/**
 * Nonlinear equations solver.
 * Uses Newton method.
 */
@Singleton
public class NonlinearSolver
{
    private static final int MAX_NEWTON_ITERS = 500;
    private final MnaEquationsSystem equations;
    private final LinearSolver linearSolver;
    private final CircuitProxy circuit;

    @Inject
    public NonlinearSolver(MnaEquationsSystem equations, LinearSolver linearSolver,
                           CircuitProxy circuit)
    {
        this.equations = equations;
        this.linearSolver = linearSolver;
        this.circuit = circuit;
    }

    public void solve()
    {
        /**
         * Initial candidate solution is X = 0.
         * DC sweep can be used to overcome convergence problems
         * from far initial candidate solution.
         */

        /**
         * Linear stamps should have been placed already.
         * Back it up so at each Newton loop we have a matrix untouched by obsolete
         * linearized stamp of nonlinear elements from previous Newton iteration.
         */
        equations.cloneBackUp();

        /** Newton loop */
        for (int iterations = 0; iterations < MAX_NEWTON_ITERS; iterations++)
        {
            /**
             * Apply new linearized stamp of nonlinear elements.
             * I don't have nonlinear components in group 2 for now.
             */
            for (Component component : circuit.getG1Components())
            {
                if (component.isNonlinear())
                {
                    setLinearizedStamp(component);
                }
            }

            /**
             * Solve the system of equations.
             * System is linear now with nonlinear elements being linearized
             * for this one Newton iteration only.
             */
            linearSolver.solve();

            // TODO Here

            // Check norm of step for step damping
            // Check for convergence
            if (converged())
                break;

            /**
             * System is now populated with obsolete stamps of nonlinear elements.
             * This stamps are not needed anymore. Restore matrix to state where
             * only stamps of linear elements present.
             */
            equations.restoreFromBackUp();
        }
    }

    private boolean converged()
    {

        return true;
    }

    private void setLinearizedStamp(Component component)
    {

    }
}
