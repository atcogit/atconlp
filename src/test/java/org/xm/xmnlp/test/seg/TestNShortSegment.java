package org.xm.xmnlp.test.seg;


import org.xm.xmnlp.seg.NShortSegment;

/**
 * 测试N-最短路径分词
 *
 * @author hankcs
 */
public class TestNShortSegment {
    public static void main(String[] args) {
        System.out.println(NShortSegment.parse("商品和服务"));
    }
}
