package org.xm.xmnlp.test.corpus;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.DijkstraSegment;

import junit.framework.TestCase;

public class TestNSRecognition extends TestCase {
    public void testSeg() throws Exception {
        Xmnlp.Config.enableDebug();
        DijkstraSegment segment = new DijkstraSegment();
        segment.enableJapaneseNameRecognize(false);
        segment.enableTranslatedNameRecognize(false);
        segment.enableNameRecognize(false);
        segment.enableCustomDictionary(false);

        segment.enablePlaceRecognize(true);
        System.out.println(segment.seg("南翔向宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机"));
    }
}
