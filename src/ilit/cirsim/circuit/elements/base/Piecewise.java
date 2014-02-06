package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.simulator.MnaEquationsSystem;

/**
 * Component has piecewise linear model
 */
public interface Piecewise
{
    void setProbeModel();

    /** @return boolean - did model change? */
    boolean updateModel(MnaEquationsSystem equations);

    boolean isModelDefined();
}
