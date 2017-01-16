package org.xm.xmnlp.test.seg;

import java.util.List;
import java.util.Scanner;

import org.xm.xmnlp.seg.NShortSegment;
import org.xm.xmnlp.seg.domain.Term;

import junit.framework.TestCase;

public class PlayGround extends TestCase {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;
        while ((line = scanner.nextLine()).length() > 0
                ) {
            seg(line);
        }
    }

    private static void seg(String sentence) {
        List<Term> terms = NShortSegment.parse(sentence);
        for (Term wr : terms) {
            System.out.print(wr.word + wr.nature);
        }
        System.out.println();
    }

    public void testCharSequence() throws Exception {
        CharSequence s = "hello";

    }
}
