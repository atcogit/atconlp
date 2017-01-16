package org.xm.xmnlp.tokenizer;

import java.util.List;

import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.seg.other.DoubleArrayTrieSegment;

/**
 * 快速分词
 * 基于Double Array Trie实现的词典分词，适用于“高吞吐量”“精度一般”的场合
 * Created by mingzai on 2016/7/30.
 */
public class SpeedTokenizer {
    public static final Segment SEGMENT = new DoubleArrayTrieSegment();

    public static List<Term> segment(String text) {
        return SEGMENT.seg(text.toCharArray());
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
