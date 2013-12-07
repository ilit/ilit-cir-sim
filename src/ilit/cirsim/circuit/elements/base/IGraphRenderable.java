package ilit.cirsim.circuit.elements.base;

import java.awt.*;

/**
 * Can be drawn by JUNG visualization
 */
public interface IGraphRenderable
{
    public String getGraphLabel();
    public Color getGraphColor();
    public float getGraphEdgeWidth();
}
