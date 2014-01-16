package ilit.cirsim.circuit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;

import java.util.Collection;
import java.util.HashMap;

/**
 * Circuit topology and device composition.
 */
@Singleton
public class CircuitProxy
{
    /**
     * Graph is used only for visualization for now.
     */
    public SparseMultigraph<Node, Component> circuitGraph;

    /**
     * Maps are used only for circuit analysis.
     */
    private HashMap<Integer, Node> bearingNodes = new HashMap<>(); /** Not grounded nodes */
    private HashMap<Integer, Component> componentsGroupOne = new HashMap<>();
    private HashMap<Integer, Component> componentsGroupTwo = new HashMap<>();

    @Inject
    public CircuitProxy(CircuitGraph circuitGraph)
    {
        this.circuitGraph = circuitGraph.graph;
    }

    public void insertComponent(Component component, Node anode, Node cathode, boolean directional)
    {
        /** Tie nodes to this component */
        component.anode = anode;
        component.cathode = cathode;

        /** Save non-ground nodes map */
        saveNotGroundedNode(anode);
        saveNotGroundedNode(cathode);

        if (component.isGroupOne())
            componentsGroupOne.put(component.getId(), component);
        else
            componentsGroupTwo.put(component.getId(), component);

        /**
         * Insert into graph.
         * Used only to display.
         */
        if (directional)
            circuitGraph.addEdge(component, anode, cathode, EdgeType.DIRECTED);
        else
            circuitGraph.addEdge(component, anode, cathode, EdgeType.UNDIRECTED);
    }

    public int bearingNodesAmount() /** non-ground */
    {
        return bearingNodes.size();
    }

    private void saveNotGroundedNode(Node node)
    {
        if (!node.isGround())
            bearingNodes.put(node.getId(), node);
    }

    public Collection<Component> getG1Components()
    {
        return componentsGroupOne.values();
    }

    public Collection<Component> getG2Components()
    {
        return componentsGroupTwo.values();
    }

    public boolean isCircuitNonlinear()
    {
        for (Component component : getG1Components())
        {
            if (component.isNonlinear())
                return true;
        }
        return false;
    }
}
