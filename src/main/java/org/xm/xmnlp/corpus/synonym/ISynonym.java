package org.xm.xmnlp.corpus.synonym;

/**
 * 同义词接口
 */
public interface ISynonym {
    /**
     * 获取原本的词语
     *
     * @return
     */
    String getRealWord();

    /**
     * 获取ID
     *
     * @return
     */
    long getId();

    /**
     * 获取字符类型的ID
     *
     * @return
     */
    String getIdString();
}
