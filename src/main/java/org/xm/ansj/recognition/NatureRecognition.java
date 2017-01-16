package org.xm.ansj.recognition;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.WordAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.dictionary.DatDictionary;
import org.xm.ansj.dictionary.NatureDictionary;
import org.xm.ansj.dictionary.UserDefineDictionary;
import org.xm.ansj.domain.AnsjItem;
import org.xm.ansj.domain.Result;
import org.xm.ansj.domain.Term;
import org.xm.ansj.domain.TermNature;
import org.xm.ansj.domain.TermNatures;
import org.xm.ansj.segmentation.BaseSegmentation;
import org.xm.xmnlp.dic.DicReader;

/**
 * @author xuming
 */
public class NatureRecognition implements IRecognition {
    public static final Logger logger = LoggerFactory.getLogger(NatureRecognition.class);
    private static final Forest SUFFIX_FOREST = new Forest();

    static {
        try {
            BufferedReader reader = DicReader.getReader("ansj/nature_class_suffix.txt");
            String temp;
            while ((temp = reader.readLine()) != null) {
                String[] split = temp.split("\t");
                String word = split[0];
                if (word.length() > 1) {
                    word = new StringBuffer(word).reverse().toString();
                }
                SUFFIX_FOREST.add(word, new String[]{split[1]});
            }
        } catch (IOException e) {
            logger.warn("IO exception", e);
        }
    }

    private NatureTerm root = new NatureTerm(TermNature.BEGIN);
    private NatureTerm[] end = {new NatureTerm(TermNature.END)};
    private List<Term> terms = null;
    private NatureTerm[][] natureTermTable = null;

    @Override
    public void recognition(Result result) {
        this.terms = result.getTerms();
        natureTermTable = new NatureTerm[terms.size() + 1][];
        natureTermTable[terms.size()] = end;
        int length = terms.size();
        for (int i = 0; i < length; i++) {
            natureTermTable[i] = getNatureTermArr(terms.get(i).getTermNatures().termNatures);
        }
        walk();
    }

    public static List<Term> recognition(List<String> words) {
        return recognition(words, 0);
    }

    public static List<Term> recognition(List<String> words, int offe) {
        List<Term> terms = new ArrayList<Term>(words.size());
        int tempOffe = 0;
        for (String word : words) {
            TermNatures tn = getTermNatures(word);
            terms.add(new Term(word, offe + tempOffe, tn));
            tempOffe += word.length();
        }
        new NatureRecognition().recognition(new Result(terms));
        return terms;
    }

    public static TermNatures getTermNatures(String word) {
        String[] params;
        AnsjItem ansjItem = DatDictionary.getItem(word);
        TermNatures tn;
        if (ansjItem != AnsjItem.NULL) {
            tn = ansjItem.termNatures;
        } else if ((params = UserDefineDictionary.getParams(word)) != null) {
            tn = new TermNatures(new TermNature(params[0], 1));
        } else if (WordAlert.isEnglish(word)) {
            tn = TermNatures.EN;
        } else if (WordAlert.isNumber(word)) {
            tn = TermNatures.M;
        } else {
            tn = TermNatures.NULL;
        }
        return tn;
    }

    public static TermNatures guessNature(String word) {
        String nature = null;
        SmartForest<String[]> smartForest = SUFFIX_FOREST;
        int len = 0;
        for (int i = word.length() - 1; i >= 0; i--) {
            smartForest = smartForest.get(word.charAt(i));
            if (smartForest == null) {
                break;
            }
            len++;
            if (smartForest.getStatus() == 2) {
                nature = smartForest.getParam()[0];
            } else if (smartForest.getStatus() == 3) {
                nature = smartForest.getParam()[0];
                break;
            }
        }
        if ("nt".equals(nature) && (len > 1 || word.length() > 3)) {
            return TermNatures.NT;
        } else if ("ns".equals(nature)) {
            return TermNatures.NS;
        } else if (word.length() < 5) {
            Result parse = BaseSegmentation.parse(word);
            for (Term term : parse.getTerms()) {
                if ("nr".equals(term.getNatureStr())) {
                    return TermNatures.NR;
                }
            }
        }
        return TermNatures.NW;
    }

    public void walk() {
        int length = natureTermTable.length - 1;
        setScore(root, natureTermTable[0]);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < natureTermTable[i].length; j++) {
                setScore(natureTermTable[i][j], natureTermTable[i + 1]);
            }
        }
        optimalRoot();
    }

    private void setScore(NatureTerm natureTerm, NatureTerm[] natureTerms) {
        for (int i = 0; i < natureTerms.length; i++) {
            natureTerms[i].setScore(natureTerm);
        }
    }

    private NatureTerm[] getNatureTermArr(TermNature[] termNatures) {
        NatureTerm[] natureTerms = new NatureTerm[termNatures.length];
        for (int i = 0; i < natureTerms.length; i++) {
            natureTerms[i] = new NatureTerm(termNatures[i]);
        }
        return natureTerms;
    }

    private void optimalRoot() {
        NatureTerm to = end[0];
        NatureTerm from;
        int index = natureTermTable.length - 1;
        while ((from = to.from) != null && index > 0) {
            terms.get(--index).setNature(from.termNature.nature);
            to = from;
        }
    }

    public class NatureTerm {
        public TermNature termNature;
        public double score = 0;
        public double selfScore;
        public NatureTerm from;

        protected NatureTerm(TermNature termNature) {
            this.termNature = termNature;
            selfScore = termNature.frequency + 1;
        }

        public double computeNatureFreq(NatureTerm from, NatureTerm to) {
            double twoWordFreq = NatureDictionary.getTwoNatureFreq(from.termNature.nature, to.termNature.nature);
            if (twoWordFreq == 0) {
                twoWordFreq = Math.log(from.selfScore + to.selfScore);
            }
            double result = from.score + Math.log((from.selfScore + to.selfScore) * twoWordFreq) + to.selfScore;
            return result;
        }

        public void setScore(NatureTerm natureTerm) {
            double tempScore = computeNatureFreq(natureTerm, this);
            if (from == null || score < tempScore) {
                this.score = tempScore;
                this.from = natureTerm;
            }
        }

        @Override
        public String toString() {
            return termNature.nature.natureStr + "/" + selfScore;
        }
    }
}
