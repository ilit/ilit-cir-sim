package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.simulator.stamps.IStampStrategy;

/**
 * Each elements affects circuit.
 * This effects are represented by some variables which are
 * stamped into analysis matrix.
 */
public interface IStampable
{
    public IStampStrategy getStamp();
    public boolean isNonlinear();
}
