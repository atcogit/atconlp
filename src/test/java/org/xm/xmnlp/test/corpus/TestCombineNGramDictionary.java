package org.xm.xmnlp.test.corpus;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dictionary.maker.TFDictionary;

import junit.framework.TestCase;

/**
 * 测试合并多个NGram词典
 *
 * @author hankcs
 */
public class TestCombineNGramDictionary extends TestCase {
    public void testCombine() throws Exception {
        System.out.println(TFDictionary.combine(Xmnlp.Config.BiGramDictionaryPath, "XXXDictionary.ngram.txt"));
    }
}
