package org.xm.xmnlp.corpus.util;

import java.util.List;

import org.xm.xmnlp.corpus.dependency.conll.PosTagCompiler;
import org.xm.xmnlp.corpus.document.word.IWord;
import org.xm.xmnlp.corpus.document.word.Word;

/**
 * 预编译与反编译一些词语
 */
public class Precompiler {
    /**
     * 将一个单词编译为等效字串
     *
     * @param word
     * @return
     */
    public static Word compile(Word word) {
        word.value = PosTagCompiler.compile(word.label, word.value);
//        switch (word.label)
//        {
//            case "ns":
//            case "nsf":
//            {
//                word.value = Predefine.TAG_PLACE;
//            }
//            break;
////            case "nz":
//            case "nx":
//            {
//                    word.value = Predefine.TAG_PROPER;
//            }
//            break;
//            case "nt":
//            case "ntc":
//            case "ntcf":
//            case "ntcb":
//            case "ntch":
//            case "nto":
//            case "ntu":
//            case "nts":
//            case "nth":
//            {
//                word.value = Predefine.TAG_GROUP;
//            }
//            break;
//            case "m":
//            case "mq":
//            {
//                    word.value = Predefine.TAG_NUMBER;
//            }
//            break;
//            case "x":
//            {
//                word.value = Predefine.TAG_CLUSTER;
//            }
//            break;
//            case "xx":
//            {
//                word.value = Predefine.TAG_OTHER;
//            }
//            break;
//            case "t":
//            {
//                    word.value = Predefine.TAG_TIME;
//            }
//            break;
//            case "nr":
//            case "nrf":
//            {
//                word.value = Predefine.TAG_PEOPLE;
//            }
//            break;
//        }
        return word;
    }

    public static Word compile(IWord word) {
        return compile((Word) word);
    }

    /**
     * 在忽略ns的前提下预编译
     *
     * @param wordList
     */
    public static void compileWithoutNS(List<IWord> wordList) {
        for (IWord word : wordList) {
            if (word.getLabel().startsWith("ns")) continue;
            word.setValue(PosTagCompiler.compile(word.getLabel(), word.getValue()));
//            switch (word.getLabel())
//            {
//                case "nx":
//                {
//                    word.setValue(Predefine.TAG_PROPER);
//                }
//                break;
//                case "nt":
//                case "ntc":
//                case "ntcf":
//                case "ntcb":
//                case "ntch":
//                case "nto":
//                case "ntu":
//                case "nts":
//                case "nth":
//                {
//                    word.setValue(Predefine.TAG_GROUP);
//                }
//                break;
//                case "m":
//                case "mq":
//                {
//                    word.setValue(Predefine.TAG_NUMBER);
//                }
//                break;
//                case "x":
//                {
//                    word.setValue(Predefine.TAG_CLUSTER);
//                }
//                break;
//                case "xx":
//                {
//                    word.setValue(Predefine.TAG_OTHER);
//                }
//                break;
//                case "t":
//                {
//                    word.setValue(Predefine.TAG_TIME);
//                }
//                break;
//                case "nr":
//                {
//                    word.setValue(Predefine.TAG_PEOPLE);
//                }
//                break;
//            }
        }
    }

    /**
     * 在忽略ns的前提下预编译
     *
     * @param wordList
     */
    public static void compileWithoutNT(List<IWord> wordList) {
        for (IWord word : wordList) {
            if (word.getLabel().startsWith("nt")) continue;
            word.setValue(PosTagCompiler.compile(word.getLabel(), word.getValue()));
        }
    }
}
