package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;

/**
 * The contribution of every element to the matrix equation is described by means
 * of a template, which is called an element stamp.
 */
public interface IStampStrategy
{
    public void setStamp(MnaEquationsSystem equationsSystem, Component component);
}
