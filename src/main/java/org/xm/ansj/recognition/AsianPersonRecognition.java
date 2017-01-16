package org.xm.ansj.recognition;

import java.util.ArrayList;
import java.util.List;

import org.xm.ansj.dictionary.NgramDictionary;
import org.xm.ansj.domain.Nature;
import org.xm.ansj.domain.NewWord;
import org.xm.ansj.domain.PersonNatureAttr;
import org.xm.ansj.domain.Term;
import org.xm.ansj.domain.TermNatures;
import org.xm.ansj.util.TermUtil;

/**
 * @author xuming
 */
public class AsianPersonRecognition implements ITermRecognition {
    private static final double[] FACTORY = {0.16271366224044456, 0.8060521860870434, 0.031234151672511947};
    private boolean skip = false;
    private Term[] terms;

    @Override
    public void recognition(Term[] terms) {
        this.terms = terms;
        List<Term> termList = recognition();
        for (Term term : termList) {
            TermUtil.insertTerm(terms, term, TermUtil.InsertTermType.SCORE_ADD_SORT);
        }
    }

    private List<Term> recognition() {
        Term term;
        Term tempTerm;
        List<Term> termList = new ArrayList<Term>();
        int beginFreq = 10;
        for (int i = 0; i < terms.length; i++) {
            term = terms[i];
            if (term == null || !term.getTermNatures().personAttr.flag) {
                continue;
            }
            term.setScore(0);
            term.setSelfScore(0);
            int freq = 0;
            for (int j = 2; j > -1; j--) {
                freq = term.getTermNatures().personAttr.getFreq(j, 0);
                if ((freq > 10) || (term.getName().length() == 2 && freq > 10)) {
                    tempTerm = nameFind(i, beginFreq, j);
                    if (tempTerm != null) {
                        termList.add(tempTerm);
                        if (skip) {
                            for (int k = i; k < tempTerm.toValue(); k++) {
                                if (terms[k] != null) {
                                    terms[k].setScore(0);
                                    terms[k].setSelfScore(0);
                                }
                            }
                            i = tempTerm.toValue() - 1;
                            break;
                        }
                    }
                }
            }
            beginFreq = term.getTermNatures().personAttr.begin + 1;
        }
        return termList;
    }

    private Term nameFind(int offe, int beginFreq, int size) {
        StringBuilder sb = new StringBuilder();
        int undefinite = 0;
        skip = false;
        PersonNatureAttr pna;
        int index = 0;
        int freq = 0;
        double allFreq = 0.0;
        Term term = null;
        int i = offe;
        for (; i < terms.length; i++) {
            if (terms[i] == null) {
                continue;
            }
            term = terms[i];
            pna = term.getTermNatures().personAttr;
            if ((freq = pna.getFreq(size, index)) == 0) {
                return null;
            }
            if (pna.allFreq > 0) {
                undefinite++;
            }
            sb.append(term.getName());
            allFreq += Math.log(term.getTermNatures().allFreq + 1);
            allFreq += -Math.log(freq);
            index++;
            if (index == size + 2) {
                break;
            }
        }
        double score = -Math.log(FACTORY[size]);
        score += allFreq;
        double endFreq = 0;
        boolean flag = true;
        while (flag) {
            i++;
            if (i > terms.length) {
                endFreq = 10;
                flag = false;
            } else if (terms[i] != null) {
                int twoWordFreq = NgramDictionary.getTwoWordFreq(term, terms[i]);
                if (twoWordFreq > 3) {
                    return null;
                }
                endFreq = terms[i].getTermNatures().personAttr.end + 1;
                flag = false;
            }
        }
        score -= Math.log(endFreq);
        score -= Math.log(beginFreq);
        if (score > -3) {
            return null;
        }
        if (allFreq > 0 && undefinite > 0) {
            return null;
        }
        skip = undefinite == 0;
        term = new Term(sb.toString(), offe, TermNatures.NR);
        term.setSelfScore(score);
        return term;
    }

    public List<NewWord> getNewWords(Term[] terms) {
        this.terms = terms;
        List<NewWord> newWords = new ArrayList<NewWord>();
        List<Term> termList = recognition();
        for (Term term : termList) {
            newWords.add(new NewWord(term.getName(), Nature.NR));
        }
        return newWords;
    }
}
