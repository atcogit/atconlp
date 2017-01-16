package org.xm.xmnlp.demo;


import java.util.Map;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.collection.ahocorasick.AhoCorasickDoubleArrayTrie;
import org.xm.xmnlp.dictionary.BaseSearcher;
import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.dictionary.CustomDictionary;
import org.xm.xmnlp.seg.other.AhoCorasickDoubleArrayTrieSegment;

/**
 * 演示用户词典的动态增删
 *
 */
public class DemoCustomDictionary {
    public static void main(String[] args) {
        // 动态增加
        CustomDictionary.add("攻城狮");

        CustomDictionary.add("黄色小电影", "bad 10");
        // 强行插入
        CustomDictionary.insert("白富美", "nz 1024");
        // 删除词语（注释掉试试）
//        CustomDictionary.remove("攻城狮");
        CustomDictionary.remove("法轮功");

        CustomDictionary.insert("法轮功", "bad 1000");
        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
        System.out.println(CustomDictionary.get("单身狗"));

        String text = "攻城狮逆袭单身狗，迎娶白富美，走上人生巅峰，这是AceBeverage公司的中华人名共和国的新世纪。" +
                "结婚的和尚未结婚的，他有一部AV黄色小电影，还是一个法轮功疯子";  // 怎么可能噗哈哈！

        // DoubleArrayTrie分词
        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>() {
            @Override
            public void hit(int begin, int end, CoreDictionary.Attribute value) {
                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
            }
        });
        // 首字哈希之后二分的trie树分词
        BaseSearcher searcher = CustomDictionary.getSearcher(text);
        Map.Entry entry;
        while ((entry = searcher.next()) != null) {
            System.out.println(entry);
        }

        AhoCorasickDoubleArrayTrieSegment acseg = new AhoCorasickDoubleArrayTrieSegment().loadDictionary(Xmnlp.Config.CoreDictionaryPath);
        // AC分词
        System.out.println("AC分词:"+acseg.seg(text).toString());
        // 标准分词
        System.out.println(Xmnlp.segment(text));

        // Note:动态增删不会影响词典文件
        // 目前CustomDictionary使用DAT储存词典文件中的词语，用BinTrie储存动态加入的词语，前者性能高，后者性能低
        // 之所以保留动态增删功能，一方面是历史遗留特性，另一方面是调试用；未来可能会去掉动态增删特性。
    }
}
