package org.xm.xmnlp.math.ahocorasick;

/**
 * 路径花费状态
 * Created by mingzai on 2016/7/30.
 */
public class DijkstraState implements Comparable<DijkstraState> {
    /**
     * 路径花费
     */
    public double cost;
    /**
     * 当前位置
     */
    public int vertex;

    @Override
    public int compareTo(DijkstraState o) {
        return Double.compare(cost, o.cost);
    }

    public DijkstraState(double cost, int vertex) {
        this.cost = cost;
        this.vertex = vertex;
    }
}
