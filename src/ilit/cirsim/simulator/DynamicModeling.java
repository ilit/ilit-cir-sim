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
    public void updateModels(MnaEquationsSystem equations, CircuitProxy circuit)
    {
        /** Replace dynamics with companions */
        for (Component component : circuit.getDynamicComponents())
        {
            if (component.isDynamic())
            {
                IDynamic dynamicComponent = (IDynamic) component;
                dynamicComponent.updateCompanionModel(equations);
                // TODO Revert part of stamp if stamp was placed before
                //stampInjector.placeLinearStamps(circuit.getDynamicComponents());
            }

            // TODO Two mementos: one full memento stamp, one partial RHS only memento stamp
        }
    }
}
