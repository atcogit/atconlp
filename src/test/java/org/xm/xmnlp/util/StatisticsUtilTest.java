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

package org.xm.xmnlp.util;

import org.junit.Test;

/**
 * @author xuming
 */
public class StatisticsUtilTest {
    //词频统计设置
    StatisticsUtil statisticsUtil = new StatisticsUtil();
    @Test
    public void seg() throws Exception {
        //开始分词
        statisticsUtil.seg("数据建模和算法才是基础吧，写代码不算什么。下雨天，明天有关于分子和原子的课程，下雨了也要去听课");
        //输出词频统计结果
        statisticsUtil.dump();
    }

    @Test
    public void clear() throws Exception {
        statisticsUtil.reset();
        //输出词频统计结果
        System.out.println(statisticsUtil.getStatisticsMap());
    }
}