package org.xm.xmnlp.scorer;

import java.util.Map;

/**
 * 打分接口
 */
public interface IScorer {
    /**
     * 计算分值
     *
     * @param outerSentence 外部句子
     * @return key为分值x，value为分值等于x的一系列句子
     */
    Map<String, Double> computeScore(String outerSentence);

    /**
     * 输入一个候选句子
     *
     * @param sentence
     */
    void addSentence(String sentence);

    /**
     * 清空该推荐器中的所有句子
     */
    void removeAllSentences();
}
