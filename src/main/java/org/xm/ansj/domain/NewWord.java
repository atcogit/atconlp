package org.xm.ansj.domain;

/**
 * @author xuming
 */
public class NewWord {
    private String name;
    private double score;
    private Nature nature;
    private int allFreq;
    private boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public int getAllFreq() {
        return allFreq;
    }

    public void setAllFreq(int allFreq) {
        this.allFreq = allFreq;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public NewWord(String name, Nature nature, double score) {
        this.name = name;
        this.nature = nature;
        this.score = score;
        this.allFreq = 1;
    }

    public NewWord(String name, Nature nature) {
        this.name = name;
        this.nature = nature;
        this.allFreq = 1;
    }

    public void update(Nature nature, int freq) {
        this.score += score * freq;
        this.allFreq += freq;
        if (Nature.NW != nature) {
            this.nature = nature;
        }
    }

    @Override
    public String toString() {
        return this.name + "\t" + this.score + "\t" + this.getNature().natureStr;
    }
}
