package com.innsmap.data.util;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommonUtil {

	/**
	 * 获取 uuid 的字符串
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 判断list 非空 非empty
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return (list == null || list.isEmpty() || list.size() <= 0);
	}

	/**
	 * 判断 Map 非空 非empty
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty() || map.size() <= 0);
	}

	/**
	 * 判断string类型数据是否非空,非""
	 * 
	 * @param str
	 * @return
	 * 		为空返回true
	 * 		不为空返回false
	 */
	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		return "".equals(str.trim());
	}

	/**
	 * 深度复制PointF的集合，防止外部修改数据影响内部
	 * @param list
	 * @return
	 */
	public static List<PointF> copyPointFList(List<PointF> list) {
		List<PointF> copyList = new ArrayList<PointF>();
		if (isEmpty(list))
			return copyList;
		for (PointF f : list) {
			PointF pointF = new PointF(f.x, f.y);
			copyList.add(pointF);
		}
		return copyList;
	}

}
