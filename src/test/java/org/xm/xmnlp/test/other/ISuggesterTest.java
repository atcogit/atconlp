package org.xm.xmnlp.test.other;


import org.xm.xmnlp.recommend.IRecommend;
import org.xm.xmnlp.recommend.Recommend;

import junit.framework.TestCase;

public class ISuggesterTest extends TestCase {

    public void testRemoveAllSentences() throws Exception {
        IRecommend recommend = new Recommend();
        String[] titleArray =
                (
                        "威廉王子发表演说 呼吁保护野生动物\n" +
                                "《时代》年度人物最终入围名单出炉 普京马云入选\n" +
                                "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
                                "日本保密法将正式生效 日媒指其损害国民知情权\n" +
                                "英报告说空气污染带来“公共健康危机”"
                ).split("\\n");
        for (String title : titleArray) {
            recommend.addSentence(title);
        }

        assertEquals(true, recommend.recommend("mayun", 1).size() > 0);

        recommend.removeAllSentences();

        assertEquals(0, recommend.recommend("mayun", 1).size());
    }
}