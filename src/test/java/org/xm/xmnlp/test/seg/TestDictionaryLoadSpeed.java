package org.xm.xmnlp.test.seg;


import org.xm.xmnlp.dictionary.CoreDictionary;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestDictionaryLoadSpeed extends TestCase {
    public void testCoreDictionary() throws Exception {
        System.out.println(CoreDictionary.get("速度"));
    }

}
