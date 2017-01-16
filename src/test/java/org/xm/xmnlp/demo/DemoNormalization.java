package org.xm.xmnlp.demo;


import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.dictionary.CustomDictionary;

/**
 * 演示正规化字符配置项的效果（繁体->简体，全角->半角，大写->小写）。
 * 该配置项位于properties中，通过Normalization=true来开启
 * 切换配置后必须删除CustomDictionary.txt.bin缓存，否则只影响动态插入的新词。
 */
public class DemoNormalization {
    public static void main(String[] args) {
        Xmnlp.Config.Normalization = true;
        CustomDictionary.insert("爱听4G", "nz 1000");
        System.out.println(Xmnlp.segment("爱听4g"));
        System.out.println(Xmnlp.segment("爱听4G"));
        System.out.println(Xmnlp.segment("爱听４G"));
        System.out.println(Xmnlp.segment("爱听４Ｇ"));
        System.out.println(Xmnlp.segment("愛聽４Ｇ"));
    }
}
