package org.xm.xmnlp.dictionary.common;


import static org.xm.xmnlp.util.Predefine.logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.xm.xmnlp.collection.trie.bintrie.BinTrie;
import org.xm.xmnlp.util.Predefine;

/**
 * 最简单的词典，每一行只有一个词
 */
public class CommonStringDictionary {
    BinTrie<Byte> trie;

    public boolean load(String path) {
        trie = new BinTrie<Byte>();
        if (loadDat(path + Predefine.TRIE_EXT)) return true;
        String line = null;
        try {
            BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            while ((line = bw.readLine()) != null) {
                trie.put(line, null);
            }
            bw.close();
        } catch (Exception e) {
            logger.warning("加载" + path + "失败，" + e);
        }
        if (!trie.save(path + Predefine.TRIE_EXT)) logger.warning("缓存" + path + Predefine.TRIE_EXT + "失败");
        return true;
    }

    boolean loadDat(String path) {
        return trie.load(path);
    }

    public Set<String> keySet() {
        Set<String> keySet = new LinkedHashSet<String>();
        for (Map.Entry<String, Byte> entry : trie.entrySet()) {
            keySet.add(entry.getKey());
        }

        return keySet;
    }
}
