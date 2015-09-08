package com.wangboo.batchSql.comp;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.wangboo.batchSql.MainFrame;

/**
 * sql执行区域
 * 
 * @author YXY
 * @date 2015年9月6日 下午4:58:16
 */
public class ExecSqlView extends JPanel {
	private static final long serialVersionUID = 1L;

	private MainFrame mainFrame;
	private final Color backColor = new Color(180, 205, 230);

	// 服务器选择区域-左侧
	private ServerSlectView ssv;

	// 右侧
	private JPanel rpane;
	// 输入区域-右上
	private JTextArea sqlArea;
	private JScrollPane sqlScroll;
	// 确认执行-右中
	private SqlButtonView sev;
	// 执行结果-右下
	private SqlResultView srv;

	private List<Map<String, Object>> servers;

	public static final int textAreaX = 50;
	public static final int textAreaY = 10;

	public ExecSqlView(MainFrame mainFrame, List<Map<String, Object>> servers) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.mainFrame = mainFrame;
		this.servers = servers;
		// 左侧
		ssv = new ServerSlectView(servers);
		add(ssv);
		// 右侧
		rpane = new JPanel();
		rpane.setLayout(new BoxLayout(rpane, BoxLayout.Y_AXIS));

		sqlArea = new JTextArea(textAreaY, textAreaX);
		sqlArea.setBackground(backColor);
		sqlArea.setLineWrap(true);
		sqlArea.setWrapStyleWord(true);
		sqlArea.setFont(new Font("Default", Font.PLAIN, 20));
		sqlScroll = new JScrollPane(sqlArea);
		sqlScroll.setBorder(BorderFactory.createTitledBorder("输入要执行的sql"));
		rpane.add(sqlScroll);

		sev = new SqlButtonView(this);
		rpane.add(sev);

		srv = new SqlResultView(this);
		rpane.add(srv);

		add(rpane);
		setVisible(true);
	}

	public JTextArea getSqlArea() {
		return sqlArea;
	}

	public ServerSlectView getSsv() {
		return ssv;
	}

	public SqlResultView getSrv() {
		return srv;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public List<Map<String, Object>> getServers() {
		return servers;
	}

	public void refreshServers(List<Map<String, Object>> servers) {
		this.servers = servers;
		ssv.setServers(servers);
		ssv.refreshServers();
	}

}
