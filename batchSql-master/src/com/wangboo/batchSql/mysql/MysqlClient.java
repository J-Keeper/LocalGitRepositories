package com.wangboo.batchSql.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Mysql方法集合
 * 
 * @author wangboo
 * 
 */
public class MysqlClient {

	private static final Logger log = Logger.getLogger(MysqlClient.class);

	public static void init() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
	}

	/**
	 * 测试连接
	 * 
	 * @param url
	 * @param user
	 * @param pwd
	 */
	public static void testConnection(MysqlTestConnectionCallback cb,
			String url, String user, String pwd) {
		try {
			log.debug("url = " + url);
			Connection conn = DriverManager.getConnection(url, user, pwd);
			if (!conn.isClosed()) {
				conn.close();
				cb.testConnectionCallback(true, null);
			} else {
				cb.testConnectionCallback(false, "未能成功连接");
			}
		} catch (SQLException e) {
			log.error("connection failed : ", e);
			cb.testConnectionCallback(false, e.getMessage());
		}
	}

}
