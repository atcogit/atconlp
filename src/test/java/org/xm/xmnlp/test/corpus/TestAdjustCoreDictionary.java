package org.xm.xmnlp.test.corpus;


import java.util.List;
import java.util.Map;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dependency.conll.CorpusLoader;
import org.xm.xmnlp.corpus.dictionary.item.Item;
import org.xm.xmnlp.corpus.dictionary.maker.DictionaryMaker;
import org.xm.xmnlp.corpus.dictionary.maker.TFDictionary;
import org.xm.xmnlp.corpus.document.Document;
import org.xm.xmnlp.corpus.document.word.CompoundWord;
import org.xm.xmnlp.corpus.document.word.IWord;
import org.xm.xmnlp.corpus.statistics.TermFrequency;

import junit.framework.TestCase;

/**
 * 往核心词典里补充等效词串
 *
 * @author hankcs
 */
public class TestAdjustCoreDictionary extends TestCase {

    public static final String DATA_DICTIONARY_CORE_NATURE_DICTIONARY_TXT = Xmnlp.Config.CoreDictionaryPath;

    public void testGetCompiledWordFromDictionary() throws Exception {
        DictionaryMaker dictionaryMaker = DictionaryMaker.load("data/test/CoreNatureDictionary.txt");
        for (Map.Entry<String, Item> entry : dictionaryMaker.entrySet()) {
            String word = entry.getKey();
            Item item = entry.getValue();
            if (word.matches(".##.")) {
                System.out.println(item);
            }
        }
    }

    public void testViewNGramDictionary() throws Exception {
        TFDictionary tfDictionary = new TFDictionary();
        tfDictionary.load("data/dictionary/CoreNatureDictionary.ngram.txt");
        for (Map.Entry<String, TermFrequency> entry : tfDictionary.entrySet()) {
            String word = entry.getKey();
            TermFrequency frequency = entry.getValue();
            if (word.contains("##")) {
                System.out.println(frequency);
            }
        }
    }

    public void testSortCoreNatureDictionary() throws Exception {
        DictionaryMaker dictionaryMaker = DictionaryMaker.load(DATA_DICTIONARY_CORE_NATURE_DICTIONARY_TXT);
        dictionaryMaker.saveTxtTo(DATA_DICTIONARY_CORE_NATURE_DICTIONARY_TXT);
    }

    public void testSimplifyNZ() throws Exception {
        final DictionaryMaker nzDictionary = new DictionaryMaker();
        CorpusLoader.walk("D:\\Doc\\语料库\\2014", new CorpusLoader.Handler() {
            @Override
            public void handle(Document document) {
                for (List<IWord> sentence : document.getComplexSentenceList()) {
                    for (IWord word : sentence) {
                        if (word instanceof CompoundWord && "nz".equals(word.getLabel())) {
                            nzDictionary.add(word);
                        }
                    }
                }
            }
        });
        nzDictionary.saveTxtTo("data/test/nz.txt");
    }

    public void testRemoveNumber() throws Exception {
        // 一些汉字数词留着没用，除掉它们
        DictionaryMaker dictionaryMaker = DictionaryMaker.load(DATA_DICTIONARY_CORE_NATURE_DICTIONARY_TXT);
        dictionaryMaker.saveTxtTo(DATA_DICTIONARY_CORE_NATURE_DICTIONARY_TXT, new DictionaryMaker.Filter() {
            @Override
            public boolean onSave(Item item) {
                if (item.key.length() == 1 && "0123456789零○〇一二两三四五六七八九十廿百千万亿壹贰叁肆伍陆柒捌玖拾佰仟".indexOf(item.key.charAt(0)) >= 0) {
                    System.out.println(item);
                    return false;
                }

                return true;
            }
        });
    }
}
