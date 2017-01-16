package org.xm.xmnlp.dependency.common;

/**
 * @author hankcs
 */
public class State implements Comparable<State> {
    public float cost;
    public int id;
    public Edge edge;

    public State(float cost, int id, Edge edge) {
        this.cost = cost;
        this.id = id;
        this.edge = edge;
    }

    @Override
    public int compareTo(State o) {
        return Float.compare(cost, o.cost);
    }
}
