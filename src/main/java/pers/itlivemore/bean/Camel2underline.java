package pers.itlivemore.bean;

import java.util.List;

import pers.itlivemore.myutil.BeanUtils;
import pers.itlivemore.myutil.FileUtils;

/**
 * @Title Camel2underline.java
 * @Package: sql
 * @Description: 驼峰法命名转下划线命名，把要转换的字段复制到/sql/camel2underline.txt，一行一个字段名
 *
 * @Author: laigc
 * @Date: 2018年2月24日 上午10:52:48
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class Camel2underline {
	public static void main(String[] args) {
		String filePath = "/bean/camel2underline.txt";
		List<String> allLines = FileUtils.gerClassPathResourceAllLine(filePath);
		if (allLines == null) {
			return;
		}
		for (String string : allLines) {
			String camel2underline = BeanUtils.camel2underline(string);
			System.out.println(camel2underline);
		}
	}
}
