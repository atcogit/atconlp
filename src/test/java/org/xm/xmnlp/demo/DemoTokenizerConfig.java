package org.xm.xmnlp.demo;


import org.xm.xmnlp.tokenizer.StandardTokenizer;

/**
 * 演示动态设置预置分词器，这里的设置是全局的
 */
public class DemoTokenizerConfig {
    public static void main(String[] args) {
        String text = "泽田依子是上外日本文化经济学院的外教";
        System.out.println(StandardTokenizer.segment(text));
        StandardTokenizer.SEGMENT.enableAllNamedEntityRecognize(true);
        System.out.println(StandardTokenizer.segment(text));
    }
}
