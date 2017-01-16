package org.xm.xmnlp.demo;


import org.xm.xmnlp.seg.NShortSegment;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.ViterbiSegment;

/**
 * N最短路径分词，该分词器比最短路分词器慢，但是效果稍微好一些，对命名实体识别能力更强
 */
public class DemoNShortSegment {
    public static void main(String[] args) {
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new ViterbiSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        String[] testCase = new String[]{
                "今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
                "江西省监狱管理局与中国太平洋财产保险股份有限公司南昌中心支公司保险合同纠纷案",
                "新北商贸有限公司",
        };
        for (String sentence : testCase) {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
        }
    }
}
