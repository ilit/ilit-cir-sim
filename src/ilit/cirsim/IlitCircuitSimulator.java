package ilit.cirsim;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ilit.cirsim.circuit.CircuitProxy;
import ilit.cirsim.circuit.SampleCircuitGenerator;
import ilit.cirsim.simulator.Analysis;
import ilit.cirsim.view.GraphVisualization;

@Singleton
public class IlitCircuitSimulator
{
    @Inject
    public IlitCircuitSimulator(GraphVisualization graphGui,
                                CircuitProxy circuit,
                                Analysis analysis)
    {
        /** Code below is used only for visualization for now */
        new SampleCircuitGenerator().generateSampleGraph(circuit);
        graphGui.show();
    }
}
