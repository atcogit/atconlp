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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xm.ansj.domain.Result;
import org.xm.ansj.segmentation.BaseSegmentation;

/**
 * @author xuming
 */
public class DemoBase {
    public static final Logger logger = Logger.getLogger("DemoBase");

    public static void main(String[] args) {
        logger.warning("warn...");
        logger.info("info...");
        logger.severe("severe...============");
        logger.setLevel(Level.INFO);
        logger.warning("warn.......");
        logger.info("info.......");
        logger.severe("severe.......");
        logger.fine("fine");

		Result parse = BaseSegmentation.parse("福利研究，故事烟酒，让战士们过一个欢乐祥和的新春佳节。");
        System.out.println(parse);
    }
}
