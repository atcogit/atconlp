package org.xm.xmnlp.demo;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 机构名识别
 */
public class DemoOrganizationRecognition {
    public static void main(String[] args) {
        String[] testCase = new String[]{
                "我在上海辉煌有限公司兼职工作，",
                "我经常在台川喜宴餐厅吃饭，",
                "偶尔去开元地中海影城看电影。",
        };
        System.out.println("未开启机构名识别的效果："+Xmnlp.segment("我在上海辉煌有限公司兼职工作，我经常在台川喜宴餐厅吃饭，"));

        Segment segment = Xmnlp.newSegment().enableOrganizationRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }

        List<String> result = new ArrayList<>();
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            result.addAll(termList.stream().filter(i -> i.getNature().startsWith("nt")).map(i -> i.word).collect(Collectors.toList()));
            result.forEach(System.out::println);
        }

        String str = "我在上海辉煌有限公司兼职工作";
        System.out.println(Xmnlp.extractOrganization(str));
    }
}
