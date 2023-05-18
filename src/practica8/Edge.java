package practica8;

import java.util.Objects;

public class Edge<E, W> {
    private E target;
    private W weight;

    public Edge(E t, W w) {
        target = t;
        weight = w;
    }

    public Edge(E t) {
        target = t;
        weight = null;
    }

    public E getTarget() {
        return target;
    }

    public void setTarget(E target) {
        this.target = target;
    }

    public W getWeight() {
        return weight;
    }

    public W setWeight(W wei) {
        W res = weight;
        weight = wei;
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?, ?> edge = (Edge<?, ?>) o;
        if (Objects.equals(target, edge.target)) {
            if (weight != null && edge.weight != null)
                return Objects.equals(weight, edge.weight);
            else
                return true;
        } else
            return false;

    }

    @Override
    public int hashCode() {
        return Objects.hash(target, weight);
    }
}
