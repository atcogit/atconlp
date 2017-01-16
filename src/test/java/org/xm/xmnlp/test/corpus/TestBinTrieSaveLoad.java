package org.xm.xmnlp.test.corpus;


import java.util.Map;
import java.util.Set;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.trie.bintrie.BinTrie;
import org.xm.xmnlp.dictionary.CustomDictionary;
import org.xm.xmnlp.util.DictionaryUtil;

import junit.framework.TestCase;

/**
 * 保存bin文件
 */
public class TestBinTrieSaveLoad extends TestCase {

    public static final String OUT_BINTRIE_DAT = "data/bintrie.dat";

    public void testSaveAndLoad() throws Exception {
        BinTrie<Integer> trie = new BinTrie<Integer>();
        trie.put("haha", 0);
        trie.put("hankcs", 1);
        trie.put("hello", 2);
        trie.put("za", 3);
        trie.put("zb", 4);
        trie.put("zzz", 5);
        System.out.println(trie.save(OUT_BINTRIE_DAT));
        trie = new BinTrie<Integer>();
        Integer[] value = new Integer[100];
        for (int i = 0; i < value.length; ++i) {
            value[i] = i;
        }
        System.out.println(trie.load(OUT_BINTRIE_DAT, value));
        Set<Map.Entry<String, Integer>> entrySet = trie.entrySet();
        System.out.println(entrySet);
    }

    public void testCustomDictionary() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(CustomDictionary.get("龟兔赛跑"));
    }

    public void testSortCustomDictionary() throws Exception {
        DictionaryUtil.sortDictionary(Xmnlp.Config.CustomDictionaryPath[0]);
    }
}
