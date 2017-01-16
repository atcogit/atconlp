package org.xm.xmnlp.test.seg;


import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.DijkstraSegment;
import org.xm.xmnlp.seg.domain.Term;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestDijkstra extends TestCase {
    public void testSeg() throws Exception {
        String text = "商品与服务";
        DijkstraSegment segment = new DijkstraSegment();
        List<Term> resultList = segment.seg(text);
        System.out.println(resultList);
    }

    public void testNameRecognize() throws Exception {
        DijkstraSegment segment = new DijkstraSegment();
        Xmnlp.Config.enableDebug();
        System.out.println(segment.seg("妈蛋，你认识mm老师吗？"));
    }

    public void testFixResult() throws Exception {
        DijkstraSegment segment = new DijkstraSegment();
        Xmnlp.Config.enableDebug();
        System.out.println(segment.seg("2014年"));
    }
}
