package ilit.cirsim.circuit;

import edu.uci.ics.jung.graph.util.Pair;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.Wire;
import ilit.cirsim.circuit.elements.base.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SampleCircuitGenerator
{
    public static final int RESISTORS_COUNT = 3;

    private CircuitProxy circuit;

    public void generateSampleGraph(CircuitProxy circuit)
    {
        this.circuit = circuit;

        List<Pair<Node>> devices = new ArrayList<Pair<Node>>();

        devices.add(createGenerator());
        createResistorsArray(devices);

        connectParallel(devices);
    }

    private void connectParallel(List<Pair<Node>> devices)
    {
        for (int i = 0; i < devices.size() - 1; i++)
        {
            Pair<Node> attachment = devices.get(i);
            Pair<Node> receiver = devices.get(i + 1);

            connectByWire(attachment, receiver);
        }
    }

    private void connectByWire(Pair<Node> attachment, Pair<Node> receiver)
    {
        Wire wire1 = new Wire(1);
        Wire wire2 = new Wire(1);
        insertComponent(wire1, attachment.getFirst(), receiver.getFirst());
        insertComponent(wire2, attachment.getSecond(), receiver.getSecond());
    }

    private Pair<Node> createGenerator()
    {
        return createWiredComponent(new VoltageSource(1.0, 120));
    }

    private void createResistorsArray(Collection<Pair<Node>> devices)
    {
        for (int i = 0; i < RESISTORS_COUNT; i++)
        {
            devices.add(createWiredComponent(new Load(140 + i)));
        }
    }

    private Pair<Node> createWiredComponent(Component component)
    {
        Pair<Node> resistorNodes = insertComponent(component);

        Node wireEnd1 = appendWire(resistorNodes.getFirst());
        Node wireEnd2 = appendWire(resistorNodes.getSecond());

        return new Pair<Node>(wireEnd1, wireEnd2);
    }

    private Node appendWire(Node subjectNode)
    {
        Component wire = new Wire(1);
        Node wireNode = new Node();
        insertComponent(wire, subjectNode, wireNode);

        return wireNode;
    }


    private Pair<Node> insertComponent(Component component)
    {
        Node node1 = new Node();
        Node node2 = new Node();

        insertComponent(component, node1, node2);

        return new Pair<Node>(node1, node2);
    }

    private void insertComponent(Component component, Node node1, Node node2)
    {
        String componentName = component.getComponentClassName();

        boolean isDirectional = componentName.equals(VoltageSource.class.getSimpleName());

        circuit.insertComponent(component, node1, node2, isDirectional);
    }
}
