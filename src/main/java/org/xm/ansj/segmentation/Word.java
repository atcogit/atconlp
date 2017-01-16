package org.xm.ansj.segmentation;

import org.xm.ansj.dictionary.DatDictionary;
import org.xm.ansj.domain.AnsjItem;

/**
 * @author xuming
 */
public class Word implements IWord {
    public int offe;
    int charsLength = 0;
    public char[] chars;
    private int charIndex;
    private int start = 0;
    private int end = 0;
    private int baseValue = 0;
    private int checkValue = 0;
    private int tempBaseValue = 0;
    private int i = 0;
    private String str;

    /**
     * 构造方法，同时加载词典
     */
    public Word() {
    }
    public Word(String str) {
        setStr(str);
    }

    @Override
    public void setStr(String str) {
        setChars(str.toCharArray(), 0, str.length());
    }

    @Override
    public void setChars(char[] chars, int start, int end) {
        this.chars = chars;
        i = start;
        this.start = start;
        charsLength = end;
        checkValue = 0;
    }

    @Override
    public int getOffe() {
        return offe;
    }

    @Override
    public String allWords() {
        for (; i < charsLength; i++) {
            charIndex = chars[i];
            end++;
            switch (getStatement()) {
                case 0:
                    if (baseValue == chars[i]) {
                        str = String.valueOf(chars[i]);
                        offe = i;
                        start = ++i;
                        end = 0;
                        baseValue = 0;
                        tempBaseValue = baseValue;
                        return str;
                    } else {
                        start = i;
                        i--;
                        end = 0;
                        baseValue = 0;
                        break;
                    }
                case 2:
                    i++;
                    offe = start;
                    tempBaseValue = baseValue;
                    return DatDictionary.getItem(tempBaseValue).getName();
                case 3:
                    offe = start;
                    start++;
                    i = start;
                    end = 0;
                    tempBaseValue = baseValue;
                    baseValue = 0;
                    return DatDictionary.getItem(tempBaseValue).getName();
            }
        }
        if (start++ != i) {
            i = start;
            baseValue = 0;
            return allWords();
        }
        end = 0;
        baseValue = 0;
        i = 0;
        return null;
    }

    private int getStatement() {
        checkValue = baseValue;
        baseValue = DatDictionary.getItem(checkValue).getBase() + charIndex;
        if (baseValue < DatDictionary.arrayLength && (DatDictionary.getItem(baseValue).getCheck() == checkValue || DatDictionary.getItem(baseValue).getCheck() == -1)) {
            return DatDictionary.getItem(baseValue).getStatus();
        }
        return 0;
    }

    public AnsjItem getItem() {
        return DatDictionary.getItem(tempBaseValue);
    }
}
