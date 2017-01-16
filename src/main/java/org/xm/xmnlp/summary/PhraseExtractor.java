package org.xm.xmnlp.summary;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.statistics.Occurrence;
import org.xm.xmnlp.corpus.statistics.PairFrequency;
import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;
import org.xm.xmnlp.dictionary.stopword.Filter;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.NotionalTokenizer;

/**
 * 利用互信息和左右熵的短语提取器
 * Created by xuming on 2016/7/27.
 */
public class PhraseExtractor {
    public static List<String> getPhraseList(String text, int size) {
        List<String> phraseList = new LinkedList<String>();
        Occurrence occurrence = new Occurrence();
        Filter[] filterChain = new Filter[]
                {
                        CoreStopWordDictionary.FILTER,
                        new Filter() {
                            @Override
                            public boolean shouldInclude(Term term) {
                                switch (term.nature) {
                                    case t:
                                    case nx:
                                        return false;
                                }
                                return true;
                            }
                        }
                };
        for (List<Term> sentence : NotionalTokenizer.seg2sentence(text, filterChain)) {
            if (Xmnlp.Config.DEBUG) {
                System.out.println(sentence);
            }
            occurrence.addAll(sentence);
        }
        occurrence.compute();
        if (Xmnlp.Config.DEBUG) {
            System.out.println(occurrence);
            for (PairFrequency phrase : occurrence.getPhraseByMi()) {
                System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tmi=" + phrase.mi + " , ");
            }
            System.out.println();
            for (PairFrequency phrase : occurrence.getPhraseByLe()) {
                System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tle=" + phrase.le + " , ");
            }
            System.out.println();
            for (PairFrequency phrase : occurrence.getPhraseByRe()) {
                System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tre=" + phrase.re + " , ");
            }
            System.out.println();
            for (PairFrequency phrase : occurrence.getPhraseByScore()) {
                System.out.print(phrase.getKey().replace(Occurrence.RIGHT, '→') + "\tscore=" + phrase.score + " , ");
            }
            System.out.println();
        }

        for (PairFrequency phrase : occurrence.getPhraseByScore()) {
            if (phraseList.size() == size) break;
            phraseList.add(phrase.first + phrase.second);
        }
        return phraseList;
    }

}
