package com.wangboo.batchSql.comp;

import java.awt.Color;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 服务器选项
 * 
 * @author YXY
 * @date 2015年9月7日 上午10:15:06
 */
public class ServerCheckItem extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Icon icon = new ImageIcon("imgs/server.jpg");

	// UI
	private JCheckBox cbox;
	private JLabel label;
	private JLabel ilabel;

	// DATA
	private int id;
	private String serverName;
	private String mysqlUrl;
	private Map<String, Object> data;

	public ServerCheckItem(Map<String, Object> data) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.id = (int) data.get("id");
		this.data = data;
		String name = (String) data.get("name");
		String ip = (String) data.get("host");
		String port = String.valueOf(data.get("port"));
		String db = (String) data.get("db");
		this.mysqlUrl = "jdbc:mysql://" + ip + ":" + port + "/" + db;

		this.serverName = name;
		cbox = new JCheckBox(serverName);
		label = new JLabel(icon);
		ilabel = new JLabel("<" + ip + ">");
		ilabel.setForeground(new Color(66, 150, 33));
		add(label);
		add(cbox);
		add(ilabel);
	}

	public String getServerName() {
		return serverName;
	}

	public String getMysqlUrl() {
		return mysqlUrl;
	}

	public String getUser() {
		return (String) data.get("user");
	}

	public String getPwd() {
		return (String) data.get("pwd");
	}

	public int getId() {
		return id;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setSelected(boolean selected) {
		cbox.setSelected(selected);
	}

	public boolean isSelected() {
		return cbox.isSelected();
	}

	public void invertSelected() {
		this.cbox.setSelected(!this.cbox.isSelected());
	}

	public JCheckBox getCbox() {
		return cbox;
	}

	public JLabel getLabel() {
		return label;
	}

	public String toString() {
		return serverName;
	}
}
