package org.xm.xmnlp.collection.trie;


import java.io.DataOutputStream;
import java.util.TreeMap;

import org.xm.xmnlp.corpus.io.ByteArray;

/**
 * trie树接口
 */
public interface ITrie<V> {
    int build(TreeMap<String, V> keyValueMap);

    boolean save(DataOutputStream out);

    boolean load(ByteArray byteArray, V[] value);

    V get(char[] key);

    V get(String key);

    V[] getValueArray(V[] a);

    boolean containsKey(String key);

    int size();
}
