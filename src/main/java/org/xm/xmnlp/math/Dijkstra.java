package org.xm.xmnlp.math;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.xm.xmnlp.math.ahocorasick.DijkstraState;
import org.xm.xmnlp.seg.domain.EdgeFrom;
import org.xm.xmnlp.seg.domain.Graph;
import org.xm.xmnlp.seg.domain.Vertex;

/**
 * 最短路径
 */
public class Dijkstra {
    public static List<Vertex> compute(Graph graph) {
        List<Vertex> resultList = new LinkedList<Vertex>();
        Vertex[] vertexes = graph.getVertexes();
        List<EdgeFrom>[] edgesTo = graph.getEdgesTo();
        double[] d = new double[vertexes.length];
        Arrays.fill(d, Double.MAX_VALUE);
        d[d.length - 1] = 0;
        int[] path = new int[vertexes.length];
        Arrays.fill(path, -1);
        PriorityQueue<DijkstraState> que = new PriorityQueue<DijkstraState>();
        que.add(new DijkstraState(0, vertexes.length - 1));
        while (!que.isEmpty()) {
            DijkstraState p = que.poll();
            if (d[p.vertex] < p.cost) continue;
            for (EdgeFrom edgeFrom : edgesTo[p.vertex]) {
                if (d[edgeFrom.from] > d[p.vertex] + edgeFrom.weight) {
                    d[edgeFrom.from] = d[p.vertex] + edgeFrom.weight;
                    que.add(new DijkstraState(d[edgeFrom.from], edgeFrom.from));
                    path[edgeFrom.from] = p.vertex;
                }
            }
        }
        for (int t = 0; t != -1; t = path[t]) {
            resultList.add(vertexes[t]);
        }
        return resultList;
    }
}
