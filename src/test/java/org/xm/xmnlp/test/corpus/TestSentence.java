package org.xm.xmnlp.test.corpus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xm.xmnlp.corpus.document.Sentence;
import org.xm.xmnlp.corpus.document.word.WordFactory;

import junit.framework.TestCase;

public class TestSentence extends TestCase {
    public void testCreate() throws Exception {
        String text = "人民网/nz 1月1日/t 讯/ng 据/p 《/w [纽约/nsf 时报/n]/nz 》/w 报道/v ，/w";
        Pattern pattern = Pattern.compile("(\\[(.+/[a-z]+)]/[a-z]+)|([^\\s]+/[a-z]+)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String param = matcher.group();
            assertEquals(param, WordFactory.create(param).toString());
        }
        assertEquals(text, Sentence.create(text).toString());
    }
}
