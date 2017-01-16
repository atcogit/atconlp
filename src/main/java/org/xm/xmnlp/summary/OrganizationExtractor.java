package org.xm.xmnlp.summary;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;

/**
 * 机构名称提取器
 * Created by xuming
 */
public class OrganizationExtractor {
    public static List<String> getOrganizationList(String text) {
        List<String> result = new LinkedList<String>();
        Segment segment = Xmnlp.newSegment().enableOrganizationRecognize(true);
        List<Term> termList = segment.seg(text);
        result.addAll(termList.stream()
                .filter(i -> i.getNature().startsWith("nt"))
                .map(i -> i.word)
                .collect(Collectors.toList()));
        return result;
    }

}
