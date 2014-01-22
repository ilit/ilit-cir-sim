package ilit.cirsim.simulator.stamps;

import ilit.cirsim.circuit.elements.Diode;
import ilit.cirsim.circuit.elements.base.Component;
import ilit.cirsim.simulator.MnaEquationsSystem;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * Linearized stamp.
 * Shockley diode model.
 */
public class DiodeStamp extends AbstractStamp
{
    public static final DiodeStamp instance = new DiodeStamp();

    public void setStamp(MnaEquationsSystem equationsSystem, Component diode)
    {
        Matrix matrix = equationsSystem.getMatrix();
        DenseVector xVector = equationsSystem.getXVector();
        SparseVector sideVector = equationsSystem.getSideVector();

        if (matrix == null)
           throw new Error("equationsSystem.getMatrix() == null");
        if (xVector == null)
            throw new Error("equationsSystem.getXVector() == null");
        if (sideVector == null)
            throw new Error("equationsSystem.getSideVector() == null");

        /** Voltage across from last Newton iteration */
        double anodeV = xVector.get(getIndex(diode.anode));
        double cathodeV = xVector.get(getIndex(diode.cathode));
        double Vd = anodeV - cathodeV;
        /**
         * Conductance
         * Geq = g'(v)
         */
        double Geq = (Diode.I_SATURATION / Diode.nVt) * Math.exp(Vd / Diode.nVt);
        /** g(v) */
        double gv = Diode.I_SATURATION * (Math.exp(Vd / Diode.nVt) - 1);
        double Ieq = gv - Geq;

        if (diode.anode.isGround() || diode.cathode.isGround())
        {
            throw new Error("Grounded diode model not implemented");
        }
        else
        {
            int i1 = getIndex(diode.anode);
            int i2 = getIndex(diode.cathode);

            matrix.add(i1, i1, Geq);
            matrix.add(i2, i1, -Geq);
            matrix.add(i2, i2, Geq);
            matrix.add(i1, i2, -Geq);
            sideVector.add(i1, -Ieq);
            sideVector.add(i2, Ieq);
        }
    }
}
