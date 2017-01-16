package org.xm.ansj.segmentation;

import static org.xm.ansj.dictionary.DatDictionary.status;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.WordAlert;
import org.xm.ansj.dictionary.DatDictionary;
import org.xm.ansj.dictionary.UserDefineDictionary;
import org.xm.ansj.domain.Graph;
import org.xm.ansj.domain.Result;
import org.xm.ansj.domain.Term;
import org.xm.ansj.domain.TermNature;
import org.xm.ansj.domain.TermNatures;
import org.xm.ansj.util.AnsjReader;
import org.xm.ansj.util.AppStaticValue;
import org.xm.xmnlp.util.StringUtil;

/**
 * @author xuming
 */
public abstract class Segmentation {
    public int offe;
    private Word gwi = new Word();
    protected Forest[] forests = null;
    private Forest ambiguityForest = UserDefineDictionary.ambiguityForest;
    private AnsjReader br;

    public Segmentation() {
    }

    private LinkedList<Term> terms = new LinkedList<Term>();

    public Term next() throws IOException {
        Term term;
        if (!terms.isEmpty()) {
            term = terms.poll();
            term.updateOffe(offe);
            return term;
        }
        String temp = br.readLine();
        offe = br.getStart();
        while (StringUtil.isBlank(temp)) {
            if (temp == null) {
                return null;
            } else {
                temp = br.readLine();
            }
        }

        fullTerms(temp);
        if (!terms.isEmpty()) {
            term = terms.poll();
            term.updateOffe(offe);
            return term;
        }
        return null;
    }

    private void fullTerms(String str) {
        List<Term> result = analysisStr(str);
        terms.addAll(result);
    }

    private List<Term> analysisStr(String str) {
        Graph gp = new Graph(str);
        int startOffe = 0;
        if (this.ambiguityForest != null) {
            GetWord gw = new GetWord(this.ambiguityForest, gp.chars);
            String[] params;
            while ((gw.getFrontWords()) != null) {
                if (gwi.offe > startOffe) {
                    analysis(gp, startOffe, gwi.offe);
                }
                params = gw.getParams();
                startOffe = gwi.offe;
                for (int i = 0; i < params.length; i += 2) {
                    gp.addTerm(new Term(params[i], startOffe, new TermNatures(
                            new TermNature(params[i + 1], 1))));
                    startOffe += params[i].length();
                }
            }
        }
        if (startOffe < gp.chars.length - 1) {
            analysis(gp, startOffe, gp.chars.length);
        }
        List<Term> result = this.getResult(gp);
        return result;
    }

    private void analysis(Graph gp, int startOffe, int endOffe) {
        int start;
        int end;
        char[] chars = gp.chars;
        String str ;
        char c;
        for (int i = startOffe; i < endOffe; i++) {
            switch (DatDictionary.status(chars[i])) {
                case 0:
                    if (Character.isHighSurrogate(chars[i]) && (i + 1) < endOffe
                            && Character.isLowSurrogate(chars[i + 1])) {
                        str = new String(Arrays.copyOfRange(chars, i, i + 2));
                        gp.addTerm(new Term(str, i, TermNatures.NULL));
                        i++;
                    } else {
                        gp.addTerm(new Term(String.valueOf(chars[i]),
                                i, TermNatures.NULL));
                    }
                    break;
                case 4:
                    start = i;
                    end = 1;
                    while (++i < endOffe && status(chars[i]) == 4) {
                        end++;
                    }
                    str = WordAlert.alertEnglish(chars, start, end);
                    gp.addTerm(new Term(str, start, TermNatures.EN));
                    i--;
                    break;
                case 5:
                    start = i;
                    end = 1;
                    while (++i < endOffe && status(chars[i]) == 5) {
                        end++;
                    }
                    str = WordAlert.alertNumber(chars, start, end);
                    gp.addTerm(new Term(str, start, TermNatures.M));
                    i--;
                    break;
                default:
                    start = i;
                    end = i;
                    c = chars[start];
                    while (DatDictionary.IN_SYSTEM[c] > 0) {
                        end++;
                        if (++i >= endOffe) {
                            break;
                        }
                        c = chars[i];
                    }
                    if (start == end) {
                        gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
                        continue;
                    }
                    gwi.setChars(chars, start, end);
                    while ((str = gwi.allWords()) != null) {
                        gp.addTerm(new Term(str, gwi.offe, gwi.getItem()));
                    }
                    if (DatDictionary.IN_SYSTEM[c] > 0 || status(c) > 3
                            || Character.isHighSurrogate(chars[i])) {
                        i -= 1;
                    } else {
                        gp.addTerm(new Term(String.valueOf(c), i, TermNatures.NULL));
                    }
                    break;
            }
        }
    }

    protected void setRealName(Graph graph, List<Term> result) {
        if (!AppStaticValue.isRealName) {
            return;
        }
        String str = graph.realStr;
        for (Term term : result) {
            term.setRealName(str.substring(term.getOffe(),
                    term.getOffe() + term.getName().length()));
        }
    }

    public Result parseStr(String temp) {
        return new Result(analysisStr(temp));
    }

    protected abstract List<Term> getResult(Graph graph);

    public abstract class Merger {
        public abstract List<Term> merger();
    }

    public void resetContent(AnsjReader br) {
        this.offe = 0;
        this.br = br;
    }

    public void resetContent(Reader reader) {
        this.offe = 0;
        this.br = new AnsjReader(reader);
    }

    public void resetContent(Reader reader, int buffer) {
        this.offe = 0;
        this.br = new AnsjReader(reader, buffer);
    }

    public Forest getAmbiguityForest() {
        return ambiguityForest;
    }

    public Segmentation setAmbiguityForest(Forest ambiguityForest) {
        this.ambiguityForest = ambiguityForest;
        return this;
    }

    public Segmentation setForests(Forest... forests) {
        this.forests = forests;
        return this;
    }
}
