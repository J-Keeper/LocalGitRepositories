package com.wangboo.batchSql;

import com.wangboo.batchSql.mysql.MysqlClient;
import com.wangboo.batchSql.util.DataUtil;

public class Bootstrap {

	public static void main(String[] args) throws Exception {
		MysqlClient.init();
		DataUtil.init();
		MainFrame frame = new MainFrame();
		frame.showFrame();
	}
	
}
