package org.xm.xmnlp.demo;


import java.util.Map;

import org.xm.xmnlp.recommend.Recommend;

/**
 * 文本推荐(句子级别，从一系列句子中挑出与输入句子最相似的那一个)
 */
public class DemoRecommend {
    public static void main(String[] args) {
        Recommend rec = new Recommend();
        String[] titleArray =
                (
                        "威廉王子发表演说 呼吁保护野生动物\n" +
                                "魅惑天后许佳慧不爱“预谋” 独唱《许某某》\n" +
                                "《时代》年度人物最终入围名单出炉 普京马云入选\n" +
                                "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
                                "日本保密法将正式生效 日媒指其损害国民知情权\n" +
                                "英报告说空气污染带来“公共健康危机”"
                ).split("\\n");
        for (String title : titleArray) {
            rec.addSentence(title);
        }

        System.out.println(rec.recommend("陈述", 1));       // 语义
        System.out.println(rec.recommend("危机公关", 1));   // 字符
        System.out.println(rec.recommend("mayun", 1));      // 拼音
        System.out.println(rec.recommend("徐家汇", 1));     // 拼音
        System.out.println(rec.recommend("殿下", 1));       // 语义

        Map<String, Double> map = rec.getRecommendScore("chenshu", 2);// 拼音
        map.forEach((i, j) -> System.out.println(i + j));
    }
}
