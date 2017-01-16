package org.xm.xmnlp.test.seg;


import org.xm.xmnlp.seg.NShortSegment;

/**
 * @author hankcs
 */
public class TestSplitMiddleSlashFromDigitalWords {
    public static void main(String[] args) {
//        System.out.println(Arrays.toString("sd-j".split("-")));
        System.out.println(NShortSegment.parse("3-4月份"));
    }
}
