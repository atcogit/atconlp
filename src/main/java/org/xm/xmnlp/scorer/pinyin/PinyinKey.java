package org.xm.xmnlp.scorer.pinyin;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.xm.xmnlp.collection.Pair;
import org.xm.xmnlp.dictionary.pinyin.Pinyin;
import org.xm.xmnlp.dictionary.pinyin.PinyinUtil;
import org.xm.xmnlp.dictionary.pinyin.String2PinyinConverter;
import org.xm.xmnlp.math.EditDistance;
import org.xm.xmnlp.math.LongestCommonSubstring;
import org.xm.xmnlp.scorer.ISentenceKey;

/**
 * 拼音Key
 */
public class PinyinKey implements Comparable<PinyinKey>, ISentenceKey<PinyinKey> {
    /**
     * 句子的拼音
     */
    Pinyin[] pinyinArray;
    /**
     * 拼音的ordinal数组
     */
    int[] pyOrdinalArray;
    /**
     * 首字母数组
     */
    char[] firstCharArray;

    public PinyinKey(String sentence) {
        Pair<List<Pinyin>, List<Boolean>> pair = String2PinyinConverter.convert2Pair(sentence, true);
        pinyinArray = PinyinUtil.convertList2Array(pair.getKey());
        List<Boolean> booleanList = pair.getValue();
        int pinyinSize = 0;
        for (Boolean yes : booleanList) {
            if (yes) {
                ++pinyinSize;
            }
        }
        int firstCharSize = 0;
        for (Pinyin pinyin : pinyinArray) {
            if (pinyin != Pinyin.none5) {
                ++firstCharSize;
            }
        }

        pyOrdinalArray = new int[pinyinSize];
        firstCharArray = new char[firstCharSize];
        pinyinSize = 0;
        firstCharSize = 0;
        Iterator<Boolean> iterator = booleanList.iterator();
        for (int i = 0; i < pinyinArray.length; ++i) {
            if (iterator.next()) {
                pyOrdinalArray[pinyinSize++] = pinyinArray[i].ordinal();
            }
            if (pinyinArray[i] != Pinyin.none5) {
                firstCharArray[firstCharSize++] = pinyinArray[i].getFirstChar();
            }
        }
    }

    @Override
    public int compareTo(PinyinKey o) {
        int len1 = pyOrdinalArray.length;
        int len2 = o.pyOrdinalArray.length;
        int lim = Math.min(len1, len2);
        int[] v1 = pyOrdinalArray;
        int[] v2 = o.pyOrdinalArray;

        int k = 0;
        while (k < lim) {
            int c1 = v1[k];
            int c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }

    @Override
    public Double similarity(PinyinKey other) {
        int firstCharArrayLength = firstCharArray.length + 1;
        return
                1.0 / (EditDistance.compute(pyOrdinalArray, other.pyOrdinalArray) + 1) +
                        (double) LongestCommonSubstring.compute(firstCharArray, other.firstCharArray) / firstCharArrayLength;
    }

    /**
     * 拼音的个数
     *
     * @return
     */
    public int size() {
        int length = 0;
        for (Pinyin pinyin : pinyinArray) {
            if (pinyin != Pinyin.none5) ++length;
        }

        return length;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PinyinKey{");
        sb.append("pinyinArray=").append(Arrays.toString(pinyinArray));
        sb.append(", pyOrdinalArray=").append(Arrays.toString(pyOrdinalArray));
        sb.append(", firstCharArray=").append(Arrays.toString(firstCharArray));
        sb.append('}');
        return sb.toString();
    }

    public char[] getFirstCharArray() {
        return firstCharArray;
    }
}
