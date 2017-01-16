package org.xm.xmnlp.test.seg;


import org.xm.xmnlp.corpus.tag.NR;
import org.xm.xmnlp.dictionary.TransformMatrixDictionary;

import junit.framework.TestCase;

/**
 * 转换矩阵词典
 */
public class TestTRMDictionary extends TestCase {
    public void testLoad() throws Exception {
        TransformMatrixDictionary<NR> nrTransformMatrixDictionary = new TransformMatrixDictionary<NR>(NR.class);
        nrTransformMatrixDictionary.load("data/dictionary/person/nr.tr.txt");
        System.out.println(nrTransformMatrixDictionary.getFrequency(NR.A, NR.A));
        System.out.println(nrTransformMatrixDictionary.getFrequency("A", "A"));
        System.out.println(nrTransformMatrixDictionary.getTotalFrequency());
        System.out.println(nrTransformMatrixDictionary.getTotalFrequency(NR.Z));
        System.out.println(nrTransformMatrixDictionary.getTotalFrequency(NR.A));
        // bug：统计总词频是直接赋值的，需要改为动态变更
    }
}
