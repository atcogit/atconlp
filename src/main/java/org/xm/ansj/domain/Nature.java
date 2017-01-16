package org.xm.ansj.domain;

import java.io.Serializable;

import org.xm.ansj.dictionary.NatureDictionary;

/**
 * @author xuming
 */
public class Nature implements Serializable {
    private static final long serialVersionUID = 4123955217922643802L;
    public final String natureStr;
    public final int index;
    public final int natureIndex;
    public final int allFrequency;
    public static final Nature NW = NatureDictionary.getNature("nw");
    public static final Nature NRF = NatureDictionary.getNature("nrf");
    public static final Nature NR = NatureDictionary.getNature("nr");
    public static final Nature NULL = NatureDictionary.getNature("null");

    public Nature(String natureStr, int index, int natureIndex, int allFrequency) {
        this.natureStr = natureStr;
        this.index = index;
        this.natureIndex = natureIndex;
        this.allFrequency = allFrequency;
    }

    public Nature(String natureStr) {
        this.natureStr = natureStr;
        this.index = 0;
        this.natureIndex = 0;
        this.allFrequency = 0;
    }

    public String toString() {
        return natureStr + ":" + index + ":" + natureIndex;
    }
}
