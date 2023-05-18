package practica8;

import java.util.*;

public class EDTreeGraph<E extends Comparable<E>, W> implements EDGraph<E, W> {

    private TreeMap<E, List<Edge<E, W>>> nodes;
    private boolean isdirected; //if true, directed graph
    private boolean isweighted;

    public EDTreeGraph() {
        this.nodes = new TreeMap<>();
        isdirected = false;
        isweighted = false;
    }

    public EDTreeGraph(boolean dir) {
        this.nodes = new TreeMap<>();
        isdirected = dir;
        isweighted = false;
    }

    public EDTreeGraph(boolean dir, boolean wei) {
        this.nodes = new TreeMap<>();
        isdirected = dir;
        isweighted = wei;
    }

    @Override
    public int size() {
        return nodes.keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public boolean insertNode(E item) {
        if (nodes.containsKey(item))
            return false;
        nodes.put(item, new LinkedList<>());
        return true;
    }

    @Override
    public boolean insertEdge(E source, E target) {
        if (!nodes.containsKey(source))
            nodes.put(source, new LinkedList<>());
        if (!nodes.containsKey(target))
            nodes.put(target, new LinkedList<>());
        List<Edge<E, W>> arcos = nodes.get(source);
        Edge<E, W> edge = new Edge<>(target);
        if (!arcos.contains(edge)) {
            arcos.add(edge);
            if (!this.isdirected) {
                Edge<E, W> reverse = new Edge<>(source);
                nodes.get(target).add(reverse);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean insertEdge(E source, E target, W weight) {
        if (!nodes.containsKey(source))
            nodes.put(source, new LinkedList<>());
        if (!nodes.containsKey(target))
            nodes.put(target, new LinkedList<>());
        List<Edge<E, W>> arcos = nodes.get(source);
        Edge<E, W> edge = new Edge<>(target, weight);
        if (!arcos.contains(edge)) {
            arcos.add(edge);
            if (!this.isdirected) {
                Edge<E, W> reverse = new Edge<>(source, weight);
                nodes.get(target).add(reverse);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean containsNode(E source) {
        return nodes.containsKey(source);
    }

    @Override
    public boolean areAdjacent(E source, E target) {
        if (!nodes.containsKey(source))
            return false;
        Edge<E, W> edge = new Edge<>(target);
        return nodes.get(source).contains(edge);
    }

    @Override
    public W getEdgeWeight(E source, E target) {
      // TODO EJECICIO 1
        if (areAdjacent(source, target)) {
            List<Edge<E, W>> arcos = getAdjacentArcs(source);
            for (Edge<E, W> arco : arcos) {
                E destino = arco.getTarget();
                if (target.equals(destino))
                    return arco.getWeight();
            }
        }
        return null;
    }

    @Override
    public List<E> getAdjacentNodes(E source) {
        if (!nodes.containsKey(source))
            return null;
        List<Edge<E, W>> adjacents = nodes.get(source);
        List<E> res = new ArrayList<>();
        for (Edge<E, W> adjacent : adjacents)
            res.add(adjacent.getTarget());
        return res;
    }

    @Override
    public List<Edge<E, W>> getAdjacentArcs(E source) {
        if (!nodes.containsKey(source))
            return null;
        return nodes.get(source);

    }

    @Override
    public E removeNode(E source) {
        if (!nodes.containsKey(source))
            return null;
        List<Edge<E, W>> adjacents = nodes.get(source);
        if (!isdirected) {
            for (Edge<E, W> ed : adjacents) {
                E target = ed.getTarget();
                W weight = ed.getWeight();
                Edge<E, W> reverse = new Edge<>(source, weight);
                nodes.get(target).remove(reverse);
            }
        } else {
            Edge<E, W> reverse = new Edge<>(source);
            for (E key : nodes.keySet())
                nodes.get(key).remove(reverse);
        }
        nodes.remove(source);
        return source;
    }

    @Override
    public W removeEdge(E source, E target) {
        if (!nodes.containsKey(source))
            return null;
        W weight = this.getEdgeWeight(source, target);
        if (weight != null) {
            Edge<E, W> edge = new Edge<>(target, weight);
            nodes.get(source).remove(edge);
        }
        return weight;
    }

    @Override
    public W removeEdge(E source, E target, W weight) {
        if (!nodes.containsKey(source))
            return null;
        Edge<E, W> edge = new Edge<>(target, weight);
        nodes.get(source).remove(edge);
        return weight;
    }

    @Override
    public Set<E> getKeys() {
        return nodes.keySet();
    }


    @Override
    public void printGraphStructure() {
        for (E source : nodes.keySet()) {
            System.out.print(source.toString() + ": {");
            for (Edge<E, W> edge : nodes.get(source)) {
                System.out.print("(" + edge.getTarget().toString() + ", " + edge.getWeight().toString() + ")->");
            }
            System.out.println("}");
        }

    }


}
