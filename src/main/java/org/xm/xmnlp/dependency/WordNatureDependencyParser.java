package org.xm.xmnlp.dependency;


import java.util.List;

import org.xm.xmnlp.corpus.dependency.conll.CoNLLSentence;
import org.xm.xmnlp.dependency.common.Edge;
import org.xm.xmnlp.dependency.common.Node;
import org.xm.xmnlp.model.bigram.WordNatureDependencyModel;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 一个简单的句法分析器
 *
 * @author hankcs
 */
public class WordNatureDependencyParser extends MinimumSpanningTreeParser {
    static final WordNatureDependencyParser INSTANCE = new WordNatureDependencyParser();

    /**
     * 分析句子的依存句法
     *
     * @param termList 句子，可以是任何具有词性标注功能的分词器的分词结果
     * @return CoNLL格式的依存句法树
     */
    public static CoNLLSentence compute(List<Term> termList) {
        return INSTANCE.parse(termList);
    }

    /**
     * 分析句子的依存句法
     *
     * @param sentence 句子
     * @return CoNLL格式的依存句法树
     */
    public static CoNLLSentence compute(String sentence) {
        return INSTANCE.parse(sentence);
    }

    @Override
    protected Edge makeEdge(Node[] nodeArray, int from, int to) {
        return WordNatureDependencyModel.getEdge(nodeArray[from], nodeArray[to]);
    }
}
