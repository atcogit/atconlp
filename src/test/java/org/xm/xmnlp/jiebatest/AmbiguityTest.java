package org.xm.xmnlp.jiebatest;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.xm.xmnlp.dic.JItem;
import org.xm.xmnlp.segword.SegMode;
import org.xm.xmnlp.segword.Segmenter;

import junit.framework.TestCase;

/**
 * 结巴分词歧义测试
 * Created by xming
 */
public class AmbiguityTest extends TestCase {
    protected Segmenter segmenter = new Segmenter();

    /**
     * 测试效果表明：差
     */
    @Test
    public void testCrossAmbiguity() {
        // 测试交叉歧义
        String sentence = "我对他有意见，总统有意见他。";
        List<JItem> tokens = segmenter.process(sentence, SegMode.SEARCH);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));

    }

    /**
     * 测试效果表明：差
     */
    @Test
    public void testCombinationalAmbiguity() {
        //测试组合歧义
        String sentence = "我将来大学，我将来上海。";
        List<JItem> tokens = segmenter.process(sentence, SegMode.SEARCH);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));

    }

    /**
     * 测试效果表明：差
     */
    @Test
    public void testMaxedAmbiguity() {
        // 测试混合歧义
        String sentence = "原子结合成分子时，他从马上下来，他从爱马上下来了。";
        List<JItem> tokens = segmenter.process(sentence, SegMode.SEARCH);
        System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
    }
}
