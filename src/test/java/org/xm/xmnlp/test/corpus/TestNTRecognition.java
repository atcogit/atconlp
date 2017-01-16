package org.xm.xmnlp.test.corpus;

import java.util.Set;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.common.CommonStringDictionary;
import org.xm.xmnlp.seg.DijkstraSegment;
import org.xm.xmnlp.util.IOUtil;

import junit.framework.TestCase;

public class TestNTRecognition extends TestCase {
    public void testSeg() throws Exception {
        Xmnlp.Config.enableDebug();
        DijkstraSegment segment = new DijkstraSegment();
        segment.enableCustomDictionary(false);

        segment.enableOrganizationRecognize(true);
        System.out.println(segment.seg("清河工业园区"));
    }

    public void testGeneratePatternJavaCode() throws Exception {
        CommonStringDictionary commonStringDictionary = new CommonStringDictionary();
        commonStringDictionary.load("data/dictionary/organization/nt.pattern.txt");
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = commonStringDictionary.keySet();
        CommonStringDictionary secondDictionary = new CommonStringDictionary();
        secondDictionary.load("data/dictionary/organization/outerNT.pattern.txt");
        keySet.addAll(secondDictionary.keySet());
        for (String pattern : keySet) {
            sb.append("trie.addKeyword(\"" + pattern + "\");\n");
        }
        IOUtil.saveTxt("data/dictionary/organization/code.txt", sb.toString());
    }
}
