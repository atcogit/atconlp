package org.xm.xmnlp.test.seg;


import java.io.File;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.io.FolderWalker;
import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.seg.DijkstraSegment;
import org.xm.xmnlp.seg.NShortSegment;
import org.xm.xmnlp.seg.Segment;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.IOUtil;
import org.xm.xmnlp.util.SentencesUtil;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestPersonRecognition extends TestCase {
    static final String FOLDER = "D:\\Doc\\语料库\\上海静安\\";

    public void testBatch() throws Exception {
        List<File> fileList = FolderWalker.open(FOLDER);
        int i = 0;
        for (File file : fileList) {
            System.out.println(++i + " / " + fileList.size() + " " + file.getName() + " ");
            String path = file.getAbsolutePath();
            String content = IOUtil.readTxt(path);
            DijkstraSegment segment = new DijkstraSegment();
            List<List<Term>> sentenceList = segment.seg2sentence(content);
            for (List<Term> sentence : sentenceList) {
                if (SentencesUtil.hasNature(sentence, Nature.nr)) {
                    System.out.println(sentence);
                }
            }
        }
    }

    public void testNameRecognition() throws Exception {
        Xmnlp.Config.enableDebug();
        NShortSegment segment = new NShortSegment();
        System.out.println(segment.seg("世界上最长的姓名是简森·乔伊·亚历山大·比基·卡利斯勒·达夫·埃利奥特·福克斯·伊维鲁莫·马尔尼·梅尔斯·帕特森·汤普森·华莱士·普雷斯顿。"));
    }

    public void testJPName() throws Exception {
        Xmnlp.Config.enableDebug();
        Segment segment = new DijkstraSegment().enableJapaneseNameRecognize(true);
        System.out.println(segment.seg("北川景子参演了林诣彬导演"));
    }

    public void testChineseNameRecognition() throws Exception {
        Xmnlp.Config.enableDebug();
        Segment segment = new DijkstraSegment();
        System.out.println(segment.seg("编剧邵钧林和稽道青说"));

    }
}
