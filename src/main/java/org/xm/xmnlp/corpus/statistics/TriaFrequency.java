package org.xm.xmnlp.corpus.statistics;

/**
 * 一个二元的词串的频度
 */
public class TriaFrequency extends PairFrequency {
    public String third;

    private TriaFrequency(String term, Integer frequency) {
        super(term, frequency);
    }

    private TriaFrequency(String term) {
        super(term);
    }

    /**
     * 构造一个三阶接续，正向
     *
     * @param first
     * @param second
     * @param third
     * @param delimiter 一般使用RIGHT！
     * @return
     */
    public static TriaFrequency create(String first, char delimiter, String second, String third) {
        TriaFrequency triaFrequency = new TriaFrequency(first + delimiter + second + Occurrence.RIGHT + third);
        triaFrequency.first = first;
        triaFrequency.second = second;
        triaFrequency.third = third;
        triaFrequency.delimiter = delimiter;
        return triaFrequency;
    }

    /**
     * 构造一个三阶接续，逆向
     *
     * @param second
     * @param third
     * @param delimiter 一般使用LEFT
     * @param first
     * @return
     */
    public static TriaFrequency create(String second, String third, char delimiter, String first) {
        TriaFrequency triaFrequency = new TriaFrequency(second + Occurrence.RIGHT + third + delimiter + first);
        triaFrequency.first = first;
        triaFrequency.second = second;
        triaFrequency.third = third;
        triaFrequency.delimiter = delimiter;
        return triaFrequency;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getKey().replace(Occurrence.LEFT, '←').replace(Occurrence.RIGHT, '→'));
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
        return sb.toString();
    }
}
