package org.xm.xmnlp.corpus.dictionary.maker;


import org.xm.xmnlp.util.Predefine;

/**
 * 做一个简单的封装
 */
public class PlaceSuffixDictionary {
    public static SuffixDictionary dictionary = new SuffixDictionary();

    static {
        dictionary.addAll(Predefine.POSTFIX_SINGLE);
        dictionary.addAll(Predefine.POSTFIX_MUTIPLE);
    }
}
