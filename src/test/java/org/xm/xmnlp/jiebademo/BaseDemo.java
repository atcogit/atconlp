package org.xm.xmnlp.jiebademo;


import org.xm.xmnlp.segword.SegMode;
import org.xm.xmnlp.segword.Segmenter;

/**
 * 结巴分词器使用示例，重写jieba-analysis（java版）源码
 * Created by xuming on 2016/5/26.
 */
public class BaseDemo {
    public static void main(String[] args) {

        Segmenter segmenter = new Segmenter();
        String[] sentences =
                new String[]{"结婚的和尚未结婚的.蝶舞打扮得漂漂亮亮出现在张公公面前." +
                        "物理学起来费劲，长春市长春节讲话，我们都爱小清新状态。" +
                        "不輸入簡體字,阿丁说你很好，黎明认识这个李明不輸入簡體字典,矿泉水瓶盖子下面有东西",
                        "我不喜欢日本和服。",
                        "雷猴回归人间。",
                        "李明不輸入簡體字",
                        "我不喜欢日本和服。",
                        "东海县迅捷贸易有限责任公司"};
        System.out.println("----------------------SEARCH：");
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, SegMode.SEARCH).toString());
        }
        System.out.println("----------------------INDEX：");
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, SegMode.INDEX).toString());
        }
    }


}
