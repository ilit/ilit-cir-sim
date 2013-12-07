package ilit.cirsim.view.elements;

import ilit.cirsim.circuit.elements.base.IGraphRenderable;

import java.awt.*;

public class VoltageSourceView implements IGraphRenderable
{
    public static VoltageSourceView instance = new VoltageSourceView();

    @Override
    public String getGraphLabel()
    {
        return "V";
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
