package org.xm.ansj.domain;


import java.io.Serializable;
import java.util.Map;

import org.nlpcn.commons.lang.dat.Item;

/**
 * @author xuming
 */
public class AnsjItem extends Item implements Serializable {
    public static final AnsjItem BEGIN = new AnsjItem();
    public static final AnsjItem END = new AnsjItem();
    public static final AnsjItem NULL = new AnsjItem();
    public String param;
    public TermNatures termNatures = null;
    public Map<Integer, Integer> bigramEntryMap = null;

    static {
        NULL.base = 0;
        BEGIN.index = 0;
        BEGIN.termNatures = TermNatures.BEGIN;
        END.index = -1;
        END.termNatures = TermNatures.END;
    }

    @Override
    public void init(String[] split) {
        this.name = split[0];
        this.param = split[1];
    }

    @Override
    public void initValue(String[] split) {
        index = Integer.parseInt(split[0]);
        base = Integer.parseInt(split[2]);
        check = Integer.parseInt(split[3]);
        status = Byte.parseByte(split[4]);
        if (status > 1) {
            name = split[1];
            termNatures = new TermNatures(TermNature.setNatureStrToArray(split[5]), index);
        } else {
            termNatures = new TermNatures(TermNature.NULL);
        }
    }

    @Override
    public String toText() {
        return index + "\t" + name + "\t" + base + "\t" + check + "\t" + status + "\t" + param;
    }
}
