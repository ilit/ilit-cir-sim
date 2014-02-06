package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;

@Singleton
public class SolverWrapper
{
    // TODO Get rid of dependency injection everywhere(?)
    private final CircuitProxy circuit;
    private final MnaEquationsSystem equations;
    private final PiecewiseLinearModeling piecewiseLinearModeling;

    private final LinearSolver linearSolver = new LinearSolver();
    private final DynamicModeling dynamicModeling = new DynamicModeling();

    @Inject
    public SolverWrapper(CircuitProxy circuit,
                         MnaEquationsSystem equations,
                         PiecewiseLinearModeling piecewiseLinearModeling)
    {
        this.circuit = circuit;
        this.equations = equations;
        this.piecewiseLinearModeling = piecewiseLinearModeling;
    }

    public void prepareSystem()
    {
        equations.prepareSystemSize();
    }

    public void solve()
    {
        /** Update stamps of dynamic components */
        // TODO dynamicModeling.removeStamps();
        // TODO dynamicModeling.updateModels(equations, circuit, stampInjector);
        // TODO dynamicModeling.placeStamps();

        for (Component component : circuit.getRegularComponents())
            if (!component.stampIsPlaced())
                component.placeStamp(equations);

        if (circuit.isNonlinear())
        {
            /** Update stamps of nonlinear components */
            // TODO Do not use probes all the time in tr loop
            piecewiseLinearModeling.removeStamps();
            piecewiseLinearModeling.updateModels();
            piecewiseLinearModeling.placeStamps();
        }

        /** At this point all linear or linearized stamps are placed */
        linearSolver.solve(equations);
        /** TODO Check if piecewise conditions are still met */
    }
}
