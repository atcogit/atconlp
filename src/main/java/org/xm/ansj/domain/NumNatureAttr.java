package org.xm.ansj.domain;

import java.io.Serializable;

/**
 * @author xuming
 */
public class NumNatureAttr implements Serializable {
    public static final NumNatureAttr NULL = new NumNatureAttr();
    public int numFreq = -1;
    public int numEndFreq = -1;
    public boolean flag = false;
}
