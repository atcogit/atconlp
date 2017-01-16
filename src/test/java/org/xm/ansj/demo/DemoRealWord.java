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
import org.xm.ansj.domain.Term;
import org.xm.ansj.segmentation.StandardSegmentation;
import org.xm.ansj.util.AppStaticValue;

/**
 * @author xuming
 */
public class DemoRealWord {
    public static void main(String[] args) {
        // 默认方式
        Result parse = StandardSegmentation.parse("Hello word是每个程序员必经之路");
        System.out.println(parse);

        // 保证方式
        AppStaticValue.isRealName = false;
        parse = StandardSegmentation.parse("Hello word是每个程序员必经之路");
        for (Term term : parse) {
            System.out.print(term.getRealName() + " ");
        }
    }
}
