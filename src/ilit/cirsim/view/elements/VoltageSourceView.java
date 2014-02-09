package ilit.cirsim.view.elements;

import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.circuit.elements.base.IGraphRenderable;

import java.awt.*;

public class VoltageSourceView implements IGraphRenderable
{
    private final VoltageSource voltageSource;

    public VoltageSourceView(VoltageSource voltageSource)
    {
        this.voltageSource = voltageSource;
    }

    @Override
    public String getGraphLabel()
    {
        return voltageSource.getVoltage() + "V";
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
