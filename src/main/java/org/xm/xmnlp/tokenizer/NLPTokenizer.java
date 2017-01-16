package org.xm.xmnlp.tokenizer;


import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 可供自然语言处理用的分词器
 *
 * @author hankcs
 */
public class NLPTokenizer {
    /**
     * 预置分词器
     */
    public static final Segment SEGMENT = Xmnlp.newSegment().enableNameRecognize(true).enableTranslatedNameRecognize(true)
            .enableJapaneseNameRecognize(true).enablePlaceRecognize(true).enableOrganizationRecognize(true)
            .enablePartOfSpeechTagging(true);

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
