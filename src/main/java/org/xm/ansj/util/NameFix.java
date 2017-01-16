package org.xm.ansj.util;

import org.nlpcn.commons.lang.util.WordAlert;
import org.xm.ansj.domain.Term;
import org.xm.ansj.domain.TermNatures;
import org.xm.ansj.recognition.NatureRecognition;

/**
 * @author xuming
 */
public class NameFix {
    /**
     * 人名消歧,比如.邓颖超生前->邓颖 超生 前 fix to 丁颖超 生 前! 规则的方式增加如果两个人名之间连接是- ， ·，•则连接
     */
    public static void nameAmbiguity(Term[] terms){
        Term from;
        Term term;
        Term to;
        for(int i = 0;i<terms.length -1;i++){
            term = terms[i];
            if(term!=null && term.getTermNatures() == TermNatures.NR && term.getName().length()==2){
                to = terms[i+2];
                if(to.getTermNatures().personAttr.split>0){
                    term.setName(term.getName()+to.getName().charAt(0));
                    terms[i+2] = null;
                    String name = to.getName().substring(1);
                    terms[i+3] = new Term(name,to.getOffe()+1, NatureRecognition.getTermNatures(name));
                    TermUtil.termLink(term,terms[i+3]);
                    TermUtil.termLink(terms[i+3],to.getTo());
                }
            }
        }

        for(int i =0;i<terms.length;i++){
            term = terms[i];
            if(term !=null && term.getName().length() ==1 && i>0
                    && WordAlert.CharCover(term.getName().charAt(0)) == '·') {
                from = term.getFrom();
                to= term.getTo();
                if(from.getNature().natureStr.startsWith("nr") && to.getNature().natureStr.startsWith("nr")){
                    from.setName(from.getName()+term.getName()+ to.getName());
                    TermUtil.termLink(from,to.getTo());
                    terms[i] = null;
                    terms[i+1] = null;
                }
            }
        }
    }

}
