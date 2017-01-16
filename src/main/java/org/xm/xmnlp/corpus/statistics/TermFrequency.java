package org.xm.xmnlp.corpus.statistics;

import java.util.AbstractMap;

/**
 * 词与词频的简单封装
 */
public class TermFrequency extends AbstractMap.SimpleEntry<String, Integer> implements Comparable<TermFrequency> {
    public TermFrequency(String term, Integer frequency) {
        super(term, frequency);
    }

    public TermFrequency(String term) {
        this(term, 1);
    }

    /**
     * 频次增加若干
     *
     * @param number
     * @return
     */
    public int increase(int number) {
        setValue(getValue() + number);
        return getValue();
    }

    public String getTerm() {
        return getKey();
    }

    public Integer getFrequency() {
        return getValue();
    }

    /**
     * 频次加一
     *
     * @return
     */
    public int increase() {
        return increase(1);
    }

    @Override
    public int compareTo(TermFrequency o) {
        if (this.getFrequency().compareTo(o.getFrequency()) == 0) return getKey().compareTo(o.getKey());
        return this.getFrequency().compareTo(o.getFrequency());
    }
}
