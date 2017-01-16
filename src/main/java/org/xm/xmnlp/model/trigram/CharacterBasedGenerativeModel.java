package org.xm.xmnlp.model.trigram;

import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.corpus.document.word.IWord;
import org.xm.xmnlp.corpus.document.word.Word;
import org.xm.xmnlp.corpus.io.ByteArray;
import org.xm.xmnlp.corpus.io.ICacheAble;

/**
 * 基于字符的生成模型（其实就是一个TriGram文法模型，或称2阶隐马模型）
 * <p/>
 * Created by mingzai on 2016/7/30.
 */
public class CharacterBasedGenerativeModel implements ICacheAble {
    /**
     * 2阶隐马的三个参数
     */
    double l1, l2, l3;
    /**
     * 频次统计
     */
    Probability tf;
    /**
     * 用到的标签
     */
    static final char[] id2tag = new char[]{'b', 'm', 'e', 's'};
    /**
     * 视野范围外的事件
     */
    static final char[] bos = {'\b', 'x'};
    /**
     * 无穷小
     */
    static final double inf = -1e10;

    public CharacterBasedGenerativeModel() {
        tf = new Probability();
    }

    /**
     * 让模型观测一个句子
     *
     * @param wordList
     */
    public void learn(List<Word> wordList) {
        LinkedList<char[]> sentence = new LinkedList<char[]>();
        for (IWord iWord : wordList) {
            String word = iWord.getValue();
            if (word.length() == 1) {
                sentence.add(new char[]{word.charAt(0), 's'});
            } else {
                sentence.add(new char[]{word.charAt(0), 'b'});
                for (int i = 1; i < word.length() - 1; ++i) {
                    sentence.add(new char[]{word.charAt(i), 'm'});
                }
                sentence.add(new char[]{word.charAt(word.length() - 1), 'e'});
            }
        }
        //转换结束，开始统计
        char[][] now = new char[3][];   //长度是3的队列
        now[1] = bos;
        now[2] = bos;
        tf.add(1, bos, bos);
        tf.add(2, bos);
        for (char[] i : sentence) {
            System.arraycopy(now, 1, now, 0, 2);
            now[2] = i;
            tf.add(1, i);                //uni
            tf.add(1, now[1], now[2]);    //bi
            tf.add(1, now);              //tri
        }
    }

    /**
     * 观测结束，开始训练
     */
    public void train() {
        double tl1 = 0.0;
        double tl2 = 0.0;
        double tl3 = 0.0;
        for (String key : tf.d.keySet()) {
            if (key.length() != 6) continue;    // tri samples
            char[][] now = new char[][]
                    {
                            {key.charAt(0), key.charAt(1)},
                            {key.charAt(2), key.charAt(3)},
                            {key.charAt(4), key.charAt(5)},
                    };
            double c3 = div(tf.get(now) - 1, tf.get(now[0], now[1]) - 1);
            double c2 = div(tf.get(now[1], now[2]) - 1, tf.get(now[1]) - 1);
            double c1 = div(tf.get(now[2]) - 1, tf.getsum() - 1);
            if (c3 >= c1 && c3 >= c2)
                tl3 += tf.get(now);
            else if (c2 >= c1 && c2 >= c3)
                tl2 += tf.get(now);
            else if (c1 >= c2 && c1 >= c3)
                tl1 += tf.get(now);
        }

        l1 = div(tl1, tl1 + tl2 + tl3);
        l2 = div(tl2, tl1 + tl2 + tl3);
        l3 = div(tl3, tl1 + tl2 + tl3);
    }

    /**
     * 求概率
     *
     * @param s1 前2个状态
     * @param s2 前1个状态
     * @param s3 当前状态
     * @return 序列的概率
     */
    private double getProb(char[] s1, char[] s2, char[] s3) {
        double uni = l1 * tf.freq(s3);
        double bi = div(l2 * tf.get(s2, s3), tf.get(s2));
        double tri = div(l3 * tf.get(s1, s2, s3), tf.get(s1, s2));
        if (uni + bi + tri == 0)
            return inf;
        return Math.log(uni + bi + tri);
    }

    /**
     * 序列标注
     *
     * @param charArray 观测序列
     * @return 标注序列
     */
    public char[] tag(char[] charArray) {
        if (charArray.length == 0) return new char[0];
        if (charArray.length == 1) return new char[]{'s'};
        char[] tag = new char[charArray.length];
        double[][] now = new double[4][4];
        double[] first = new double[4];

        // link[i][s][t] := 第i个节点在前一个状态是s，当前状态是t时，前2个状态的tag的值
        int[][][] link = new int[charArray.length][4][4];
        // 第一个字，只可能是bs
        for (int s = 0; s < 4; ++s) {
            double p = (s == 1 || s == 2) ? inf : getProb(bos, bos, new char[]{charArray[0], id2tag[s]});
            first[s] = p;
        }

        // 第二个字，尚不能完全利用TriGram
        for (int f = 0; f < 4; ++f) {
            for (int s = 0; s < 4; ++s) {
                double p = first[f] + getProb(bos, new char[]{charArray[0], id2tag[f]}, new char[]{charArray[1], id2tag[s]});
                now[f][s] = p;
                link[1][f][s] = f;
            }
        }

        // 第三个字开始，利用TriGram标注
        double[][] pre = new double[4][4];
        for (int i = 2; i < charArray.length; i++) {
            // swap(now, pre)
            double[][] d = pre;
            pre = now;
            now = d;
            // end of swap
            for (int s = 0; s < 4; ++s) {
                for (int t = 0; t < 4; ++t) {
                    now[s][t] = -1e20;
                    for (int f = 0; f < 4; ++f) {
                        double p = pre[f][s] + getProb(new char[]{charArray[i - 2], id2tag[f]},
                                new char[]{charArray[i - 1], id2tag[s]},
                                new char[]{charArray[i], id2tag[t]});
                        if (p > now[s][t]) {
                            now[s][t] = p;
                            link[i][s][t] = f;
                        }
                    }
                }
            }
        }
        double score = inf;
        int s = 0;
        int t = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (now[i][j] > score) {
                    score = now[i][j];
                    s = i;
                    t = j;
                }
            }
        }
        for (int i = link.length - 1; i >= 0; --i) {
            tag[i] = id2tag[t];
            int f = link[i][s][t];
            t = s;
            s = f;
        }
        return tag;
    }

    /**
     * 安全除法
     *
     * @param v1
     * @param v2
     * @return
     */
    private static double div(int v1, int v2) {
        if (v2 == 0) return 0.0;
        return v1 / (double) v2;
    }

    /**
     * 安全除法
     *
     * @param v1
     * @param v2
     * @return
     */
    private static double div(double v1, double v2) {
        if (v2 == 0) return 0.0;
        return v1 / v2;
    }

    @Override
    public void save(DataOutputStream out) throws Exception {
        out.writeDouble(l1);
        out.writeDouble(l2);
        out.writeDouble(l3);
        tf.save(out);
    }

    @Override
    public boolean load(ByteArray byteArray) {
        l1 = byteArray.nextDouble();
        l2 = byteArray.nextDouble();
        l3 = byteArray.nextDouble();
        tf.load(byteArray);
        return true;
    }
}
