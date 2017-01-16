package org.xm.xmnlp.seg.other;


import static org.xm.xmnlp.util.Predefine.logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.xm.xmnlp.collection.ahocorasick.AhoCorasickDoubleArrayTrie;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.IOUtil;
import org.xm.xmnlp.util.TextUtil;

/**
 * 使用AhoCorasickDoubleArrayTrie实现的最长分词器<br>
 * 需要用户调用setTrie()提供一个AhoCorasickDoubleArrayTrie
 */
public class AhoCorasickDoubleArrayTrieSegment extends DictionaryBasedSegment {
    AhoCorasickDoubleArrayTrie<CoreDictionary.Attribute> trie;

    @Override
    protected List<Term> segSentence(char[] sentence) {
        if (trie == null) {
            System.err.println("还未加载任何词典");
            return Collections.emptyList();
        }
        final int[] wordNet = new int[sentence.length];
        Arrays.fill(wordNet, 1);
        final Nature[] natureArray = config.speechTagging ? new Nature[sentence.length] : null;
        trie.parseText(sentence, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>() {
            @Override
            public void hit(int begin, int end, CoreDictionary.Attribute value) {
                int length = end - begin;
                if (length > wordNet[begin]) {
                    wordNet[begin] = length;
                    if (config.speechTagging) {
                        natureArray[begin] = value.nature[0];
                    }
                }
            }
        });
        LinkedList<Term> termList = new LinkedList<Term>();
        if (config.speechTagging) {
            DoubleArrayTrieSegment.getSpeechTagging(sentence, wordNet, natureArray);
        }
        for (int i = 0; i < wordNet.length; ) {
            Term term = new Term(new String(sentence, i, wordNet[i]), config.speechTagging ? (natureArray[i] == null ? Nature.nz : natureArray[i]) : null);
            term.offset = i;
            termList.add(term);
            i += wordNet[i];
        }
        return termList;
    }

    public AhoCorasickDoubleArrayTrieSegment() {
        super();
        config.useCustomDictionary = false;
        config.speechTagging = true;
    }

    @Override
    public Segment enableCustomDictionary(boolean enable) {
        throw new UnsupportedOperationException("AhoCorasickDoubleArrayTrieSegment暂时不支持用户词典。");
    }

    public AhoCorasickDoubleArrayTrie<CoreDictionary.Attribute> getTrie() {
        return trie;
    }

    public void setTrie(AhoCorasickDoubleArrayTrie<CoreDictionary.Attribute> trie) {
        this.trie = trie;
    }

    public AhoCorasickDoubleArrayTrieSegment loadDictionary(String... pathArray) {
        trie = new AhoCorasickDoubleArrayTrie<CoreDictionary.Attribute>();
        TreeMap<String, CoreDictionary.Attribute> map = null;
        try {
            map = IOUtil.loadDictionary(pathArray);
        } catch (IOException e) {
            logger.warning("加载词典失败\n" + TextUtil.exceptionToString(e));
            return this;
        }
        if (map != null && !map.isEmpty()) {
            trie.build(map);
        }

        return this;
    }
}
