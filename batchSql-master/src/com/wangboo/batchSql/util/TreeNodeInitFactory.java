package com.wangboo.batchSql.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.wangboo.batchSql.comp.CommonTreeNode;

/**
 * 节点创建工具类
 * 
 * @author YXY
 * @date 2015年9月2日 下午4:22:18
 */
public class TreeNodeInitFactory {
	private static Logger log = Logger.getLogger(DataUtil.class);
	/** 父节点名称 */
	public static final String FATHERNAME = "服务器列表";
	/** TODO 父节点图标 */
	public static final String FATHERICON = "";
	/** 根节点 */
	public static final int FATHER_NODE = 0;

	/** 服务器节点 */
	public static final int SERVER_NODE = 1;
	/** 数据库节点 */
	public static final int DATABASE_NODE = 2;
	/** 数据表节点 */
	public static final int TABLE_NODE = 3;

	/**
	 * 创建一个根节点
	 * 
	 * @param data
	 *            void
	 * @throws
	 * @author YXY
	 */
	public static CommonTreeNode createFatherTreeNode() {
		CommonTreeNode dmt = new CommonTreeNode(FATHERNAME);
		dmt.setType(0);
		return dmt;
	}

	/**
	 * 创建一个服务器节点
	 * 
	 * @param data
	 *            void
	 * @throws
	 * @author YXY
	 */
	public static CommonTreeNode createServerTreeNode(CommonTreeNode father,
			String name, Map<String, Object> data) {
		CommonTreeNode ctn = new CommonTreeNode(name, SERVER_NODE, father);
		ctn.setServer(data);
		log.info("创建服务器节点:" + name);
		father.add(ctn);
		return ctn;
	}

	/**
	 * 创建通用节点
	 * 
	 * @param name
	 * @return CommonTreeNode
	 * @throws
	 * @author YXY
	 */
	public static CommonTreeNode createCommonTreeNode(String name) {
		CommonTreeNode ctn = new CommonTreeNode(name);
		return ctn;
	}

	/**
	 * 创建数据库节点
	 * 
	 * @param father
	 * @param name
	 * @param data
	 * @return boolean
	 * @throws
	 * @author YXY
	 */
	public static CommonTreeNode createDataBaseTreeNode(CommonTreeNode father,
			String name, Map<String, Object> data) {
		CommonTreeNode ctn = new CommonTreeNode(name, DATABASE_NODE, father);
		ctn.setServer(data);
		father.add(ctn);
		return ctn;
	}

	/**
	 * 创建表节点
	 * 
	 * @param father
	 * @param name
	 * @param data
	 * @return boolean
	 * @throws
	 * @author YXY
	 */
	public static boolean createTableTreeNode(CommonTreeNode father, String name) {
		if (father.getType() != DATABASE_NODE) {
			log.error("必须在父节点下创建子节点");
			return false;
		} else {
			CommonTreeNode ctn = new CommonTreeNode(name, TABLE_NODE, father);
			father.add(ctn);
		}
		return true;
	}
}
