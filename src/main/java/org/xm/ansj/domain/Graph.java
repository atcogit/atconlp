package org.xm.ansj.domain;

import java.util.List;
import java.util.Map;

import org.xm.ansj.dictionary.DatDictionary;
import org.xm.ansj.segmentation.Segmentation;
import org.xm.ansj.util.TermUtil;

/**
 * @author xuming
 */
public class Graph {
    public char[] chars;
    public String realStr;
    public Term[] terms;
    protected Term end;
    protected Term root;
    protected static final String E = "末##末";
    protected static final String B = "始##始";
    public boolean hasPersonName;
    public boolean hasNum;

    public Graph(String str) {
        realStr = str;
        this.chars = str.toCharArray();
        terms = new Term[chars.length + 1];
        end = new Term(E, chars.length, AnsjItem.END);
        root = new Term(B, -1, AnsjItem.BEGIN);
        terms[chars.length] = end;
    }

    public List<Term> getResult(Segmentation.Merger merger) {

        return merger.merger();
    }

    public void addTerm(Term term) {
        if (!hasNum && term.getTermNatures().numAttr.numFreq > 0) {
            hasNum = true;
        }
        if (!hasPersonName && term.getTermNatures().personAttr.flag) {
            hasPersonName = true;
        }
        TermUtil.insertTerm(terms, term, TermUtil.InsertTermType.REPLACE);
    }

    protected Term optimalRoot() {
        Term to = end;
        to.clearScore();
        Term from;
        while ((from = to.getFrom()) != null) {
            for (int i = from.getOffe() + 1; i < to.getOffe(); i++) {
                terms[i] = null;
            }
            if (from.getOffe() > -1) {
                terms[from.getOffe()] = from;
            }
            from.setNext(null);
            from.setTo(to);
            from.clearScore();
            to = from;

        }
        return root;
    }

    public void rmLittlePath() {
        int maxTo = -1;
        Term temp = null;
        Term maxTerm = null;
        // 是否有交叉
        boolean flag = false;
        int length = terms.length - 1;
        for (int i = 0; i < length; i++) {
            maxTerm = getMaxTerm(i);
            if (maxTerm == null)
                continue;

            maxTo = maxTerm.toValue();

            /**
             * 对字数进行优化.如果一个字.就跳过..两个字.
             * 且第二个为null则.也跳过.从第二个后开始
             */
            switch (maxTerm.getName().length()) {
                case 1:
                    continue;
                case 2:
                    if (terms[i + 1] == null) {
                        i = i + 1;
                        continue;
                    }
            }

            /**
             * 判断是否有交叉
             */
            for (int j = i + 1; j < maxTo; j++) {
                temp = getMaxTerm(j);
                if (temp == null) {
                    continue;
                }
                if (maxTo < temp.toValue()) {
                    maxTo = temp.toValue();
                    flag = true;
                }
            }

            if (flag) {
                i = maxTo - 1;
                flag = false;
            } else {
                maxTerm.setNext(null);
                terms[i] = maxTerm;
                for (int j = i + 1; j < maxTo; j++) {
                    terms[j] = null;
                }

            }
        }

    }

    /**
     * 得道最到本行最大term,也就是最右面的term
     *
     * @param i
     * @return
     */
    private Term getMaxTerm(int i) {
        Term maxTerm = terms[i];
        if (maxTerm == null) {
            return null;
        }
        Term term = maxTerm;
        while ((term = term.next()) != null) {
            maxTerm = term;
        }
        return maxTerm;
    }

    public void walkPathByScore() {
        Term term = null;
        // BEGIN先行打分
        mergerByScore(root, 0);
        // 从第一个词开始往后打分
        for (int i = 0; i < terms.length; i++) {
            term = terms[i];
            while (term != null && term.getFrom() != null && term != end) {
                int to = term.toValue();
                mergerByScore(term, to);
                term = term.next();
            }
        }
        optimalRoot();//这个是计算分数重新算路径用
    }

    public void walkPath() {
        walkPath(null);
    }

    /**
     * 干涉性增加相对权重
     *
     * @param relationMap
     */
    public void walkPath(Map<String, Double> relationMap) {
        Term term = null;
        // BEGIN先行打分
        merger(root, 0, relationMap);
        // 从第一个词开始往后打分
        for (int i = 0; i < terms.length; i++) {
            term = terms[i];
            while (term != null && term.getFrom() != null && term != end) {
                //第一轮分词，依据core词典合并单字,set from
                merger(term, term.toValue(), relationMap);
                term = term.next();
            }
        }
        optimalRoot();//重要，计算有向无环图优化set next null
    }

    /**
     * 具体的遍历打分方法
     * <p>
     * 起始位置
     * 起始属性
     *
     * @param toOffe
     */
    private void merger(Term fromTerm, int toOffe, Map<String, Double> relationMap) {
        Term term = null;
        if (terms[toOffe] != null) {
            term = terms[toOffe];
            while (term != null) {
                // 关系式to.set(from)
                term.setPathScore(fromTerm, relationMap);
                // 分词，依据core词典合并单字,set from
                term = term.next();
            }
        } else {
            char c = chars[toOffe];
            TermNatures tn = DatDictionary.getItem(c).termNatures;
            if (tn == null || tn == TermNatures.NULL) {
                tn = TermNatures.NULL;
            }
            terms[toOffe] = new Term(String.valueOf(c), toOffe, tn);
            terms[toOffe].setPathScore(fromTerm, relationMap);
        }
    }

    /**
     * 根据分数
     * <p>
     * 起始位置
     * 起始属性
     *
     * @param to
     */
    private void mergerByScore(Term fromTerm, int to) {
        Term term = null;
        if (terms[to] != null) {
            term = terms[to];
            while (term != null) {
                // 关系式to.set(from)
                term.setPathSelfScore(fromTerm);
                term = term.next();
            }
        }

    }

    /**
     * 对graph进行调试用的
     */
    public void printGraph() {
        for (Term term : terms) {
            if (term == null) {
                continue;
            }
            System.out.print(term.getName() + "\t" + term.getScore() + " ,");
            while ((term = term.next()) != null) {
                System.out.print(term + "\t" + term.getScore() + " ,");
            }
            System.out.println();
        }
    }


}
