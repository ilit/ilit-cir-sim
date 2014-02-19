package ilit.cirsim.simulator;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.IDynamic;
import ilit.cirsim.circuit.elements.dynamic.Transformer;

/**
 * Replaces capacitors and inductors with linearized
 * ideal voltage and current sources each time step.
 * Simple companion model thanks to simple Forward Euler method.
 */
class DynamicModeling
{
    public void updatedAndPlaceStamps(MnaEquationsSystem equations, CircuitProxy circuit,
                                      double timeStep)
    {
        /** Update dynamics companions */
        for (Component component : circuit.getDynamicComponents())
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

        /** Update transformer companions */
        for (int i = 0; i < circuit.getTransformers().size(); i++)
        {
            Transformer transformer = circuit.getTransformers().get(i);

            transformer.updateCompanionModel(equations, timeStep);
            /**
             * Obsolete stamp from previous model
             * does not meet current circuit conditions.
             * Remove obsolete stamp and place updated one.
             */
            transformer.primaryCoil.removeStamp(equations);
            transformer.primaryCoil.placeStamp(equations);
            transformer.secondaryCoil.removeStamp(equations);
            transformer.secondaryCoil.placeStamp(equations);
        }
    }
}
