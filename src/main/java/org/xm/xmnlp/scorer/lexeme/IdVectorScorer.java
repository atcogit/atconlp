package org.xm.xmnlp.scorer.lexeme;


import org.xm.xmnlp.scorer.BaseScorer;

/**
 * 单词语义向量打分器
 */
public class IdVectorScorer extends BaseScorer<IdVector> {
    @Override
    public IdVector generateKey(String sentence) {
        IdVector idVector = new IdVector(sentence);
        if (idVector.idArrayList.size() == 0) return null;
        return idVector;
    }
}
