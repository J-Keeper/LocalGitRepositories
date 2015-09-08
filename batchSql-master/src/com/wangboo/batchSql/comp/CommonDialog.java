package com.wangboo.batchSql.comp;

import javax.swing.JOptionPane;

/**
 * 常用对话框
 * 
 * @author wangboo
 * 
 */
public class CommonDialog {

	/**
	 * 显示普通对话框（带描述）
	 * 
	 * @param msg
	 */
	public static void showOkDialog(String title, String msg) {
		JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.OK_OPTION);
	}
	
	/**
	 * 显示错误提示对话框
	 * 
	 * @param title
	 * @param msg
	 * @author YXY
	 */
	public static void showErrorDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

}
