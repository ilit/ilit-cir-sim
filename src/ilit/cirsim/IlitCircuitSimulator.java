package ilit.cirsim;

import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.SampleCircuitGenerator;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.SolverWrapper;
import ilit.cirsim.view.GraphVisualization;

public class IlitCircuitSimulator
{
    public IlitCircuitSimulator(GraphVisualization graphGui,
                                CircuitProxy circuit,
                                MnaEquationsSystem equations,
                                SolverWrapper solver)
    {
        /** Code below is used only for visualization for now */
        new SampleCircuitGenerator().generateSampleGraph(circuit);
        equations.prepareSystemSize();

        solver.solve();

        graphGui.show();

        outputSolution(circuit, equations);
    }

    private void outputSolution(CircuitProxy circuit, MnaEquationsSystem equations)
    {
        /*
        Collection<Component> sources = circuit.getG2LinearComponents();
        System.out.println("");
        for (Component source : sources)
        {
            double current = equations.getSolution(source);

            System.out.println("Current of " + source.getId() + " is " + current);
        }
        System.out.println("");
        */
    }

    /**
     * Possible optimizations:
     * 1) Matrix reordering for sparsity. (?)
     * 2) Alternative groups of component stamps to provide more diagonal matrix. (?)
     */
}
