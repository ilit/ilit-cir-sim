package ilit.cirsim.simulator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.elements.Piecewise;
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

    /**
     * Places all component stamps into matrix.
     *
     * The contribution of every element to the matrix equation is described by means
     * of a template, which is called an element stamp.
     */
    public void placeNonlinearProbeStamps()
    {
        /**
         * Set all nonlinear components to return probing resistances stamps
         * instead of working linear segments.
         */
        for (Component component : circuit.getG1NonlinearComponents())
        {
            Piecewise piecewiseComponent = (Piecewise) component;
            piecewiseComponent.setProbeStamp();
        }

        placeLinearStamps(circuit.getG1NonlinearComponents());
    }

    private void placeLinearGroupedStamps()
    {
        placeLinearStamps(circuit.getG1LinearComponents());
        placeLinearStamps(circuit.getG2LinearComponents());
    }

    private void placeLinearStamps(Collection<Component> components)
    {
        for (Component component : components)
            placeLinearStamp(component);
    }

    private void placeLinearStamp(Component component)
    {
        if (component == null)
            throw new Error("component == null");

        IStampStrategy stamper = component.getStamp();

        if (stamper == null)
            throw new Error("stamper == null");
        if (equations == null)
            throw new Error("equations == null");

        stamper.setStamp(equations, component);
    }
}
