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
    private MnaEquationsSystem equations;

    @Inject
    public SolverFacade(LinearSolver linearEquationsSolver,
                        PiecewiseLinearSolver piecewiseLinearSolver,
                        CircuitProxy circuit,
                        StampInjector stampInjector,
                        MnaEquationsSystem equations)
    {
        this.linearEquationsSolver = linearEquationsSolver;
        this.piecewiseLinearSolver = piecewiseLinearSolver;
        this.circuit = circuit;
        this.stampInjector = stampInjector;
        this.equations = equations;
    }

    public void prepareSystem()
    {
        equations.prepareSystemSize();
        stampInjector.placeLinearStamps();
    }

    public void solve()
    {
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
