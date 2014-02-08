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

    /** DC analysis */
    public void solve()
    {
        for (Component component : circuit.getRegularComponents())
            if (!component.stampIsPlaced())
                component.placeStamp(equations);

        piecewiseLinearModeling.updatedAndPlaceStamps();

        /** At this point all linear or linearized stamps are placed */
        linearSolver.solve(equations);
    }

    /** Transient analysis */
    public void solve(double timeStep)
    {
        /** Update stamps of dynamic components */
        dynamicModeling.updatedAndPlaceStamps(equations, circuit, timeStep);

        solve();
    }
}
