package ilit.cirsim.circuit.elements;

import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.stamps.DiodeStamp;
import ilit.cirsim.simulator.stamps.IStampStrategy;

public class Diode extends Component
{
    public static final double IDEALITY_FACTOR_n = 1.0; /** From 1 to 2; 1 - ideal */
    public static final double THERMAL_VOLTAGE_Vt = 0.02585;
    public static final double nVt = IDEALITY_FACTOR_n * THERMAL_VOLTAGE_Vt;
    /** Reverse bias saturation current */
    public static final double I_SATURATION = 1.0;

    @Override
    public IStampStrategy getStamp()
    {
        return DiodeStamp.instance;
    }

    public boolean isNonlinear()
    {
        return true;
    }

    @Override
    public boolean isGroupOne()
    {
        return true;
    }
}
