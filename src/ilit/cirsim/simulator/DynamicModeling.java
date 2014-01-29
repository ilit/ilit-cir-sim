package ilit.cirsim.simulator;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.IDynamic;

/**
 * Replaces capacitors and inductors with linearized
 * ideal voltage and current sources each time step.
 * Simple companion model thanks to simple Forward Euler method.
 * Modifies only RHS
 */
public class DynamicModeling
{
    public void model(MnaEquationsSystem equations, CircuitProxy circuit, StampInjector stampInjector)
    {
        /** Replace dynamics with companions */

        /** Remove obsolete dynamic stamps of previous time steps */
        equations.restoreRhsFromBackUp();

        for (Component component : circuit.getDynamicComponents())
        {
            IDynamic dynamicComponent = (IDynamic) component;
            dynamicComponent.updateCompanionModel(equations);
            equations.restoreRhsFromBackUp();
            stampInjector.placeDynamicStamps();
        }
    }
}
