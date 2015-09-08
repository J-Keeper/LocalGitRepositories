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
 * 服务器编辑修改对话框
 * 
 * @author YXY
 * @date 2015年9月6日 下午1:42:26
 */
public class ServerEditDialog extends JDialog implements ActionListener,
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
	private CommonTreeNode ctn;
	private JPanel layout;
	private FlowLayout flowLayout;

	private JButton testConnBtn, submitBtn;

	private int serverId;

	public ServerEditDialog(MainFrame mainFrame, Map<String, Object> data,
			CommonTreeNode ctn) {
		this.mainFrame = mainFrame;
		this.serverId = (int) data.get("id");
		this.ctn = ctn;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400, 360);
		// 设置布局
		flowLayout = new FlowLayout(FlowLayout.LEADING, 10, 5);
		layout = new JPanel(flowLayout);
		setContentPane(layout);

		serverNameView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT,
				"连接名");
		String serverName = (String) data.get("name");
		serverNameView.getInputField().setText(serverName);
		layout.add(serverNameView);

		ipView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT, "数据库IP");
		String host = (String) data.get("host");
		ipView.getInputField().setText(host);
		layout.add(ipView);

		portView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT, "端口");
		String port = String.valueOf(data.get("port"));
		portView.getInputField().setText(port);
		layout.add(portView);

		dbView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT, "  数据库名");
		String dbName = (String) data.get("db");
		dbView.getInputField().setText(dbName);
		layout.add(dbView);

		userView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT, "用户名");
		String user = (String) data.get("user");
		userView.getInputField().setText(user);
		layout.add(userView);

		pwdView = new LabelAndInputFieldView(LABEL_WIDTH, LABEL_HEIGHT, "密码");
		String pwd = (String) data.get("pwd");
		pwdView.getInputField().setText(pwd);
		layout.add(pwdView);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		testConnBtn = new JButton("测试连接");
		submitBtn = new JButton("确认修改");
		submitBtn.setVisible(false);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// 数据校验
		String ipInput = ipView.getIputString();
		String portInput = portView.getIputString();
		String dbInput = dbView.getIputString();
		String userInput = userView.getIputString();
		String pwdInput = pwdView.getIputString();
		// 点击测试连接按钮
		if (e.getSource() == testConnBtn) {
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
		// 更新数据
		else if (e.getSource() == submitBtn) {
			DataUtil.updateServer(serverId, getServerName(), ipInput,
					Integer.parseInt(portInput), dbInput, userInput, pwdInput);
			ctn.setName(getServerName());
			mainFrame.getTree().deleteNode(ctn);
			CommonTreeNode serverNode = TreeNodeInitFactory
					.createCommonTreeNode(getServerName());
			Map<String, Object> data = createServerData();
			serverNode.setServer(data);
			TreeNodeInitFactory.createDataBaseTreeNode(serverNode, getDB()
					.trim(), data);
			mainFrame.getTree().addServerNode(serverNode);
			this.dispose();
		}
	}

	public String getConnectionUrl() {
		return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDB()
				+ "?useUnicode=true&characterEncoding=utf8";
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
		String serverName = serverNameView.getIputString().trim();
		if (serverName == null || serverName.equals("")) {
			serverName = getHost();
		}
		return serverName;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public int getServerId() {
		return serverId;
	}

	public CommonTreeNode getCtn() {
		return ctn;
	}

	public Map<String, Object> createServerData() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", serverId);
		map.put("name", getServerName().trim());
		map.put("host", getHost().trim());
		map.put("port", getPort());
		map.put("user", getUser().trim());
		map.put("pwd", getPwd().trim());
		map.put("db", getDB().trim());
		return map;
	}

	public void lockData() {
		this.submitBtn.setVisible(true);
		this.testConnBtn.setVisible(false);
		this.serverNameView.getInputField().setEditable(false);
		this.ipView.getInputField().setEditable(false);
		this.portView.getInputField().setEditable(false);
		this.dbView.getInputField().setEditable(false);
		this.userView.getInputField().setEditable(false);
		this.pwdView.getInputField().setEditable(false);
	}

	@Override
	public void testConnectionCallback(boolean isConnected, String failDesc) {
		log.debug("testConnectionCallback: isConnected : " + isConnected
				+ ", failDesc = " + failDesc);
		if (isConnected) {
			CommonDialog.showErrorDialog("连接成功，请点击确认修改");
			lockData();
		} else {
			CommonDialog.showErrorDialog("测试失败,请检查填写的内容");
		}
	}

}
