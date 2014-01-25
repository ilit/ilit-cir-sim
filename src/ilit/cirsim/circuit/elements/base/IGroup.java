package ilit.cirsim.circuit.elements.base;

/**
 * At MNA analysis its matrix has two groups of variables.
 * Group 1 variables are represented in matrix by element
 * effect to nodes and group 2 variables are represented by currents.
 */
public interface IGroup
{
    /**
     * Should this concrete component type be represented in matrix
     * by two node voltages
     * or by its current?
     */
    public boolean isGroupOne();

    public boolean isNonlinear();
}
