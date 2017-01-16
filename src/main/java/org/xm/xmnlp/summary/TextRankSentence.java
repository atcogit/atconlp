package org.xm.xmnlp.summary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.StandardTokenizer;
import org.xm.xmnlp.util.TextUtil;

/**
 * Text Rank 自动摘要
 * Created by xuming on 2016/7/27.
 */
public class TextRankSentence {
    /**
     * 阻尼系数
     */
    private final static double D = 0.85;
    /**
     * 最大迭代次数
     */
    final static int MAX_ITER = 200;

    final static double MIN_DIFF = 0.001;
    /**
     * 文档句子个数
     */
    int Num;
    /**
     * 拆分为[句子[单词]]形式的文档
     */
    List<List<String>> docs;
    /**
     * 排序后的最终结果 score <-> index</->
     */
    TreeMap<Double, Integer> top;

    /**
     * 句子和其他句子的相关程度
     */
    double[][] weight;
    /**
     * 该句子和其他句子相关程度之和
     */
    double[] weightSum;
    /**
     * 迭代之后收敛的权重
     */
    double[] vertex;

    /**
     * BM25相似度
     */
    BM25 bm25;

    public TextRankSentence(List<List<String>> docs) {
        this.docs = docs;
        bm25 = new BM25(docs);
        Num = docs.size();
        weight = new double[Num][Num];
        weightSum = new double[Num];
        vertex = new double[Num];
        top = new TreeMap<Double, Integer>(Collections.reverseOrder());
        solve();
    }

    private void solve() {
        int cnt = 0;
        for (List<String> sentence : docs) {
            double[] scores = bm25.simAll(sentence);
            weight[cnt] = scores;
            weightSum[cnt] = sum(scores) - scores[cnt]; // 减掉自己，自己跟自己肯定最相似
            vertex[cnt] = 1.0;
            ++cnt;
        }
        for (int i1 = 0; i1 < MAX_ITER; ++i1) {
            double[] m = new double[Num];
            double max_diff = 0;
            for (int i = 0; i < Num; ++i) {
                m[i] = 1 - D;
                for (int j = 0; j < Num; ++j) {
                    if (j == i || weightSum[j] == 0) continue;
                    m[i] += (D * weight[j][i] / weightSum[j] * vertex[j]);
                }
                double diff = Math.abs(m[i] - vertex[i]);
                if (diff > max_diff) {
                    max_diff = diff;
                }
            }
            vertex = m;
            if (max_diff <= MIN_DIFF) break;
        }
        // 我们来排个序吧
        for (int i = 0; i < Num; ++i) {
            top.put(vertex[i], i);
        }
    }

    /**
     * 获取前几个关键句子
     *
     * @param size 要几个
     * @return 关键句子的下标
     */
    public int[] getTopSentence(int size) {
        Collection<Integer> values = top.values();
        size = Math.min(size, values.size());
        int[] indexArray = new int[size];
        Iterator<Integer> it = values.iterator();
        for (int i = 0; i < size; ++i) {
            indexArray[i] = it.next();
        }
        return indexArray;
    }

    /**
     * 简单的求和
     *
     * @param array
     * @return
     */
    private static double sum(double[] array) {
        double total = 0;
        for (double v : array) {
            total += v;
        }
        return total;
    }


    /**
     * 把文章分割为句子
     *
     * @param document
     * @return
     */
    private static List<String> splitSentence(String document) {
        List<String> sentences = new ArrayList<String>();
        for (String line : document.split("[\r\n]")) {
            line = line.trim();
            if (line.length() == 0) continue;
            for (String s : line.split("[,，.。；;“”？?!！：:]")) {
                s = s.trim();
                if (s.length() == 0) continue;
                sentences.add(s);
            }
        }
        return sentences;
    }

