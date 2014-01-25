package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;

@Singleton
public class SolverFacade
{
    private LinearSolver linearEquationsSolver;
    private PiecewiseLinearSolver piecewiseLinearSolver;
    private CircuitProxy circuit;
    private StampInjector stampInjector;

    @Inject
    public SolverFacade(LinearSolver linearEquationsSolver,
                        PiecewiseLinearSolver piecewiseLinearSolver,
                        CircuitProxy circuit,
                        StampInjector stampInjector)
    {
        this.linearEquationsSolver = linearEquationsSolver;
        this.piecewiseLinearSolver = piecewiseLinearSolver;
        this.circuit = circuit;
        this.stampInjector = stampInjector;
    }

    public void solve()
    {
        stampInjector.placeLinearStamps();

        if (circuit.isCircuitNonlinear())
        {
            piecewiseLinearSolver.solve();
        }
        else
        {
            linearEquationsSolver.solve();
        }
    }
}
