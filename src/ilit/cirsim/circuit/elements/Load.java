package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.IGraphRenderable;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.view.elements.LoadView;

public class Load extends Resistor
{
    private LoadView view;

    public Load(double resistance)
    {
        super(resistance);

        view = new LoadView(this);
    }

    public IGraphRenderable getView()
    {
        return view;
    }
}
