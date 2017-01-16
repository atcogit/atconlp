package org.xm.xmnlp.seg.other;

import org.xm.xmnlp.seg.Segment;

/**
 * 基于词典的机械分词器基类
 */
public abstract class DictionaryBasedSegment extends Segment {
    /**
     * 开启数词和英文识别（与标准意义上的词性标注不同，只是借用这个配置方法，不是真的开启了词性标注。
     * 一般用词典分词的用户不太可能是NLP专业人士，对词性准确率要求不高，所以干脆不为词典分词实现词性标注。）
     *
     * @param enable
     * @return
     */
    public Segment enablePartOfSpeechTagging(boolean enable) {
        return super.enablePartOfSpeechTagging(enable);
    }
}
