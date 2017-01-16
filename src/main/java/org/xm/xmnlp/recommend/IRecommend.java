package org.xm.xmnlp.recommend;

import java.util.List;

/**
 * 推荐器接口
 * Created by xuming on 2016/7/28.
 */
public interface IRecommend {
    void addSentence(String sentence);

    /**
     * 清空
     */
    void removeAllSentences();

    /**
     * 根据一个输入的句子推荐相似的句子
     *
     * @param key
     * @param size
     * @return
     */
    List<String> recommend(String key,int size);
}
