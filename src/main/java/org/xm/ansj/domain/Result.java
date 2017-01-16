package org.xm.ansj.domain;


import java.util.Iterator;
import java.util.List;

import org.xm.ansj.recognition.IRecognition;
import org.xm.xmnlp.util.StringUtil;

/**
 * @author xuming
 */
public class Result implements Iterable<Term> {
    private List<Term> terms;

    public Result(List<Term> terms) {
        this.terms = terms;
    }

    @Override
    public Iterator<Term> iterator() {
        return terms.iterator();
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public int getSize() {
        return terms.size();
    }

    public Term get(int index) {
        return terms.get(index);
    }

    public Result recognition(IRecognition rec) {
        rec.recognition(this);
        return this;
    }

    @Override
    public String toString() {
        return toString(",");
    }

    public String toString(String split) {
        return StringUtil.joiner(this.terms, split);
    }

    public String toStringWithoutNature() {
        return toStringWithoutNature(",");
    }

    public String toStringWithoutNature(String split) {
        StringBuilder sb = new StringBuilder(terms.get(0).getRealName());
        for (int i = 1; i < terms.size(); i++) {
            sb.append(split);
            sb.append(terms.get(i).getRealName());
        }
        return sb.toString();
    }
}
