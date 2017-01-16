package org.xm.xmnlp.test.seg;


import java.util.List;

import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.IndexTokenizer;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestOffset extends TestCase {
    public void testOffset() throws Exception {
        String text = "中华人民共和国在哪里";
        for (int i = 0; i < text.length(); ++i) {
            System.out.print(text.charAt(i) + "" + i + " ");
        }
        System.out.println();
        List<Term> termList = IndexTokenizer.segment(text);
        for (Term term : termList) {
            System.out.println(term.word + " " + term.offset + " " + (term.offset + term.length()));
        }
    }
}
