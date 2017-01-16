package org.xm.ansj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.dictionary.DatDictionary;
import org.xm.ansj.dictionary.UserDefineDictionary;
import org.xm.ansj.domain.AnsjItem;
import org.xm.xmnlp.dic.DicReader;
import org.xm.xmnlp.util.FileFinder;
import org.xm.xmnlp.util.IOUtil;
import org.xm.xmnlp.util.StringUtil;

/**
 * @author xuming
 */
public class AppStaticValue {
    public static final Logger LOGGER = LoggerFactory.getLogger(AppStaticValue.class);
    public static final String DIC_DEFAULT = "dic";
    public static final String CRF_DEFAULT = "crf";
    public static boolean isNameRecognition = true;
    public static boolean isNumRecognition = true;
    public static boolean isQuantifierRecognition = true;
    public static boolean isRealName = false;
    public static final Map<String, Object> DIC = new HashMap<String, Object>();
    public static final Map<String, Object> CRF = new HashMap<String, Object>();
    public static String ambiguityDictionaryPath = "library/ambiguity.dic";
    public static String synonymsDictionaryPath = "library/synonyms.dic";
    public static boolean isSkipUserDefine = false;

    static {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle("ansj");
        } catch (Exception e) {
            try {
                File find = FileFinder.find("ansj.properties", 1);
                if (find != null && find.isFile()) {
                    rb = new PropertyResourceBundle(IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
                    LOGGER.info("load ansj {}.", find.getAbsolutePath());
                }
            } catch (Exception e1) {
                LOGGER.warn("not find ansj_library.properties.", e1.getMessage());
            }
        }
        if (rb == null) {
            try {
                rb = ResourceBundle.getBundle("library");
            } catch (Exception e) {
                try {
                    File find = FileFinder.find("ansj.properties", 2);
                    if (find != null && find.isFile()) {
                        rb = new PropertyResourceBundle(IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
                        LOGGER.info("load ansj {}.", find.getAbsolutePath());
                    }
                } catch (Exception e1) {
                    LOGGER.warn("not find ansj.properties.", e1.getMessage());
                }
            }
        }

        if (rb == null) {
            LOGGER.warn("not find ansj.properties in classpath use it by default !");
        } else {
            for (String key : rb.keySet()) {
                if (key.equals("dic")) {
                    DIC.put(key, rb.getString(key));
                } else if (key.equals("ambiguityLibrary")) {
                    ambiguityDictionaryPath = rb.getString(key);
                }//其他的同理
            }

        }
    }

    public static void initBigramTable() {
        try {
            BufferedReader reader = IOUtil.getReader(DicReader.getInputStream("ansj/bigramdict.dic"), "UTF-8");
            String temp;
            String[] strs;
            int freq;
            while ((temp = reader.readLine()) != null) {
                if (StringUtil.isBlank(temp)) {
                    continue;
                }
                strs = temp.split("\t");
                freq = Integer.parseInt(strs[1]);
                strs = strs[0].split("@");
                AnsjItem fromItem = DatDictionary.getItem(strs[0]);
                AnsjItem toItem = DatDictionary.getItem(strs[1]);
                if (fromItem == AnsjItem.NULL && strs[0].contains("#")) {
                    fromItem = AnsjItem.BEGIN;
                }
                if (toItem == AnsjItem.NULL && strs[1].contains("#")) {
                    toItem = AnsjItem.END;
                }
                if (fromItem == AnsjItem.NULL || toItem == AnsjItem.NULL) {
                    continue;
                }
                if (fromItem.bigramEntryMap == null) {
                    fromItem.bigramEntryMap = new HashMap<Integer, Integer>();
                }
                fromItem.bigramEntryMap.put(toItem.getIndex(), freq);
            }
            reader.close();
        } catch (NumberFormatException e) {
            LOGGER.warn("数字格式异常", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("不支持的编码", e);
        } catch (IOException e) {
            LOGGER.warn("IO异常", e);
        }
    }

    public static BufferedReader getNatureMapReader() {
        return DicReader.getReader("ansj/nature/nature.map");
    }

    public static BufferedReader getNatureTableReader() {
        return DicReader.getReader("ansj/nature/nature.table");
    }

    /**
     * 人名词典
     *
     */
    public static BufferedReader getPersonReader() {
        return DicReader.getReader("ansj/person/person.dic");
    }

    /**
     * 机构名词典
     */
    public static BufferedReader getCompanReader() {
        return DicReader.getReader("ansj/company/company.data");
    }

    public static InputStream getCoreInputStream() {
        return DicReader.getInputStream("ansj/core.dic");
    }

    public static Map<String, int[][]> getPersonFreqMap() {
        Map<String, int[][]> map = new HashMap<String, int[][]>(0);
        try {
            InputStream is = DicReader.getInputStream("ansj/person/asian_name_freq.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(is);
            map = (Map<String, int[][]>) objectInputStream.readObject();
            is.close();
        } catch (IOException e) {
            LOGGER.warn("io exception", e);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("can not found class", e);
        }
        return map;
    }

    public static Forest getDicForest() {
        return getDicForest(DIC_DEFAULT);
    }

    public static Forest getDicForest(String key) {
        Object temp = DIC.get(key);
        if (temp == null) {
            LOGGER.warn("dic {} not found in config ", key);
            return null;
        } else if (temp instanceof String) {
            return initForest(key, (String) temp);
        } else {
            return (Forest) temp;
        }
    }

    private synchronized static Forest initForest(String key, String dicPath) {
        Object obj = CRF.get(key);
        if (obj != null && obj instanceof Forest) {
            return (Forest) obj;
        }
        Forest forest = new Forest();
        UserDefineDictionary.loadLibrary(forest, dicPath);
        DIC.put(key, forest);
        return forest;
    }
}
