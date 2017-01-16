package org.xm.xmnlp.test.seg;


import org.xm.xmnlp.util.SentencesUtil;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestSentenceSplit extends TestCase {
    public void testSplitSentence() throws Exception {
        String content = "我白天是一名语言学习者，晚上是一名初级码农。空的时候喜欢看算法和应用数学书，也喜欢悬疑推理小说，ACG方面喜欢型月、轨迹。喜欢有思想深度的事物，讨厌急躁、拜金与安逸的人\r\n目前在魔都某女校学习，这是我的个人博客。闻道有先后，术业有专攻，请多多关照。";
        System.out.println(SentencesUtil.toSentenceList(content));
    }
}
