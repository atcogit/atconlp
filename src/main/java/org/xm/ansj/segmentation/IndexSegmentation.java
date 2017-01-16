package org.xm.ansj.segmentation;

import java.io.Reader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.ObjConver;
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
public class IndexSegmentation extends Segmentation {
    protected static final Forest[] DEFAULT_FORESTS = new Forest[]{UserDefineDictionary.FOREST};

    @Override
    protected List<Term> getResult(final Graph graph) {
        Merger merger = new Merger() {
            @Override
            public List<Term> merger() {
                graph.walkPath();
                ;
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
                String temp;
                Set<String> set = new HashSet<String>();
                List<Term> result = new LinkedList<Term>();
                int length = graph.terms.length - 1;
                for (int i = 0; i < length; i++) {
                    if (graph.terms[i] != null) {
                        result.add(graph.terms[i]);
                        set.add(graph.terms[i].getName() + graph.terms[i].getOffe());
                    }
                }
                LinkedList<Term> last = new LinkedList<Term>();
                Forest[] tempForests = DEFAULT_FORESTS;
                if (forests != null && forests.length > 0) {
                    tempForests = forests;
                }
                char[] chars = graph.chars;
                for (Forest forest : tempForests) {
                    GetWord word = forest.getWord(chars);
                    while ((temp = word.getAllWords()) != null) {
                        if (!set.contains(temp + word.offe)) {
                            set.add(temp + word.offe);
                            last.add(new Term(temp, word.offe, word.getParam(0), ObjConver.getIntValue(word.getParam(1))));
                        }
                    }
                }
                result.addAll(last);
                Collections.sort(result, new Comparator<Term>() {

                    @Override
                    public int compare(Term o1, Term o2) {
                        if (o1.getOffe() == o2.getOffe()) {
                            return o2.getName().length() - o1.getName().length();
                        } else {
                            return o1.getOffe() - o2.getOffe();
                        }
                    }
                });

                setRealName(graph, result);
                return result;
            }
        };

        return merger.merger();
    }

    public IndexSegmentation(Forest... forests) {
        if (forests == null) {
            forests = new Forest[]{UserDefineDictionary.FOREST};
        }
        this.forests = forests;
    }

    public IndexSegmentation(Reader reader, Forest... forests) {
        this.forests = forests;
        super.resetContent(new AnsjReader(reader));
    }

    public static Result parse(String str) {
        return new IndexSegmentation().parseStr(str);
    }

    public static Result parse(String str, Forest... forests) {
        return new IndexSegmentation(forests).parseStr(str);
    }
}
