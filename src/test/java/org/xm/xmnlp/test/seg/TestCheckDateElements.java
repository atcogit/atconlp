package org.xm.xmnlp.test.seg;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.seg.NShortSegment;
import org.xm.xmnlp.seg.domain.Term;

public class TestCheckDateElements {
    public static void main(String[] args) {
        List<List<Term>> wordResults = new LinkedList<List<Term>>();
        wordResults.add(NShortSegment.parse("3-4月"));
        wordResults.add(NShortSegment.parse("3-4月份"));
        wordResults.add(NShortSegment.parse("3-4季"));
        wordResults.add(NShortSegment.parse("3-4年"));
        wordResults.add(NShortSegment.parse("3-4人"));
        wordResults.add(NShortSegment.parse("2014年"));
        wordResults.add(NShortSegment.parse("04年"));
        wordResults.add(NShortSegment.parse("12点半"));
        wordResults.add(NShortSegment.parse("1.abc"));

        for (List<Term> result : wordResults) {
            System.out.println(result);
        }
    }
}
