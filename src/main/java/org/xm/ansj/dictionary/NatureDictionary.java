package org.xm.ansj.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.nlpcn.commons.lang.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.domain.Nature;
import org.xm.ansj.util.AppStaticValue;

/**
 * @author xuming
 */
public class NatureDictionary {
    public static final Logger LOGGER = LoggerFactory.getLogger(NatureDictionary.class);
    private static final int YI = 1;
    private static final int FYI = -1;
    private static final HashMap<String, Nature> NATURE_MAP = new HashMap<String, Nature>();
    private static int[][] NATURE_TABLE = null;

    static {
        init();
    }

    private static void init() {
        String split = "\t";
        int maxLength = 0;
        String temp;
        String[] strs;
        try {
            BufferedReader reader = AppStaticValue.getNatureMapReader();
            int p0;
            int p1;
            int p2;
            while ((temp = reader.readLine()) != null) {
                strs = temp.split(split);
                if (strs.length != 4)
                    continue;

                p0 = Integer.parseInt(strs[0]);
                p1 = Integer.parseInt(strs[1]);
                p2 = Integer.parseInt(strs[3]);
                NATURE_MAP.put(strs[2], new Nature(strs[2], p0, p1, p2));
                maxLength = Math.max(maxLength, p1);
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.warn("词性列表加载失败!", e);
        }
        // 加载词性关系
        try {
            BufferedReader reader = AppStaticValue.getNatureTableReader();
            NATURE_TABLE = new int[maxLength + 1][maxLength + 1];
            int j = 0;
            while ((temp = reader.readLine()) != null) {
                if (StringUtil.isBlank(temp))
                    continue;
                strs = temp.split(split);
                for (int i = 0; i < strs.length; i++) {
                    NATURE_TABLE[j][i] = Integer.parseInt(strs[i]);
                }
                j++;
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.warn("加载词性关系失败!", e);
        }
    }

    public static Nature getNature(String natureStr) {
        Nature nature = NATURE_MAP.get(natureStr);
        if (nature == null) {
            nature = new Nature(natureStr, FYI, FYI, YI);
            NATURE_MAP.put(natureStr, nature);
            return nature;
        }
        return nature;
    }

    public static int getTwoNatureFreq(Nature from, Nature to) {
        if (from.index < 0 || to.index < 0) {
            return 0;
        }
        return NATURE_TABLE[from.index][to.index];
    }
}
