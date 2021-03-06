package ilit.cirsim.circuit;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.circuit.elements.dynamic.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Circuit topology and device composition.
 */
public class CircuitProxy
{
    /**
     * Graph is used only for visualization for now.
     */
    private final SparseMultigraph<Node, Component> circuitGraph;

    /**
     * All components are grouped by three aspects:
     * - Linear or nonlinear(piecewise) model.
     * - Group one or group two. Group two can be only voltage source or its child - a capacitor.
     * - Static or dynamic. The only dynamic components are capacitors and inductors.
     *   Dynamic models implemented are all linear.
     * - Alternating voltage and current sources are also in separate group.
     */
    private final HashMap<Integer, Component> linearRegularComponents = new HashMap<>();
    private final HashMap<Integer, Component> nonlinearComponents = new HashMap<>();
    private final HashMap<Integer, Component> dynamicComponents = new HashMap<>();
    private final HashMap<Integer, Component> alternatingSources = new HashMap<>();
    private final ArrayList<Transformer> transformers = new ArrayList<>();

    private final HashMap<Integer, Node> bearingNodes = new HashMap<>();

    /** Not grounded nodes */

    public CircuitProxy(CircuitGraph circuitGraph)
    {
        this.circuitGraph = circuitGraph.graph;
    }

    /**
     * @return components that are linear and static
     */
    public Collection<Component> getRegularComponents()
    {
        return linearRegularComponents.values();
    }

    public Collection<Component> getNonlinearComponents()
    {
        return nonlinearComponents.values();
    }

    public Collection<Component> getDynamicComponents()
    {
        return dynamicComponents.values();
    }

    public Collection<Component> getAlternatingSources()
    {
        return alternatingSources.values();
    }

    public ArrayList<Transformer> getTransformers()
    {
        return transformers;
    }

    public void insertComponent(Component component, Node anode, Node cathode, boolean directional)
    {
        /** Tie nodes to this component */
        component.anode = anode;
        component.cathode = cathode;

        /** Save non-ground nodes map */
        saveNotGroundedNode(anode);
        saveNotGroundedNode(cathode);

        if (component.isDynamic())
            dynamicComponents.put(component.getId(), component);
        else if (component.isNonlinear())
            nonlinearComponents.put(component.getId(), component);
        else if (component.isAlternatingSource())
            alternatingSources.put(component.getId(), component);
        else
            linearRegularComponents.put(component.getId(), component);

        /**
         * Insert into graph.
         * Used only to display.
         */
        if (directional)
            circuitGraph.addEdge(component, anode, cathode, EdgeType.DIRECTED);
        else
            circuitGraph.addEdge(component, anode, cathode, EdgeType.UNDIRECTED);
    }

    public void insertTransformer(Transformer transformer, Node n0, Node n1, Node n2, Node n3)
    {
        /** Tie nodes to this component */
        transformer.primaryCoil.anode = n0;
        transformer.primaryCoil.cathode = n2;
        transformer.secondaryCoil.anode = n1;
        transformer.secondaryCoil.cathode = n3;

        insertComponent(transformer.primaryCoil, n0, n2, false);
        insertComponent(transformer.secondaryCoil, n1, n3, false);

        transformers.add(transformer);
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
}
