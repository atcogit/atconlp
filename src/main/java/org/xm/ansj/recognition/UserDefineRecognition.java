package org.xm.ansj.recognition;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.dictionary.UserDefineDictionary;
import org.xm.ansj.domain.Term;
import org.xm.ansj.domain.TermNature;
import org.xm.ansj.domain.TermNatures;
import org.xm.ansj.util.TermUtil;


/**
 * @author xuming
 */
public class UserDefineRecognition implements ITermRecognition {
    public static final Logger logger = LoggerFactory.getLogger(UserDefineRecognition.class);
    private Term[] terms = null;
    private Forest[] forests = {UserDefineDictionary.FOREST};
    private int offe = -1;
    private int endOffe = -1;
    private int tempFreq = 50;
    private String tempNature;
    private SmartForest<String[] > branch = null;
    private SmartForest<String[] > forest = null;
    private TermUtil.InsertTermType type = TermUtil.InsertTermType.SKIP;
    public UserDefineRecognition(TermUtil.InsertTermType type,Forest... forests){
        this.type = type;
        if(forests !=null && forests.length>0){
            this.forests = forests;
        }
    }
    @Override
    public void recognition(Term[] terms) {
        this.terms = terms;
        for(Forest forest:forests){
            if(forest == null){
                continue;
            }
            reset();
            this.forest = forest;
            branch = forest;
            int length = terms.length -1;
            boolean flag = true;
            for(int i =0;i<length;i++){
                if(terms[i] == null){
                    continue;
                }
                if(branch == forest){
                    flag = false;
                }else{
                    flag = true;
                }
                branch = termStatus(branch,terms[i]);
                if(branch == null){
                    if(offe !=-1){
                        i = offe;
                    }
                    reset();
                }else if(branch .getStatus() ==3){
                    endOffe = i;
                    tempNature = branch.getParam()[0];
                    tempFreq = getInt(branch.getParam()[1],50);
                    if(offe != -1&& offe<endOffe){
                        i = offe;
                        makeNewTerm();
                        reset();
                    }else{
                        reset();
                    }
                }else if(branch .getStatus() ==2){
                    endOffe = i;
                    if (offe == -1) {
                        offe = i;
                    } else {
                        tempNature = branch.getParam()[0];
                        tempFreq = getInt(branch.getParam()[1], 50);
                        if (flag) {
                            makeNewTerm();
                        }
                    }
                } else if (branch.getStatus() == 1) {
                    if (offe == -1) {
                        offe = i;
                    }
                }
            }
            if (offe != -1 && offe < endOffe) {
                makeNewTerm();
            }
        }
    }

    private int getInt(String str,int def){
        try{
            return Integer.parseInt(str);
        }catch (NumberFormatException e){
            logger.warn(str+" not a num"+e);
            return def;
        }
    }
    private void makeNewTerm(){
        StringBuilder sb = new StringBuilder();
        for(int j = offe;j<=endOffe;j++){
            if(terms[j] == null){
                continue;
            }else{
                sb.append(terms[j].getName());
            }
        }
        TermNatures termNatures = new TermNatures(new TermNature(tempNature,tempFreq));
        Term term = new Term(sb.toString(),offe,termNatures);
        term.setSelfScore(-1*tempFreq);
        TermUtil.insertTerm(terms,term,type);
    }
    private void reset(){
        offe = -1;
        endOffe =-1;
        tempFreq = 50;
        tempNature = null;
        branch = forest;
    }
    private SmartForest<String[] > termStatus(SmartForest<String[]> branch,Term term){
        String name = term.getName();
        SmartForest<String[]> sf = branch;
        for(int i=0;i<name.length();i++){
            sf = sf.get(name.charAt(i));
            if(sf == null){
                return null;
            }
        }
        return sf;
    }
}
