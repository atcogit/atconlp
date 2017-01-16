package org.xm.ansj.segmentation;

/**
 * @author xuming
 */
public interface IWord {
    String allWords();
    void setStr(String str);
    void setChars(char[] chars,int start,int end);
    int getOffe();
}
