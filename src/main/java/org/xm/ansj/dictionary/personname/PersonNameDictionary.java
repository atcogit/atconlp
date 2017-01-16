package org.xm.ansj.dictionary.personname;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.domain.PersonNatureAttr;
import org.xm.ansj.util.AppStaticValue;

/**
 * @author xuming
 */
public class PersonNameDictionary {
    public static final Logger LOGGER = LoggerFactory.getLogger(PersonNameDictionary.class);
    private HashMap<String, PersonNatureAttr> personMap = null;

    public PersonNameDictionary() {
    }

    public HashMap<String, PersonNatureAttr> getPersonMap() {
        if (personMap != null) {
            return personMap;
        }
        initPersonDic();
        initNameFreq();
        return personMap;
    }

    private void initPersonDic() {
        try {
            BufferedReader br = AppStaticValue.getPersonReader();
            personMap = new HashMap<String, PersonNatureAttr>();
            String temp;
            String[] strs;
            PersonNatureAttr pna;
            while ((temp = br.readLine()) != null) {
                strs = temp.split("\t");
                pna = personMap.get(strs[0]);
                if (pna == null) {
                    pna = new PersonNatureAttr();
                }
                pna.addFreq(Integer.parseInt(strs[1]), Integer.parseInt(strs[2]));
                personMap.put(strs[0], pna);
            }
        } catch (NumberFormatException e) {
            LOGGER.warn("数字格式不正确", e);
        } catch (IOException e) {
            LOGGER.warn("IO异常", e);
        }
    }

    private void initNameFreq() {
        Map<String, int[][]> personFreqMap = AppStaticValue.getPersonFreqMap();
        Set<Map.Entry<String, int[][]>> entrySet = personFreqMap.entrySet();
        PersonNatureAttr pna;
        for (Map.Entry<String, int[][]> entry : entrySet) {
            pna = personMap.get(entry.getKey());
            if (pna == null) {
                pna = new PersonNatureAttr();
                pna.setLocFreq(entry.getValue());
                personMap.put(entry.getKey(), pna);
            } else {
                pna.setLocFreq(entry.getValue());
            }
        }
    }

}
