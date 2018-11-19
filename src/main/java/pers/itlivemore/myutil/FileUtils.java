package pers.itlivemore.myutil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Title FileUtils.java
 * @Package: pers.itlivemore.myutil
 * @Description: 文件工具类
 *
 * @Author: laigc
 * @Date: 2018年2月24日 上午10:58:34
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class FileUtils {
	// 文件路径分隔符
	private final static String filePathSeparator = java.io.File.separator;

	/**
	 * 从类路径中读取资源文件的所有行
	 * 
	 * @Title: gerClassPathResourceAllLine
	 * @Description: 从类路径中读取资源文件的所有行
	 * @param filePath
	 *            文件的类路径，以/开头
	 * @param @return
	 *            设定文件
	 * @return List<String> 返回文件中的所有行内容
	 * @throws @Author
	 *             itlivemore
	 * @Date 2018年2月24日 上午11:13:53
	 */
	public static List<String> gerClassPathResourceAllLine(String filePath) {
		List<String> readAllLines = null;
		try {
			URL url = FileUtils.class.getResource(filePath);
			Path path = Paths.get(url.toURI());
			readAllLines = Files.readAllLines(path);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return readAllLines;
	}

	/**
	 * 
	 * @Title: classPath2AbsolutePath
	 * @Description:类路径处理成绝对路径
	 * @param filePath
	 * @param type
	 *            type=1，处理成原始路径，即未编译后的路径,type=2，处理成编译后的路径
	 * @return String
	 * @throws @Author
	 *             laigc
	 * @Date 2018年11月19日 下午3:28:44
	 */
	public static String classPath2AbsolutePath(String filePath, Integer type) {
		if (filePath == null || filePath.trim().equals("")) {
			return "";
		}
		// 如果是以路径分隔符开头，就去掉分隔符
		if (filePath.startsWith("\\")) {
			filePath = filePath.replaceFirst("\\\\", "");
		}
		if (filePath.startsWith("/")) {
			filePath = filePath.replaceFirst("/", "");
		}

		if (type.equals(1)) {
			// type=1，处理成原始路径，即未编译后的路径
			// 项目根路径
			String projectRootPath = System.getProperty("user.dir");

			// 是否是在test包下，maven项目java源文件在src\main\java或是src\test\java下
			boolean isTestPackage = false;
			// 当前类编译后路径
			URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
			String proFilePath = classPath.toString();
			if (proFilePath.contains("test-classes")) {
				// 在test包下
				isTestPackage = true;
			}

			if (!projectRootPath.endsWith(java.io.File.separator)) {
				// 这里写的是src\test\resources，因为是在maven的test目录中，如果是在main目录中，则是src\main\resources
				if (isTestPackage) {
					filePath = projectRootPath + "\\src\\test\\resources\\" + filePath;
				} else {
					filePath = projectRootPath + "\\src\\main\\resources\\" + filePath;
				}
			} else {
				if (isTestPackage) {
					filePath = projectRootPath + "src\\test\\resources\\" + filePath;
				} else {
					filePath = projectRootPath + "src\\main\\resources\\" + filePath;
				}
			}
		} else if (type.equals(2)) {
			// type=2，处理成编译后的路径
			// 以URL形式获取工程的资源文件 classpath 路径, 得到以file:/为开头的URL
			// 例如返回: file:/D:/workspace/myproject01/WEB-INF/classes/
			URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
			String proFilePath = classPath.toString();

			// 移除开头的file:/六个字符
			proFilePath = proFilePath.substring(6);

			// 兼容处理最后一个字符是否为 window系统的文件路径分隔符,同时建立 properties 文件路径
			// 例如返回: D:\workspace\myproject01\WEB-INF\classes\config.properties
			if (proFilePath.endsWith("/") || proFilePath.endsWith("\\")) {
				filePath = proFilePath + filePath;
			} else {
				filePath = proFilePath + java.io.File.separator + filePath;
			}
		}
		// 如果为window系统下,则把路径中的路径分隔符替换为window系统的文件路径分隔符
		filePath = filePath.replace("/", filePathSeparator);
		return filePath;
	}

}
