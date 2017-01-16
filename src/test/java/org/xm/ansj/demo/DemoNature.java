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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.domain.Result;
import org.xm.ansj.segmentation.StandardSegmentation;

/**
 * @author xuming
 */
public class DemoNature {
    private final static Logger logger = LoggerFactory.getLogger(DemoNature.class);
    public static void main(String[] args) {
        Result terms = StandardSegmentation.parse("张艺谋的卡宴，马明哲的戏王明艳要去复读了。Ansj中文分词是一个真正的ict的12.32是二十一号球员实现!");
//        terms.recognition(new NatureRecognition());
        System.out.println(terms);
        logger.info("logback info 成功了");
        logger.error("logback error 成功了");

    }
}
