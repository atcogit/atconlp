package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.other.AhoCorasickDoubleArrayTrieSegment;

/**
 * 基于AhoCorasickDoubleArrayTrie的分词器，该分词器允许用户跳过核心词典，直接使用自己的词典。
 * 需要注意的是，自己的词典必须遵守词典格式。
 */
public class DemoUseAhoCorasickDoubleArrayTrieSegment {
    public static void main(String[] args) {
        // AhoCorasickDoubleArrayTrieSegment要求用户必须提供自己的词典路径
        AhoCorasickDoubleArrayTrieSegment segment = new AhoCorasickDoubleArrayTrieSegment()
                .loadDictionary(Xmnlp.Config.CustomDictionaryPath[0]);
        System.out.println(segment.seg("微观经济学继续教育循环经济,结婚的和尚未结婚的和尚"));
    }
}
