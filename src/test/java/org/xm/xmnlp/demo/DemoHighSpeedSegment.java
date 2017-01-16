package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.tokenizer.SpeedTokenizer;

/**
 * 演示极速分词，
 * 基于DoubleArrayTrie实现的词典正向最长分词，适用于“高吞吐量”“精度一般”“无词性标注”的场合
 */
public class DemoHighSpeedSegment {
    public static void main(String[] args) {
        String text = "结婚的和尚未结婚的，一次性交易，我在湖北餐馆吃团圆饭。江西鄱阳湖干枯，中国最大淡水湖变成大草原这个研究生会五种语言";
        Xmnlp.Config.ShowTermNature = false;
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; ++i) {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("SpeedTokenizer分词速度：%.2f字每秒\n", text.length() * pressure / costTime);
    }
}
