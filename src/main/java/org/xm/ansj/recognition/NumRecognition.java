package org.xm.ansj.recognition;

import org.xm.ansj.domain.Term;
import org.xm.ansj.util.AppStaticValue;
import org.xm.ansj.util.TermUtil;

/**
 * @author xuming
 */
public class NumRecognition implements ITermRecognition {

    @Override
    public void recognition(Term[] terms) {
        int length = terms.length -1;
        Term from;
        Term to;
        Term temp;
        for(int i =0;i<length;i++){
            if(terms[i] == null){
                continue;
            }else if(".".equals(terms[i].getName())|| "．".equals(terms[i].getName()) ){
                //.
                to = terms[i].getTo();
                from = terms[i].getFrom();
                if(from.getTermNatures().numAttr.flag && to.getTermNatures().numAttr.flag){
                    from.setName(from.getName() +"."+to.getName());
                    TermUtil.termLink(from,to.getTo());
                    terms[to.getOffe()]=null;
                    terms[i]=null;
                    i  = from.getOffe()-1;
                }
                continue;
            }else if(!terms[i].getTermNatures().numAttr.flag){
                continue;
            }
            temp = terms[i];
            while((temp = temp.getTo()).getTermNatures().numAttr.flag){
                terms[i].setName(terms[i].getName() + temp.getName());
            }
            if(AppStaticValue.isQuantifierRecognition && temp.getTermNatures().numAttr.numEndFreq>0){
                terms[i].setName(terms[i].getName() + temp.getName());
                temp = temp.getTo();
            }
            if(terms[i].getTo() != temp){
                TermUtil.termLink(terms[i],temp);
                for(int j = i+1;j<temp.getOffe();j++){
                    terms[j] = null;
                }
                i = temp.getOffe() -1;
            }
        }
    }
}
