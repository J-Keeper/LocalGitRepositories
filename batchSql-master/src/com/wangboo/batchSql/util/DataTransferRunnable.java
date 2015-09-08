package com.wangboo.batchSql.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wangboo.batchSql.comp.SqlResultView;

/**
 * 数据同步线程
 * 
 * @author YXY
 * @date 2015年9月8日 上午10:02:45
 */
public class DataTransferRunnable implements Runnable {
	private static final Logger log = Logger
			.getLogger(DataTransferRunnable.class);

	private String user;
	private String pwd;
	private String host;
	private int port;
	private String db;

	private SqlResultView srv;

	public DataTransferRunnable(Map<String, Object> config, SqlResultView srv) {
		this.srv = srv;
		this.user = (String) config.get("user");
		this.pwd = (String) config.get("pwd");
		this.host = (String) config.get("host");
		this.port = (int) config.get("port");
		this.db = (String) config.get("db");
	}

	@Override
	public void run() {
		try {
			Runtime rt = Runtime.getRuntime();
			StringBuffer excSql = new StringBuffer();
			excSql.append("mysql -u").append(user).append(" -p").append(pwd)
					.append(" -h").append(host).append(" -P").append(port)
					.append(" ").append(db);
			log.info("服务器" + host + "准备同步");
			srv.getSqlArea().append("服务器[" + host + "]开始同步......\r\n");
			srv.getSqlArea().setCaretPosition(
					srv.getSqlArea().getText().length());
			Process child = rt.exec(excSql.toString());
			// 输入信息作为输出流
			OutputStream out = child.getOutputStream();
			String inStr;
			StringBuffer sb = new StringBuffer("");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(DataUtil.sqlPath), "utf8"));
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			String outStr = sb.toString();
			OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
			writer.write(outStr);
			writer.flush();
			out.close();
			br.close();
			writer.close();
			log.info("服务器[" + host + "]同步完成");
			srv.getSqlArea().append("服务器[" + host + "]同步完成!\r\n");
			srv.getSqlArea().setCaretPosition(
					srv.getSqlArea().getText().length());
		} catch (Exception e) {
			e.printStackTrace();
			srv.getSqlArea().append("服务器[" + host + "]同步失败，请检查该服务器配置!\r\n");
			srv.getSqlArea().setCaretPosition(
					srv.getSqlArea().getText().length());
		}
	}
}
