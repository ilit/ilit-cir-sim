package ilit.cirsim.simulator;

import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Component;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

/**
 * Saves relations map: MNA matrix index <-> component or node id.
 *
 * Reference is either node id in case of group 1 element
 * or component id in case of group 2 element.
 *
 * Data contained by the reference is components _effect_ on node voltages in case of group 1 element
 * and onto current in case of group 2 element.
 */
public class IdToMatrixIndexRelations
{
    public static IdToMatrixIndexRelations instance = new IdToMatrixIndexRelations();

    /**
     * Variable <-> Matrix index relation.
     * Bidirectional Map. Stores two hash maps.
     * Key - matrix or side vector index.
     * Value - node or component id.
     */
    private DualHashBidiMap<Integer, Integer> indexVariableRelations = new DualHashBidiMap<>();

    public void reset()
    {
        indexVariableRelations = new DualHashBidiMap<>();
    }

    public int getIndex(int id)
    {
        // TODO private
        Integer index = indexVariableRelations.getKey(id);
        if (index == null)
            index = createNewIndexAssociatedWithId(id);

        return index;
    }

    public int getIndex(Node node)
    {
        return getIndex(node.getId());
    }

    public int getIndex(Component component)
    {
        return getIndex(component.getId());
    }

    private int createNewIndexAssociatedWithId(int id)
    {
        int index = getLastUnusedIndex();
        indexVariableRelations.put(index, id);

        return index;
    }

    private int getLastUnusedIndex()
    {
        return indexVariableRelations.size();
    }
}
