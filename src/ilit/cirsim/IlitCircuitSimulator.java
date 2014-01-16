package ilit.cirsim;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.SampleCircuitGenerator;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.LinearEquationsSolver;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.simulator.MnaEquationsSystem;
import ilit.cirsim.simulator.StampInjector;
import ilit.cirsim.view.GraphVisualization;

import java.util.Collection;

@Singleton
public class IlitCircuitSimulator
{
    @Inject
    public IlitCircuitSimulator(GraphVisualization graphGui,
                                CircuitProxy circuit,
                                MnaEquationsSystem equations,
                                StampInjector stampInjector,
                                LinearEquationsSolver linearEquationsSolver)
    {
        /** Code below is used only for visualization for now */
        new SampleCircuitGenerator().generateSampleGraph(circuit);
        equations.createEmptySystem();

        stampInjector.placeStamps();
        linearEquationsSolver.solve();

        graphGui.show();

        outputSolution(circuit, equations);
    }

    private void outputSolution(CircuitProxy circuit, MnaEquationsSystem equations)
    {
        Collection<Component> sources = circuit.getG2Components();
        System.out.println("");
        for (Component source : sources)
        {
            int index = IdToMatrixIndexRelations.instance.getIndex(source);
            double current = equations.getXVector().get(index);

            System.out.println("Current of " + source.getId() + " is " + current);
        }
        System.out.println("");
    }

    /**
     * Possible optimizations:
     * 1) Matrix reordering for sparsity. (?)
     * 2) Alternative groups of component stamps to provide more diagonal matrix. (?)
     */
}
