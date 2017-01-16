package org.xm.ansj.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.domain.Term;
import org.xm.ansj.util.AppStaticValue;

/**
 * @author xuming
 */
public class NgramDictionary {
    public static final Logger LOGGER = LoggerFactory.getLogger(NgramDictionary.class);

    static {
        long start = System.currentTimeMillis();
        AppStaticValue.initBigramTable();
        LOGGER.info("init ngram ok,use time:{}", System.currentTimeMillis() - start);
    }

    public static int getTwoWordFreq(Term from, Term to) {
        if (from.getItem().bigramEntryMap == null) {
            return 0;
        }
        Integer freq = from.getItem().bigramEntryMap.get(to.getItem().getIndex());
        if (freq == null) {
            return 0;
        }
        return freq;
    }
}
