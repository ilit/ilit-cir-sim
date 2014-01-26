package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Piecewise;
import ilit.cirsim.circuit.elements.base.Component;

@Singleton
public class PiecewiseLinearSolver extends LinearSolver
{
    private final CircuitProxy circuit;
    private final StampInjector stampInjector;

    @Inject
    public PiecewiseLinearSolver(MnaEquationsSystem equations,
                                 CircuitProxy circuit,
                                 StampInjector stampInjector)
    {
        super(equations);

        this.circuit = circuit;
        this.stampInjector = stampInjector;
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
         * Back it up so at each piecewise loop we have a matrix untouched by obsolete
         * linearized stamp of nonlinear elements from previous piecewise iteration.
         */
        equations.cloneBackUp();

        /**
         * Install regular resistors instead of linear sections of nonlinear diodes
         * to define conditions of nonlinear diodes.
         * This is needed to define initial condition of components
         * which define which linear segment of piecewise model to return
         * to linear solver.
         */
        for (Component component : circuit.getG1NonlinearComponents())
        {
            Piecewise piecewiseComponent = (Piecewise) component;
            piecewiseComponent.setProbeStamp();
        }
        stampInjector.placeNonlinearStamps();

        /** Solve to test conditions (current direction in diodes) */
        /** Linear solve */
        super.solve();

        /** Update linear model of piecewise component */
        for (Component component : circuit.getG1NonlinearComponents())
        {
            Piecewise piecewise = (Piecewise) component;
            piecewise.updateModel(equations);
        }

        /** Get rid of obsolete linear stamps of piecewise components */
        equations.restoreFromBackUp();

        stampInjector.placeNonlinearStamps();

        /** Linear solve */
        super.solve();

        /**
         * TODO Check if conditions are still met.
         */
    }
}
