package com.wangboo.batchSql.comp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.wangboo.batchSql.MainFrame;
import com.wangboo.batchSql.mysql.MysqlClient;
import com.wangboo.batchSql.mysql.MysqlTestConnectionCallback;
import com.wangboo.batchSql.util.DataUtil;
import com.wangboo.batchSql.util.StringUtils;
import com.wangboo.batchSql.util.TreeNodeInitFactory;

/**
 * 新建服务器对话框
 * 
 * @author wangboo
 * 
 */
public class NewServerDialog extends JDialog implements ActionListener,
		MysqlTestConnectionCallback {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(NewServerDialog.class);
	private static final int LABEL_HEIGHT = 50;
	private static final int LABEL_WIDTH = 380;

	private LabelAndInputFieldView serverNameView;
	private LabelAndInputFieldView ipView;
	private LabelAndInputFieldView portView;
	private LabelAndInputFieldView dbView;
	private LabelAndInputFieldView userView;
	private LabelAndInputFieldView pwdView;

	private MainFrame mainFrame;
	private JPanel layout;
	private FlowLayout flowLayout;

	private JButton testConnBtn, submitBtn;

	public NewServerDialog(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400, 360);
		// 设置布局
		flowLayout = new FlowLayout(FlowLayout.LEADING, 10, 5);
		layout = new JPanel(flowLayout);
		setContentPane(layout);
		serverNameView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT,
				"    连接名");
		ipView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT,
				"  数据库ip");
		portView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT,
				"        端口");
		dbView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT, "  数据库名");
		userView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT,
				"     用户名");
		pwdView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT,
				"        密码");
		layout.add(serverNameView);
		layout.add(ipView);
		layout.add(portView);
		layout.add(dbView);
		layout.add(userView);
		layout.add(pwdView);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		testConnBtn = new JButton("测试连接");
		submitBtn = new JButton("添加");
		panel.setSize(LABEL_WIDTH, LABEL_HEIGHT);
		panel.add(testConnBtn, BorderLayout.WEST);
		panel.add(submitBtn, BorderLayout.EAST);

		testConnBtn.addActionListener(this);
		submitBtn.addActionListener(this);
		submitBtn.setVisible(false);
		layout.add(panel);

		// 设置居中显示
		Container myParent = mainFrame.getContentPane();
		Point topLeft = myParent.getLocationOnScreen();
		Dimension parentSize = myParent.getSize();
		Dimension mySize = this.getSize();
		int x, y;
		if (parentSize.width > mySize.width) {
			x = ((parentSize.width - mySize.width) / 2) + topLeft.x;
		} else {
			x = topLeft.x;
		}
		if (parentSize.height > mySize.height) {
			y = ((parentSize.height - mySize.height) / 2) + topLeft.y;
		} else {
			y = topLeft.y;
		}
		this.setLocation(x, y);
	}

	public void showDialog() {
		setLocationRelativeTo(mainFrame);
		setResizable(false);
		setVisible(true);
		setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 点击测试连接按钮
		if (e.getSource() == testConnBtn) {
			// 数据校验
			String ipInput = ipView.getIputString();
			String portInput = portView.getIputString();
			String dbInput = dbView.getIputString();
			String userInput = userView.getIputString();
			String pwdInput = pwdView.getIputString();
			String ip = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
			Pattern pattern = Pattern.compile(ip);
			if (StringUtils.isBlank(ipInput)) {
				CommonDialog.showErrorDialog("IP地址不能为空");
			} else if (!pattern.matcher(ipInput).matches()) {
				CommonDialog.showErrorDialog("IP地址格式不正确");
			} else if (StringUtils.isBlank(portInput)) {
				CommonDialog.showErrorDialog("端口不能为空");
			} else if (Integer.parseInt(portInput) <= 0
					|| 65535 <= Integer.parseInt(portInput)) {
				CommonDialog.showErrorDialog("端口超出范围");
			} else if (StringUtils.isBlank(dbInput)) {
				CommonDialog.showErrorDialog("数据库名称不能为空");
			} else if (StringUtils.isBlank(userInput)) {
				CommonDialog.showErrorDialog("用户名不能为空");
			} else if (StringUtils.isBlank(pwdInput)) {
				CommonDialog.showErrorDialog("密码不能为空");
			} else {
				MysqlClient.testConnection(this, getConnectionUrl(), getUser(),
						getPwd());
			}
		}
		// 点击增加服务器
		else if (e.getSource() == submitBtn) {
			DataUtil.addServer(getServerName(), getHost(), getPort(), getDB(),
					getUser(), getPwd());
			int serverId = DataUtil.getMaxId();
			String serverName = getServerName();
			if (serverName == null || serverName.trim().equals("")) {
				serverName = getHost();
			}
			CommonTreeNode serverNode = TreeNodeInitFactory
					.createCommonTreeNode(serverName);
			Map<String, Object> data = createServerData(serverId);
			serverNode.setServer(data);
			TreeNodeInitFactory.createDataBaseTreeNode(serverNode, getDB()
					.trim(), data);
			mainFrame.getTree().addServerNode(serverNode);
		}
	}

	public String getUser() {
		return userView.getIputString();
	}

	public String getPwd() {
		return pwdView.getIputString();
	}

	public String getHost() {
		return ipView.getIputString();
	}

	public int getPort() {
		return Integer.parseInt(portView.getIputString());
	}

	public String getDB() {
		return dbView.getIputString();
	}

	public String getServerName() {
		return serverNameView.getIputString();
	}

	public Map<String, Object> createServerData(int maxId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", maxId);
		map.put("name", getServerName().trim());
		map.put("host", getHost().trim());
		map.put("port", getPort());
		map.put("user", getUser().trim());
		map.put("pwd", getPwd().trim());
		map.put("db", getDB().trim());
		return map;
	}

	public String getConnectionUrl() {
		return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDB()
				+ "?useUnicode=true&characterEncoding=utf8";
	}

	@Override
	public void testConnectionCallback(boolean isConnected, String failDesc) {
		log.debug("testConnectionCallback: isConnected : " + isConnected
				+ ", failDesc = " + failDesc);
		if (isConnected) {
			CommonDialog.showOkDialog("连接成功！", "测试结果");
			this.submitBtn.setVisible(true);
		} else {
			CommonDialog.showOkDialog("连接失败：" + failDesc, "测试结果");
		}
	}

}
