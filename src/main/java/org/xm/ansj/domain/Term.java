package org.xm.ansj.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.xm.ansj.util.MathUtil;
import org.xm.xmnlp.util.StringUtil;

/**
 * @author xuming
 */
public class Term implements Serializable {
    private static final long serialVersionUID = -4281607433549403167L;
    private String name;
    private String realName;
    private int offe;
    private TermNatures termNatures = TermNatures.NULL;
    private AnsjItem item = AnsjItem.NULL;
    private Term next;
    private double score = 0;
    private double selfScore = 1;
    private Term from;
    private Term to;
    private Nature nature = Nature.NULL;
    private boolean isNewWord;
    private List<Term> subTerm = null;

    public Term(String name, int offe, AnsjItem item) {
        this.name = name;
        this.offe = offe;
        this.item = item;
        if (item.termNatures != null) {
            this.termNatures = item.termNatures;
            if (termNatures.nature != null) {
                this.nature = termNatures.nature;
            }
        }
    }

    public Term(String name, int offe, TermNatures termNatures) {
        this.name = name;
        this.offe = offe;
        this.termNatures = termNatures;
        if (termNatures.nature != null) {
            this.nature = termNatures.nature;
        }
    }

    public Term(String name, int offe, String natureStr, int natureFreq) {
        this.name = name;
        this.offe = offe;
        TermNature termNature = new TermNature(natureStr, natureFreq);
        this.nature = termNature.nature;
        this.termNatures = new TermNatures(termNature);
    }

    public void setPathScore(Term from, Map<String, Double> relationMap) {
        double score = MathUtil.computeScore(from, this, relationMap);
        if (this.from == null || this.score == 0 || this.score >= score) {
            this.setFromAndScore(from, score);//core segmentation logic
        }
    }

    public void setPathSelfScore(Term from) {
        double score = this.selfScore + from.score;
        if (this.from == null || this.score > score) {
            this.setFromAndScore(from, score);
        }
    }

    private void setFromAndScore(Term from, double score) {
        this.from = from;
        this.score = score;
    }

    public Term merge(Term to) {
        this.name = this.name + to.getName();
        if (StringUtil.isNotBlank(this.realName) && StringUtil.isNotBlank(to.getRealName())) {
            this.realName = this.realName + to.getRealName();
        }
        this.setTo(to.to);
        return this;
    }

    @Override
    public String toString() {
        if ("null".equals(nature.natureStr)) {
            return this.getRealName();
        }
        return this.getRealName() + "/" + nature.natureStr;
    }

    public void clearScore() {
        this.score = 0;
        this.selfScore = 0;
    }

    public void updateOffe(int offe) {
        this.offe += offe;
    }


    public int toValue() {
        return offe + name.length();
    }

    public int getOffe() {
        return offe;
    }

    public void setOffe(int offe) {
        this.offe = offe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        if (realName == null) {
            return name;
        }
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public TermNatures getTermNatures() {
        return termNatures;
    }

    public void setTermNatures(TermNatures termNatures) {
        this.termNatures = termNatures;
    }

    public AnsjItem getItem() {
        return item;
    }

    public void setItem(AnsjItem item) {
        this.item = item;
    }

    public Term next() {
        return next;
    }

    public Term setNext(Term next) {
        this.next = next;
        return this;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(double selfScore) {
        this.selfScore = selfScore;
    }

    public Term getFrom() {
        return from;
    }

    public void setFrom(Term from) {
        this.from = from;
    }

    public Term getTo() {
        return to;
    }

    public void setTo(Term to) {
        this.to = to;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public String getNatureStr() {
        return nature.natureStr;
    }

    public void updateTermNaturesAndNature(TermNatures termNatures) {
        this.termNatures = termNatures;
        this.nature = termNatures.nature;
    }

    public boolean isNewWord() {
        return isNewWord;
    }

    public void setNewWord(boolean newWord) {
        isNewWord = newWord;
    }

    public List<Term> getSubTerm() {
        return subTerm;
    }

    public void setSubTerm(List<Term> subTerm) {
        this.subTerm = subTerm;
    }
}
