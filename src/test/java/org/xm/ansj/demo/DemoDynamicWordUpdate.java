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

import org.xm.ansj.dictionary.UserDefineDictionary;
import org.xm.ansj.domain.Result;
import org.xm.ansj.segmentation.DicSegmentation;

/**
 * @author xuming
 */
public class DemoDynamicWordUpdate {
    public static void main(String[] args) {
        UserDefineDictionary.insertWord("yinguo雪佛兰汽车","userdefine",1000);
        Result terms = DicSegmentation.parse("w很喜欢yinguo雪佛兰汽车的高级跑车，样子酷毙了。");
        System.out.println("new add word demo:"+terms);

        UserDefineDictionary.removeWord("yinguo雪佛兰汽车");
        terms = DicSegmentation.parse("w很喜欢yinguo雪佛兰汽车的高级跑车，样子酷毙了。");
        System.out.println("删除用户自定义词典例子:" + terms);

    }
}
