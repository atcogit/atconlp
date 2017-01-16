package org.xm.xmnlp.corpus.dictionary;

import static org.xm.xmnlp.util.Predefine.logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.xm.xmnlp.collection.trie.bintrie.BinTrie;
/**
 * 可以调整大小的词典
 */
public abstract class SimpleDictionary<V> {
    public BinTrie<V> trie = new BinTrie<V>();

    public boolean load(String path) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                Map.Entry<String, V> entry = onGenerateEntry(line);
                if (entry == null) continue;
                trie.put(entry.getKey(), entry.getValue());
            }
            br.close();
        } catch (Exception e) {
            logger.warning("读取" + path + "失败" + e);
            return false;
        }
        return true;
    }

    /**
     * 查询一个单词
     *
     * @param key
     * @return 单词对应的条目
     */
    public V get(String key) {
        return trie.get(key);
    }

    /**
     * 由参数构造一个词条
     *
     * @param line
     * @return
     */
    protected abstract Map.Entry<String, V> onGenerateEntry(String line);

    /**
     * 以我为主词典，合并一个副词典，我有的词条不会被副词典覆盖
     *
     * @param other 副词典
     */
    public void combine(SimpleDictionary<V> other) {
        if (other.trie == null) {
            logger.warning("有个词典还没加载");
            return;
        }
        for (Map.Entry<String, V> entry : other.trie.entrySet()) {
            if (trie.containsKey(entry.getKey())) continue;
            trie.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取键值对集合
     *
     * @return
     */
    public Set<Map.Entry<String, V>> entrySet() {
        return trie.entrySet();
    }

    /**
     * 键集合
     *
     * @return
     */
    public Set<String> keySet() {
        TreeSet<String> keySet = new TreeSet<String>();

        for (Map.Entry<String, V> entry : entrySet()) {
            keySet.add(entry.getKey());
        }

        return keySet;
    }

    /**
     * 过滤部分词条
     *
     * @param filter 过滤器
     * @return 删除了多少条
     */
    public int remove(Filter filter) {
        int size = trie.size();
        for (Map.Entry<String, V> entry : entrySet()) {
            if (filter.remove(entry)) {
                trie.remove(entry.getKey());
            }
        }

        return size - trie.size();
    }

    public interface Filter<V> {
        boolean remove(Map.Entry<String, V> entry);
    }

    /**
     * 向中加入单词
     *
     * @param key
     * @param value
     */
    public void add(String key, V value) {
        trie.put(key, value);
    }

    public int size() {
        return trie.size();
    }
}
