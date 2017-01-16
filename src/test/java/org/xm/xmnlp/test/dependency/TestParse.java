package org.xm.xmnlp.test.dependency;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLLoader;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLSentence;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLWord;
import org.xm.xmnlp.corpus.dependency.conll.Evaluator;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dependency.CRFDependencyParser;
import org.xm.xmnlp.dependency.MaxEntDependencyParser;
import org.xm.xmnlp.dependency.WordNatureDependencyParser;
import org.xm.xmnlp.seg.domain.Term;

import junit.framework.TestCase;

public class TestParse extends TestCase {
    public void testParse() throws Exception {
        List<Term> termList = new LinkedList<Term>();
        termList.add(new Term("坚决", Nature.ad));
        termList.add(new Term("惩治", Nature.v));
        termList.add(new Term("贪污", Nature.v));
        termList.add(new Term("贿赂", Nature.n));
        termList.add(new Term("等", Nature.udeng));
        termList.add(new Term("经济", Nature.n));
        termList.add(new Term("犯罪", Nature.vn));

        System.out.println(CRFDependencyParser.compute(termList));
    }

    public void testSegAndParse() throws Exception {
        System.out.println(MaxEntDependencyParser.compute("我喜欢刘膺奥"));
    }

    public void testMaxEntParser() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(MaxEntDependencyParser.compute("我每天骑车上学"));
    }

    public void testCRFParser() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(CRFDependencyParser.compute("我每天骑车上学"));
    }

    public void testWordNatureParser() throws Exception {
        Xmnlp.Config.enableDebug();
        System.out.println(WordNatureDependencyParser.compute("我每天骑车上学"));
    }

    /**
     * 神经网络分类模型arc-standard转移动作的判决式依存句法分析器
     *
     * @throws Exception
     */
    public void testNNParser() throws Exception {
//        System.out.println(NeuralNetworkDependencyParser.compute("徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。"));
    }

//    public void testNatureMap() throws Exception {
//        System.out.println('Ｏ' == 'Ｏ');
//        String text = "二Ｏ一二年四月五日";
//        List<Term> termList = NeuralNetworkDependencyParser.INSTANCE.getSegment().seg(text);
//        System.out.println(termList);
//        System.out.println(NeuralNetworkDependencyParser.compute(termList));
//    }

    public void testCrfParser() throws Exception {
        Xmnlp.Config.enableDebug();
        List<Term> termList = new LinkedList<Term>();
        termList.add(new Term("坚决", Nature.ad));
        termList.add(new Term("惩治", Nature.v));
        termList.add(new Term("贪污", Nature.v));
        termList.add(new Term("贿赂", Nature.n));
        termList.add(new Term("等", Nature.udeng));
        termList.add(new Term("经济", Nature.n));
        termList.add(new Term("犯罪", Nature.vn));
        System.out.println(CRFDependencyParser.compute(termList));
    }

    public void testEvaluate() throws Exception {
        testParse();
        LinkedList<CoNLLSentence> sentenceList = CoNLLLoader.loadSentenceList("D:\\Doc\\语料库\\依存分析训练数据\\THU\\dev.conll");
        Evaluator evaluator = new Evaluator();
        int id = 1;
        for (CoNLLSentence sentence : sentenceList) {
            //System.out.printf("%d / %d...", id++, sentenceList.size());
            long start = System.currentTimeMillis();
            List<Term> termList = new LinkedList<Term>();
            for (CoNLLWord word : sentence.word) {
                termList.add(new Term(word.LEMMA, Nature.valueOf(word.POSTAG)));
            }
            CoNLLSentence out = CRFDependencyParser.compute(termList);
            evaluator.e(sentence, out);
            System.out.println("done in " + (System.currentTimeMillis() - start) + " ms.");
        }
        System.out.println(evaluator);
    }
}
