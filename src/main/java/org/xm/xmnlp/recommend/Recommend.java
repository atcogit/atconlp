package org.xm.xmnlp.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.xm.xmnlp.scorer.BaseScorer;
import org.xm.xmnlp.scorer.IScorer;
import org.xm.xmnlp.scorer.editdistance.EditDistanceScorer;
import org.xm.xmnlp.scorer.lexeme.IdVectorScorer;
import org.xm.xmnlp.scorer.pinyin.PinyinScorer;

/**
 * 文本推荐器
 * Created by xuming on 2016/7/28.
 */
public class Recommend implements IRecommend {
    List<BaseScorer> scorerList;

    public Recommend() {
        scorerList = new ArrayList<BaseScorer>();
        scorerList.add(new IdVectorScorer());
        scorerList.add(new EditDistanceScorer());
        scorerList.add(new PinyinScorer());
    }

    /**
     * 推荐器
     *
     * @param scorers 达芬奇
     */
    public Recommend(BaseScorer... scorers) {
        scorerList = new ArrayList<BaseScorer>(scorers.length);
        for (BaseScorer scorer : scorers) {
            scorerList.add(scorer);
        }
    }

    public Recommend(List<BaseScorer> scorerList) {
        this.scorerList = scorerList;
    }

    @Override
    public void addSentence(String sentence) {
        for (IScorer scorer : scorerList) {
            scorer.addSentence(sentence);
        }
    }

    @Override
    public void removeAllSentences() {
        for (IScorer scorer : scorerList) {
            scorer.removeAllSentences();
        }
    }

    @Override
    public List<String> recommend(String key, int size) {
        List<String> resultList = new ArrayList<String>(size);
        TreeMap<String, Double> scoreMap = new TreeMap<String, Double>();
        for (BaseScorer scorer : scorerList) {
            Map<String, Double> map = scorer.computeScore(key);
            Double max = max(map);// 正规化一个map
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                Double score = scoreMap.get(entry.getKey());
                if (score == null) score = 0.0;
                scoreMap.put(entry.getKey(), score / max + entry.getValue() * scorer.boost);
            }
        }
        for (Map.Entry<Double, Set<String>> entry : sortScoreMap(scoreMap).entrySet()) {
            for (String sentence : entry.getValue()) {
                if (resultList.size() >= size) return resultList;
                resultList.add(sentence);
            }
        }

        return resultList;
    }

    public Map<String,Double> getRecommendScore(String key,int size) {
        Map<String,Double> result = new HashMap<String,Double>(size);
        TreeMap<String, Double> scoreMap = new TreeMap<String, Double>();
        for (BaseScorer scorer : scorerList) {
            Map<String, Double> map = scorer.computeScore(key);
            Double max = max(map);// 正规化一个map
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                Double score = scoreMap.get(entry.getKey());
                if (score == null) score = 0.0;
                scoreMap.put(entry.getKey(), score / max + entry.getValue() * scorer.boost);
            }
        }
        for (Map.Entry<Double, Set<String>> entry : sortScoreMap(scoreMap).entrySet()) {
            for (String sentence : entry.getValue()) {
                if (result.size() >= size) return result;
                result.put(sentence,entry.getKey());
            }
        }

        return result;
    }

    /**
     * 将分数map排序折叠
     *
     * @param scoreMap
     * @return
     */
    private static TreeMap<Double, Set<String>> sortScoreMap(TreeMap<String, Double> scoreMap) {
        TreeMap<Double, Set<String>> result = new TreeMap<Double, Set<String>>(Collections.reverseOrder());
        for (Map.Entry<String, Double> entry : scoreMap.entrySet()) {
            Set<String> sentenceSet = result.get(entry.getValue());
            if (sentenceSet == null) {
                sentenceSet = new HashSet<String>();
                result.put(entry.getValue(), sentenceSet);
            }
            sentenceSet.add(entry.getKey());
        }

        return result;
    }

    /**
     * 从map的值中找出最大值，这个值是从0开始的
     *
     * @param map
     * @return
     */
    private static Double max(Map<String, Double> map) {
        Double maxmum = 0.0;
        for (Double v : map.values()) {
            maxmum = Math.max(maxmum, v);
        }

        return maxmum;
    }

}
