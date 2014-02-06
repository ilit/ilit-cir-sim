package ilit.cirsim.circuit.elements.util;

/**
 * Allocates IDs for nodes and components
 */
public class UniqueIDManager
{
    /**
     * 0 value is stored anyway after integer initiation.
     * Keep different to distinguish ground from just initiated id while debugging.
     */
    public static final int GROUND_NODE_ID = -1;

    public static final UniqueIDManager instance = new UniqueIDManager();

    private int lastUnassignedId = 1;

    public int getNewID()
    {
        int newId = lastUnassignedId++;

        return newId;
    }

    public void reset()
    {
        lastUnassignedId = 1;
    }
}
