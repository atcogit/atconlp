package org.xm.xmnlp.scorer.editdistance;


import org.xm.xmnlp.scorer.BaseScorer;

/**
 * 编辑距离打分器
 */
public class EditDistanceScorer extends BaseScorer<CharArray> {
    @Override
    public CharArray generateKey(String sentence) {
        char[] charArray = sentence.toCharArray();
        if (charArray.length == 0) return null;
        return new CharArray(charArray);
    }
}
