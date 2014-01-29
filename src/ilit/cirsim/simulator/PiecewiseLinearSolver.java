package ilit.cirsim.simulator;

import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Piecewise;
import ilit.cirsim.circuit.elements.base.Component;

@Singleton
public class PiecewiseLinearSolver
{
    public void model(MnaEquationsSystem equations,
                      CircuitProxy circuit,
                      StampInjector stampInjector,
                      LinearSolver linearSolver)
    {
        /**
         * Linear stamps should have been placed already.
         * Back it up so at each piecewise loop we have a matrix untouched by obsolete
         * linearized stamp of nonlinear elements from previous piecewise iteration.
         */
        equations.cloneMatrixBackUp();

        /** Solve with probes to test conditions (define current direction in diodes) */
        setProbeModels(equations, circuit, stampInjector);
        stampInjector.placeNonlinearStamps();
        linearSolver.solve(equations);

        /** Get rid of obsolete probe stamps */
        equations.restoreMatrixFromBackUp();

        updateModels(equations, circuit); /** Based on X computed above */
        stampInjector.placeNonlinearStamps();
    }

    private void setProbeModels(MnaEquationsSystem equations, CircuitProxy circuit, StampInjector stampInjector)
    {
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
    }

    private void updateModels(MnaEquationsSystem equations, CircuitProxy circuit)
    {
        /** Update linear model of piecewise component */
        for (Component component : circuit.getG1NonlinearComponents())
        {
            Piecewise piecewise = (Piecewise) component;
            piecewise.updateModel(equations);
        }
    }
}
