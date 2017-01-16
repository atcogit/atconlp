package org.xm.xmnlp.test.corpus;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.other.CharTable;

import junit.framework.TestCase;

/**
 * @author hankcs
 */
public class TestCharTable extends TestCase {
    public void testConvert() throws Exception {
        System.out.println(CharTable.CONVERT['關']);
        System.out.println(CharTable.CONVERT['Ａ']);
        System.out.println(CharTable.CONVERT['“']);
        System.out.println(CharTable.CONVERT['．']);
    }

    public void testEnd() throws Exception {
        System.out.println(CharTable.CONVERT['，']);
        System.out.println(CharTable.CONVERT['。']);
        System.out.println(CharTable.CONVERT['！']);
        System.out.println(CharTable.CONVERT['…']);
    }

    public void testFix() throws Exception {
        char[] CONVERT = CharTable.CONVERT;
        CONVERT['.'] = '.';
        CONVERT['．'] = '.';
        CONVERT['。'] = '.';
        CONVERT['！'] = '!';
        CONVERT['，'] = ',';
        CONVERT['!'] = '!';
        CONVERT['#'] = '#';
        CONVERT['&'] = '&';
        CONVERT['*'] = '*';
        CONVERT[','] = ',';
        CONVERT['/'] = '/';
        CONVERT[';'] = ';';
        CONVERT['?'] = '?';
        CONVERT['\\'] = '\\';
        CONVERT['^'] = '^';
        CONVERT['_'] = '_';
        CONVERT['`'] = '`';
        CONVERT['|'] = '|';
        CONVERT['~'] = '~';
        CONVERT['¡'] = '¡';
        CONVERT['¦'] = '¦';
        CONVERT['´'] = '´';
        CONVERT['¸'] = '¸';
        CONVERT['¿'] = '¿';
        CONVERT['ˇ'] = 'ˇ';
        CONVERT['ˉ'] = 'ˉ';
        CONVERT['ˊ'] = 'ˊ';
        CONVERT['ˋ'] = 'ˋ';
        CONVERT['˜'] = '˜';
        CONVERT['—'] = '—';
        CONVERT['―'] = '―';
        CONVERT['‖'] = '‖';
        CONVERT['…'] = '…';
        CONVERT['∕'] = '∕';
        CONVERT['︳'] = '︳';
        CONVERT['︴'] = '︴';
        CONVERT['﹉'] = '﹉';
        CONVERT['﹊'] = '﹊';
        CONVERT['﹋'] = '﹋';
        CONVERT['﹌'] = '﹌';
        CONVERT['﹍'] = '﹍';
        CONVERT['﹎'] = '﹎';
        CONVERT['﹏'] = '﹏';
        CONVERT['﹐'] = '﹐';
        CONVERT['﹑'] = '﹑';
        CONVERT['﹔'] = '﹔';
        CONVERT['﹖'] = '﹖';
        CONVERT['﹟'] = '﹟';
        CONVERT['﹠'] = '﹠';
        CONVERT['﹡'] = '﹡';
        CONVERT['﹨'] = '﹨';
        CONVERT['＇'] = '＇';
        CONVERT['；'] = '；';
        CONVERT['？'] = '？';
        for (int i = 0; i < CONVERT.length; i++) {
            if (CONVERT[i] == '\u0000') {
                if (i != '\u0000') CONVERT[i] = (char) i;
                else CONVERT[i] = ' ';
            }
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Xmnlp.Config.CharTablePath));
        out.writeObject(CONVERT);
        out.close();
    }

}
