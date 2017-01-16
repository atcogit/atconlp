package org.xm.xmnlp.test.corpus;


import org.xm.xmnlp.collection.trie.DoubleArrayTrie;
import org.xm.xmnlp.util.IOUtil;

import junit.framework.TestCase;

public class TestLoadByChannel extends TestCase {
    public void testLoad() throws Exception {
        DoubleArrayTrie<Integer> trie = new DoubleArrayTrie<Integer>();
        trie.load("data/dictionary/CoreNatureDictionary.txt.trie.dat", new Integer[0]);
    }

    public void testHasNext() throws Exception {
        IOUtil.LineIterator iterator = IOUtil.readLine("data/test/other/f.txt");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public void testNext() throws Exception {
        String line;
        IOUtil.LineIterator iterator = IOUtil.readLine("data/test/other/f.txt");
        while ((line = iterator.next()) != null) {
            System.out.println(line);
        }
    }

    public void testUTFString() throws Exception {

    }
}
