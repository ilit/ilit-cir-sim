package ilit.cirsim.circuit.elements.util;

import java.util.ArrayList;

public class StampMemento
{
    ArrayList<StampMementoTriplet> matrixLines = new ArrayList<>(0);
    ArrayList<StampMementoTuple> rhsLines = new ArrayList<>(0);

    public void matrixAdd(int i1, int i2, double value)
    {
        matrixLines.add(new StampMementoTriplet(i1, i2, value));
    }

    public void rhsAdd(int i, double value)
    {
        rhsLines.add(new StampMementoTuple(i, value));
    }

    public ArrayList<StampMementoTriplet> getMatrixLines()
    {
        return matrixLines;
    }

    public ArrayList<StampMementoTuple> getRhsLines()
    {
        return rhsLines;
    }
}
