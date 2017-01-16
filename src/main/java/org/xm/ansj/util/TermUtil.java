package org.xm.ansj.util;

import java.util.List;

import org.xm.ansj.domain.Term;
import org.xm.ansj.domain.TermNatures;

/**
 * @author xuming
 */
public class TermUtil {
    public static void termLink(Term from, Term to) {
        if (from == null || to == null) {
            return;
        }
        from.setTo(to);
        to.setFrom(from);
    }

    public enum InsertTermType {
        SKIP,
        REPLACE,
        SCORE_ADD_SORT
    }

    public static void insertTerm(Term[] terms, Term term, InsertTermType type) {
        Term self = terms[term.getOffe()];
        if (self == null) {
            terms[term.getOffe()] = term;
            return;
        }

        int len = term.getName().length();

        // 如果是第一位置
        if (self.getName().length() == len) {
            if (type == InsertTermType.REPLACE) {
                term.setNext(self.next());
                terms[term.getOffe()] = term;
            } else if (type == InsertTermType.SCORE_ADD_SORT) {
                self.setScore(self.getScore() + term.getScore());
                self.setSelfScore(self.getSelfScore() + term.getSelfScore());
            }
            return;
        }

        if (self.getName().length() > len) {
            term.setNext(self);
            terms[term.getOffe()] = term;
            return;
        }

        Term next = self;
        Term before = self;
        while ((next = before.next()) != null) {
            if (next.getName().length() == len) {
                if (type == InsertTermType.REPLACE) {
                    term.setNext(next.next());
                    before.setNext(term);
                } else if (type == InsertTermType.SCORE_ADD_SORT) {
                    next.setScore(next.getScore() + term.getScore());
                    next.setSelfScore(next.getSelfScore() + term.getSelfScore());
                }
                return;
            } else if (next.getName().length() > len) {
                before.setNext(term);
                term.setNext(next);
                return;
            }
            before = next;
        }

        before.setNext(term); // 如果都没有命中
    }

    public static void insertTermNum(Term[] terms, Term term) {
        terms[term.getOffe()] = term;
    }

    public static void insertTerm(Term[] terms, List<Term> tempList, TermNatures nr) {
        StringBuilder sb = new StringBuilder();
        int offe = tempList.get(0).getOffe();
        for (Term term : tempList) {
            sb.append(term.getName());
            terms[term.getOffe()] = null;
        }
        Term term = new Term(sb.toString(), offe, nr);
        insertTermNum(terms, term);
    }
}
