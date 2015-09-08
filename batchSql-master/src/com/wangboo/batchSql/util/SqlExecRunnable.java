package com.wangboo.batchSql.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.wangboo.batchSql.MainFrame;
import com.wangboo.batchSql.comp.ExecSqlView;

/**
 * sql远程执行线程
 * 
 * @author YXY
 * @date 2015年9月7日 上午10:28:06
 */
public class SqlExecRunnable implements Runnable {
	private static final Logger log = Logger.getLogger(MainFrame.class);

	private ExecSqlView esv;
	private String serverName;
	private String sql;
	private String url;
	private String user;
	private String pwd;

	public SqlExecRunnable(ExecSqlView esv, String serverName, String sql,
			String url, String user, String pwd) {
		this.esv = esv;
		this.serverName = serverName;
		this.sql = sql;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
	}

	@Override
	public void run() {
		Connection conn = null;
		Statement stmt = null;
		try {
			String standSql = new String(sql.getBytes(), "UTF-8");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			stmt = conn.createStatement();
			log.debug("服务器[" + serverName + "]开始执行sql:" + sql);
			stmt.execute(standSql);
			String suc = "服务器[" + serverName + "]执行sql:" + sql + "成功\r\n";
			esv.getSrv().getSqlArea().append(suc);
			esv.getSqlArea().setCaretPosition(
					esv.getSqlArea().getText().length());
		} catch (Exception e) {
			e.printStackTrace();
			String error = "服务器[" + serverName + "]执行sql:" + sql
					+ "失败,请检查sql语法及数据库连接\r\n";
			esv.getSrv().getSqlArea().append(error);
			esv.getSqlArea().setCaretPosition(
					esv.getSqlArea().getText().length());
			log.error(error + e.toString());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ExecSqlView getEsv() {
		return esv;
	}

	public String getServerName() {
		return serverName;
	}

	public String getSql() {
		return sql;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPwd() {
		return pwd;
	}

}
