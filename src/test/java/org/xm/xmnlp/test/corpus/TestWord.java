package org.xm.xmnlp.test.corpus;


import org.xm.xmnlp.corpus.document.word.CompoundWord;
import org.xm.xmnlp.corpus.document.word.Word;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestWord extends TestCase {
    public void testCreate() throws Exception {
        assertEquals("人民网/nz", Word.create("人民网/nz").toString());
        assertEquals("[纽约/nsf 时报/n]/nz", CompoundWord.create("[纽约/nsf 时报/n]/nz").toString());
    }

    public void testSpace() throws Exception {
        CompoundWord compoundWord = CompoundWord.create("[9/m  11/m 后/f]/mq");
        System.out.println(compoundWord);
    }
}
