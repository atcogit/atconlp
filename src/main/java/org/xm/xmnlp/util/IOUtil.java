package org.xm.xmnlp.util;

import static org.xm.xmnlp.util.Predefine.logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.xm.xmnlp.corpus.tag.Nature;
import org.xm.xmnlp.dictionary.CoreDictionary;

/**
 * io操作
 */
public class IOUtil {
    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String TABLE = "\t";
    public static final String LINE = "\n";
    public static final byte[] TABBYTE = TABLE.getBytes();
    public static final byte[] LINEBYTE = LINE.getBytes();

    public static InputStream getInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedReader getReader(String path, String charEncoding) throws FileNotFoundException, UnsupportedEncodingException {
        return getReader(new File(path), charEncoding);
    }

    public static BufferedReader getReader(File file, String charEncoding) throws FileNotFoundException, UnsupportedEncodingException {
        InputStream is = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(is, charEncoding));
    }

    public static RandomAccessFile getRandomAccessFile(String path, String charEncoding) throws FileNotFoundException {
        InputStream is = getInputStream(path);
        if (is != null) {
            return new RandomAccessFile(new File(path), "r");
        }
        return null;
    }

    public static void Writer(String path, String charEncoding, String content) {
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
            fos.write(content.getBytes(charEncoding));
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
    }

    /**
     * 将输入流转化为字节流
     *
     * @param inputStream
     * @param charEncoding
     * @return
     * @throws UnsupportedEncodingException
     */
    public static BufferedReader getReader(InputStream inputStream, String charEncoding) throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(inputStream, charEncoding));
    }

    /**
     * 读取文件获得正文
     *
     * @param path
     * @param charEncoding
     * @return
     */
    public static String getContent(String path, String charEncoding) {
        return getContent(new File(path), charEncoding);
    }

    /**
     * 从流中读取正文内容
     *
     * @param is
     * @param charEncoding
     * @return
     */
    public static String getContent(InputStream is, String charEncoding) {
        BufferedReader reader = null;
        try {
            reader = IOUtil.getReader(is, charEncoding);
            return getContent(reader);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 从文件中读取正文内容
     *
     * @param file
     * @param charEncoding
     * @return
     */
    public static String getContent(File file, String charEncoding) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return getContent(is, charEncoding);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
        return "";
    }

    /**
     * @param reader
     * @return
     * @throws IOException
     */
    public static String getContent(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
        } finally {
            close(reader);
        }
        return sb.toString();
    }

    /**
     * 将一个对象序列化到硬盘中
     *
     * @param
     * @param hm
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void WriterObj(String path, Serializable hm) throws FileNotFoundException, IOException {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
            objectOutputStream.writeObject(hm);
        } finally {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
        }
    }

    /**
     * 关闭字符流
     *
     * @param reader
     */
    public static void close(Reader reader) {
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭字节流
     *
     * @param is
     */
    public static void close(InputStream is) {
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭字节流
     *
     * @param os
     */
    public static void close(OutputStream os) {
        try {
            if (os != null) {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileIterator instanceFileIterator(String path, String charEncoding) {
        try {
            return instanceFileIterator(IOUtil.getInputStream(path), charEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileIterator instanceFileIterator(InputStream is, String charEncoding) {
        try {
            return new FileIterator(is, charEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载一个文件到hashMap
     *
     * @param path
     * @param charEncoding
     * @param key
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings({"unchecked"})
    public static <K, V> HashMap<K, V> loadMap(String path, String charEncoding, Class<K> key, Class<V> value)
            throws UnsupportedEncodingException {
        FileIterator iteartor = null;
        HashMap<K, V> hm = null;
        try {
            iteartor = instanceFileIterator(path, charEncoding);
            hm = new HashMap<K, V>();
            String[] split = null;
            int index = 0;
            while (iteartor.hasNext()) {
                index++;
                String readLine = iteartor.next();
                split = readLine.split("\t");
                if (split.length < 2) {
                    System.err.println(path + " line:" + index + " has err :" + readLine + " err to load !");
                    continue;
                }
                hm.put((K) ObjConver.conversion(split[0], key), (V) ObjConver.conversion(split[1], value));
            }
        } finally {
            iteartor.close();
        }
        return hm;
    }

    /**
     * 把一個map写入到文件
     *
     * @param hm
     * @param path
     * @param charEncoding
     * @throws IOException
     */
    public static <K, V> void writeMap(Map<K, V> hm, String path, String charEncoding) throws IOException {
        Iterator<Entry<K, V>> iterator = hm.entrySet().iterator();
        FileOutputStream fos = null;
        Entry<K, V> next;
        try {
            fos = new FileOutputStream(path);
            while (iterator.hasNext()) {
                next = iterator.next();
                fos.write(next.getKey().toString().getBytes(charEncoding));
                fos.write(TABBYTE);
                fos.write(next.getValue().toString().getBytes(charEncoding));
                fos.write(LINEBYTE);
            }
            fos.flush();
        } finally {
            fos.close();
        }
    }

    /**
     * 把一個list写入到文件
     *
     * @param list
     * @param path
     * @param charEncoding
     * @throws IOException
     */
    public static <T> void writeList(List<T> list, String path, String charEncoding) throws IOException {
        Iterator<T> iterator = list.iterator();
        FileOutputStream fos = null;
        T next;
        try {
            fos = new FileOutputStream(path);
            while (iterator.hasNext()) {
                next = iterator.next();
                fos.write(next.toString().getBytes(charEncoding));
                fos.write(LINEBYTE);
            }
            fos.flush();
        } finally {
            fos.close();
        }
    }

    public static List<String> readFile2List(String path, String charEncoding) throws UnsupportedEncodingException, FileNotFoundException {
        return readFile2List(getReader(path, charEncoding));
    }

    public static List<String> readFile2List(File file, String charEncoding) throws FileNotFoundException, UnsupportedEncodingException {
        return readFile2List(getReader(file, charEncoding));
    }

    public static List<String> readFile2List(InputStream inputStream, String charEncoding) throws UnsupportedEncodingException {
        return readFile2List(getReader(inputStream, charEncoding));
    }

    /**
     * 从一个字符流读取文件到list.
     *
     * @param br
     * @return
     * @throws IOException
     */
    public static List<String> readFile2List(BufferedReader br) {
        List<String> all = new ArrayList<String>();
        String temp = null;
        try {
            while ((temp = br.readLine()) != null) {
                all.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return all;
    }

    /**
     * 序列化对象
     *
     * @param o
     * @param path
     * @return
     */
    public static boolean saveObjectTo(Object o, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            logger.warning("在保存对象" + o + "到" + path + "时发生异常" + e);
            return false;
        }

        return true;
    }

    /**
     * 反序列化对象
     *
     * @param path
     * @return
     */
    public static Object readObjectFrom(String path) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            logger.warning("在从" + path + "读取对象时发生异常" + e);
        }

        return null;
    }

    /**
     * 一次性读入纯文本
     *
     * @param path
     * @return
     */
    public static String readTxt(String path) {
        if (path == null) return null;
        File file = new File(path);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
            logger.warning("找不到" + path + e);
            return null;
        } catch (IOException e) {
            logger.warning("读取" + path + "发生IO异常" + e);
            return null;
        }

        return new String(fileContent, Charset.forName("UTF-8"));
    }

    public static LinkedList<String[]> readCsv(String path) {
        LinkedList<String[]> resultList = new LinkedList<String[]>();
        LinkedList<String> lineList = readLineList(path);
        for (String line : lineList) {
            resultList.add(line.split(","));
        }
        return resultList;
    }

    /**
     * 快速保存
     *
     * @param path
     * @param content
     * @return
     */
    public static boolean saveTxt(String path, String content) {
        try {
            FileChannel fc = new FileOutputStream(path).getChannel();
            fc.write(ByteBuffer.wrap(content.getBytes()));
            fc.close();
        } catch (Exception e) {
            logger.throwing("IOUtil", "saveTxt", e);
            logger.warning("IOUtil saveTxt 到" + path + "失败" + e.toString());
            return false;
        }
        return true;
    }

    public static boolean saveTxt(String path, StringBuilder content) {
        return saveTxt(path, content.toString());
    }

    public static <T> boolean saveCollectionToTxt(Collection<T> collection, String path) {
        StringBuilder sb = new StringBuilder();
        for (Object o : collection) {
            sb.append(o);
            sb.append('\n');
        }
        return saveTxt(path, sb.toString());
    }

    /**
     * 将整个文件读取为字节数组
     *
     * @param path
     * @return
     */
    public static byte[] readBytes(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            FileChannel channel = fis.getChannel();
            int fileSize = (int) channel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
            channel.read(byteBuffer);
            byteBuffer.flip();
            byte[] bytes = byteBuffer.array();
            byteBuffer.clear();
            channel.close();
            fis.close();
            return bytes;
        } catch (Exception e) {
            logger.warning("读取" + path + "时发生异常" + e);
            logger.warning("开始生成" + path + " ...");
        }

        return null;
    }

    public static LinkedList<String> readLineList(String path) {
        LinkedList<String> result = new LinkedList<String>();
        String txt = readTxt(path);
        if (txt == null) return result;
        StringTokenizer tokenizer = new StringTokenizer(txt, "\n");
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }

        return result;
    }

    /**
     * 用省内存的方式读取大文件
     *
     * @param path
     * @return
     */
    public static LinkedList<String> readLineListWithLessMemory(String path) {
        LinkedList<String> result = new LinkedList<String>();
        String line = null;
        try {
            BufferedReader bw = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            while ((line = bw.readLine()) != null) {
                result.add(line);
            }
            bw.close();
        } catch (Exception e) {
            logger.warning("加载" + path + "失败，" + e);
        }

        return result;
    }

    public static boolean saveMapToTxt(Map<Object, Object> map, String path) {
        return saveMapToTxt(map, path, "=");
    }

    public static boolean saveMapToTxt(Map<Object, Object> map, String path, String separator) {
        map = new TreeMap<Object, Object>(map);
        return saveEntrySetToTxt(map.entrySet(), path, separator);
    }

    public static boolean saveEntrySetToTxt(Set<Map.Entry<Object, Object>> entrySet, String path, String separator) {
        StringBuilder sbOut = new StringBuilder();
        for (Map.Entry<Object, Object> entry : entrySet) {
            sbOut.append(entry.getKey());
            sbOut.append(separator);
            sbOut.append(entry.getValue());
            sbOut.append('\n');
        }
        return saveTxt(path, sbOut.toString());
    }

    /**
     * 获取文件所在目录的路径
     *
     * @param path
     * @return
     */
    public static String dirname(String path) {
        int index = path.lastIndexOf('/');
        if (index == -1) return path;
        return path.substring(0, index + 1);
    }

    public static LineIterator readLine(String path) {
        return new LineIterator(path);
    }

    /**
     * 方便读取按行读取大文件
     */
    public static class LineIterator implements Iterator<String> {
        BufferedReader bw;
        String line;

        public LineIterator(String path) {
            try {
                bw = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
                line = bw.readLine();
            } catch (FileNotFoundException e) {
                logger.warning("文件" + path + "不存在，接下来的调用会返回null" + TextUtil.exceptionToString(e));
                bw = null;
            } catch (IOException e) {
                logger.warning("在读取过程中发生错误" + TextUtil.exceptionToString(e));
                bw = null;
            }
        }

        public void close() {
            if (bw == null) return;
            try {
                bw.close();
                bw = null;
            } catch (IOException e) {
                logger.warning("关闭文件失败" + TextUtil.exceptionToString(e));
            }
            return;
        }

        @Override
        public boolean hasNext() {
            if (bw == null) return false;
            if (line == null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    logger.warning("关闭文件失败" + TextUtil.exceptionToString(e));
                }
                return false;
            }

            return true;
        }

        @Override
        public String next() {
            String preLine = line;
            try {
                if (bw != null) {
                    line = bw.readLine();
                    if (line == null && bw != null) {
                        try {
                            bw.close();
                            bw = null;
                        } catch (IOException e) {
                            logger.warning("关闭文件失败" + TextUtil.exceptionToString(e));
                        }
                    }
                } else {
                    line = null;
                }
            } catch (IOException e) {
                logger.warning("在读取过程中发生错误" + TextUtil.exceptionToString(e));
            }
            return preLine;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("只读，不可写！");
        }
    }

    /**
     * 创建一个BufferedWriter
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static BufferedWriter newBufferedWriter(String path) throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
    }

    /**
     * 创建一个BufferedReader
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static BufferedReader newBufferedReader(String path) throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
    }

    public static BufferedWriter newBufferedWriter(String path, boolean append) throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append), "UTF-8"));
    }

    /**
     * 获取最后一个分隔符的后缀
     *
     * @param name
     * @param delimiter
     * @return
     */
    public static String getSuffix(String name, String delimiter) {
        return name.substring(name.lastIndexOf(delimiter) + 1);
    }

    /**
     * 写数组，用制表符分割
     *
     * @param bw
     * @param params
     * @throws IOException
     */
    public static void writeLine(BufferedWriter bw, String... params) throws IOException {
        for (int i = 0; i < params.length - 1; i++) {
            bw.write(params[i]);
            bw.write('\t');
        }
        bw.write(params[params.length - 1]);
    }

    /**
     * 加载词典，词典必须遵守核心词典格式
     *
     * @param pathArray 词典路径，可以有任意个
     * @return 一个储存了词条的map
     * @throws IOException 异常表示加载失败
     */
    public static TreeMap<String, CoreDictionary.Attribute> loadDictionary(String... pathArray) throws IOException {
        TreeMap<String, CoreDictionary.Attribute> map = new TreeMap<String, CoreDictionary.Attribute>();
        for (String path : pathArray) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            loadDictionary(br, map);
        }

        return map;
    }

    /**
     * 将一个BufferedReader中的词条加载到词典
     *
     * @param br      源
     * @param storage 储存位置
     * @throws IOException 异常表示加载失败
     */
    public static void loadDictionary(BufferedReader br, TreeMap<String, CoreDictionary.Attribute> storage) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String param[] = line.split("\\s");
            int natureCount = (param.length - 1) / 2;
            CoreDictionary.Attribute attribute = new CoreDictionary.Attribute(natureCount);
            for (int i = 0; i < natureCount; ++i) {
                attribute.nature[i] = Enum.valueOf(Nature.class, param[1 + 2 * i]);
                attribute.frequency[i] = Integer.parseInt(param[2 + 2 * i]);
                attribute.totalFrequency += attribute.frequency[i];
            }
            storage.put(param[0], attribute);
        }
        br.close();
    }

    public static void writeCustomNature(DataOutputStream out, LinkedHashSet<Nature> customNatureCollector) throws IOException {
        if (customNatureCollector.size() == 0) return;
        out.writeInt(-customNatureCollector.size());
        for (Nature nature : customNatureCollector) {
            TextUtil.writeString(nature.toString(), out);
        }
    }
}
