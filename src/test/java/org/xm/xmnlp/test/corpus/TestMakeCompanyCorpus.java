package org.xm.xmnlp.test.corpus;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dependency.conll.CorpusLoader;
import org.xm.xmnlp.corpus.dictionary.maker.DictionaryMaker;
import org.xm.xmnlp.corpus.dictionary.maker.EasyDictionary;
import org.xm.xmnlp.corpus.dictionary.maker.NTDictionaryMaker;
import org.xm.xmnlp.corpus.document.Document;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.seg.DijkstraSegment;
import org.xm.xmnlp.seg.domain.Term;

import junit.framework.TestCase;

public class TestMakeCompanyCorpus extends TestCase {
    public void testMake() throws Exception {
        DijkstraSegment segment = new DijkstraSegment();
        String line = null;
        BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\Doc\\语料库\\company.dic")));
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/test/nt/company.txt")));
        int limit = Integer.MAX_VALUE;
        while ((line = bw.readLine()) != null && limit-- > 0) {
            if (line.endsWith("）")) continue;
            if (line.length() < 4) continue;
            if (line.contains("个体") || line.contains("个人")) {
                continue;
            }
            List<Term> termList = segment.seg(line);
            if (termList.size() == 0) continue;
            Term last = termList.get(termList.size() - 1);
            last.nature = Nature.nis;
            br.write("[");
            for (Term term : termList) {
                br.write(term.toString());
                if (term != last) br.write(" ");
            }
            br.write("]/ntc");
            br.newLine();
            br.flush();
        }
        bw.close();
        br.close();
    }

    public void testParse() throws Exception {
        EasyDictionary dictionary = EasyDictionary.create("data/dictionary/2014_dictionary.txt");
        final NTDictionaryMaker nsDictionaryMaker = new NTDictionaryMaker(dictionary);
        // CorpusLoader.walk("D:\\JavaProjects\\CorpusToolBox\\data\\2014\\", new CorpusLoader.Handler()
        CorpusLoader.walk("data/test/nt/part/", new CorpusLoader.Handler() {
            @Override
            public void handle(Document document) {
                nsDictionaryMaker.compute(document.getComplexSentenceList());
            }
        });
        nsDictionaryMaker.saveTxtTo("D:\\JavaProjects\\HanLP\\data\\dictionary\\organization\\outerNT");
    }

    public void testSplitLargeFile() throws Exception {
        String line = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/test/nt/company.txt")));
        int id = 1;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/test/nt/part/" + id + ".txt")));
        int count = 1;
        while ((line = br.readLine()) != null) {
            if (count == 1000) {
                bw.close();
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/test/nt/part/" + id + ".txt")));
                ++id;
                count = 0;
            }
            bw.write(line);
            bw.newLine();
            ++count;
        }
        br.close();
    }

    public void testCase() throws Exception {
        Xmnlp.Config.enableDebug();
        DijkstraSegment segment = new DijkstraSegment();
        segment.enableOrganizationRecognize(true);
        System.out.println(segment.seg("黑龙江建筑职业技术学院近百学生发生冲突"));
    }

    public void testCombine() throws Exception {
        DictionaryMaker.combine("data/dictionary/organization/nt.txt", "data/dictionary/organization/outerNT.txt")
                .saveTxtTo("data/dictionary/organization/nt.txt");
    }
}
