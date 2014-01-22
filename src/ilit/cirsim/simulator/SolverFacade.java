package ilit.cirsim.simulator;

import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;

@Singleton
public class SolverFacade
{
    private LinearSolver linearEquationsSolver;
    private NonlinearSolver nonlinearSolver;
    private CircuitProxy circuit;
    private StampInjector stampInjector;

    public SolverFacade(LinearSolver linearEquationsSolver,
                        NonlinearSolver nonlinearSolver,
                        CircuitProxy circuit,
                        StampInjector stampInjector)
    {
        this.linearEquationsSolver = linearEquationsSolver;
        this.nonlinearSolver = nonlinearSolver;
        this.circuit = circuit;
        this.stampInjector = stampInjector;
    }

    public void solve()
    {
        stampInjector.placeLinearStamps();

        if (circuit.isCircuitNonlinear())
        {
            nonlinearSolver.solve();
        }
        else
        {
            linearEquationsSolver.solve();
        }
    }
}
