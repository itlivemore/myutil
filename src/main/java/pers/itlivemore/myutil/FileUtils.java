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

}
