package ilit.cirsim.circuit.elements.base;

import ilit.cirsim.simulator.MnaEquationsSystem;

public interface IDynamic
{
    public void updateCompanionModel(MnaEquationsSystem equations);
}
