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

import org.xm.ansj.segmentation.IndexSegmentation;

/**
 * @author xuming
 */
public class DemoIndexSegmentation {
    public static void main(String[] args) {
        System.out.println(IndexSegmentation.parse("花生油"));
    }
}
