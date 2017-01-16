package org.xm.xmnlp.corpus.dictionary.maker;

/**
 * 保存到磁盘
 */
public interface ISaveAble {
    /**
     * 将自己以文本文档的方式保存到磁盘
     *
     * @param path 保存位置，包含文件名，不一定包含后缀
     * @return 是否成功
     */
    public boolean saveTxtTo(String path);
}
