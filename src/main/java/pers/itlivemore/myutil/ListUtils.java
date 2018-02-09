package pers.itlivemore.myutil;

import java.util.List;

/**
 * @Title ListUtils.java
 * @Package: pers.itlivemore.myutil
 * @Description: List相关工具类
 *
 * @Author: itlivemore
 * @Date: 2018年2月9日 下午2:56:35
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class ListUtils {
	// 找到第一个以findStr打头的index
	public static int getIndexStartsWith(List<String> list, String findStr) {
		int index = -1; // 没有找到返回-1
		if (list == null || list.isEmpty() || findStr == null) {
			return index;
		}
		findStr = findStr.trim();
		if (findStr.equals("")) {
			return index;
		}
		for (int i = 0; i < list.size(); i++) {
			String line = list.get(i);
			if (line != null) {
				line = line.trim();
				if (line.startsWith(findStr)) {
					index = i;
					break;
				}
			}
		}
		return index;
	}
}
