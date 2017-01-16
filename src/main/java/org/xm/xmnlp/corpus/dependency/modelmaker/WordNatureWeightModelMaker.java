package org.xm.xmnlp.corpus.dependency.modelmaker;


import java.util.Set;
import java.util.TreeSet;

import org.xm.xmnlp.corpus.dependency.conll.CoNLLLoader;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLSentence;
import org.xm.xmnlp.corpus.dependency.conll.CoNLLWord;
import org.xm.xmnlp.corpus.dictionary.maker.DictionaryMaker;
import org.xm.xmnlp.corpus.document.word.Word;
import org.xm.xmnlp.util.IOUtil;

/**
 * 生成模型打分器模型构建工具
 *
 * @author hankcs
 */
public class WordNatureWeightModelMaker {
    public static boolean makeModel(String corpusLoadPath, String modelSavePath) {
        Set<String> posSet = new TreeSet<String>();
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        for (CoNLLSentence sentence : CoNLLLoader.loadSentenceList(corpusLoadPath)) {
            for (CoNLLWord word : sentence.word) {
                addPair(word.NAME, word.HEAD.NAME, word.DEPREL, dictionaryMaker);
                addPair(word.NAME, wrapTag(word.HEAD.POSTAG), word.DEPREL, dictionaryMaker);
                addPair(wrapTag(word.POSTAG), word.HEAD.NAME, word.DEPREL, dictionaryMaker);
                addPair(wrapTag(word.POSTAG), wrapTag(word.HEAD.POSTAG), word.DEPREL, dictionaryMaker);
                posSet.add(word.POSTAG);
            }
        }
        for (CoNLLSentence sentence : CoNLLLoader.loadSentenceList(corpusLoadPath)) {
            for (CoNLLWord word : sentence.word) {
                addPair(word.NAME, word.HEAD.NAME, word.DEPREL, dictionaryMaker);
                addPair(word.NAME, wrapTag(word.HEAD.POSTAG), word.DEPREL, dictionaryMaker);
                addPair(wrapTag(word.POSTAG), word.HEAD.NAME, word.DEPREL, dictionaryMaker);
                addPair(wrapTag(word.POSTAG), wrapTag(word.HEAD.POSTAG), word.DEPREL, dictionaryMaker);
                posSet.add(word.POSTAG);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String pos : posSet) {
            sb.append("case \"" + pos + "\":\n");
        }
        IOUtil.saveTxt("data/model/dependency/pos-thu.txt", sb.toString());
        return dictionaryMaker.saveTxtTo(modelSavePath);
    }

    private static void addPair(String from, String to, String label, DictionaryMaker dictionaryMaker) {
        dictionaryMaker.add(new Word(from + "@" + to, label));
        dictionaryMaker.add(new Word(from + "@", "频次"));
    }

    /**
     * 用尖括号将标签包起来
     *
     * @param tag
     * @return
     */
    public static String wrapTag(String tag) {
        return "<" + tag + ">";
    }

    public static void main(String[] args) {
        makeModel("D:\\Doc\\语料库\\依存分析训练数据\\THU\\train.conll.fixed.txt", "data/model/dependency/WordNature.txt");
    }
}
