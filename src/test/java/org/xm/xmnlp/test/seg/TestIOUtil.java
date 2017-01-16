package org.xm.xmnlp.test.seg;


import org.xm.xmnlp.util.IOUtil;

/**
 * 测试IO
 *
 * @author hankcs
 */
public class TestIOUtil {
    public void testSaveTxt() throws Exception {
        String path = "data/out.txt";
        String content = "你好123\nabc";
        System.out.println(IOUtil.saveTxt(path, content));
    }
}
