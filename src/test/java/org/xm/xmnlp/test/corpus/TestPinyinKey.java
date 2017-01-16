package org.xm.xmnlp.test.corpus;

import org.xm.xmnlp.math.LongestCommonSubstring;
import org.xm.xmnlp.scorer.pinyin.PinyinKey;

import junit.framework.TestCase;

public class TestPinyinKey extends TestCase {
    public void testConstruct() throws Exception {
        PinyinKey pinyinKeyA = new PinyinKey("专题分析");
        PinyinKey pinyinKeyB = new PinyinKey("教室资格");
        System.out.println(pinyinKeyA);
        System.out.println(pinyinKeyB);
        System.out.println(LongestCommonSubstring.compute(pinyinKeyA.getFirstCharArray(), pinyinKeyB.getFirstCharArray()));
    }
}
