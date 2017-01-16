package org.xm.xmnlp.test.algorithm;


import org.xm.xmnlp.math.LongestCommonSubstring;

import junit.framework.TestCase;

public class LongestCommonSubstringTest extends TestCase {
    String a = "www.baidu.com";
    String b = "www";

    public void testCompute() throws Exception {
        System.out.println(LongestCommonSubstring.compute(a.toCharArray(), b.toCharArray()));
        assertEquals(6, LongestCommonSubstring.compute(a.toCharArray(), b.toCharArray()));
    }

    public void testLongestCommonSubstring() throws Exception {
        System.out.println(LongestCommonSubstring.compute(a, b));
        assertEquals(6, LongestCommonSubstring.compute(a, b));
    }
}