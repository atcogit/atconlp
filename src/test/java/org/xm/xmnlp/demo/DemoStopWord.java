package org.xm.xmnlp.demo;


import java.util.List;

import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;
import org.xm.xmnlp.dictionary.stopword.Filter;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.NotionalTokenizer;
import org.xm.xmnlp.tokenizer.StandardTokenizer;

/**
 * 演示如何去除停用词
 */
public class DemoStopWord {
    public static void main(String[] args) {
        String text = "小区居民有的反对喂养流浪猫，而有的居民却赞成喂养这些小宝贝";
        // 可以动态修改停用词词典
        CoreStopWordDictionary.add("居民");
        System.out.println(NotionalTokenizer.segment(text));
        CoreStopWordDictionary.remove("居民");
        System.out.println(NotionalTokenizer.segment(text));
        // 可以对任意分词器的结果执行过滤
        List<Term> termList = StandardTokenizer.segment(text);
        System.out.println(termList);
        CoreStopWordDictionary.apply(termList);
        System.out.println(termList);
        // 还可以自定义过滤逻辑
        CoreStopWordDictionary.FILTER = new Filter() {
            @Override
            public boolean shouldInclude(Term term) {
                switch (term.nature) {
                    case nz:
                        return !CoreStopWordDictionary.contains(term.word);
                }
                return false;
            }
        };
        System.out.println(NotionalTokenizer.segment(text));
    }
}
