package org.xm.ansj.dictionary;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.nlpcn.commons.lang.dat.DoubleArrayTire;
import org.nlpcn.commons.lang.dat.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.dictionary.personname.PersonNameDictionary;
import org.xm.ansj.domain.AnsjItem;
import org.xm.ansj.domain.PersonNatureAttr;
import org.xm.ansj.domain.TermNature;
import org.xm.ansj.domain.TermNatures;
import org.xm.ansj.util.AppStaticValue;

/**
 * @author xuming
 */
public class DatDictionary {
    public static final Logger LOGGER = LoggerFactory.getLogger(DatDictionary.class);
    public static final char[] IN_SYSTEM = new char[65536];
    private static final DoubleArrayTire DAT = loadDAT();
    public static int arrayLength = DAT.arrayLength;

    private static DoubleArrayTire loadDAT() {
        long start = System.currentTimeMillis();
        try {
            DoubleArrayTire dat = DoubleArrayTire.loadText(AppStaticValue.getCoreInputStream(), AnsjItem.class);
            personNameFull(dat);
            for (Item item : dat.getDAT()) {
                if (item == null || item.getName() == null) {
                    continue;
                }
                if (item.getStatus() < 4) {
                    for (int i = 0; i < item.getName().length(); i++) {
                        IN_SYSTEM[item.getName().charAt(i)] = item.getName().charAt(i);
                    }
                }
                if (item.getStatus() < 2) {
                    item.setName(null);
                    continue;
                }
            }
            // 特殊字符标准化
            IN_SYSTEM['％'] = '%';
            LOGGER.info("init core dic ok,use time :{}", System.currentTimeMillis() - start);
            return dat;
        } catch (InstantiationException e) {
            LOGGER.warn("无法实例化", e);
        } catch (IllegalAccessException e) {
            LOGGER.warn("非法访问", e);
        } catch (NumberFormatException e) {
            LOGGER.warn("数字格式异常", e);
        } catch (IOException e) {
            LOGGER.warn("IO 异常" + e);
        }
        return null;
    }

    private static void personNameFull(DoubleArrayTire dat)
            throws NumberFormatException, IOException {
        HashMap<String, PersonNatureAttr> personMap = new PersonNameDictionary().getPersonMap();
        AnsjItem ansjItem;
        Set<Map.Entry<String, PersonNatureAttr>> entrySet = personMap.entrySet();
        char c = 0;
        String temp;
        for (Map.Entry<String, PersonNatureAttr> entry : entrySet) {
            temp = entry.getKey();
            if (temp.length() == 1 && (dat.getDAT()[temp.charAt(0)]) == null) {
                ansjItem = new AnsjItem();
                ansjItem.setBase(c);
                ansjItem.setCheck(-1);
                ansjItem.setStatus((byte) 3);
                ansjItem.setName(temp);
                dat.getDAT()[temp.charAt(0)] = ansjItem;
            } else {
                ansjItem = dat.getItem(temp);
            }

            if (ansjItem == null) {
                continue;
            }
            if ((ansjItem.termNatures) == null) {
                if (temp.length() == 1 && temp.charAt(0) < 256) {
                    ansjItem.termNatures = TermNatures.NULL;
                } else {
                    ansjItem.termNatures = new TermNatures(TermNature.NR);
                }
            }
            ansjItem.termNatures.setPersonAttr(entry.getValue());
        }
    }

    public static int status(char c) {
        Item item = DAT.getDAT()[c];
        if (item == null) {
            return 0;
        }
        return item.getStatus();
    }

    public static boolean isInSystemDic(String word) {
        Item item = DAT.getItem(word);
        return item != null && item.getStatus() > 1;
    }

    public static AnsjItem getItem(int index) {
        AnsjItem item = DAT.getItem(index);
        if (item == null) {
            return AnsjItem.NULL;
        }
        return item;
    }

    public static AnsjItem getItem(String str) {
        AnsjItem item = DAT.getItem(str);
        if (item == null) {
            return AnsjItem.NULL;
        }
        return item;
    }

    public static int getId(String str) {
        return DAT.getId(str);
    }

}
