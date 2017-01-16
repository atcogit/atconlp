package org.xm.xmnlp.demo;


import org.xm.xmnlp.web.Server;

/**
 * web服务测试 启动后访问：http://localhost:8888/  http://localhost:8888/page/index.html
 * 
 * @author xuming
 * 
 */
public class DemoWeb {
	public static void main(String[] args) throws Exception {
		args = new String[] { "8888" };
		Server.main(args);
	}
}
