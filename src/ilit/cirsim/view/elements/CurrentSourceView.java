package ilit.cirsim.view.elements;

import ilit.cirsim.circuit.elements.CurrentSource;
import ilit.cirsim.circuit.elements.base.IGraphRenderable;

import java.awt.*;

public class CurrentSourceView implements IGraphRenderable
{
    private final CurrentSource currentSource;

    public CurrentSourceView(CurrentSource currentSource)
    {
        this.currentSource = currentSource;
    }

    @Override
    public String getGraphLabel()
    {
        return currentSource.getCurrent() + "I";
    }

    @Override
    public Color getGraphColor()
    {
        return Color.CYAN;
    }

    @Override
    public float getGraphEdgeWidth()
    {
        return 10f;
    }
}
