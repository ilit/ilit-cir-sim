package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.stamps.IStampStrategy;

import java.util.Collection;

@Singleton
public class Analysis
{
    /** Holds circuit topology\composition */
    private CircuitProxy circuit;

    MnaEquationsSystem equations;

    @Inject
    public Analysis(CircuitProxy circuit, MnaEquationsSystem equations)
    {
        this.circuit = circuit;
        this.equations = equations;

        /**
         * Places all component stamps into matrix.
         *
         * The contribution of every element to the matrix equation is described by means
         * of a template, which is called an element stamp.
         */
        placeGroupedStamps();
    }

    private void placeGroupedStamps()
    {
        placeStamps(circuit.getG1Components());
        placeStamps(circuit.getG2Components());
    }

    private void placeStamps(Collection<Component> components)
    {
        for (Component component : components)
        {
            IStampStrategy stamper = component.getStamp();
            stamper.setStamp(equations, component);
        }
    }
}
