package org.xm.xmnlp.test.corpus;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.xm.xmnlp.corpus.dependency.conll.CorpusLoader;
import org.xm.xmnlp.corpus.document.Document;
import org.xm.xmnlp.corpus.document.word.Word;
import org.xm.xmnlp.util.IOUtil;

import junit.framework.TestCase;

/**
 * 玩玩ICWB的数据
 *
 * @author hankcs
 */
public class TestICWB extends TestCase {

    public static final String PATH = "D:\\Doc\\语料库\\icwb2-data\\training\\msr_training.utf8";

    public void testGenerateBMES() throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATH + ".bmes.txt")));
        for (String line : IOUtil.readLineListWithLessMemory(PATH)) {
            String[] wordArray = line.split("\\s");
            for (String word : wordArray) {
                if (word.length() == 1) {
                    bw.write(word + "\tS\n");
                } else if (word.length() > 1) {
                    bw.write(word.charAt(0) + "\tB\n");
                    for (int i = 1; i < word.length() - 1; ++i) {
                        bw.write(word.charAt(i) + "\tM\n");
                    }
                    bw.write(word.charAt(word.length() - 1) + "\tE\n");
                }
            }
            bw.newLine();
        }
        bw.close();
    }

    public void testDumpPeople2014ToBEMS() throws Exception {
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\Tools\\CRF++-0.58\\example\\seg_cn\\2014.txt")));
        CorpusLoader.walk("D:\\JavaProjects\\CorpusToolBox\\data\\2014", new CorpusLoader.Handler() {
            @Override
            public void handle(Document document) {
                List<List<Word>> simpleSentenceList = document.getSimpleSentenceList();
                for (List<Word> wordList : simpleSentenceList) {
                    try {
                        for (Word word : wordList) {

                            bw.write(word.value);
                            bw.write(' ');

                        }
                        bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bw.close();
    }
}
