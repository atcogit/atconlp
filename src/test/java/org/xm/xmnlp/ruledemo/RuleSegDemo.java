package org.xm.xmnlp.ruledemo;

import java.util.Locale;
import java.util.Vector;

import org.xm.xmnlp.segword.Rule;

/**
 * 规则分词器：向前最大匹配法，向后最大匹配法，双向最大匹配法
 * Created by xuming on 2016/7/25.
 */
public class RuleSegDemo {
    public static void main(String[] args){
        System.out.println(Rule.forwardMaxSeg("北京天安门广场人民币种种族主义静态结果").toString());
        RuleSegDemo rule = new RuleSegDemo();
        rule.testRuleSeg();
    }

    public void testRuleSeg() {
        String[] bugs =
                new String[]{
                        "北京天安门广场人民币种",
                        "干脆就把那部蒙人的闲法给废了拉倒！在医患纠纷中本已处于弱势地位的消费者由此将陷入万劫不复的境地。 "};
        for (String sentence : bugs) {
            Vector<String> f = Rule.forwardMaxSeg(sentence);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, f.toString()));
        }
    }
}
