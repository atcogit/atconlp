/*
 * <author>Xu Ming</author>
 * <email>huluobo624@gmail.com</email>
 * <create-date>2016</create-date>
 * <copyright>
 *  Copyright (c) 2016. All Right Reserved, http://www.pycredit.cn/
 *  This source is subject to the Apache 2.0 License.
 *  Please contact http://www.pycredit.cn/ to get more information.
 * </copyright>
 *
 */

package org.xm.ansj.test.dictionary;

import org.junit.Test;
import org.xm.ansj.dictionary.NgramDictionary;

/**
 * @author xuming
 */
public class NgramDictionaryTest {
    @Test
    public void testGetTwoWordFreq() {
        System.out.println("hh");
    }

    public static void main(String[] args) {

        System.out.println(NgramDictionary.getTwoWordFreq(null,null));
        System.out.println("ok");
    }

}