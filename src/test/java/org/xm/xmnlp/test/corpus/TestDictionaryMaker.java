package org.xm.xmnlp.test.corpus;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.xm.xmnlp.corpus.dependency.conll.CorpusLoader;
import org.xm.xmnlp.corpus.dictionary.item.Item;
import org.xm.xmnlp.corpus.dictionary.maker.DictionaryMaker;
import org.xm.xmnlp.corpus.dictionary.maker.EasyDictionary;
import org.xm.xmnlp.corpus.document.Document;
import org.xm.xmnlp.corpus.document.word.CompoundWord;
import org.xm.xmnlp.corpus.document.word.IWord;
import org.xm.xmnlp.corpus.document.word.Word;

import junit.framework.TestCase;

public class TestDictionaryMaker extends TestCase {
    public void testSingleDocument() throws Exception {
        Document document = CorpusLoader.convert2Document(new File("data/2014/0101/c1002-23996898.txt"));
        DictionaryMaker dictionaryMaker = new DictionaryMaker();
        System.out.println(document);
        addToDictionary(document, dictionaryMaker);
        dictionaryMaker.saveTxtTo("data/dictionaryTest.txt");
    }

    private void addToDictionary(Document document, DictionaryMaker dictionaryMaker) {
        for (IWord word : document.getWordList()) {
            if (word instanceof CompoundWord) {
                for (Word inner : ((CompoundWord) word).innerList) {
                    // 暂时不统计人名
                    if (inner.getLabel().equals("nr")) {
                        continue;
                    }
                    // 如果需要人名，注销上面这句即可
                    dictionaryMaker.add(inner);
                }
            }
            // 暂时不统计人名
            if (word.getLabel().equals("nr")) {
                continue;
            }
            // 如果需要人名，注销上面这句即可
            dictionaryMaker.add(word);
        }
    }

    public void testMakeDictionary() throws Exception {
        final DictionaryMaker dictionaryMaker = new DictionaryMaker();
        CorpusLoader.walk("data/2014", new CorpusLoader.Handler() {
            @Override
            public void handle(Document document) {
                addToDictionary(document, dictionaryMaker);
            }
        });
        dictionaryMaker.saveTxtTo("data/2014_dictionary.txt");
    }

    public void testLoadItemList() throws Exception {
        List<Item> itemList = DictionaryMaker.loadAsItemList("data/2014_dictionary.txt");
        Map<String, Integer> labelMap = new TreeMap<String, Integer>();
        for (Item item : itemList) {
            for (Map.Entry<String, Integer> entry : item.labelMap.entrySet()) {
                Integer frequency = labelMap.get(entry.getKey());
                if (frequency == null) frequency = 0;
                labelMap.put(entry.getKey(), frequency + entry.getValue());
            }
        }
        for (String label : labelMap.keySet()) {
            System.out.println(label);
        }
        System.out.println(labelMap.size());
    }

    public void testLoadEasyDictionary() throws Exception {
        EasyDictionary dictionary = EasyDictionary.create("data/2014_dictionary.txt");
        System.out.println(dictionary.GetWordInfo("高峰"));
    }

}