    /**
     * 将句子列表转化为文档
     *
     * @param sentenceList
     * @return
     */
    private static List<List<String>> convertSentenceListToDoc(List<String> sentenceList) {
        List<List<String>> docs = new ArrayList<List<String>>(sentenceList.size());
        for (String sentence : sentenceList) {
            List<Term> termList = StandardTokenizer.segment(sentence.toCharArray());
            List<String> wordList = new LinkedList<String>();
            for (Term term : termList) {
                if (CoreStopWordDictionary.shouldInclude(term)) {
                    wordList.add(term.word);
                }
            }
            docs.add(wordList);
        }
        return docs;
    }

    /**
     * 一句话调用接口
     *
     * @param document 目标文档
     * @param size     需要的关键句的个数
     * @return 关键句列表
     */
    public static List<String> getTopSentenceList(String document, int size) {
        List<String> sentenceList = splitSentence(document);
        List<List<String>> docs = convertSentenceListToDoc(sentenceList);
        TextRankSentence textRank = new TextRankSentence(docs);
        int[] topSentence = textRank.getTopSentence(size);
        Arrays.sort(topSentence);
        List<String> resultList = new LinkedList<String>();
        for (int i : topSentence) {
            resultList.add(sentenceList.get(i));
        }
        return resultList;
    }

    /**
     * 一句话调用接口
     *
     * @param document  目标文档
     * @param maxLength 需要摘要的长度
     * @return 摘要文本
     */
    public static String getSummary(String document, int maxLength) {
        List<String> SentenceList = splitSentence(document);
        int count = SentenceList.size();
        int length = document.length();
        int avgLength = length / count;
        int size = maxLength / avgLength + 1;
        List<List<String>> docs = convertSentenceListToDoc(SentenceList);
        TextRankSentence textRank = new TextRankSentence(docs);
        int[] topSentence = textRank.getTopSentence(size);
        List<String> resultList = new LinkedList<String>();
        for (int i : topSentence) {
            resultList.add(SentenceList.get(i));
        }
        resultList = permutation(resultList, SentenceList);
        resultList = pickSentences(resultList, maxLength);
        return TextUtil.join("。", resultList);
    }

    /**
     * 转置
     *
     * @param resultList
     * @param sentenceList
     * @return
     */
    private static List<String> permutation(List<String> resultList, List<String> sentenceList) {
        int xIndex;
        int yIndex;
        String xSentence;
        String ySentence;
        int length = resultList.size();
        for (int i = 0; i < length; i++) {
            for (int offset = 0; offset < length - i; offset++) {
                xSentence = resultList.get(i);
                ySentence = resultList.get(i + offset);
                xIndex = sentenceList.indexOf(xSentence);
                yIndex = sentenceList.indexOf(ySentence);
                if (xIndex > yIndex) {
                    resultList.set(i, ySentence);
                    resultList.set(i + offset, xSentence);
                }
            }
        }
        return resultList;
    }

    /**
     * 取摘要文本
     *
     * @param resultList
     * @param maxLength
     * @return
     */
    private static List<String> pickSentences(List<String> resultList, int maxLength) {
        int count = 0;
        int buffer;
        int jump;
        List<String> resultBuffer = new LinkedList<String>();
        for (int i = 0; i < resultList.size(); i++) {
            buffer = count + resultList.get(i).length();
            if (buffer <= maxLength) {
                resultBuffer.add(resultList.get(i));
                count += resultList.get(i).length();
            } else if (i < (resultList.size() - 1)) {
                jump = count + resultList.get(i + 1).length();
                if (jump <= maxLength) {
                    resultBuffer.add(resultList.get(i + 1));
                    count += resultList.get(i + 1).length();
                    i++;
                }
            }
        }

        return resultBuffer;
    }

    public static void main(String[] args) {
        String document = "算法 大致分为基本算法、数据 结构 的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
                "算法可以宽泛 的分为三类，\n" +
                "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
                "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
                "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
        System.out.println(TextRankSentence.getTopSentenceList(document, 3));
    }
}
