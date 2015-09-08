package com.wangboo.batchSql.mysql;

/**
 * mysql测试连接回调接口
 * @author wangboo
 *
 */
public interface MysqlTestConnectionCallback {

	/**
	 * 是否连接成功
	 * @param isConnected
	 */
	public void testConnectionCallback(boolean isConnected, String failDesc);
	
}
