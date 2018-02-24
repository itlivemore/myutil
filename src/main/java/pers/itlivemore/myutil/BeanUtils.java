package pers.itlivemore.myutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title BeanUtils.java
 * @Package: pers.itlivemore.sql
 * @Description: 实体类相关的工具类
 *
 * @Author: itlivemore
 * @Date: 2018年2月24日 上午10:49:52
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class BeanUtils {
	/**
	 * 驼峰命名改下划线命名
	 * 
	 * @Title: camel2underline
	 * @Description: 驼峰命名改下划线命名
	 * @param name
	 *            字段名
	 * @param @return
	 *            设定文件
	 * @return String 返回修改的字段名
	 * @throws @Author
	 *             itlivemore
	 * @Date 2018年2月24日 上午10:50:11
	 */
	public static String camel2underline(String name) {
		Pattern p = Pattern.compile("[A-Z]");
		if (name == null || name.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(name);
		Matcher mc = p.matcher(name);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
			i++;
		}

		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}
}
