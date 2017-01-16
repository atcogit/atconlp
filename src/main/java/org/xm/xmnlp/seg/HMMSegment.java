package org.xm.xmnlp.seg;

import static org.xm.xmnlp.util.Predefine.logger;

import java.util.LinkedList;
import java.util.List;

import org.xm.xmnlp.Xmnlp;
import org.xm.xmnlp.corpus.io.ByteArray;
import org.xm.xmnlp.model.trigram.CharacterBasedGenerativeModel;
import org.xm.xmnlp.seg.domain.Term;
import org.xm.xmnlp.util.TextUtil;

/**
 * 基于2阶HMM（A Second-Order Hidden Markov Model, TriGram3阶文法模型）+ BMES序列标注的分词器
 * <p/>
 * Created by mingzai on 2016/7/30.
 */
public class HMMSegment extends Segment {

    static CharacterBasedGenerativeModel model;

    static {
        model = new CharacterBasedGenerativeModel();
        long start = System.currentTimeMillis();
        logger.info("开始从[ " + Xmnlp.Config.HMMSegmentModelPath + " ]加载2阶HMM模型");
        try {
            ByteArray byteArray = ByteArray.createByteArray(Xmnlp.Config.HMMSegmentModelPath);
            if (byteArray == null) {
                logger.severe("HMM分词模型[ " + Xmnlp.Config.HMMSegmentModelPath + " ]不存在");
                System.exit(-1);
            }
            model.load(byteArray);
        } catch (Exception e) {
            logger.severe("发生了异常：" + TextUtil.exceptionToString(e));
            System.exit(-1);
        }
        logger.info("加载成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

    @Override
    protected List<Term> segSentence(char[] sentence) {
        char[] tag = model.tag(sentence);
        List<Term> termList = new LinkedList<Term>();
        int offset = 0;
        for (int i = 0; i < tag.length; offset += 1, ++i) {
            switch (tag[i]) {
                case 'b': {
                    int begin = offset;
                    while (tag[i] != 'e') {
                        offset += 1;
                        ++i;
                        if (i == tag.length) {
                            break;
                        }
                    }
                    if (i == tag.length) {
                        termList.add(new Term(new String(sentence, begin, offset - begin), null));
                    } else
                        termList.add(new Term(new String(sentence, begin, offset - begin + 1), null));
                }
                break;
                default: {
                    termList.add(new Term(new String(sentence, offset, 1), null));
                }
                break;
            }
        }

        return termList;
    }
}
