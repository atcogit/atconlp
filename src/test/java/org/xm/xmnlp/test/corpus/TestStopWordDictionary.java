package org.xm.xmnlp.test.corpus;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.MDAG.MDAGSet;
import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;

import junit.framework.TestCase;

public class TestStopWordDictionary extends TestCase
{
    public void testContains() throws Exception
    {
        Xmnlp.Config.enableDebug();
        System.out.println(CoreStopWordDictionary.contains("这就是说"));
    }

    public void testContainsSomeWords() throws Exception
    {
        assertEquals(true, CoreStopWordDictionary.contains("可以"));
    }

    public void testMDAG() throws Exception
    {
        List<String> wordList = new LinkedList<String>();
        wordList.add("zoo");
        wordList.add("hello");
        wordList.add("world");
        MDAGSet set = new MDAGSet(wordList);
        set.add("bee");
        assertEquals(true, set.contains("bee"));
        set.remove("bee");
        assertEquals(false, set.contains("bee"));
    }
}
