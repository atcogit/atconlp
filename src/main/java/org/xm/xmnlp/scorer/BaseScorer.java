package org.xm.xmnlp.scorer;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * 基本打分器
 *
 * @param <T> 这是储存器map中key的类型，具有相同key的句子会存入同一个entry
 */
public abstract class BaseScorer<T extends ISentenceKey> implements IScorer {
    public BaseScorer() {
        storage = new TreeMap<T, Set<String>>();
    }

    /**
     * 储存
     */
    protected Map<T, Set<String>> storage;
    /**
     * 权重
     */
    public double boost = 1.0;

    /**
     * 设置权重
     *
     * @param boost
     * @return
     */
    public BaseScorer setBoost(double boost) {
        this.boost = boost;
        return this;
    }

    @Override
    public void addSentence(String sentence) {
        T key = generateKey(sentence);
        if (key == null) return;
        Set<String> set = storage.get(key);
        if (set == null) {
            set = new TreeSet<String>();
            storage.put(key, set);
        }
        set.add(sentence);
    }

    /**
     * 生成能够代表这个句子的键
     *
     * @param sentence
     * @return
     */
    protected abstract T generateKey(String sentence);

    @Override
    public Map<String, Double> computeScore(String outerSentence) {
        TreeMap<String, Double> result = new TreeMap<String, Double>(Collections.reverseOrder());
        T keyOuter = generateKey(outerSentence);
        if (keyOuter == null) return result;
        for (Map.Entry<T, Set<String>> entry : storage.entrySet()) {
            T key = entry.getKey();
            Double score = keyOuter.similarity(key);
            for (String sentence : entry.getValue()) {
                result.put(sentence, score);
            }
        }
        return result;
    }

    @Override
    public void removeAllSentences() {
        storage.clear();
    }
}
