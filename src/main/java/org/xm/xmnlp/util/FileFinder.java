package org.xm.xmnlp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 从系统各个环境中找文件.或者文件夹
 *
 */
public class FileFinder {
	/**
	 * 系统路径分隔符
	 */
	private static final String SEPARATOR = System.getProperty("PATHS.separator");
	private static final String[] PATHS_PROPERTIES = new String[]{ "java.class.PATHS",  "java.library.PATHS"};

	public static List<File> fileDir = new ArrayList<File>();

	static{
		fileDir.add(new File("").getAbsoluteFile()) ;
	}

	
	/**
	 * 输入一个文件名或者文件的最后路径寻找文件
	 * default deep Integer.max
	 * @param
	 * @return
	 */
	public static File find(String lastPath) {
		return find(lastPath,Integer.MAX_VALUE) ;
	}

	/**
	 * 输入一个文件名或者文件的最后路径寻找文件
	 *
	 * @param
	 * @return
	 */
	public static File find(String lastPath , int deep) {
		
		// 先深度查找
		for (File file : fileDir) {
			if (file.exists() && file.canRead()) {
				file = findByFile(file, lastPath,deep);
				if (file != null) {
					return file;
				}
			}
		}
		// 再从基本几个目录中查找
		for (String pathProperties : PATHS_PROPERTIES) {
			String[] propertyPath = System.getProperty(pathProperties).split(SEPARATOR);
			for (String path : propertyPath) {
				File file = new File(path);
				if (file.exists() && file.canRead()) {
					file = findByFile(file, lastPath,deep);
					if (file != null) {
						return file;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据一个文件深度查找
	 *
	 * @param file
	 * @param lastPath
	 * @return
	 */
	public static File findByFile(File file, String lastPath){
		return findByFile(file, lastPath, Integer.MAX_VALUE) ;
	}

	/**
	 * 根据一个文件深度查找
	 *
	 * @param file
	 * @param lastPath
	 * @param deep
	 * @return
	 */
	public static File findByFile(File file, String lastPath,int deep) {
		if (deep==0||!file.exists() || !file.canRead()) {
			return null;
		}
		if (file.getAbsolutePath().endsWith(lastPath)) {
			return file;
		}
		if (file.isDirectory()) {

			File[] listFiles = file.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File file2 : listFiles) {
					File temp = findByFile(file2, lastPath, deep-1);			
					if (temp != null) {
						return temp;
					}
				}
			}
		}
		return null;
	}


}
