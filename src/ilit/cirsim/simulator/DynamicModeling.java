package ilit.cirsim.simulator;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.IDynamic;

/**
 * Replaces capacitors and inductors with linearized
 * ideal voltage and current sources each time step.
 * Simple companion model thanks to simple Forward Euler method.
 */
public class DynamicModeling
{
    public void updatedAndPlaceStamps(MnaEquationsSystem equations, CircuitProxy circuit,
                                      double timeStep)
    {
        /** Replace dynamics with companions */
        for (Component component : circuit.getDynamicComponents())
        {
            if (component.isDynamic())
            {
                IDynamic dynamicComponent = (IDynamic) component;
                dynamicComponent.updateCompanionModel(equations, timeStep);
                /**
                 * Obsolete stamp from previous model
                 * does not meet current circuit conditions.
                 * Remove obsolete stamp and place updated one.
                 */
                component.removeStamp(equations);
                component.placeStamp(equations);
            }
        }
    }
}
