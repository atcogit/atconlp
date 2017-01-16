package org.xm.ansj.domain;

import java.io.Serializable;

/**
 * @author xuming
 */
public class PersonNatureAttr implements Serializable {
    public static final PersonNatureAttr NULL = new PersonNatureAttr();
    private int[][] locFreq = null;
    public int split;
    public int begin;// 12
    public int end;// 11+12
    public int allFreq;
    public boolean flag;

    public void addFreq(int index, int freq) {
        switch (index) {
            case 11:
                this.end += freq;
                allFreq += freq;
                break;
            case 12:
                this.end += freq;
                this.begin += freq;
                allFreq += freq;
                break;
            case 44:
                this.split += freq;
                allFreq += freq;
                break;
        }
    }

    public int getFreq(int length, int loc) {
        if (locFreq == null) {
            return 0;
        }
        if (length > 3) length = 3;
        if (loc > 4) loc = 4;
        return locFreq[length][loc];
    }

    public void setLocFreq(int[][] ints) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i][0] > 0) {
                flag = true;
                break;
            }
        }
        locFreq = ints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("begin=" + begin);
        sb.append(",");
        sb.append("end=" + end);
        sb.append(",");
        sb.append("split=" + split);
        return sb.toString();
    }
}
