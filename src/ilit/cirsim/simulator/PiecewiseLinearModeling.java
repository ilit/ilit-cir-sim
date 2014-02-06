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


    public void removeStamps()
    {
        for (Component component : circuit.getNonlinearComponents())
        {
            component.removeStamp(equations);
        }
    }

    public void updateModels()
    {
        /** Solve with probes to test conditions (define current direction in diodes) */
        setProbeModels();

        placeStamps();

        linearSolver.solve(equations);

        /** Get rid of obsolete probe stamps */
        removeStamps();

        updateModels(equations, circuit); /** Based on X computed above */
    }

    public void placeStamps()
    {
        for (Component component : circuit.getNonlinearComponents())
            component.placeStamp(equations);
    }

    private void setProbeModels()
    {
        /**
         * Install regular resistors instead of linear sections of nonlinear diodes
         * to define conditions of nonlinear diodes.
         * This is needed to define initial condition of components
         * which define which linear segment of piecewise model to return
         * to linear solver.
         */
        for (Component component : circuit.getNonlinearComponents())
        {
            Piecewise piecewiseComponent = (Piecewise) component;
            piecewiseComponent.setProbeStamp();
        }
    }

    private void updateModels(MnaEquationsSystem equations, CircuitProxy circuit)
    {
        /** Update linear model of piecewise component */
        for (Component component : circuit.getNonlinearComponents())
        {
            Piecewise piecewise = (Piecewise) component;
            piecewise.updateModel(equations);
        }
    }
}
