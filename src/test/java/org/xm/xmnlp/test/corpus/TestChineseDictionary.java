package org.xm.xmnlp.test.corpus;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.traditionalsimplified.SimplifiedChineseDictionary;
import org.xm.xmnlp.dictionary.traditionalsimplified.TraditionalChineseDictionary;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestChineseDictionary extends TestCase {
    public void testF2J() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(TraditionalChineseDictionary.convertToSimplifiedChinese("士多啤梨是紅色的"));
    }

    public void testJ2F() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(SimplifiedChineseDictionary.convertToTraditionalChinese("草莓是红色的"));
    }

    public void testInterface() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(Xmnlp.convertToSimplifiedChinese("「以後等妳當上皇后，就能買士多啤梨慶祝了」"));
        System.out.println(Xmnlp.convertToTraditionalChinese("“以后等你当上皇后，就能买草莓庆祝了”"));
    }
}
