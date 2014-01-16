package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.stamps.IStampStrategy;

import java.util.Collection;

@Singleton
public class StampInjector
{
    /** Holds circuit topology\composition */
    private final CircuitProxy circuit;
    private final MnaEquationsSystem equations;

    @Inject
    public StampInjector(CircuitProxy circuit, MnaEquationsSystem equations)
    {
        this.circuit = circuit;
        this.equations = equations;
    }

    /**
     * Places all component stamps into matrix.
     *
     * The contribution of every element to the matrix equation is described by means
     * of a template, which is called an element stamp.
     */
    public void placeLinearStamps()
    {
        placeLinearGroupedStamps();
    }

    private void placeLinearGroupedStamps()
    {
        placeLinearStamps(circuit.getG1Components());
        placeLinearStamps(circuit.getG2Components());
    }

    private void placeLinearStamps(Collection<Component> components)
    {
        for (Component component : components)
        {
            IStampStrategy stamper = component.getStamp();
            stamper.setStamp(equations, component);
        }
    }
}
