package org.xm.xmnlp.test.corpus;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.CoreDictionary;
import org.xm.xmnlp.util.IOUtil;

import junit.framework.TestCase;

/**
 * 部分标注有问题，比如逗号缺少标注等等，尝试修复它
 */
public class AdjustCorpus extends TestCase {

    private static void handle(File file) {
        try {
            String text = IOUtil.readTxt(file.getPath());
            int length = text.length();
            text = addW(text, "：");
            text = addW(text, "？");
            text = addW(text, "，");
            text = addW(text, "）");
            text = addW(text, "（");
            text = addW(text, "！");
            text = addW(text, "(");
            text = addW(text, ")");
            text = addW(text, ",");
            text = addW(text, "‘");
            text = addW(text, "’");
            text = addW(text, "“");
            text = addW(text, "”");
            text = addW(text, ";");
            text = addW(text, "……");
            text = addW(text, "。");
            text = addW(text, "、");
            text = addW(text, "《");
            text = addW(text, "》");
            if (text.length() != length) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                bw.write(text);
                bw.close();
                System.out.println("修正了" + file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String addW(String text, String c) {
        text = text.replaceAll("\\" + c + "/w ", c);
        return text.replaceAll("\\" + c, c + "/w ");
    }


    public void testAdjustNGram() throws Exception {
        IOUtil.LineIterator iterator = new IOUtil.LineIterator(Xmnlp.Config.BiGramDictionaryPath);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Xmnlp.Config.BiGramDictionaryPath + "adjust.txt"), "UTF-8"));
        while (iterator.hasNext()) {
            String line = iterator.next();
            String[] params = line.split(" ");
            String first = params[0].split("@", 2)[0];
            String second = params[0].split("@", 2)[1];
//            if (params.length != 2)
//                System.err.println(line);
            CoreDictionary.Attribute attribute = CoreDictionary.get(first + second);
            if (attribute != null && (first.length() == 1 || second.length() == 1)) {
                System.out.println(line);
                continue;
            }
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }
}
