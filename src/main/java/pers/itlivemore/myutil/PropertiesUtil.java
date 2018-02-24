package pers.itlivemore.myutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Title PropertiesUtil.java
 * @Package: pers.itlivemore.myutil
 * @Description: Properties相关的工具类
 *
 * @Author: itlivemore
 * @Date: 2018年2月24日 上午9:12:32
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class PropertiesUtil {
	/**
	 * 读取Properties文件
	 * 
	 * @Title: getProperties
	 * @Description: 读取Properties文件
	 * @param filePath
	 *            Properties文件路径
	 * @param isClassPath
	 *            是否是类路径
	 * @param @return
	 *            设定文件
	 * @return Properties 返回Properties类型
	 * @throws @Author
	 *             itlivemore
	 * @Date 2018年2月24日 上午9:13:52
	 */
	public static Properties getProperties(String filePath, boolean isClassPath) {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			String pathDesc = null; // 文件路径描述，是系统路径还是类路径
			if (isClassPath) {
				if (filePath.startsWith("\\")) {
					filePath = filePath.replaceFirst("\\\\", "");
				}
				if (filePath.startsWith("/")) {
					is = PropertiesUtil.class.getResourceAsStream(filePath);
				} else {
					is = PropertiesUtil.class.getResourceAsStream("/" + filePath);
				}
				pathDesc = "class path file ";
			} else {
				is = new FileInputStream(new File(filePath));
				pathDesc = "file system file ";
			}
			if (is == null) {
				throw new IllegalArgumentException(pathDesc + filePath + " is not found");
			}
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	public static boolean getBooleanProperty(Properties properties, final String name) {
		return getBooleanProperty(properties, name, false);
	}

	public static boolean getBooleanProperty(Properties properties, final String name, final boolean defaultValue) {
		String prop = properties.getProperty(name);
		return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
	}

	public static int getIntProperty(Properties properties, final String name) {
		return getIntProperty(properties, name, 0);
	}

	public static int getIntProperty(Properties properties, final String name, final int defaultValue) {
		String prop = properties.getProperty(name);
		return (prop == null) ? defaultValue : Integer.parseInt(prop);
	}

	// 保存到Properties文件,filePath必须是绝对路径

	/**
	 * 更新属性及属性值到Properties文件
	 * 
	 * @Title: saveProperties
	 * @Description: 更新属性及属性值到Properties文件
	 * @param filePath
	 *            Properties文件路径，必须是绝对路径
	 * @param key
	 *            属性名
	 * @param @param
	 *            value 属性值
	 * @return void 返回类型
	 * @throws @Author
	 *             itlivemore
	 * @Date 2018年2月24日 上午9:47:52
	 */
	public static void updateProperties(String filePath, String key, String value) {
		if (key == null || key.trim().equals("")) {
			return;
		}
		File file = new File(filePath);
		Properties properties = PropertiesUtil.getProperties(filePath, false);
		FileOutputStream fos = null;
		try {
			properties.setProperty(key, value);
			// true追加,false覆盖
			fos = new FileOutputStream(file, false);
			properties.store(fos, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
