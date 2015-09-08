package com.wangboo.batchSql.comp;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.wangboo.batchSql.mysql.MysqlClient;
import com.wangboo.batchSql.mysql.MysqlTestConnectionCallback;
import com.wangboo.batchSql.util.DataUtil;
import com.wangboo.batchSql.util.StringUtils;

/**
 * 服务器单项选择菜单
 * 
 * @Description: TODO
 * @author YongXinYu
 * @date 2015年9月8日 下午10:41:22
 */
public class ServerSelectBox extends JPanel implements ActionListener,
		MysqlTestConnectionCallback {
	private static final long serialVersionUID = 1L;

	private DataSourceSelectView dsv;

	private JLabel label;
	private JComboBox<Server> comBox;
	private List<Map<String, Object>> servers;

	private static String initSelect = "---请选择---";

	public ServerSelectBox(DataSourceSelectView dsv) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.dsv = dsv;
	}

	public void init(List<Map<String, Object>> servers) {
		this.removeAll();
		this.servers = servers;
		label = new JLabel("源服务器:");

		add(label);
		comBox = new JComboBox<>();
		comBox.setPreferredSize(new Dimension(200, 30));
		comBox.addActionListener(this);

		comBox.addItem(new Server(initSelect));
		for (Map<String, Object> data : servers) {
			Server server = new Server(data);
			comBox.addItem(server);
		}
		add(comBox);
		this.setVisible(true);
	}

	public void refreshServerBox(List<Map<String, Object>> servers) {
		this.comBox.removeAll();
		comBox.addItem(new Server("---请选择---"));
		for (Map<String, Object> data : servers) {
			Server server = new Server(data);
			comBox.addItem(server);
		}
		add(comBox);
	}

	public boolean isSelected() {
		Server server = (Server) comBox.getSelectedItem();
		if (server.getName().equals(initSelect)) {
			return false;
		}
		return true;
	}

	public Map<String, Object> getServerConf() {
		Server server = (Server) comBox.getSelectedItem();
		return server.data;
	}

	public List<Map<String, Object>> getServers() {
		return servers;
	}

	public DataSourceSelectView getDsv() {
		return dsv;
	}

	class Server {
		private String name;
		private Map<String, Object> data;

		public Server(String name) {
			this.name = name;
		}

		public Server(Map<String, Object> data) {
			this.name = (String) data.get("name");
			this.data = data;
		}

		@Override
		public String toString() {
			return name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == comBox) {
			Server server = (Server) (comBox.getSelectedItem());
			if (server.getData() == null) {
				return;
			}
			Map<String, Object> data = server.getData();
			MysqlClient.testConnection(this, StringUtils.getSqlUrl(data),
					(String) data.get("user"), (String) data.get("pwd"));
			List<String> tables = DataUtil.getTables(server.getData());
			String url = StringUtils.getSqlUrl(data);
			dsv.getTsb().init(tables, url);
		}
	}

	@Override
	public void testConnectionCallback(boolean isConnected, String failDesc) {
		if (!isConnected) {
			CommonDialog.showErrorDialog("连接失败,请检查配置");
		}
	}

}
