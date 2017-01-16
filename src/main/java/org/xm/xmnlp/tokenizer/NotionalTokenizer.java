package org.xm.xmnlp.tokenizer;

import java.util.List;
import java.util.ListIterator;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;
import org.xm.xmnlp.dictionary.stopword.Filter;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 实词分词器，自动去掉停用词
 * Created by xuming on 2016/7/26.
 */
public class NotionalTokenizer {

    private static final Segment SEGMENT = Xmnlp.newSegment();

    public static List<Term> segment(String text) {
        return segment(text.toCharArray());
    }

    private static List<Term> segment(char[] text) {
        List<Term> resultList = SEGMENT.seg(text);
        ListIterator<Term> listIterator = resultList.listIterator();
        while (listIterator.hasNext()) {
            if (!CoreStopWordDictionary.shouldInclude(listIterator.next())) {
                listIterator.remove();
            }
        }
        return resultList;
    }

    /**
     * 切分为句子形式
     *
     * @param text
     * @return
     */
    public static List<List<Term>> seg2sentence(String text) {
        List<List<Term>> sentenceList = SEGMENT.seg2sentence(text);
        for (List<Term> sentence : sentenceList) {
            ListIterator<Term> listIterator = sentence.listIterator();
            while (listIterator.hasNext()) {
                if (!CoreStopWordDictionary.shouldInclude(listIterator.next())) {
                    listIterator.remove();
                }
            }
        }

        return sentenceList;
    }

    /**
     * 切分为句子形式
     *
     * @param text
     * @param filterArrayChain 自定义过滤器链
     * @return
     */
    public static List<List<Term>> seg2sentence(String text, Filter... filterArrayChain) {
        List<List<Term>> sentenceList = SEGMENT.seg2sentence(text);
        for (List<Term> sentence : sentenceList) {
            ListIterator<Term> listIterator = sentence.listIterator();
            while (listIterator.hasNext()) {
                if (filterArrayChain != null) {
                    Term term = listIterator.next();
                    for (Filter filter : filterArrayChain) {
                        if (!filter.shouldInclude(term)) {
                            listIterator.remove();
                            break;
                        }
                    }
                }
            }
        }

        return sentenceList;
    }
}
