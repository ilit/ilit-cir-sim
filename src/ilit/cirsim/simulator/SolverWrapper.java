package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;

@Singleton
public class SolverWrapper
{
    private final CircuitProxy circuit;
    private final StampInjector stampInjector;
    private final MnaEquationsSystem equations;

    private final LinearSolver linearSolver = new LinearSolver();
    private final DynamicModeling dynamicModeling = new DynamicModeling();
    private final PiecewiseLinearSolver piecewiseLinearModeling = new PiecewiseLinearSolver();

    @Inject
    public SolverWrapper(CircuitProxy circuit,
                         StampInjector stampInjector,
                         MnaEquationsSystem equations)
    {
        this.circuit = circuit;
        this.stampInjector = stampInjector;
        this.equations = equations;
    }

    public void prepareSystem()
    {
        equations.prepareSystemSize();
        stampInjector.placeLinearStamps();

        if (circuit.isCircuitDynamic())
            equations.cloneRhsBackUp();
    }

    public void solve()
    {
        if (circuit.isCircuitDynamic())
            dynamicModeling.model(equations, circuit, stampInjector);

        if (circuit.isCircuitNonlinear())
            piecewiseLinearModeling.model(equations, circuit, stampInjector, linearSolver);

        linearSolver.solve(equations);

        /** TODO Check if piecewise conditions are still met */
    }
}
