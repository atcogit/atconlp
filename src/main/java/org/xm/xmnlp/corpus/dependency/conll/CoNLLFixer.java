package org.xm.xmnlp.corpus.dependency.conll;


import org.xm.xmnlp.util.IOUtil;

/**
 * 修正一些非10行的依存语料
 */
public class CoNLLFixer {
    public static boolean fix(String path) {
        StringBuilder sbOut = new StringBuilder();
        for (String line : IOUtil.readLineListWithLessMemory(path)) {
            if (line.trim().length() == 0) {
                sbOut.append(line);
                sbOut.append('\n');
                continue;
            }
            String[] args = line.split("\t");
            for (int i = 10 - args.length; i > 0; --i) {
                line += "\t_";
            }
            sbOut.append(line);
            sbOut.append('\n');
        }
        return IOUtil.saveTxt(path + ".fixed.txt", sbOut.toString());
    }

    public static void main(String[] args) {
        CoNLLFixer.fix("D:\\Doc\\语料库\\依存分析训练数据\\THU\\test.conll");
    }
}
