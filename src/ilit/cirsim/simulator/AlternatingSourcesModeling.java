package ilit.cirsim.simulator;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.base.IAlternatingSource;
import ilit.cirsim.circuit.elements.base.IDynamic;

/**
 * Replaces alternating voltage and current sources
 * ideal DC voltage and current sources each time step.
 */
class AlternatingSourcesModeling
{
    public void updatedAndPlaceStamps(MnaEquationsSystem equations, CircuitProxy circuit,
                                      double timeStep)
    {
        for (Component component : circuit.getAlternatingSources())
        {
            IAlternatingSource alternatingSource = (IAlternatingSource) component;
            alternatingSource.updateModel(timeStep);
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
