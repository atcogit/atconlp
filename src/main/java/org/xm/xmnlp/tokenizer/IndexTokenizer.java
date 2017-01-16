package org.xm.xmnlp.tokenizer;


import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 索引分词器
 */
public class IndexTokenizer {
    /**
     * 预置分词器
     */
    public static final Segment SEGMENT = Xmnlp.newSegment().enableIndexMode(true);

    public static List<Term> segment(String text) {
        return SEGMENT.seg(text);
    }

    /**
     * 分词
     *
     * @param text 文本
     * @return 分词结果
     */
    public static List<Term> segment(char[] text) {
        return SEGMENT.seg(text);
    }

    /**
     * 切分为句子形式
     *
     * @param text 文本
     * @return 句子列表
     */
    public static List<List<Term>> seg2sentence(String text) {
        return SEGMENT.seg2sentence(text);
    }
}
