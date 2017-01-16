package org.xm.xmnlp.demo;


import java.util.List;

import org.xm.xmnlp.Xmnlp;

/**
 * 自动摘要
 */
public class DemoSummary {
    public static void main(String[] args) {
        String document = "水利部水资源司司长陈明忠9月29日在国务院新闻办举行的新闻发布会上透露，" +
                "根据刚刚完成了水资源管理制度的考核，有部分省接近了红线的指标，" +
                "有部分省超过红线的指标。对一些超过红线的地方，陈明忠表示，对一些取用水项目进行区域的限批，" +
                "严格地进行水资源论证和取水许可的批准。";
        // bug：排序有问题，需要加入结果排序 ,已经处理 xuming 20160727
        List<String> sentenceList = Xmnlp.extractSummary(document, 3);
        System.out.println(sentenceList);
    }
}
