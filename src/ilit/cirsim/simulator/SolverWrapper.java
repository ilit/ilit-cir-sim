package ilit.cirsim.simulator;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;

public class SolverWrapper
{
    private final CircuitProxy circuit;
    private final MnaEquationsSystem equations;

    private final LinearSolver linearSolver = new LinearSolver();
    private final DynamicModeling dynamicModeling = new DynamicModeling();
    private final AlternatingSourcesModeling alternatingSourcesModeling = new AlternatingSourcesModeling();
    private final PiecewiseLinearModeling piecewiseLinearModeling;

    public SolverWrapper(CircuitProxy circuit,
                         MnaEquationsSystem equations)
    {
        this.circuit = circuit;
        this.equations = equations;
        this.piecewiseLinearModeling = new PiecewiseLinearModeling(equations, circuit);
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
        /** Update alternating voltage and current sources stamps */
        alternatingSourcesModeling.updatedAndPlaceStamps(equations, circuit, timeStep);

        /** Update stamps of single dynamic components and transformers */
        dynamicModeling.updatedAndPlaceStamps(equations, circuit, timeStep);

        solve();
    }
}
