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

package org.xm.xmnlp.scorer.editdistance;

import org.junit.Test;

/**
 * @author xuming
 */
public class EditDistanceScorerTest {
    @Test
    public void generateKey() throws Exception {
        EditDistanceScorer ed = new EditDistanceScorer();
        CharArray ca = ed.generateKey("中国人爱吃鱼");
        CharArray other = ed.generateKey("湖北人爱吃");
        System.out.println(ca.similarity(other));
    }

}