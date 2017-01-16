package org.xm.xmnlp.corpus.dependency.conll;

/**
 * CoNLL语料中的一行
 */
public class CoNllLine {
    /**
     * 十个值
     */
    public String[] value = new String[10];

    /**
     * 第一个值化为id
     */
    public int id;

    public CoNllLine(String... args) {
        int length = Math.min(args.length, value.length);
        for (int i = 0; i < length; ++i) {
            value[i] = args[i];
        }
        id = Integer.parseInt(value[0]);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (String value : this.value) {
            sb.append(value);
            sb.append('\t');
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
