package org.xm.xmnlp.scorer.lexeme;


import java.util.Iterator;
import java.util.List;

import org.xm.xmnlp.dictionary.CoreSynonymDictionaryEx;
import org.xm.xmnlp.math.ArrayCompare;
import org.xm.xmnlp.math.ArrayDistance;
import org.xm.xmnlp.scorer.ISentenceKey;
import org.xm.xmnlp.tokenizer.IndexTokenizer;

/**
 * 一个同义词有多个id，多个同义词用这个封装做key
 */
public class IdVector implements Comparable<IdVector>, ISentenceKey<IdVector> {
    public List<Long[]> idArrayList;

    public IdVector(String sentence) {
        this(CoreSynonymDictionaryEx.convert(IndexTokenizer.segment(sentence), false));
    }

    public IdVector(List<Long[]> idArrayList) {
        this.idArrayList = idArrayList;
    }

    @Override
    public int compareTo(IdVector o) {
        int len1 = idArrayList.size();
        int len2 = o.idArrayList.size();
        int lim = Math.min(len1, len2);
        Iterator<Long[]> iterator1 = idArrayList.iterator();
        Iterator<Long[]> iterator2 = o.idArrayList.iterator();

        int k = 0;
        while (k < lim) {
            Long[] c1 = iterator1.next();
            Long[] c2 = iterator2.next();
            if (ArrayDistance.computeMinimumDistance(c1, c2) != 0) {
                return ArrayCompare.compare(c1, c2);
            }
            ++k;
        }
        return len1 - len2;
    }

    @Override
    public Double similarity(IdVector other) {
        Double score = 0.0;
        for (Long[] a : idArrayList) {
            for (Long[] b : other.idArrayList) {
                Long distance = ArrayDistance.computeAverageDistance(a, b);
                score += 1.0 / (0.1 + distance);
            }
        }

        return score / other.idArrayList.size();
    }
}
