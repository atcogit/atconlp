package org.xm.xmnlp.demo;


import java.util.List;

import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.NotionalTokenizer;

/**
 * 演示自动去除停用词、自动断句的分词器
 */
public class DemoNotionalTokenizer {
    public static void main(String[] args) {
        String text = "小区居民有的反对喂养流浪猫，而有的居民却赞成喂养这些小宝贝";
        // 自动去除停用词
        System.out.println(NotionalTokenizer.segment(text));    // 停用词典位于data/dictionary/stopwords.txt，可以自行修改
        // 自动断句+去除停用词
        for (List<Term> sentence : NotionalTokenizer.seg2sentence(text)) {
            System.out.println(sentence);
        }
    }
}
