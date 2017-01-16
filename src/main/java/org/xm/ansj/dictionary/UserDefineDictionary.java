package org.xm.ansj.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.ansj.util.AppStaticValue;
import org.xm.xmnlp.util.IOUtil;
import org.xm.xmnlp.util.StringUtil;

/**
 * @author xuming
 */
public class UserDefineDictionary {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserDefineDictionary.class);
    public static final String DEFAULT_NATURE = "user";
    public static final Integer DEFAULT_FREQ = 1000;
    public static final String DEAFULT_FREQ_STR = "1000";
    public static Forest FOREST = null;
    public static Forest ambiguityForest = null;
    public static Forest synonymForest = null;
    static {
        initUserDictionary();
        initAmbiguityDictionary();
        initSynonymsDictionary();
    }
    public static void insertWord(String keyword,String nature,int freq){
        if(FOREST == null){
            FOREST = new Forest();
        }
        String [] paramers = new String[2];
        paramers[0] =  nature;
        paramers[1] = String.valueOf(freq);
        Value value = new Value(keyword,paramers);
        Library.insertWord(FOREST,value);
    }
    public static void insertWord(String keyword){
        insertWord(keyword,DEFAULT_NATURE,DEFAULT_FREQ);
    }
    private static File[] findLibrary(String path){
        File[] libs = new File[0];
        File file = new File(path);
        if(!file.exists()){
            //try load from classpath
            URL url = UserDefineDictionary.class.getClassLoader().getResource(path);
            if(url != null){
                file = new File(url.getPath());
            }
        }
        if(file .canRead()){
            if(file.isFile()){
                libs = new File[1];
                libs[0] = file;
            }else if(file.isDirectory()){
                File[] files = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".dic") && dir.canRead()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                if (files != null && files.length > 0) {
                    libs = files;
                }
            }
        }
        return libs;
    }
    private static void initUserDictionary(){
        FOREST = AppStaticValue.getDicForest();
    }
    private static void initAmbiguityDictionary(){
        File[] lib = findLibrary(AppStaticValue.ambiguityDictionaryPath);
        if(lib.length>0){
            ambiguityForest = new Forest();
            for(File file :lib){
                try{
                    BufferedReader br = IOUtil.getReader(file,"utf-8");
                    String temp;
                    while((temp = br.readLine()) !=null){
                        if(StringUtil.isNotBlank(temp) ){
                            temp = StringUtil.trim(temp);
                            String[] split = temp.split("\t");
                            StringBuilder sb = new StringBuilder();
                            if(split.length %2 !=0){
                                LOGGER.error("init ambiguity err in line: "+temp);
                            }
                            for(int i= 0;i<split.length;i+=2){
                                sb.append(split[i]);
                            }
                            ambiguityForest.addBranch(sb.toString(),split);
                        }
                    }
                }catch (UnsupportedEncodingException e){
                    LOGGER.warn("unsupport encoding exception"+e);
                }catch (IOException e){
                    LOGGER.warn("IO exception dictionary :{},path:{}",e.getMessage(),file.getPath());
                }
            }
            LOGGER.info("init ambiguity dictionary ok.");
        }else{
            LOGGER.warn("init ambiguity dict warning :{} because file error. ",AppStaticValue.ambiguityDictionaryPath);
        }
    }

    private static void initSynonymsDictionary(){
        File[] lib = findLibrary(AppStaticValue.synonymsDictionaryPath);
        if(lib.length>0){
            synonymForest = new Forest();
            for(File file:lib){
                try{
                    BufferedReader br = IOUtil.getReader(file,"UTF-8");
                    String temp ;
                    while((temp = br.readLine()) !=null){
                        if(StringUtil.isNotBlank(temp)){
                            temp = StringUtil.trim(temp);
                            String[] split = temp.split("\t");
                            LOGGER.info("init synonyms in line:",temp);
                            synonymForest.addBranch(split[0],split);
                        }
                    }
                }catch (UnsupportedEncodingException e){
                    LOGGER.warn("不支持的编码", e);
                } catch (IOException e) {
                    LOGGER.warn("Init synonyms dictionary error :{}, path: {}", e.getMessage(), file.getPath());
                }
            }
            LOGGER.info("init synonyms dict ok.");
        }else{
            LOGGER.warn("init synonyms library warning :{} because : file not found or failed to read !",
                    AppStaticValue.synonymsDictionaryPath);
        }
    }
    public static void loadLibrary(Forest forest,String path){
        File[] lib = findLibrary(path);
        if(lib.length>0){
            for(File file:lib){
                String temp;
                String[] strs;
                Value value;
                try{
                    BufferedReader br = IOUtil.getReader(new FileInputStream(file),"UTF-8");
                    while((temp = br.readLine()) !=null){
                        if(StringUtil.isNotBlank(temp)){
                            temp = StringUtil.trim(temp);
                            strs = temp.split("\t");
                            strs[0] = strs[0].toLowerCase();
                            if(AppStaticValue.isSkipUserDefine && DatDictionary.getId(strs[0])>0){
                                continue;
                            }
                            if(strs.length !=3){
                                value = new Value(strs[0],DEFAULT_NATURE,DEAFULT_FREQ_STR);
                            }else{
                                value = new Value(strs[0],strs[1],strs[2]);
                            }
                            Library.insertWord(forest,value);
                        }
                    }
                }catch  (UnsupportedEncodingException e) {
                    LOGGER.warn("不支持的编码", e);
                } catch (IOException e) {
                    LOGGER.warn("Init user library error :{}, path: {}", e.getMessage(), file.getPath());
                }
            }
            LOGGER.info("Init user library ok!");
        } else {
            LOGGER.warn("Init user library  error :{} because : not find that file !", path);
        }
    }
    public static void removeWord(String word){
        Library.removeWord(FOREST,word);
    }
    public static String[] getParams(String word){
        return getParams(FOREST,word);
    }
    public static String[] getParams(Forest forest,String word){
        SmartForest<String[]> temp = forest;
        for(int i =0;i<word.length();i++){
            temp = temp.get(word.charAt(i));
            if(temp == null){
                return null;
            }
        }
        if(temp.getStatus()>1){
            return temp.getParam();
        }else{
            return null;
        }
    }
    public static boolean contains(String word){
        return getParams(word)!=null;
    }
    public static void clear(){
        FOREST.clear();
    }
}
