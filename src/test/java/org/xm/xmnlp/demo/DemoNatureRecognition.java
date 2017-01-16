package org.xm.xmnlp.demo;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;

/**
 * 词性标注，词性纠正
 * Created by xuming on 2016/7/27.
 */
public class DemoNatureRecognition {
    public static void main(String[] args) {
        String text = "教授正在教授自然语言处理课程；学生敬仰他，表示对他的敬仰";
        Segment segment = Xmnlp.newSegment();

        System.out.println("未标注：" + segment.seg(text));
        segment.enablePartOfSpeechTagging(true);
        System.out.println("标注后：" + segment.seg(text));
    }
}
