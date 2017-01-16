package org.xm.xmnlp.test.corpus;


import java.io.DataOutputStream;
import java.io.FileOutputStream;

import org.xm.xmnlp.corpus.io.ByteArray;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestBytesArray extends TestCase {

    public static final String DATA_OUT_DAT = "data/test/out.dat";

    public void testWriteAndRead() throws Exception {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(DATA_OUT_DAT));
        out.writeChar('H');
        out.writeChar('e');
        out.writeChar('l');
        out.writeChar('l');
        out.writeChar('o');
        out.close();
        ByteArray byteArray = ByteArray.createByteArray(DATA_OUT_DAT);
        while (byteArray.hasMore()) {
            System.out.println(byteArray.nextChar());
        }
    }

    public void testWriteBigFile() throws Exception {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(DATA_OUT_DAT));
        for (int i = 0; i < 10000; i++) {
            out.writeInt(i);
        }
        out.close();
    }
}
