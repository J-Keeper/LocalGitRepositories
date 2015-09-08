package com.wangboo.batchSql.util;

import java.util.Map;

public class StringUtils {
	/**
	 * 校验字符串是否为空
	 * 
	 * @param str
	 * @return boolean
	 * @throws
	 * @author YXY
	 */
	public static boolean isBlank(String str) {
		boolean isNull = true;
		if (str == null) {
			return isNull;
		}
		if (str.trim().equals("")) {
			return isNull;
		}
		return false;
	}

	/**
	 * 根据配置得到数据库连接地址
	 * 
	 * @param data
	 * @return String
	 * @throws
	 * @author YXY
	 */
	public static String getSqlUrl(Map<String, Object> data) {
		String host = (String) data.get("host");
		int port = (int) data.get("port");
		String db = (String) data.get("db");
		return "jdbc:mysql://" + host + ":" + port + "/" + db
				+ "?useUnicode=true&characterEncoding=utf8";
	}
}
