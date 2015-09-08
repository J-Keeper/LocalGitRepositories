package com.wangboo.batchSql;

import java.awt.Toolkit;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.wangboo.batchSql.comp.DataTransferView;
import com.wangboo.batchSql.comp.ExecSqlView;
import com.wangboo.batchSql.comp.ServerConfigView;
import com.wangboo.batchSql.comp.ServerTree;
import com.wangboo.batchSql.util.DataUtil;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	// private static final Logger log = Logger.getLogger(MainFrame.class);

	/** 选项卡 */
	private JTabbedPane tabbedPane;

	/** 服务器配置选项 */
	private ServerConfigView firstPane;
	/** sql执行选项 */
	private ExecSqlView secondPane;
	/** 数据库同步选项 */
	private DataTransferView thirdPane;

	public int height;
	public int width;

	public MainFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		int screenWidth = kit.getScreenSize().width;
		int sceenHeight = kit.getScreenSize().height;
		width = (int) (screenWidth / 1.5);
		height = (int) (sceenHeight / 1.5);
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane();
		firstPane = new ServerConfigView(this);
		List<Map<String, Object>> loadServers = DataUtil.loadServers();
		secondPane = new ExecSqlView(this, loadServers);
		thirdPane = new DataTransferView(this, loadServers);
		tabbedPane.addTab("服务器配置", null, firstPane, "配置服务器参数");
		tabbedPane.addTab("执行sql", null, secondPane, "执行指定的sql语句");
		tabbedPane.addTab("数据同步", null, thirdPane, "数据库之间数据的传输");

		add(tabbedPane);

		// 服务器左侧列表区域
		// leftContent = new JPanel();
		// this.add(leftContent);
		// tree = new ServerTree(TreeNodeInitFactory.createFatherTreeNode(),
		// this);
		// leftContent.add(tree);
		// add(leftContent, BorderLayout.WEST);
		//
		// // 右侧执行sql区域
		// sqlContent = new ExecSqlView(this, DataUtil.loadServers());
		// sqlContent.setPreferredSize(new Dimension(600, 600));
		// add(sqlContent, BorderLayout.CENTER);

	}

	public void showFrame() {
		setVisible(true);
	}

	public void reloadOtherData(List<Map<String, Object>> data) {
		getSqlContent().refreshServers(data);
		getThirdPane().init(data);
	}

	public ServerTree getTree() {
		return firstPane.getTree();
	}

	public ExecSqlView getSqlContent() {
		return secondPane;
	}

	public DataTransferView getThirdPane() {
		return thirdPane;
	}

}
