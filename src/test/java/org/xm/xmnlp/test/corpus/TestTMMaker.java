package org.xm.xmnlp.test.corpus;

import org.xm.xmnlp.corpus.dictionary.maker.TMDictionaryMaker;

import junit.framework.TestCase;

/**
 * 测试转移矩阵制作工具
 *
 */
public class TestTMMaker extends TestCase {
    public void testCreate() throws Exception {
        TMDictionaryMaker tmDictionaryMaker = new TMDictionaryMaker();
        tmDictionaryMaker.addPair("ab", "cd");
        tmDictionaryMaker.addPair("ab", "cd");
        tmDictionaryMaker.addPair("ab", "Y");
        tmDictionaryMaker.addPair("ef", "gh");
        tmDictionaryMaker.addPair("ij", "kl");
        tmDictionaryMaker.addPair("ij", "kl");
        tmDictionaryMaker.addPair("ij", "kl");
        tmDictionaryMaker.addPair("X", "Y");
        System.out.println(tmDictionaryMaker);
    }
}
