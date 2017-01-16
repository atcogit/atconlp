package org.xm.xmnlp.demo;


import java.util.List;

import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.IndexTokenizer;

/**
 * 索引分词
 */
public class DemoIndexSegment {
    public static void main(String[] args) {
        List<Term> termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList) {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }
    }
}
