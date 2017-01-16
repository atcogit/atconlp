package org.xm.xmnlp.test.corpus;


import java.io.File;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.stopword.CoreStopWordDictionary;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.IOUtil;

import junit.framework.TestCase;

public class TestLDA extends TestCase {
    public void testSegmentCorpus() throws Exception {
        File root = new File("D:\\Doc\\语料库\\搜狗文本分类语料库精简版");
        for (File folder : root.listFiles()) {
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    System.out.println(file.getAbsolutePath());
                    List<Term> termList = Xmnlp.segment(IOUtil.readTxt(file.getAbsolutePath()));
                    StringBuilder sbOut = new StringBuilder();
                    for (Term term : termList) {
                        if (CoreStopWordDictionary.shouldInclude(term)) {
                            sbOut.append(term.word).append(" ");
                        }
                    }
                    IOUtil.saveTxt("D:\\Doc\\语料库\\segmented\\" + folder.getName() + "_" + file.getName(), sbOut.toString());
                }
            }
        }
    }
}
