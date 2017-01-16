package org.xm.xmnlp.corpus.statistics;

/**
 * 一个二元的词串的频度
 */
public class PairFrequency extends TermFrequency {
    /**
     * 互信息值
     */
    public double mi;
    /**
     * 左信息熵
     */
    public double le;
    /**
     * 右信息熵
     */
    public double re;
    /**
     * 分数
     */
    public double score;
    public String first;
    public String second;
    public char delimiter;

    protected PairFrequency(String term, Integer frequency) {
        super(term, frequency);
    }

    protected PairFrequency(String term) {
        super(term);
    }

    /**
     * 构造一个pf
     *
     * @param first
     * @param delimiter
     * @param second
     * @return
     */
    public static PairFrequency create(String first, char delimiter, String second) {
        PairFrequency pairFrequency = new PairFrequency(first + delimiter + second);
        pairFrequency.first = first;
        pairFrequency.delimiter = delimiter;
        pairFrequency.second = second;
        return pairFrequency;
    }

    /**
     * 该共现是否统计的是否是从左到右的顺序
     *
     * @return
     */
    public boolean isRight() {
        return delimiter == Occurrence.RIGHT;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(first);
        sb.append(isRight() ? '→' : '←');
        sb.append(second);
        sb.append('=');
        sb.append(" tf=");
        sb.append(getValue());
        sb.append(' ');
        sb.append("mi=");
        sb.append(mi);
        sb.append(" le=");
        sb.append(le);
        sb.append(" re=");
        sb.append(re);
        sb.append(" score=");
        sb.append(score);
        return sb.toString();
    }
}
