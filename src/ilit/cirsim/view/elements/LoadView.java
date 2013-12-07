package ilit.cirsim.view.elements;

import ilit.cirsim.circuit.elements.base.IGraphRenderable;
import ilit.cirsim.circuit.elements.base.Resistor;

import java.awt.*;

public class LoadView implements IGraphRenderable
{
    private Resistor resistor;

    public LoadView(Resistor resistor)
    {
        this.resistor = resistor;
    }

    @Override
    public String getGraphLabel()
    {
        return "" + resistor.getResistance();
    }

    @Override
    public Color getGraphColor()
    {
        return Color.RED;
    }

    @Override
    public float getGraphEdgeWidth()
    {
        return 10f;
    }
}
