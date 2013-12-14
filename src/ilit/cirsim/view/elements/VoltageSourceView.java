package ilit.cirsim.view.elements;

import ilit.cirsim.circuit.elements.VoltageSource;
import ilit.cirsim.circuit.elements.base.IGraphRenderable;
import ilit.cirsim.circuit.elements.base.Resistor;

import java.awt.*;

public class VoltageSourceView implements IGraphRenderable
{
    private VoltageSource voltageSource;

    public VoltageSourceView(VoltageSource voltageSource)
    {
        this.voltageSource = voltageSource;
    }

    @Override
    public String getGraphLabel()
    {
        return voltageSource.dcV + "V";
    }

    @Override
    public Color getGraphColor()
    {
        return Color.GREEN;
    }

    @Override
    public float getGraphEdgeWidth()
    {
        return 10f;
    }
}
