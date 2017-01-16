package org.xm.xmnlp.ruletest;


import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Vector;

import org.junit.Test;
import org.xm.xmnlp.segword.Rule;

import junit.framework.TestCase;


public class RuleSegTest extends TestCase {
    protected Rule segmenter = new Rule();
    String[] sentences =
            new String[]{
                    "北京天安门广场，人民币种种族主义静态结果",
                    "找美女",
                    "干脆就把那部蒙人的闲法给废了拉倒！" +
                    "RT @la : 27日，全国人大常委会第三次审议侵权责任法"};

    @Test
    public void testForwardMaxSeg() {
        for (String sentence : sentences) {
            Vector<String> result = segmenter.forwardMaxSeg(sentence);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, result.toString()));
        }
    }


    @Test
    public void testReverseMaxSeg() {
        for (String sentence : sentences) {
            Vector<String> result = segmenter.reverseMaxSeg(sentence);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, result.toString()));
        }
    }


    @Test
    public void testBiMaxSeg() {
        for (String sentence : sentences) {
            Vector<String> result = segmenter.biMaxSeg(sentence);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, result.toString()));
        }
    }


    @Test
    public void testSegmentSpeed() {
        long length = 0L;
        long wordCount = 0L;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; ++i)
            for (String sentence : sentences) {
                segmenter.biMaxSeg(sentence);
                length += sentence.getBytes(Charset.forName("UTF-8")).length;
                wordCount += sentence.length();
            }
        long elapsed = (System.currentTimeMillis() - start);
        System.out.println(String.format(Locale.getDefault(), "time elapsed:%d ms, rate:%fkb/s, sentences:%.2f/s", elapsed,
                (length * 1.0) / 1024.0f / (elapsed * 1.0 / 1000.0f), wordCount * 1000.0f / (elapsed * 1.0)));
    }

}
