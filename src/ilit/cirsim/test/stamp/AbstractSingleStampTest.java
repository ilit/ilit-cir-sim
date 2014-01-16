package ilit.cirsim.test.stamp;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.test.AbstractStampTest;

public abstract class AbstractSingleStampTest extends AbstractStampTest
{
    protected void initComponent(Component component, Node anode, Node cathode)
    {
        initEmptyCircuit();
        circuit.insertComponent(component, anode, cathode, false);
        placeLinearStamps();
    }
}
