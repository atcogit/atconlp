
package org.xm.ansj.domain;

import java.io.Serializable;

/**
 * @author xuming
 */
public class TermNatures implements Serializable {

    public static final TermNatures M = new TermNatures(TermNature.M);
    public static final TermNatures NR = new TermNatures(TermNature.NR);
    public static final TermNatures EN = new TermNatures(TermNature.EN);
    public static final TermNatures END = new TermNatures(TermNature.END, 50610, -1);
    public static final TermNatures BEGIN = new TermNatures(TermNature.BEGIN, 50610, 0);
    public static final TermNatures NT = new TermNatures(TermNature.NT);
    public static final TermNatures NS = new TermNatures(TermNature.NS);
    public static final TermNatures NRF = new TermNatures(TermNature.NRF);
    public static final TermNatures NW = new TermNatures(TermNature.NW);
    public static final TermNatures NULL = new TermNatures(TermNature.NULL);
    public TermNature[] termNatures = null;
    public NumNatureAttr numAttr = NumNatureAttr.NULL;
    public PersonNatureAttr personAttr = PersonNatureAttr.NULL;
    public Nature nature = null;
    public int allFreq = 0;
    public int id = -2;

    public TermNatures(TermNature[] termNatures, int id) {
        this.id = id;
        this.termNatures = termNatures;
        int maxFreq = -1;
        TermNature termNature = null;
        for (int i = 0; i < termNatures.length; i++) {
            if (maxFreq < termNatures[i].frequency) {
                maxFreq = termNatures[i].frequency;
                termNature = termNatures[i];
            }
        }
        if (termNature != null) {
            this.nature = termNature.nature;
        }
        setAttribute();
    }

    public TermNatures(TermNature termNature) {
        termNatures = new TermNature[1];
        this.termNatures[0] = termNature;
        this.nature = termNature.nature;
        setAttribute();
    }

    public TermNatures(TermNature termNature, int allFreq, int id) {
        this.id = id;
        termNatures = new TermNature[1];
        termNature.frequency = allFreq;
        this.termNatures[0] = termNature;
        this.allFreq = allFreq;
    }

    private void setAttribute() {
        TermNature termNature;
        int max = 0;
        NumNatureAttr numNatureAttr = null;
        for (int i = 0; i < termNatures.length; i++) {
            termNature = termNatures[i];
            allFreq += termNature.frequency;
            max = Math.max(max, termNature.frequency);
            switch (termNature.nature.index) {
                case 18:
                    if (numNatureAttr == null) {
                        numNatureAttr = new NumNatureAttr();
                    }
                    numNatureAttr.numFreq = termNature.frequency;
                    break;
                case 29:
                    if (numNatureAttr == null) {
                        numNatureAttr = new NumNatureAttr();
                    }
                    numNatureAttr.numEndFreq = termNature.frequency;
                    break;
            }
        }
        if (numNatureAttr != null) {
            if (max == numNatureAttr.numFreq) {
                numNatureAttr.flag = true;
            }
            this.numAttr = numNatureAttr;
        }
    }

    public void setPersonAttr(PersonNatureAttr personAttr) {
        this.personAttr = personAttr;
    }
}
