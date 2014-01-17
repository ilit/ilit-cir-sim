package ilit.cirsim.simulator;

import com.google.inject.Inject;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;

/**
 * Nonlinear equations solver.
 * Uses Newton method.
 */
public class NonlinearSolver
{
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
        boolean converged = false;
        do
        {
            /**
             * Apply new linearized stamp of nonlinear elements.
             * I don't have nonlinear components in group 2.
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

            /**
             * System is now populated with obsolete stamps of nonlinear elements.
             * This stamps are not needed anymore. Restore matrix to state where
             * only stamps of linear elements present.
             */
            equations.restoreFromBackUp();

        } while (!converged);
    }

    private void setLinearizedStamp(Component component)
    {

    }
}
