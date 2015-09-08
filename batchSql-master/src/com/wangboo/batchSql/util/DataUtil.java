package com.wangboo.batchSql.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import com.mysql.jdbc.DatabaseMetaData;
import com.wangboo.batchSql.comp.SqlResultView;

public class DataUtil {

	private static Logger log = Logger.getLogger(DataUtil.class);
	private static Connection conn;
	private static String dbName = "data";

	public static String driverName = "com.mysql.jdbc.Driver";

	public static String sqlPath = "/temp.sql";

	public static void init() throws Exception {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		createTable();
	}

	/**
	 * 创建数据库文件
	 */
	private static void createTable() throws Exception {
		log.debug("创建数据库文件");
		Statement stmt = conn.createStatement();
		try {
			log.debug("创建数据库：servers");
			stmt.executeUpdate(FileUtils.readConfig("servers.sql"));
		} catch (Exception e) {
			log.error("需要创建数据库" + dbName, e);
			throw e;
		} finally {
			stmt.close();
		}
	}

	/**
	 * 加载服务器配置列表
	 * 
	 * @return
	 */
	public static List<Map<String, Object>> loadServers() {
		MapListHandler handler = new MapListHandler();
		QueryRunner q = new QueryRunner();
		try {
			log.debug("----目前存在的配置-----");
			List<Map<String, Object>> list = q.query(conn,
					"select * from servers", handler);
			if (list == null) {
				return null;
			}
			for (Map<String, Object> map : list) {
				if (map == null) {
					continue;
				}
				log.debug(map.toString());
			}
			return list;
		} catch (Exception e) {
			log.error("loadServers error", e);
			return null;
		}
	}

	/**
	 * 增加意向服务器连接配置
	 * 
	 * @param name
	 * @param ip
	 * @param port
	 * @param db
	 * @param user
	 * @param pwd
	 */
	public static void addServer(String name, String host, int port, String db,
			String user, String pwd) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = String
					.format("insert into servers(name,host,port,user,pwd,db) values ('%s','%s',%d,'%s','%s','%s')",
							name, host, port, user, pwd, db);
			log.debug("sql:" + sql);
			stmt.execute(sql);
		} catch (SQLException e) {
			log.error("addServer error : ", e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询最大的服务器id
	 * 
	 * @return int
	 * @throws
	 * @author YXY
	 */
	public static int getMaxId() {
		int maxId = -1;
		Statement stmt = null;
		QueryRunner q = new QueryRunner();
		try {

			stmt = conn.createStatement();
			String sql = "select MAX(id) idmax from servers";
			Object[] objects = q.query(conn, sql, new ArrayHandler());
			maxId = (int) objects[0];
			log.debug("idmax:" + maxId);
		} catch (SQLException e) {
			log.error(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return maxId;
	}

	/**
	 * 删除服务器
	 * 
	 * @param index
	 *            void
	 * @throws
	 * @author YXY
	 */
	public static void deleteServer(int id) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "delete from servers where id=" + id;
			log.debug("sql:" + sql);
			stmt.execute(sql);
		} catch (SQLException e) {
			log.error("deleteServer error : ", e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 更新服务器数据
	 * 
	 * @param id
	 * @param name
	 * @param host
	 * @param port
	 * @param db
	 * @param user
	 * @param pwd
	 *            void
	 * @throws
	 * @author YXY
	 */
	public static void updateServer(int id, String name, String host, int port,
			String db, String user, String pwd) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = String
					.format("update servers set name='%s', host='%s',port=%d,user='%s',pwd='%s',db='%s' where id="
							+ id, name, host, port, user, pwd, db);
			log.debug("sql:" + sql);
			stmt.execute(sql);
		} catch (SQLException e) {
			log.error("addServer error : ", e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取数据库所有数据表
	 * 
	 * @param server
	 * @return List<String>
	 * @throws
	 * @author YXY
	 */
	public static List<String> getTables(Map<String, Object> server) {
		List<String> tables = new ArrayList<>();
		// 解析配置
		String url = StringUtils.getSqlUrl(server);
		String user = (String) server.get("user");
		String pwd = (String) server.get("pwd");
		ResultSet rs = null;
		Connection myconn = null;
		try {
			Class.forName(driverName);
			myconn = DriverManager.getConnection(url, user, pwd);
			DatabaseMetaData meta = (DatabaseMetaData) myconn.getMetaData();
			rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				tables.add(rs.getString(3));
			}
		} catch (Exception e) {
			log.error("数据" + url + "连接出错");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (myconn != null) {
					myconn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tables;
	}

	/**
	 * 导出数据库脚本
	 * 
	 * @param sv
	 * @param config
	 * @param tables
	 *            void
	 * @throws
	 * @author YXY
	 */
	public static void exportSqlData(SqlResultView srv,
			Map<String, Object> config, List<String> tables, boolean allSelected) {
		StringBuilder sql = new StringBuilder();
		String user = (String) config.get("user");
		String pwd = (String) config.get("pwd");
		String host = (String) config.get("host");
		String port = String.valueOf(config.get("port"));
		String db = (String) config.get("db");
		sql.append("mysqldump -u").append(user).append(" -p").append(pwd)
				.append(" -h").append(host).append(" -P").append(port)
				.append(" --set-charset=utf8 ").append(db);
		if (!allSelected) {
			for (String tname : tables) {
				sql.append(" ").append(tname);
			}
		}
		try {
			Runtime rt = Runtime.getRuntime();
			srv.getSqlArea().removeAll();
			srv.getSqlArea().append("生成指令:" + sql.toString() + "\r\n");
			srv.getSqlArea().append("开始生成脚本......\r\n");
			log.debug("生成指令:" + sql.toString());
			// 调用 mysql-cmd
			Process child = rt.exec(sql.toString());
			InputStream in = child.getInputStream();
			InputStreamReader isr = new InputStreamReader(in, "utf8");
			StringBuffer sb = new StringBuffer("");
			// 捕获控制台输出信息字符串
			BufferedReader br = new BufferedReader(isr);
			String inStr;
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			String outStr = sb.toString();
			// 导出sql脚本
			FileOutputStream fout = new FileOutputStream(sqlPath);
			OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
			writer.write(outStr);
			writer.flush();
			// 关闭输入输出流
			in.close();
			isr.close();
			br.close();
			writer.close();
			fout.close();
			srv.getSqlArea().append("脚本生成完成，准备同步数据.....\r\n");
			srv.getSqlArea().setCaretPosition(
					srv.getSqlArea().getText().length());
		} catch (Exception e) {
			e.printStackTrace();
			srv.getSqlArea().append("脚本生成失败，请检查源数据库连接及配置\r\n");
			srv.getSqlArea().setCaretPosition(
					srv.getSqlArea().getText().length());
		}
	}

	/**
	 * ---- test blow -------
	 */
	public static void test() {
		QueryRunner q = new QueryRunner();
		MapListHandler rsh = new MapListHandler();
		try {
			List<Map<String, Object>> list = q.query(conn,
					"select * from servers", rsh);
			System.out.println("list = " + list.size());
			for (Map<String, Object> item : list) {
				log.debug("item : " + item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// init();
		// addServer("1", "192.168.1.182", 3033, "jiyu", "root", "root");
		// getTables(null);
	}
}
