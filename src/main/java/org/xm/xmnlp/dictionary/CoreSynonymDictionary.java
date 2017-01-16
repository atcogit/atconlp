package org.xm.xmnlp.dictionary;

import static org.xm.xmnlp.util.Predefine.logger;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.common.CommonSynonymDictionary;
import org.xm.xmnlp.math.EditDistance;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 核心同义词词典
 * Created by xuming on 2016/7/28.
 */
public class CoreSynonymDictionary {
    static CommonSynonymDictionary dictionary;

    static {
        try {
            long start = System.currentTimeMillis();
            dictionary = CommonSynonymDictionary.create(new FileInputStream(Xmnlp.Config.CoreSynonymDictionaryDictionaryPath));
            logger.info("载入核心同义词词典成功，耗时 " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            System.err.println("载入核心同义词词典失败" + e);
            System.exit(-1);
        }
    }

    public static CommonSynonymDictionary.SynonymItem get(String key) {
        return dictionary.get(key);
    }

    /**
     * 不分词直接转换为同义词
     *
     * @param text
     * @return
     */
    public static String reWriteQuickly(String text) {
        return dictionary.rewriteQuickly(text);
    }

    public static String rewrite(String text) {
        return dictionary.rewrite(text);
    }

    /**
     * 语义距离
     *
     * @param itemA
     * @param itemB
     * @return
     */
    public static long distance(CommonSynonymDictionary.SynonymItem itemA, CommonSynonymDictionary.SynonymItem itemB) {
        return itemA.distance(itemB);
    }

    /**
     * 判断两个单词之间的语义距离
     *
     * @param A
     * @param B
     * @return
     */
    public static long distance(String A, String B) {
        CommonSynonymDictionary.SynonymItem itemA = get(A);
        CommonSynonymDictionary.SynonymItem itemB = get(B);
        if (itemA == null || itemB == null) return Long.MAX_VALUE;

        return distance(itemA, itemB);
    }

    /**
     * 计算两个单词之间的相似度，0表示不相似，1表示完全相似
     *
     * @param A
     * @param B
     * @return
     */
    public static double similarity(String A, String B) {
        long distance = distance(A, B);
        if (distance > dictionary.getMaxSynonymItemIdDistance()) return 0.0;
        return (dictionary.getMaxSynonymItemIdDistance() - distance) / (double) dictionary.getMaxSynonymItemIdDistance();
    }

    /**
     * 将分词结果转换为同义词列表
     *
     * @param sentence          句子
     * @param withUndefinedItem 是否保留词典中没有的词语
     * @return
     */
    public static List<CommonSynonymDictionary.SynonymItem> convert(List<Term> sentence, boolean withUndefinedItem) {
        List<CommonSynonymDictionary.SynonymItem> synonymItemList = new ArrayList<CommonSynonymDictionary.SynonymItem>();
        for (Term term : sentence) {
            CommonSynonymDictionary.SynonymItem item = get(term.word);
            if (item == null) {
                if (withUndefinedItem) {
                    item = CommonSynonymDictionary.SynonymItem.createUndefined(term.word);
                    synonymItemList.add(item);
                }
            } else {
                synonymItemList.add(item);
            }
        }
        return synonymItemList;
    }

    /**
     * 获取语义id
     *
     * @param synonymItemList
     * @return
     */
    public static long[] getLexemeArray(List<CommonSynonymDictionary.SynonymItem> synonymItemList) {
        long[] array = new long[synonymItemList.size()];
        int i = 0;
        for (CommonSynonymDictionary.SynonymItem item : synonymItemList) {
            array[i++] = item.entry.id;
        }
        return array;
    }

    public long distance(List<CommonSynonymDictionary.SynonymItem> synonymItemListA, List<CommonSynonymDictionary.SynonymItem> synonymItemListB) {
        return EditDistance.compute(synonymItemListA, synonymItemListB);
    }

    public long distance(long[] arrayA, long[] arrayB) {
        return EditDistance.compute(arrayA, arrayB);
    }

}
