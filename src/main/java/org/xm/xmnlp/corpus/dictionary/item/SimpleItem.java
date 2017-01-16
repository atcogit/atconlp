package org.xm.xmnlp.corpus.dictionary.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 多个条目
 */
public class SimpleItem {
    /**
     * 该条目的标签
     */
    public Map<String, Integer> labelMap;

    public SimpleItem() {
        labelMap = new TreeMap<String, Integer>();
    }

    public void addLabel(String label) {
        Integer frequency = labelMap.get(label);
        if (frequency == null) {
            frequency = 1;
        } else {
            ++frequency;
        }

        labelMap.put(label, frequency);
    }

    /**
     * 添加一个标签和频次
     *
     * @param label
     * @param frequency
     */
    public void addLabel(String label, Integer frequency) {
        Integer innerFrequency = labelMap.get(label);
        if (innerFrequency == null) {
            innerFrequency = frequency;
        } else {
            innerFrequency += frequency;
        }

        labelMap.put(label, innerFrequency);
    }

    public boolean containsLabel(String label) {
        return labelMap.containsKey(label);
    }

    public int getFrequency(String label) {
        Integer frequency = labelMap.get(label);
        if (frequency == null) return 0;
        return frequency;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(labelMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry<String, Integer> entry : entries) {
            sb.append(entry.getKey());
            sb.append(' ');
            sb.append(entry.getValue());
            sb.append(' ');
        }
        return sb.toString();
    }

    public static SimpleItem create(String param) {
        if (param == null) return null;
        String[] array = param.split(" ");
        return create(array);
    }

    public static SimpleItem create(String param[]) {
        if (param.length % 2 == 1) return null;
        SimpleItem item = new SimpleItem();
        int natureCount = (param.length) / 2;
        for (int i = 0; i < natureCount; ++i) {
            item.labelMap.put(param[2 * i], Integer.parseInt(param[1 + 2 * i]));
        }
        return item;
    }

    /**
     * 合并两个条目，两者的标签map会合并
     *
     * @param other
     */
    public void combine(SimpleItem other) {
        for (Map.Entry<String, Integer> entry : other.labelMap.entrySet()) {
            addLabel(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取全部频次
     *
     * @return
     */
    public int getTotalFrequency() {
        int frequency = 0;
        for (Integer f : labelMap.values()) {
            frequency += f;
        }
        return frequency;
    }

    public String getMostLikelyLabel() {
        return labelMap.entrySet().iterator().next().getKey();
    }
}
