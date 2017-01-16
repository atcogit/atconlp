package org.xm.xmnlp.scorer.pinyin;


import org.xm.xmnlp.scorer.BaseScorer;

/**
 * 拼音打分器
 */
public class PinyinScorer extends BaseScorer<PinyinKey> {
    @Override
    public PinyinKey generateKey(String sentence) {
        PinyinKey pinyinKey = new PinyinKey(sentence);
        if (pinyinKey.size() == 0) return null;
        return pinyinKey;
    }
}
