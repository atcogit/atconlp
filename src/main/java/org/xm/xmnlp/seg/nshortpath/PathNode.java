package org.xm.xmnlp.seg.nshortpath;

/**
 * 路径上的节点
 */
public class PathNode {
    /**
     * 节点前驱
     */
    public int from;

    /**
     * 节点在顶点数组中的下标
     */
    public int index;

    /**
     * 构造一个节点
     *
     * @param from  节点前驱
     * @param index 节点在顶点数组中的下标
     */
    public PathNode(int from, int index) {
        this.from = from;
        this.index = index;
    }

    @Override
    public String toString() {
        return "PathNode{" +
                "from=" + from +
                ", index=" + index +
                '}';
    }
}
