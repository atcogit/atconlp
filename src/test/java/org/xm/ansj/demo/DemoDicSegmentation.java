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

package org.xm.ansj.demo;

import org.xm.ansj.domain.Result;
import org.xm.ansj.segmentation.DicSegmentation;

/**
 * @author xuming
 */
public class DemoDicSegmentation {
    public static void main(String[] args) {
        Result result = DicSegmentation.parse("来自大学生小说网大学,让战士们过一个欢乐祥和的新春佳节.");
        System.out.println(result);
    }
}
