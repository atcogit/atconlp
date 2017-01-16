package org.xm.xmnlp.test.corpus;

import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.dictionary.StringDictionary;
import org.xm.xmnlp.dictionary.CoreBiGramTableDictionary;
import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.dictionary.CustomDictionary;
import org.xm.xmnlp.dictionary.person.JapanesePersonDictionary;
import org.xm.xmnlp.seg.DijkstraSegment;
import org.xm.xmnlp.util.IOUtil;
import org.xm.xmnlp.util.TextUtil;

import junit.framework.TestCase;

public class TestMakeJapaneseName extends TestCase {
    public void testCombine() throws Exception {
        String root = "D:\\JavaProjects\\SougouDownload\\data\\";
        String[] pathArray = new String[]{"日本名人大合集.txt", "日剧电影动漫和日本明星.txt", "日本女优.txt", "日本AV女优(A片)EXTEND版.txt", "日本女优大全.txt"};
        Set<String> wordSet = new TreeSet<String>();
        for (String path : pathArray) {
            path = root + path;
            for (String word : IOUtil.readLineList(path)) {
                word = word.replaceAll("[a-z\r\n]", "");
                if (CoreDictionary.contains(word) || CustomDictionary.contains(word)) continue;
                wordSet.add(word);
            }
        }

        TreeSet<String> firstNameSet = new TreeSet<String>();
        firstNameSet.addAll(IOUtil.readLineList("data/dictionary/person/日本姓氏.txt"));
        Iterator<String> iterator = wordSet.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name.length() > 6 || name.length() < 3 || (!firstNameSet.contains(name.substring(0, 1)) && !firstNameSet.contains(name.substring(0, 2)) && !firstNameSet.contains(name.substring(0, 3)))) {
                iterator.remove();
            }
        }

        IOUtil.saveCollectionToTxt(wordSet, "data/dictionary/person/日本人名.txt");
    }

    public void testMakeRoleDictionary() throws Exception {
        TreeSet<String> firstNameSet = new TreeSet<String>();
        firstNameSet.addAll(IOUtil.readLineList("data/dictionary/person/日本姓氏.txt"));
        TreeSet<String> fullNameSet = new TreeSet<String>();
        fullNameSet.addAll(IOUtil.readLineList("data/dictionary/person/日本人名.txt"));
        StringDictionary dictionary = new StringDictionary(" ");
        for (String fullName : fullNameSet) {
            for (int i = Math.min(3, fullName.length() - 1); i > 0; --i) {
                String firstName = fullName.substring(0, i);
                if (firstNameSet.contains(firstName)) {
                    dictionary.add(fullName.substring(i), "m");
                    break;
                }
            }
        }
        for (String firstName : firstNameSet) {
            dictionary.add(firstName, "x");
        }
        dictionary.save("data/dictionary/person/nrj.txt");
    }

    public void testRecognize() throws Exception {
        Xmnlp.Config.enableDebug();
        DijkstraSegment segment = new DijkstraSegment();
        System.out.println(segment.seg("我叫大杉亚依里"));
    }

    private String getLongestSuffix(String a, String b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length() && i < b.length(); ++i) {
            if (a.charAt(i) == b.charAt(i)) {
                sb.append(a.charAt(i));
            } else {
                return sb.toString();
            }
        }
        return sb.toString();
    }

    public void testImport() throws Exception {
        TreeSet<String> set = new TreeSet<String>();
        for (String name : IOUtil.readLineList("D:\\Doc\\语料库\\corpus-master\\日本姓氏.txt")) {
            name = Xmnlp.convertToSimplifiedChinese(Arrays.toString(name.toCharArray()));
            name = name.replaceAll("[\\[\\], ]", "");
            if (!TextUtil.isAllChinese(name)) continue;
            set.add(name);
        }
        IOUtil.saveCollectionToTxt(set, "data/dictionary/person/日本姓氏.txt");
    }

    public void testLoadJapanese() throws Exception {
        System.out.println(JapanesePersonDictionary.get("太郎"));
    }

    public void testSeg() throws Exception {
        Xmnlp.Config.enableDebug();
        DijkstraSegment segment = new DijkstraSegment();
        segment.enableJapaneseNameRecognize(true);
        System.out.println(segment.seg("林志玲亮相网友:确定不是波多野结衣？"));
    }

    public void testCountBadCase() throws Exception {
        BufferedWriter bw = IOUtil.newBufferedWriter(Xmnlp.Config.JapanesePersonDictionaryPath + ".badcase.txt");
        List<String> xList = new LinkedList<String>();
        List<String> mList = new LinkedList<String>();
        IOUtil.LineIterator iterator = new IOUtil.LineIterator(Xmnlp.Config.JapanesePersonDictionaryPath);
        while (iterator.hasNext()) {
            String line = iterator.next();
            String[] args = line.split("\\s");
            if ("x".equals(args[1])) xList.add(args[0]);
            else mList.add(args[0]);
        }
        for (String x : xList) {
            for (String m : mList) {
                if (CoreBiGramTableDictionary.getBiFrequency(x, m) > 0) {
                    bw.write(x + m + " A");
                    bw.newLine();
                }
            }
        }
        bw.close();
    }
}
