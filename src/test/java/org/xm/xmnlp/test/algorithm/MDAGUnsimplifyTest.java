package org.xm.xmnlp.test.algorithm;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xm.xmnlp.collection.MDAG.MDAG;
import org.xm.xmnlp.collection.MDAG.MDAGMap;
import org.xm.xmnlp.corpus.io.ByteArray;

import junit.framework.TestCase;

/**
 * 希望在此测试解压缩
 */
public class MDAGUnsimplifyTest extends TestCase {
    private static final String DATA_TEST_OUT_BIN = "data/test/out.bin";

    public void testSimplify() throws Exception {
        MDAG mdag = new MDAG();
        mdag.addString("hers");
        mdag.addString("his");
        mdag.addString("she");
        mdag.addString("he");
        DataOutputStream out = new DataOutputStream(new FileOutputStream(DATA_TEST_OUT_BIN));
        mdag.save(out);

        mdag = new MDAG();
        mdag.load(ByteArray.createByteArray(DATA_TEST_OUT_BIN));
        System.out.println(mdag.contains("his"));
    }

    public void testSimplifyWithoutSave() throws Exception {
        MDAG mdag = new MDAG();
        mdag.addString("hers");
        mdag.addString("his");
        mdag.addString("she");
        mdag.addString("he");

        mdag.simplify();
        System.out.println(mdag.contains("hers"));
    }

    public void testSimplifyMap() throws Exception {
        MDAGMap<String> mdagMap = new MDAGMap<String>();
        List<String> validKeySet = new ArrayList<String>();
        validKeySet.add("hers");
        validKeySet.add("his");
        validKeySet.add("she");
        validKeySet.add("he");
        for (String key : validKeySet) {
            mdagMap.put(key, key);
        }
        mdagMap.simplify();

        System.out.println(mdagMap.get("he"));
    }
}
