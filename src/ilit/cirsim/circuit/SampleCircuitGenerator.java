package ilit.cirsim.circuit;

import ilit.cirsim.circuit.elements.*;
import ilit.cirsim.circuit.elements.base.Component;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Collections;

public class SampleCircuitGenerator
{
    public static final int VSOURCES_AMOUNT = 3;

    public static final int MAX_VOLTAGE = 50;
    public static final int MAX_RESISTANCE = 500;

    public static final int GRID_SIZE = 3; /** In amount of nodes at grid side */
    public static final int ROUND_SCALE = 3;

    private CircuitProxy circuit;

    private MersenneTwister randomizer = new MersenneTwister();
    private Node[][] nodeMatrix = new Node[GRID_SIZE][GRID_SIZE];
    private ArrayList<Component> componentsList = new ArrayList<>();


    public void generateSampleGraph(CircuitProxy circuit)
    {
        this.circuit = circuit;

        fillNodesMatrix();
        generateComponentsList();
        connectComponentsByGridTopology();
        attachGroundToCorners();
    }

    private void attachGroundToCorners()
    {
        insertComponent(new Wire(1), nodeMatrix[0][0], new Ground());
        insertComponent(new Wire(1), nodeMatrix[GRID_SIZE - 1][GRID_SIZE - 1], new Ground());
    }

    private void connectComponentsByGridTopology()
    {
        /** Place components horizontally */
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE - 1; j++)
                placeWiredComponent(nodeMatrix[i][j], nodeMatrix[i][j + 1]);

        /** Place components vertically */
        for (int i = 0; i < GRID_SIZE - 1; i++)
            for (int j = 0; j < GRID_SIZE; j++)
                placeWiredComponent(nodeMatrix[i][j], nodeMatrix[i + 1][j]);
    }

    private void placeWiredComponent(Node node1, Node node2)
    {
        node1 = appendWire(node1);
        node2 = appendWire(node2);
        Component component;
        try
        {
            component = componentsList.remove(0);
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new Error("Not enough components");
        }
        insertComponent(component, node1, node2);
    }

    private void generateComponentsList()
    {
        int numberOfComponents = GRID_SIZE * (GRID_SIZE - 1) * 2;
        for (int i = 0; i < numberOfComponents; i++)
        {
            if (i < VSOURCES_AMOUNT) /** Some sources */
                componentsList.add(new VoltageSource(getNewPropValue(MAX_VOLTAGE)));
            else  /** And the rest are resistors */
                componentsList.add(new Load(getNewPropValue(MAX_RESISTANCE)));
        }

        Collections.shuffle(componentsList);
    }

    private double getNewPropValue(int maxValue)
    {
        return randomizer.nextInt(maxValue) + Precision.round(randomizer.nextDouble(), ROUND_SCALE);
    }

    private void fillNodesMatrix()
    {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++)
                nodeMatrix[i][j] = new Node();
    }

    private Node appendWire(Node subjectNode)
    {
        Component wire = new Wire(1);
        Node wireNode = new Node();
        insertComponent(wire, subjectNode, wireNode);

        return wireNode;
    }

    private void insertComponent(Component component, Node node1, Node node2)
    {
        String componentName = component.getComponentClassName();

        boolean isDirectional = componentName.equals(VoltageSource.class.getSimpleName());

        circuit.insertComponent(component, node1, node2, isDirectional);
    }
}
