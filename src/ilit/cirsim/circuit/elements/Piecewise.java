package ilit.cirsim.circuit.elements;

import ilit.cirsim.simulator.MnaEquationsSystem;

/**
 * Component has piecewise linear model
 */
public interface Piecewise
{
    void setProbeStamp();

    void updateModel(MnaEquationsSystem equations);
}
