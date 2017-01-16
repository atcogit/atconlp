package org.xm.xmnlp.test.corpus;


import java.util.Map;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dictionary.StringDictionary;

import junit.framework.TestCase;

/**
 * 简繁体词典
 */
public class TestJianFanDictionaryMaker extends TestCase {
    public void testCombine() throws Exception {
        StringDictionary dictionaryHanLP = new StringDictionary("=");
        dictionaryHanLP.load(Xmnlp.Config.TraditionalChineseDictionaryPath);

        StringDictionary dictionaryOuter = new StringDictionary("=");
        dictionaryOuter.load("D:\\Doc\\语料库\\简繁分歧词表.txt");

        for (Map.Entry<String, String> entry : dictionaryOuter.entrySet()) {
            String t = entry.getKey();
            String s = entry.getValue();
            if (t.length() == 1) continue;
            if (Xmnlp.convertToTraditionalChinese(s).equals(t)) continue;
            dictionaryHanLP.add(t, s);
        }

        dictionaryHanLP.save(Xmnlp.Config.TraditionalChineseDictionaryPath);
    }

    public void testConvertSingle() throws Exception {
        System.out.println(Xmnlp.convertToTraditionalChinese("一个劲"));
    }
}
