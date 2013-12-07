package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IGraphRenderable;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.view.elements.WireView;

public class Wire extends Resistor
{
    public Wire(double resistance)
    {
        super(resistance);
    }

    public IGraphRenderable getView()
    {
        return WireView.instance;
    }
}
