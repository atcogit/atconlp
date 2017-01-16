package org.xm.xmnlp.segword;


import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.xm.xmnlp.util.StaticUtils;

/**
 * 基础分词算法
 *
 * xuming
 */
public class Rule {

    private static final HashMap<String, String[]> dictionary = StaticUtils.getRuleDicMap();


    /**
     * 反向最大匹配
     */
    public static Vector<String> reverseMaxSeg(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        int max_words = 16;
        Vector<String> results = new Vector<String>();
        while (text.length() > 0) {
            String temp;
            //temp就是待分词的短语
            if (text.length() < max_words) {
                temp = text;
            }
            else {
                temp = text.substring(text.length() - max_words);
            }
            while (temp.length() > 0) {
                if (dictionary.get(temp) != null || temp.length() == 1) {
                    //如果在字典中找到某个词，这个词被加入到分词结果中同时从原始输入中删除这个词
                    results.add(temp);
                    text = text.substring(0, text.length() - temp.length());
                    break;
                }
                else {
                    //待分词短语从左向右不断变短
                    temp = temp.substring(1);
                }
            }
        }
        Collections.reverse(results);
        return results;
    }


    /**
     * 正向最大匹配
     */
    public static Vector<String> forwardMaxSeg(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        int max_words = 16;
        Vector<String> results = new Vector<String>();
        while (text.length() > 0) {
            String temp;
            if (text.length() < max_words) {
                temp = text;
            }
            else {
                temp = text.substring(0, max_words);
            }
            while (temp.length() > 0) {
                if (dictionary.get(temp) != null || temp.length() == 1) {
                    results.add(temp);
                    text = text.substring(temp.length());
                    break;
                }
                else {
                    temp = temp.substring(0, temp.length() - 1);
                }
            }
        }
        return results;
    }

    /**
     * 双向最大匹配分词 Bidirectional maximum matching
     * fresults 正向最大匹配的分词结果
     * bresults 逆向最大匹配的分词结果
     */
    public static Vector<String> biMaxSeg(String text) {
        Vector<String> fresults = forwardMaxSeg(text);
        Vector<String> bresults = reverseMaxSeg(text);
        //如果正反向分词结果词数不同，则取分词数量较少的那个
        if (fresults.size() != bresults.size()) {
            if (fresults.size() > bresults.size())
                return bresults;
            else
                return fresults;
        }
        //如果分词结果词数相同
        else {
            //如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
            int i, FSingle = 0, BSingle = 0;
            boolean isSame = true;
            for (i = 0; i < fresults.size(); i++) {
                if (!fresults.get(i).equals(bresults.get(i)))
                    isSame = false;
                if (fresults.get(i).length() == 1)
                    FSingle += 1;
                if (bresults.get(i).length() == 1)
                    BSingle += 1;
            }
            if (isSame)
                return fresults;
            else {
                //分词结果不同，返回其中单字较少的那个
                if (BSingle > FSingle)
                    return fresults;
                else
                    return bresults;
            }
        }
    }


    /**
     * 正向最小匹配
     */
    public static Vector<String> forwardMinSeg(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        Vector<String> results = new Vector<String>();
        int len = 1;
        int start = 0;
        while (start < text.length()) {
            while (dictionary.get(text.substring(start, len + start)) == null) {
                if (len == (text.length() - start)) {
                    len = 1;
                    break;
                }
                len++;
            }
            results.add(text.substring(start, len + start));
            start += len;
            len = 1;
        }
        return results;
    }

    /**
     * 反向最小匹配
     *
     * @param text 输入字符串
     * @return 列表
     */
    public static Vector<String> reverseMinSeg(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        Vector<String> results = new Vector<String>();
        int length = text.length();
        int step = 1;
        int start = length - step;
        while (start > 0) {
            while (dictionary.get(text.substring(start, step + start)) == null) {
                step++;
                start--;
                if (start < 0) {
                    start += step - 1;
                    step = 1;
                    break;
                }
            }
            results.add(text.substring(start, step + start));
            start--;
            step = 1;
        }
        Collections.reverse(results);
        return results;
    }

    /**
     * 双向最小匹配分词 Bidirectional minimum matching
     */
    public static Vector<String> biMinSeg(String text) {
        Vector<String> minResults = forwardMinSeg(text);
        Vector<String> rminResults = reverseMinSeg(text);
        //如果正反向分词结果词数不同，则取分词数量较少的那个
        if (minResults.size() != rminResults.size()) {
            if (minResults.size() > rminResults.size())
                return rminResults;
            else
                return minResults;
        }
        //如果分词结果词数相同
        else {
            //如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
            int i, FSingle = 0, BSingle = 0;
            boolean isSame = true;
            for (i = 0; i < minResults.size(); i++) {
                if (!minResults.get(i).equals(rminResults.get(i)))
                    isSame = false;
                if (minResults.get(i).length() == 1)
                    FSingle += 1;
                if (rminResults.get(i).length() == 1)
                    BSingle += 1;
            }
            if (isSame)
                return minResults;
            else {
                //分词结果不同，返回其中单字较少的那个
                if (BSingle > FSingle)
                    return minResults;
                else
                    return rminResults;
            }
        }
    }

    /**
     * 双向最大最小匹配分词 Bidirectional maximum minimum matching
     */
    public static Vector<String> biMaxMinSeg(String text) {
        Vector<String> minResults = forwardMinSeg(text);
        Vector<String> rminResults = reverseMinSeg(text);
        Vector<String> maxResults = forwardMaxSeg(text);
        Vector<String> rmaxResults = reverseMaxSeg(text);
        //如果正反向分词结果词数不同，则取分词数量较少的那个
        if (minResults.size() != rminResults.size()) {
            if (minResults.size() > rminResults.size())
                return rminResults;
            else
                return minResults;
        }
        //如果分词结果词数相同
        else {
            //如果正反向的分词结果相同，就说明没有歧义，可返回任意一个
            int i, FSingle = 0, BSingle = 0;
            boolean isSame = true;
            for (i = 0; i < minResults.size(); i++) {
                if (!minResults.get(i).equals(rminResults.get(i)))
                    isSame = false;
                if (minResults.get(i).length() == 1)
                    FSingle += 1;
                if (rminResults.get(i).length() == 1)
                    BSingle += 1;
            }
            if (isSame)
                return minResults;
            else {
                //分词结果不同，返回其中单字较少的那个
                if (BSingle > FSingle)
                    return minResults;
                else
                    return rminResults;
            }
        }
    }


}
