package pers.itlivemore.myutil;

import java.net.URL;

/**
 * @Title ClassUtils.java
 * @Package: pers.itlivemore.myutil
 * @Description: 类相关的工具类
 *
 * @Author: itlivemore
 * @Date: 2018年2月9日 下午2:40:53
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class ClassUtils {
	
	// 获取类的源文件.java文件的绝对路径
	public static String getClassSourceAbsolutePath(Class<?> clazz) {
		// 类的全限定名
		String canonicalName = clazz.getCanonicalName();
		// 全限定名转为路径
		String canonicalNamePath = canonicalName.replaceAll("\\.", "/") + ".java";
		// System.out.println(canonicalNamePath);

		// 项目根路径
		String projectRootPath = System.getProperty("user.dir");
		// System.out.println(projectRootPath);

		// 获得类编译后路径
		URL compilePath = clazz.getResource("");
		String proFilePath = compilePath.toString();
		// 是否是在test包下，maven项目java源文件在src\main\java或是src\test\java下
		boolean isTestPackage = proFilePath.contains("test-classes");
		String javapath = isTestPackage ? "/src/test/java/" : "/src/main/java/";

		// 类源文件的绝对路径
		String realpath = projectRootPath + javapath + canonicalNamePath;
		// System.out.println(realpath);
		return realpath;
	}
}
