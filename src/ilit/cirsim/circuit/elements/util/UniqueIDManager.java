package ilit.cirsim.circuit.elements.util;

/**
 * Allocates IDs for nodes and components
 */
public class UniqueIDManager
{
    public static int GROUND_NODE_ID = 0;

    public static UniqueIDManager instance = new UniqueIDManager();

    private int lastUnassignedComponent = 1;

    public int getNewID()
    {
        return lastUnassignedComponent++;
    }

    public void reset()
    {
        lastUnassignedComponent = 1;
    }
}
