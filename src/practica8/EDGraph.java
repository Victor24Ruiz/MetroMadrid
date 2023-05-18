package practica8;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EDGraph<E, W> {

    //returns number of nodes of the graph
    int size();

    //true if the graph has no nodes; false in other case
    boolean isEmpty();

    //inserts a new node with the label item
    boolean insertNode(E item);

    boolean containsNode(E item);

    //inserts a new edge
    boolean insertEdge(E source, E target);

    boolean insertEdge(E source, E target, W weight);

    boolean areAdjacent(E source, E target);

    W getEdgeWeight(E source, E target);

    List<E> getAdjacentNodes(E source);

    List<Edge<E, W>> getAdjacentArcs(E source);

    Set<E> getKeys();

    E removeNode(E source);

    W removeEdge(E source, E target);

    W removeEdge(E source, E dest, W weight);


    //prints the structure
    void printGraphStructure();


}
