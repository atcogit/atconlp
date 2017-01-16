package org.xm.ansj.segmentation;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.xm.ansj.dictionary.UserDefineDictionary;
import org.xm.ansj.domain.Graph;
import org.xm.ansj.domain.Result;
import org.xm.ansj.domain.Term;
import org.xm.ansj.recognition.AsianPersonRecognition;
import org.xm.ansj.recognition.NumRecognition;
import org.xm.ansj.recognition.UserDefineRecognition;
import org.xm.ansj.util.AnsjReader;
import org.xm.ansj.util.AppStaticValue;
import org.xm.ansj.util.NameFix;
import org.xm.ansj.util.TermUtil;

/**
 * @author xuming
 */
public class StandardSegmentation extends Segmentation {
    @Override
    protected List<Term> getResult(final Graph graph) {
        Merger merger = new Merger() {
            @Override
            public List<Term> merger() {
                graph.walkPath();
                if (AppStaticValue.isNumRecognition && graph.hasNum) {
                    new NumRecognition().recognition(graph.terms);
                }
                if (graph.hasPersonName && AppStaticValue.isNameRecognition) {
                    new AsianPersonRecognition().recognition(graph.terms);
                    graph.walkPathByScore();
                    NameFix.nameAmbiguity(graph.terms);
                }
                userDefineRecognition(graph, forests);
                return getResult();
            }

            private void userDefineRecognition(final Graph graph, Forest... forests) {
                new UserDefineRecognition(TermUtil.InsertTermType.SKIP, forests).recognition(graph.terms);
                graph.rmLittlePath();
                graph.walkPathByScore();
            }

            private List<Term> getResult() {
                List<Term> result = new ArrayList<Term>();
                int length = graph.terms.length - 1;
                for (int i = 0; i < length; i++) {
                    if (graph.terms[i] != null) {
                        result.add(graph.terms[i]);
                    }
                }
                setRealName(graph, result);
                return result;
            }
        };
        return merger.merger();
    }

    public StandardSegmentation(Forest... forests) {
        if (forests == null) {
            forests = new Forest[]{UserDefineDictionary.FOREST};
        }
        this.forests = forests;
    }

    public StandardSegmentation(Reader reader, Forest... forests) {
        this.forests = forests;
        super.resetContent(new AnsjReader(reader));
    }

    public static Result parse(String str) {
        return new StandardSegmentation().parseStr(str);
    }

    public static Result parse(String str, Forest... forests) {
        return new StandardSegmentation(forests).parseStr(str);
    }
}
