package org.xm.xmnlp.demo;


import java.util.List;

import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.tokenizer.URLTokenizer;

/**
 * 演示URL识别
 */
public class DemoURLRecognition {
    public static void main(String[] args) {
        String text =
                "xmnlp的项目地址是https://github.com/shibing624/xmnlp，" +
                        "我有时候会在www.baidu.com上面发布一些消息，" +
                        "我的微博是http://weibo.com/baidu/，会同步推送baidu.com的新闻。" +
                        "听说.中国域名开放申请了,但我并没有申请baidu.中国,因为穷……";
        List<Term> termList = URLTokenizer.segment(text);
        System.out.println(termList);
        for (Term term : termList) {
            if (term.nature == Nature.xu)
                System.out.println(term.word);
        }
    }
}
