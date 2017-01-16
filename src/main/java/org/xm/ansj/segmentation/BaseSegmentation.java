package org.xm.ansj.segmentation;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.xm.ansj.domain.Graph;
import org.xm.ansj.domain.Result;
import org.xm.ansj.domain.Term;
import org.xm.ansj.util.AnsjReader;

/**
 * @author xuming
 */
public class BaseSegmentation extends Segmentation {
    public BaseSegmentation(){}

    @Override
    protected List<Term> getResult(final Graph graph) {
        Merger merger = new Merger() {
            @Override
            public List<Term> merger() {
                graph.walkPath();
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

    public BaseSegmentation(Reader reader){
        super.resetContent(new AnsjReader(reader));
    }
    public static Result parse(String str){
        return new BaseSegmentation().parseStr(str);
    }
}
