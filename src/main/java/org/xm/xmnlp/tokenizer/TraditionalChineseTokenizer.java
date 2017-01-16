package org.xm.xmnlp.tokenizer;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.other.CharTable;
import org.xm.xmnlp.dictionary.traditionalsimplified.SimplifiedChineseDictionary;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.SentencesUtil;

/**
 * 繁体中文分词器
 * Created by xuming on 2016/7/29.
 */
public class TraditionalChineseTokenizer {
    public static Segment SEGMENT = Xmnlp.newSegment();

    /**
     * 一句话分词
     *
     * @param text 文本 String
     * @return 分词结果 List<Term>
     */
    private static List<Term> segSentence(String text) {
        String sText = CharTable.convert(text);
        List<Term> termList = SEGMENT.seg(sText);
        int offset = 0;
        for (Term term : termList) {
            String tText;
            term.offset = offset;
            if (term.length() == 1 || (tText = SimplifiedChineseDictionary.getTraditionalChinese(term.word)) == null) {
                term.word = text.substring(offset, offset + term.length());
                offset += term.length();
            } else {
                offset += term.length();
                term.word = tText;
            }
        }
        return termList;
    }

    /**
     * 分词
     *
     * @param text 文本 String
     * @return 分词结果 List<Term>
     */
    public static List<Term> segment(String text) {
        List<Term> termList = new LinkedList<Term>();
        for (String sentence : SentencesUtil.toSentenceList(text)) {
            termList.addAll(segSentence(sentence));
        }
        return termList;
    }

    /**
     * 分词
     *
     * @param text 文本 char[]
     * @return 分词结果 List<Term>
     */
    public static List<Term> segment(char[] text) {
        return segment(CharTable.convert(text));
    }

    /**
     * 切分为句子形式
     *
     * @param text 文本
     * @return 句子列表
     */
    public static List<List<Term>> seg2Sentence(String text) {
        List<List<Term>> resultList = new LinkedList<List<Term>>();
        for (String sentence : SentencesUtil.toSentenceList(text)) {
            resultList.add(segment(sentence));
        }
        return resultList;
    }
}
