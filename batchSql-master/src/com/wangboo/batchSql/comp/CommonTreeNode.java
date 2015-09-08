package com.wangboo.batchSql.comp;

import java.util.List;
import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 节点组件
 * 
 * @author YXY
 * @date 2015年9月2日 下午3:48:03
 */
public class CommonTreeNode extends DefaultMutableTreeNode implements
		TreeSelectionListener {
	private static final long serialVersionUID = 1L;

	/** 父节点 */
	private CommonTreeNode ct;

	/** 节点类型 0-父节点,1-服务器节点,2-数据库节点,3-表名节点 */
	private int type;
	/** 节点名称 */
	private String name;
	/** 节点图标 */
	private String icon;

	/** 所有服务器配置,用于根节点 */
	private List<Map<String, Object>> servers;
	/** 节点数据，用于服务器节点 */
	private Map<String, Object> server;
	/** 数据表名,用于数据库节点 */
	private List<String> tables;

	public CommonTreeNode(String name, int type, CommonTreeNode ct) {
		super(name);
		this.type = type;
		this.name = name;
		// this.icon="";
		this.ct = ct;
	}

	public CommonTreeNode(String name) {
		super(name);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Map<String, Object> getServer() {
		return server;
	}

	public void setServer(Map<String, Object> server) {
		this.server = server;
	}

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}

	public CommonTreeNode getCt() {
		return ct;
	}

	public void setCt(CommonTreeNode ct) {
		this.ct = ct;
	}

	public List<Map<String, Object>> getServers() {
		return servers;
	}

	public void setServers(List<Map<String, Object>> servers) {
		this.servers = servers;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
