package org.xm.xmnlp.ruletest;

import java.util.Locale;
import java.util.Vector;

import org.junit.Test;
import org.xm.xmnlp.segword.Rule;

import junit.framework.TestCase;

/**
 * 规则机械分词歧义测试
 * Created by xming
 */
public class AmbiguityTest extends TestCase {
    protected Rule segmenter = new Rule();

    /**
     * 测试效果表明：差
     */
    @Test
    public void testCrossAmbiguity() {
        // 测试交叉歧义
        String sentence = "我对他有意见，总统有意见他。";
        Vector<String> forwardMaxSeg = segmenter.forwardMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "forwardMaxSeg:"+forwardMaxSeg.toString()));

        Vector<String> reverseMaxSeg = segmenter.reverseMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "reverseMaxSeg:"+reverseMaxSeg.toString()));

        Vector<String> biMaxSeg = segmenter.biMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "biMaxSeg:"+biMaxSeg.toString()));

        Vector<String> forwardMinSeg = segmenter.forwardMinSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "forwardMinSeg:"+forwardMinSeg.toString()));

        Vector<String> reverseMinSeg = segmenter.reverseMinSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "reverseMinSeg:"+reverseMinSeg.toString()));
    }

    /**
     * 测试效果表明：差
     */
    @Test
    public void testCombinationalAmbiguity() {
        //测试组合歧义
        String sentence = "我将来大学，我将来上海。";
        Vector<String> forwardMaxSeg = segmenter.forwardMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "forwardMaxSeg:"+forwardMaxSeg.toString()));

        Vector<String> reverseMaxSeg = segmenter.reverseMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "reverseMaxSeg:"+reverseMaxSeg.toString()));

        Vector<String> biMaxSeg = segmenter.biMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "biMaxSeg:"+biMaxSeg.toString()));

        Vector<String> forwardMinSeg = segmenter.forwardMinSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "forwardMinSeg:"+forwardMinSeg.toString()));

        Vector<String> reverseMinSeg = segmenter.reverseMinSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "reverseMinSeg:"+reverseMinSeg.toString()));

    }

    /**
     * 测试效果表明：差
     */
    @Test
    public void testMaxedAmbiguity() {
        // 测试混合歧义
        String sentence = "原子结合成分子时，他从马上下来，他从爱马上下来了。结婚的和尚未结婚的,独立自主和平等互利的原则。";
        Vector<String> forwardMaxSeg = segmenter.forwardMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "forwardMaxSeg:"+forwardMaxSeg.toString()));

        Vector<String> reverseMaxSeg = segmenter.reverseMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "reverseMaxSeg:"+reverseMaxSeg.toString()));

        Vector<String> biMaxSeg = segmenter.biMaxSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "biMaxSeg:"+biMaxSeg.toString()));

        Vector<String> forwardMinSeg = segmenter.forwardMinSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "forwardMinSeg:"+forwardMinSeg.toString()));

        Vector<String> reverseMinSeg = segmenter.reverseMinSeg(sentence);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, "reverseMinSeg:"+reverseMinSeg.toString()));
    }
}
