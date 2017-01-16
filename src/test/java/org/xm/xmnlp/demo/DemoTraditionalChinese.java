package org.xm.xmnlp.demo;

import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.TraditionalChineseTokenizer;

/**
 * 繁体中文
 * Created by xuming on 2016/7/29.
 */
public class DemoTraditionalChinese {
    public static void main(String[] args) {

        String sentence = "大衛貝克漢不僅僅是名著名球員，球場以外，其妻為,等方面都具很大的影響力，在足球圈外所獲得,黑牛沟村捐赠了挖掘机";
        System.out.println("未开启繁体中文识别的效果：" + Xmnlp.segment("大衛貝克漢不僅僅是名著名球員,黑牛沟村捐赠了挖掘机，"));

        List<Term> termList = TraditionalChineseTokenizer.segment(sentence);
        System.out.println(termList);

        // 简繁转换
        System.out.println(Xmnlp.convertToTraditionalChinese("“以后等你当上皇后，就能买草莓庆祝了”。以後发现一根白头发"));
        System.out.println(Xmnlp.convertToSimplifiedChinese("用筆記簿型電腦寫程式HelloWorld挖掘机"));

    }
}
