package org.xm.xmnlp.test.seg;

import java.util.Map;

import org.xm.xmnlp.dictionary.BaseSearcher;
import org.xm.xmnlp.dictionary.person.NRDictionary;

import junit.framework.TestCase;

public class TestNRDictionary extends TestCase {
    public void testLoad() throws Exception {
        NRDictionary dictionary = new NRDictionary();
        dictionary.load("data/dictionary/person/nr.txt");
        System.out.println(dictionary.get("为"));
        BaseSearcher searcher = dictionary.getSearcher("为");
        Map.Entry<String, String> entry;
        while ((entry = searcher.next()) != null) {
            System.out.println(entry);
        }
    }
}
