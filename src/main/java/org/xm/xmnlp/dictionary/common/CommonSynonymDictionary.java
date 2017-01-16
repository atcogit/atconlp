package org.xm.xmnlp.dictionary.common;

import static org.xm.xmnlp.util.Predefine.logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

import org.xm.xmnlp.collection.trie.DoubleArrayTrie;
import org.xm.xmnlp.corpus.dependency.conll.PosTagCompiler;
import org.xm.xmnlp.corpus.synonym.Synonym;
import org.xm.xmnlp.corpus.synonym.Synonym.Type;
import org.xm.xmnlp.corpus.synonym.SynonymHelper;
import org.xm.xmnlp.dictionary.CoreBiGramTableDictionary;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.StandardTokenizer;
import org.xm.xmnlp.util.Predefine;

/**
 * 通用同义词词典
 * Created by xuming on 2016/7/28.
 */
public class CommonSynonymDictionary {
    DoubleArrayTrie<SynonymItem> trie;
    /**
     * 词典中最大的语义ID距离
     */
    private long maxSynonymItemIdDistance;

    private CommonSynonymDictionary() {
    }

    public static CommonSynonymDictionary create(InputStream is) {
        CommonSynonymDictionary dictionary = new CommonSynonymDictionary();
        if (dictionary.load(is)) {
            return dictionary;
        }
        return null;
    }

    private boolean load(InputStream is) {
        trie = new DoubleArrayTrie<SynonymItem>();
        TreeMap<String, SynonymItem> treeMap = new TreeMap<String, SynonymItem>();
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            ArrayList<Synonym> synonymList = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(" ");
                synonymList = Synonym.create(strs);
                char type = strs[0].charAt(strs[0].length() - 1);
                for (Synonym synonym : synonymList) {
                    treeMap.put(synonym.realWord, new SynonymItem(synonym, synonymList, type));
                }
            }
            br.close();
            // 获取最大语义Id
            if (synonymList != null && synonymList.size() > 0) {
                maxSynonymItemIdDistance = synonymList.get(synonymList.size() - 1).id - SynonymHelper.convertString2IdWithIndex("Aa01A01", 0) + 1;

            }
            int resultCode = trie.build(treeMap);
            if (resultCode != 0) {
                logger.warning("构建" + is + "失败，错误码" + resultCode);
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.warning("读取" + is + "失败，可能由行" + line + "造成");
            return false;
        }
    }

    public SynonymItem get(String key) {
        return trie.get(key);
    }

    /**
     * 获取最大id
     *
     * @return 一个长整型的id
     */
    public long getMaxSynonymItemIdDistance() {
        return maxSynonymItemIdDistance;
    }

    /**
     * 语义距离
     *
     * @param a
     * @param b
     * @return
     */
    public long distance(String a, String b) {
        SynonymItem itemA = get(a);
        if (itemA == null) return Long.MAX_VALUE / 3;
        SynonymItem itemB = get(b);
        if (itemB == null) return Long.MAX_VALUE / 3;

        return itemA.distance(itemB);
    }

    public String rewriteQuickly(String text) {
        assert text != null;
        StringBuilder sbOut = new StringBuilder((int) (text.length() * 1.2));
        String preWord = Predefine.TAG_BEGIN;
        for (int i = 0; i < text.length(); ++i) {
            int state = 1;
            state = trie.transition(text.charAt(i), state);
            if (state > 0) {
                int start = i;
                int to = i + 1;
                int end = -1;
                SynonymItem value = null;
                for (; to < text.length(); ++to) {
                    state = trie.transition(text.charAt(to), state);
                    if (state < 0) break;
                    SynonymItem output = trie.output(state);
                    if (output != null) {
                        value = output;
                        end = to + 1;
                    }
                }
                if (value != null) {
                    Synonym synonym = value.randomSynonym(Type.EQUAL, preWord);
                    if (synonym != null) {
                        sbOut.append(synonym.realWord);
                        preWord = synonym.realWord;
                    } else {
                        preWord = text.substring(start, end);
                        sbOut.append(preWord);
                    }
                    i = end - 1;
                } else {
                    preWord = String.valueOf(text.charAt(i));
                    sbOut.append(text.charAt(i));
                }
            } else {
                preWord = String.valueOf(text.charAt(i));
                sbOut.append(text.charAt(i));
            }
        }

        return sbOut.toString();
    }

    public String rewrite(String text) {
        List<Term> termList = StandardTokenizer.segment(text.toCharArray());
        StringBuilder sbOut = new StringBuilder((int) (text.length() * 1.2));
        String preWord = Predefine.TAG_BEGIN;
        for (Term term : termList) {
            SynonymItem synonymItem = get(term.word);
            Synonym synonym;
            if (synonymItem != null && (synonym = synonymItem.randomSynonym(Type.EQUAL, preWord)) != null) {
                sbOut.append(synonym.realWord);
            } else sbOut.append(term.word);
            preWord = PosTagCompiler.compile(term.nature.toString(), term.word);
        }
        return sbOut.toString();
    }

    /**
     * 词典中的一个条目
     */
    public static class SynonymItem {
        /**
         * 条目的key
         */
        public Synonym entry;
        /**
         * 条目的value，是key的同义词列表
         */
        public List<Synonym> synonymList;
        /**
         * 这个条目的类型，同义词或同类词或封闭词
         */
        public Type type;

        public SynonymItem(Synonym entry, List<Synonym> synonymList, Synonym.Type type) {
            this.entry = entry;
            this.synonymList = synonymList;
            this.type = type;
        }

        public SynonymItem(Synonym entry, List<Synonym> synonymList, char type) {
            this.entry = entry;
            this.synonymList = synonymList;
            switch (type) {
                case '=':
                    this.type = Type.EQUAL;
                    break;
                case '#':
                    this.type = Type.LIKE;
                    break;
                default:
                    this.type = Type.SINGLE;
                    break;
            }
        }

        /**
         * 随机挑一个近义词
         *
         * @param type 类型
         * @return
         */
        public Synonym randomSynonym(Type type, String preWord) {
            ArrayList<Synonym> synonymArrayList = new ArrayList<Synonym>(synonymList);
            ListIterator<Synonym> listIterator = synonymArrayList.listIterator();
            if (type != null) while (listIterator.hasNext()) {
                Synonym synonym = listIterator.next();
                if (synonym.type != type || (preWord != null && CoreBiGramTableDictionary.getBiFrequency(preWord, synonym.realWord) == 0))
                    listIterator.remove();
            }
            if (synonymArrayList.size() == 0) return null;
            return synonymArrayList.get((int) (System.currentTimeMillis() % (long) synonymArrayList.size()));
        }

        public Synonym randomSynonym() {
            return randomSynonym(null, null);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(entry);
            sb.append(' ');
            sb.append(type);
            sb.append(' ');
            sb.append(synonymList);
            return sb.toString();
        }

        /**
         * 语义距离
         *
         * @param other
         * @return
         */
        public long distance(SynonymItem other) {
            return entry.distance(other.entry);
        }

        /**
         * 创建一个@类型的词典之外的条目
         *
         * @param word
         * @return
         */
        public static SynonymItem createUndefined(String word) {
            SynonymItem item = new SynonymItem(new Synonym(word, word.hashCode() * 1000000 + Long.MAX_VALUE / 3 ,Type.UNDEFINED), null, Type.UNDEFINED);
            return item;
        }

    }

}
