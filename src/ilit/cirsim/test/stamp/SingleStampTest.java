package ilit.cirsim.test.stamp;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.test.AbstractAnalysisTest;

public abstract class SingleStampTest extends AbstractAnalysisTest
{
    protected void initComponent(Component component, Node node1, Node node2)
    {
        initCircuit();
        circuit.insertComponent(component, node1, node2, false);
        initAnalysis();
    }
}
