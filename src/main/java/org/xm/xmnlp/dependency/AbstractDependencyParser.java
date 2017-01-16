package org.xm.xmnlp.dependency;


import java.util.Map;
import java.util.TreeMap;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLSentence;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLWord;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.util.IOUtil;


/**
 * 句法依存抽象类
 * @author xuming
 */
public abstract class AbstractDependencyParser implements IDependencyParser {
    /**
     * 本Parser使用的分词器，可以自由替换
     */
    private Segment segment = Xmnlp.newSegment().enablePartOfSpeechTagging(true);
    /**
     * 依存关系映射表（可以将英文标签映射为中文）
     */
    private Map<String, String> deprelTranslater;
    /**
     * 是否自动转换依存关系
     */
    private boolean enableDeprelTranslater;

    @Override
    public CoNLLSentence parse(String sentence) {
        assert sentence != null;
        CoNLLSentence output = parse(segment.seg(sentence.toCharArray()));
        if (enableDeprelTranslater && deprelTranslater != null) {
            for (CoNLLWord word : output) {
                String translatedDeprel = deprelTranslater.get(word.DEPREL);
                word.DEPREL = translatedDeprel;
            }
        }
        return output;
    }

    @Override
    public Segment getSegment() {
        return segment;
    }

    @Override
    public IDependencyParser setSegment(Segment segment) {
        this.segment = segment;
        return this;
    }

    @Override
    public Map<String, String> getDeprelTranslator() {
        return deprelTranslater;
    }

    @Override
    public IDependencyParser setDeprelTranslator(Map<String, String> deprelTranslator) {
        this.deprelTranslater = deprelTranslator;
        return this;
    }

    /**
     * 设置映射表
     *
     * @param deprelTranslatorPath 映射表路径
     * @return
     */
    public IDependencyParser setDeprelTranslater(String deprelTranslatorPath) {
        IOUtil.LineIterator iterator = new IOUtil.LineIterator(deprelTranslatorPath);
        deprelTranslater = new TreeMap<String, String>();
        while (iterator.hasNext()) {
            String[] args = iterator.next().split("\\s");
            deprelTranslater.put(args[0], args[1]);
        }
        if (deprelTranslater.size() == 0) {
            deprelTranslater = null;
        }

        return this;
    }

    @Override
    public IDependencyParser enableDeprelTranslator(boolean enable) {
        enableDeprelTranslater = enable;
        return this;
    }
}