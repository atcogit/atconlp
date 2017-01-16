package org.xm.ansj.util;

import java.io.IOException;
import java.io.Reader;

/**
 * @author xuming
 */
public class AnsjReader extends Reader {
    private Reader in;
    private char cb[];
    private int start = 0;
    private int tempStart = 0;
    int offe = 0;
    int len = 0;

    boolean isRead = false;
    boolean ok = false;
    boolean firstRead = true;

    int tempOffe;
    int tempLen;

    private static int defaultCharBufferSize = 8192;

    public AnsjReader(Reader in, int sz) {
        super(in);
        if (sz <= 0) throw new IllegalArgumentException("buffer size <= 0");
        this.in = in;
        cb = new char[sz];
    }


    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (in == null)
                return;
            try {
                in.close();
            } finally {
                in = null;
                cb = null;
            }
        }
    }

    public int getStart() {
        return this.start;
    }

    /**
     * Creates a buffering character-input stream that uses a default-sized
     * input buffer.
     *
     * @param in A Reader
     */
    public AnsjReader(Reader in) {
        this(in, defaultCharBufferSize);
    }

    /**
     * Checks to make sure that the stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (in == null)
            throw new IOException("Stream closed");
    }

    /**
     * 为了功能的单一性我还是不实现了
     */
    public int read(char cbuf[], int off, int len) throws IOException {
        throw new IOException("AnsjBufferedReader not support this interface! ");
    }

    private void readString() throws IOException {

        if (offe <= 0) {
            if (offe == -1) {
                isRead = false;
                return;
            }

            len = in.read(cb);
            if (len <= 0) { // 说明到结尾了
                isRead = false;
                return;
            }
        }

        isRead = true;

        char c = 0;
        int i = offe;
        for (; i < len; i++) {
            c = cb[i];
            if (c != '\r' && c != '\n') {
                break;
            }
            if (!firstRead) {
                i++;
                tempStart++;
                offe = i;
                tempOffe = offe;
                isRead = false;
                return;
            }
            tempStart++;
            start++;
        }

        if (i == len) {
            isRead = true;
            offe = 0;
            return;
        }

        firstRead = false;

        offe = i;

        for (; i < len; i++) {
            c = cb[i];
            if (c == '\n' || c == '\r') {
                isRead = false;
                break;
            }
        }

        tempOffe = offe;
        tempLen = i - offe;

        if (i == len) {
            if (len < cb.length) { // 说明到结尾了
                isRead = false;
                offe = -1;
            } else {
                offe = 0;
            }
        } else {
            offe = i;
        }

    }

    /**
     * 读取一行数据。ps 读取结果会忽略 \n \r
     */
    public String readLine() throws IOException {

        ensureOpen();
        assert in != null;

        StringBuilder sb = null;

        start = tempStart;

        firstRead = true;

        while (true) {

            tempLen = 0;
            ok = false;

            readString();
            // if (tempLen != 0)
            // System.out.println(new String(cb, tempOffe, tempLen));

            if (!isRead && (tempLen == 0 || len == 0)) {
                if (sb != null) {
                    return sb.toString();
                }
                return null;
            }

            if (!isRead) { // 如果不是需要读状态，那么返回
                tempStart += tempLen;
                if (sb == null) {
                    return new String(cb, tempOffe, tempLen);
                } else {
                    sb.append(cb, tempOffe, tempLen);
                    return sb.toString();
                }
            }

            if (tempLen == 0) {
                continue;
            }

            // 如果是需要读状态那么读取
            if (sb == null) {
                sb = new StringBuilder();
            }
            sb.append(cb, tempOffe, tempLen);
            tempStart += tempLen;
        }

    }

}
