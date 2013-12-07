package ilit.cirsim.circuit;

import com.google.inject.Singleton;
import edu.uci.ics.jung.graph.SparseMultigraph;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;

@Singleton
public class CircuitGraph
{
    /**
     * Graph by JUNG framework.
     *
     * Graph vertex - numbered node.
     * Graph edge - electronic component. Resistive wire or device.
     */
    public SparseMultigraph<Node, Component> graph = new SparseMultigraph<>();
}
