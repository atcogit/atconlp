package org.xm.xmnlp.dependency.common;

/**
 * 一条边
 */
public class Edge {
    public int from;
    public int to;
    public float cost;
    public String label;

    public Edge(int from, int to, String label, float cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.label = label;
    }
}
