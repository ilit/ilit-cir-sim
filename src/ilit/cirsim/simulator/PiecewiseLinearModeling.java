package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Piecewise;
import ilit.cirsim.circuit.elements.base.Component;

@Singleton
public class PiecewiseLinearModeling
{
    private final MnaEquationsSystem equations;
    private final CircuitProxy circuit;

    private final LinearSolver linearSolver = new LinearSolver();

    @Inject
    public PiecewiseLinearModeling(MnaEquationsSystem equations, CircuitProxy circuit)
    {
        this.equations = equations;
        this.circuit = circuit;
    }

    public void updatedAndPlaceStamps()
    {
        /** Solve with probes to test conditions (define current direction in diodes) */
        defineModelsForNewComponents();

        /**
         * Update models that are inconsistent with current circuit state
         * or that have used probe models installed above.
         */
        updateModelsAndStamps();
    }

    private void defineModelsForNewComponents()
    {
        /**
         * Install regular resistors instead of linear sections of nonlinear diodes
         * to define conditions of nonlinear diodes.
         * This is needed to define initial condition of components
         * which define which linear segment of piecewise model to return
         * to linear solver.
         */
        int probesSet = 0;
        for (Component component : circuit.getNonlinearComponents())
        {
            Piecewise piecewiseComponent = (Piecewise) component;
            if (!piecewiseComponent.isModelDefined())
            {
                piecewiseComponent.setProbeModel();
                component.placeStamp(equations);
                probesSet++;
            }
        }

        if (probesSet > 0)
            linearSolver.solve(equations);
    }

    private void updateModelsAndStamps()
    {
        /** Update linear model of piecewise component */
        for (Component component : circuit.getNonlinearComponents())
        {
            Piecewise piecewise = (Piecewise) component;
            boolean modelChanged = piecewise.updateModel(equations);
            if (modelChanged)
            {
                /**
                 * Obsolete stamp from previous model
                 * does not meet current circuit conditions.
                 * Remove obsolete stamp and place updated one.
                 */
                component.removeStamp(equations);
                component.placeStamp(equations);
            }
        }
    }
}
