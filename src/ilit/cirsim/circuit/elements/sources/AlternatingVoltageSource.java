package ilit.cirsim.circuit.elements.sources;

import ilit.cirsim.circuit.elements.base.IAlternatingSource;
import ilit.cirsim.view.elements.VoltageSourceView;

public class AlternatingVoltageSource extends VoltageSource implements IAlternatingSource
{
    private static final double INITIAL_VOLTAGE = 0;

    private double amplitude; /** peak voltage */
    private double frequency; /** f */
    private double period;    /** T */
    private double time;      /** t */

    public AlternatingVoltageSource(double amplitude, double frequency)
    {
        super(INITIAL_VOLTAGE);

        this.amplitude = amplitude;
        this.frequency = frequency;

        period = 1 / frequency;
        time = 0;

        view = new VoltageSourceView(this);
    }

    public void updateModel(double timeStep)
    {
        time += timeStep;
        voltage = amplitude * Math.sin(2 * Math.PI * frequency * time);

        /** Avoid time floating point variable becoming too large when much time passes */
        if (time > period)
            time -= period;
    }

    @Override
    public boolean isAlternatingSource()
    {
        return true;
    }
}
