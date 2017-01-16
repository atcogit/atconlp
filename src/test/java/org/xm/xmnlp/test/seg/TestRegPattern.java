package org.xm.xmnlp.test.seg;

import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * 测试正则匹配数词
 *
 * @author hankcs
 */
public class TestRegPattern extends TestCase {
    public void testPattern() {
        assertEquals(true, Pattern.compile("^(-?\\d+)(\\.\\d+)?$").matcher("2014").matches());  // 浮点数
        assertEquals(true, Pattern.compile("^(-?\\d+)(\\.\\d+)?$").matcher("-2014").matches());  // 浮点数
        assertEquals(true, Pattern.compile("^(-?\\d+)(\\.\\d+)?$").matcher("20.14").matches());  // 浮点数
    }
}
