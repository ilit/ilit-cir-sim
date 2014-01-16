package ilit.cirsim.view.elements;

import ilit.cirsim.circuit.elements.base.IGraphRenderable;

import java.awt.*;

public class WireView implements IGraphRenderable
{
    public static final WireView instance = new WireView();

    @Override
    public String getGraphLabel()
    {
        return "";
    }

    @Override
    public Color getGraphColor()
    {
        return Color.GRAY;
    }

    @Override
    public float getGraphEdgeWidth()
    {
        return 2f;
    }
}
