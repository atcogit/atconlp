package org.xm.xmnlp.test.algorithm;


import java.util.Set;
import java.util.TreeSet;

import org.xm.xmnlp.collection.MDAG.MDAGMap;
import org.xm.xmnlp.collection.trie.bintrie.BinTrie;
import org.xm.xmnlp.util.IOUtil;

import junit.framework.TestCase;

public class MDAGMapTest extends TestCase {
    MDAGMap<Integer> mdagMap = new MDAGMap<Integer>();
    Set<String> validKeySet;

    public void setUp() throws Exception {
        IOUtil.LineIterator iterator = new IOUtil.LineIterator("data/dictionary/custom/CustomDictionary.txt");
        validKeySet = new TreeSet<String>();
        while (iterator.hasNext()) {
            validKeySet.add(iterator.next().split("\\s")[0]);
        }
    }

    public void testPut() throws Exception {
        for (String word : validKeySet) {
            mdagMap.put(word, word.length());
        }
    }

    public void testGet() throws Exception {
        testPut();
        mdagMap.simplify();
//        mdagMap.unSimplify();
        for (String word : validKeySet) {
            assertEquals(word.length(), (int) mdagMap.get(word));
        }
    }

    public void testSingle() throws Exception {
        testPut();
        mdagMap.simplify();
        System.out.println(mdagMap.get("齿轮厂"));
    }

    public void testCommonPrefixSearch() throws Exception {
        testPut();
        System.out.println(mdagMap.commonPrefixSearchWithValue("一举一动"));
    }

    public void testBenchmark() throws Exception {
        testPut();
        BinTrie<Integer> binTrie = new BinTrie<Integer>();
        for (String key : validKeySet) {
            binTrie.put(key, key.length());
        }
        mdagMap.simplify();
        for (String key : validKeySet) {
            assertEquals(binTrie.commonPrefixSearchWithValue(key).size(), mdagMap.commonPrefixSearchWithValue(key).size());
        }

        long start;
        start = System.currentTimeMillis();
        for (String key : validKeySet) {
            binTrie.commonPrefixSearchWithValue(key);
        }
        System.out.printf("binTrie: %d ms\n", System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (String key : validKeySet) {
            mdagMap.commonPrefixSearchWithValue(key);
        }
        System.out.printf("mdagMap: %d ms\n", System.currentTimeMillis() - start);
    }
}