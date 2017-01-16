package org.xm.xmnlp.dependency;


import java.util.List;
import java.util.Map;

import org.xm.xmnlp.corpus.dependency.conll.CoNLLSentence;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 依存句法分析器接口
 *
 * @author hankcs
 */
public interface IDependencyParser {
    /**
     * 分析句子的依存句法
     *
     * @param termList 句子，可以是任何具有词性标注功能的分词器的分词结果
     * @return CoNLL格式的依存句法树
     */
    CoNLLSentence parse(List<Term> termList);

    /**
     * 分析句子的依存句法
     *
     * @param sentence 句子
     * @return CoNLL格式的依存句法树
     */
    CoNLLSentence parse(String sentence);

    /**
     * 获取Parser使用的分词器
     *
     * @return
     */
    Segment getSegment();

    /**
     * 设置Parser使用的分词器
     *
     * @param segment
     */
    IDependencyParser setSegment(Segment segment);

    /**
     * 获取依存关系映射表
     *
     * @return
     */
    Map<String, String> getDeprelTranslator();

    /**
     * 设置依存关系映射表
     *
     * @param deprelTranslator
     */
    IDependencyParser setDeprelTranslator(Map<String, String> deprelTranslator);

    /**
     * 依存关系自动转换开关
     *
     * @param enable
     */
    IDependencyParser enableDeprelTranslator(boolean enable);
}
