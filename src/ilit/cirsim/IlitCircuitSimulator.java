package ilit.cirsim;

import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.SampleCircuitGenerator;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.SolverWrapper;
import ilit.cirsim.view.GraphVisualization;

import java.util.Collection;

public class IlitCircuitSimulator
{
    public IlitCircuitSimulator()
    {
        CircuitGraph circuitGraph = new CircuitGraph();

        GraphVisualization graphGui = new GraphVisualization(circuitGraph);
        CircuitProxy circuit = new CircuitProxy(circuitGraph);

        MnaEquationsSystem equations = new MnaEquationsSystem(circuit);
        SolverWrapper solver = new SolverWrapper(circuit, equations);

        new SampleCircuitGenerator().generateSampleGraph(circuit);

        equations.prepareSystemSize();

        solver.solve();

        graphGui.show();

        outputSolution(circuit, equations);
    }

    private void outputSolution(CircuitProxy circuit, MnaEquationsSystem equations)
    {
        Collection<Component> components = circuit.getRegularComponents();
        System.out.println("");
        for (Component component : components)
        {
            if (!component.isGroupOne())
            {
                double current = equations.getSolution(component);
                System.out.println("Current of " + component.getId() + " is " + current);
            }
        }
        System.out.println("");
    }

    /**
     * Possible optimizations:
     * 1) Matrix reordering for sparsity. (?)
     * 2) Alternative groups of component stamps to provide more diagonal matrix. (?)
     */
}
