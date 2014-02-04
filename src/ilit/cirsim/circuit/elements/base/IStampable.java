package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.simulator.MnaEquationsSystem;

/**
 * Each elements affects circuit.
 * This effects are represented by some variables which are
 * stamped into analysis matrix.
 */
public interface IStampable
{
    public void placeStamp(MnaEquationsSystem equations);
    public void removeStamp(MnaEquationsSystem equations);
}
