package org.xm.xmnlp.test.corpus;


import org.xm.xmnlp.corpus.dictionary.item.Item;
import org.xm.xmnlp.corpus.dictionary.item.SimpleItem;

import junit.framework.TestCase;

public class TestItem extends TestCase {
    public void testCreate() throws Exception {
        assertEquals("希望 v 7685 vn 616", Item.create("希望 v 7685 vn 616").toString());
    }

    public void testSpilt() throws Exception {
        System.out.println("D 16 L 14 E 4 ".split(" ").length);

    }

    public void testCombine() throws Exception {
        SimpleItem itemA = SimpleItem.create("A 1 B 2");
        SimpleItem itemB = SimpleItem.create("B 1 C 2 D 3");
        itemA.combine(itemB);
        System.out.println(itemA);
    }
}
