package org.xm.ansj.util;

import java.util.Map;

import org.xm.ansj.dictionary.NgramDictionary;
import org.xm.ansj.domain.Term;

/**
 * @author xuming
 */
public class MathUtil {
    private static final double dSmoothingPara = 0.1;
    private static final String TAB = "\t";
    private static final int MAX_FREQUENCY = 2079997;
    private static final double dTemp = (double) 1 / MAX_FREQUENCY;

    public static double computeScore(Term from, Term to, Map<String, Double> relationMap) {
        double frequency = from.getTermNatures().allFreq + 1;
        if (frequency < 0) {
            double score = from.getScore() + MAX_FREQUENCY;
            from.setScore(score);
            return score;
        }
        double nTwoWordsFreq = NgramDictionary.getTwoWordFreq(from, to);
        if (relationMap != null) {
            Double d = relationMap.get(from.getName() + TAB + to.getName());
            if (d != null) {
                nTwoWordsFreq += d;
            }
        }
        double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY + 80000)
                + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp));
        if (value < 0) {
            value += frequency;
        }
        return from.getScore() + value;
    }

}
