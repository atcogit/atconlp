package org.xm.xmnlp.dependency;


import static org.xm.xmnlp.util.Predefine.logger;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.Pair;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLSentence;
import org.xm.xmnlp.corpus.io.ByteArray;
import org.xm.xmnlp.corpus.io.ByteArrayStream;
import org.xm.xmnlp.dependency.common.Edge;
import org.xm.xmnlp.dependency.common.Node;
import org.xm.xmnlp.model.maxent.MaxEntModel;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.Predefine;
/**
 * 最大熵句法分析器
 */
public class MaxEntDependencyParser extends MinimumSpanningTreeParser {
    static final MaxEntDependencyParser INSTANCE = new MaxEntDependencyParser();
    static MaxEntModel model;

    static {
        long start = System.currentTimeMillis();
        ByteArray byteArray = ByteArrayStream.createByteArrayStream(Xmnlp.Config.MaxEntModelPath + Predefine.BIN_EXT);
        if (byteArray != null) {
            model = MaxEntModel.create(byteArray);
        } else {
            model = MaxEntModel.create(Xmnlp.Config.MaxEntModelPath);
        }
        String result = model == null ? "失败" : "成功";
        logger.info("最大熵依存句法模型载入" + result + "，耗时" + (System.currentTimeMillis() - start) + " ms");
    }

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
        LinkedList<String> context = new LinkedList<String>();
        int index = from;
        for (int i = index - 2; i < index + 2 + 1; ++i) {
            Node w = i >= 0 && i < nodeArray.length ? nodeArray[i] : Node.NULL;
            context.add(w.compiledWord + "i" + (i - index));      // 在尾巴上做个标记，不然特征冲突了
            context.add(w.label + "i" + (i - index));
        }
        index = to;
        for (int i = index - 2; i < index + 2 + 1; ++i) {
            Node w = i >= 0 && i < nodeArray.length ? nodeArray[i] : Node.NULL;
            context.add(w.compiledWord + "j" + (i - index));      // 在尾巴上做个标记，不然特征冲突了
            context.add(w.label + "j" + (i - index));
        }
        context.add(nodeArray[from].compiledWord + '→' + nodeArray[to].compiledWord);
        context.add(nodeArray[from].label + '→' + nodeArray[to].label);
        context.add(nodeArray[from].compiledWord + '→' + nodeArray[to].compiledWord + (from - to));
        context.add(nodeArray[from].label + '→' + nodeArray[to].label + (from - to));
        Node wordBeforeI = from - 1 >= 0 ? nodeArray[from - 1] : Node.NULL;
        Node wordBeforeJ = to - 1 >= 0 ? nodeArray[to - 1] : Node.NULL;
        context.add(wordBeforeI.compiledWord + '@' + nodeArray[from].compiledWord + '→' + nodeArray[to].compiledWord);
        context.add(nodeArray[from].compiledWord + '→' + wordBeforeJ.compiledWord + '@' + nodeArray[to].compiledWord);
        context.add(wordBeforeI.label + '@' + nodeArray[from].label + '→' + nodeArray[to].label);
        context.add(nodeArray[from].label + '→' + wordBeforeJ.label + '@' + nodeArray[to].label);
        List<Pair<String, Double>> pairList = model.predict(context.toArray(new String[0]));
        Pair<String, Double> maxPair = new Pair<String, Double>("null", -1.0);
//        System.out.println(context);
//        System.out.println(pairList);
        for (Pair<String, Double> pair : pairList) {
            if (pair.getValue() > maxPair.getValue() && !"null".equals(pair.getKey())) {
                maxPair = pair;
            }
        }
//        System.out.println(nodeArray[from].word + "→" + nodeArray[to].word + " : " + maxPair);

        return new Edge(from, to, maxPair.getKey(), (float) -Math.log(maxPair.getValue()));
    }
}
