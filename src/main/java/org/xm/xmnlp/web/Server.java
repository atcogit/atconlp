package org.xm.xmnlp.web;

import static org.xm.xmnlp.util.Predefine.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.xm.xmnlp.tokenizer.StandardTokenizer;
import org.xm.xmnlp.util.IOUtil;
import org.xm.xmnlp.util.StringUtil;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * http 服务类
 * Created by xuming on 2016/8/1.
 */
public class Server {
    private final static String HI = "你好中国";
    private final static String FILE_ENCODING = "UTF-8";//System.getProperty("file.encoding");

    public void startServer(int serverPort) throws Exception {
        logger.info("start server ...");
        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpServer = provider.createHttpServer(new InetSocketAddress(serverPort), 100); //监听端口,能同时接100个请求
        httpServer.createContext("/", new MHttpHandler());
        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("server started!");
        System.out.println("open:　http://localhost:8888/page/index.html");

    }

    private static class MHttpHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                if (path != null && path.startsWith("/page")) {
                    writeToClient(httpExchange, readFileToString(path));
                    return;
                }

                String responseMsg = "<html>中文分词演示. 这是api接口的页面..<a href='./page/index.html'>我要看演示!</a><script>location.href='./page/index.html'</script></html>"; // 响应信息
                Map<String, String> paramers = parseParamers(httpExchange);
                String input = paramers.get("input");
                String method = paramers.get("method");
                String nature = paramers.get("nature");
                if (StringUtil.isNotBlank(input)) {
                    responseMsg = Servlet.processRequest(input, method, nature);
                }

                writeToClient(httpExchange, responseMsg);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    writeToClient(httpExchange, e.getMessage());
                } catch (IOException e1) {
                    System.out.println("error:"+e1);
                }
            } finally {
                httpExchange.close();
            }
        }

        private String readFileToString(String path) {
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = this.getClass().getResourceAsStream(path);
                resourceAsStream.available();
                return IOUtil.getContent(resourceAsStream, IOUtil.UTF8);
            } catch (Exception e) {
                return String.valueOf("发生了错误 :404 文件找不到鸟！");
            } finally {
                if (resourceAsStream != null) {
                    try {
                        resourceAsStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void writeToClient(HttpExchange httpExchange, String responseMsg) throws IOException {
            byte[] bytes = responseMsg.getBytes();
            httpExchange.sendResponseHeaders(200, bytes.length); // 设置响应头属性及响应信息的长度
            OutputStream out = httpExchange.getResponseBody(); // 获得输出流
            out.write(bytes);
            out.flush();
        }

        private Map<String, String> parseParamers(HttpExchange httpExchange) throws IOException {
            BufferedReader reader = null;
            try {
                Map<String, String> parameters = new HashMap<String, String>();
                URI requestedUri = httpExchange.getRequestURI();
                String query = requestedUri.getRawQuery();
                // get 请求解析
                parseQuery(query, parameters);
                // post 请求解析
                reader = IOUtil.getReader(httpExchange.getRequestBody(), FILE_ENCODING);
                query = IOUtil.getContent(reader).trim();
                parseQuery(query, parameters);
                httpExchange.setAttribute("parameters", parameters);
                return parameters;
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

        /**
         * 从get请求中解析参数
         *
         * @param query
         * @param parameters
         */
        private void parseQuery(String query, Map<String, String> parameters) {
            if (StringUtil.isBlank(query)) {
                return;
            }
            String split[] = query.split("\\?");
            query = split[split.length - 1];
            split = query.split("&");
            String[] param;
            String key;
            String value;
            for (String kv : split) {
                try {
                    param = kv.split("=");
                    if (param.length == 2) {
                        key = URLDecoder.decode(param[0], FILE_ENCODING);
                        value = URLDecoder.decode(param[1], FILE_ENCODING);
                        parameters.put(key, value);
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out.println("encoding error");
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.err.println("Usage: nlp Server <serverPort> so by default 8888");
            args = new String[]{"8888"};
        }
        /* warm up engine */
        System.out.println(StandardTokenizer.segment(HI));
        /* set up server */
        int serverPort = Integer.valueOf(args[0]);
        new Server().startServer(serverPort);
    }
}
