package com.wangboo.batchSql.comp;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.wangboo.batchSql.MainFrame;
import com.wangboo.batchSql.mysql.MysqlClient;
import com.wangboo.batchSql.mysql.MysqlTestConnectionCallback;
import com.wangboo.batchSql.util.DataUtil;
import com.wangboo.batchSql.util.StringUtils;
import com.wangboo.batchSql.util.TreeNodeInitFactory;

public class ServerTree extends JTree implements MouseListener, ActionListener,
		MysqlTestConnectionCallback {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ServerTree.class);

	private MainFrame jframe;

	/** 添加服务器菜单 */
	private JPopupMenu addServerMenu;
	/** 编辑服务器菜单 */
	private JPopupMenu editServerMenu;
	/** 刷新 */
	private JPopupMenu refreshMenu;

	/** 添加服务器 */
	private JMenuItem addServer;
	/** 编辑服务器 */
	private JMenuItem editServer;
	/** 删除服务器 */
	private JMenuItem deleteServer;
	/** 测试连接 */
	private JMenuItem testConn;
	/** 刷新 */
	private JMenuItem refreshDb;

	/** 根节点 */
	public static CommonTreeNode father;

	private final int TREEWIDTH = 280;
	private final int TREEHEIGHT = 1000;

	public ServerTree(CommonTreeNode dft, MainFrame jframe) {
		super(dft);
		father = dft;
		this.jframe = jframe;
		setPreferredSize(new Dimension(TREEWIDTH, TREEHEIGHT));
		initChildNodes();
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addMouseListener(this);
		// 设置单元格可编辑
		this.setCellEditor(new DefaultTreeCellEditor(this,
				new DefaultTreeCellRenderer()));

		addServerMenu = new JPopupMenu();
		editServerMenu = new JPopupMenu();
		refreshMenu = new JPopupMenu();

		addServer = new JMenuItem("添加服务器");
		addServer.addActionListener(this);
		addServerMenu.add(addServer);

		editServer = new JMenuItem("修改配置");
		editServer.addActionListener(this);
		deleteServer = new JMenuItem("删除服务器");
		deleteServer.addActionListener(this);
		testConn = new JMenuItem("测试连接");
		testConn.addActionListener(this);

		editServerMenu.add(editServer);
		editServerMenu.add(deleteServer);
		editServerMenu.add(testConn);

		refreshDb = new JMenuItem("刷新");
		refreshDb.addActionListener(this);
		refreshMenu.add(refreshDb);
	}

	/**
	 * 初始化子节点
	 * 
	 * @return void
	 * @throws
	 */
	public void initChildNodes() {
		// 加载根节点
		List<Map<String, Object>> servers = DataUtil.loadServers();
		father.setServers(servers);
		for (Map<String, Object> data : servers) {
			if (data == null || data.size() == 0) {
				continue;
			}
			// 添加服务器节点
			String serverName = (String) data.get("name");
			CommonTreeNode serverNode = TreeNodeInitFactory
					.createServerTreeNode(father, serverName, data);
			String dataBaseName = (String) data.get("db");
			TreeNodeInitFactory.createDataBaseTreeNode(serverNode,
					dataBaseName, data);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CommonTreeNode node = (CommonTreeNode) this
				.getLastSelectedPathComponent();
		// 根节点-加服务器
		if (node.getType() == TreeNodeInitFactory.FATHER_NODE) {
			if (e.getSource() == addServer) {
				NewServerDialog nsd = new NewServerDialog(jframe);
				nsd.setVisible(true);
			}
		} else if (node.getType() == TreeNodeInitFactory.SERVER_NODE) {// 服务器节点
			if (e.getSource() == editServer) {
				Map<String, Object> data = node.getServer();
				ServerEditDialog sed = new ServerEditDialog(jframe, data, node);
				sed.setVisible(true);
				jframe.reloadOtherData(DataUtil.loadServers());
			} else if (e.getSource() == deleteServer) {
				int id = (int) node.getServer().get("id");
				DataUtil.deleteServer(id);
				deleteNode(node);
			} else if (e.getSource() == testConn) {
				Map<String, Object> data = node.getServer();
				MysqlClient.testConnection(this, StringUtils.getSqlUrl(data),
						(String) data.get("user"), (String) data.get("pwd"));
			}
		} else if (node.getType() == TreeNodeInitFactory.DATABASE_NODE) {
			if (e.getSource() == refreshDb) {
				initTables(node);
				((DefaultTreeModel) this.getModel()).reload(node);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// 设置选中目标路径
		TreePath path = this.getPathForLocation(e.getX(), e.getY());
		if (path == null) {
			return;
		}
		this.setSelectionPath(path);
		CommonTreeNode node = (CommonTreeNode) this
				.getLastSelectedPathComponent();
		// 鼠标右键按下事件处理
		if (e.getButton() == 3) {
			if (node.getType() == TreeNodeInitFactory.FATHER_NODE) {
				addServerMenu.show(this, e.getX(), e.getY());
			} else if (node.getType() == TreeNodeInitFactory.SERVER_NODE) {
				editServerMenu.show(this, e.getX(), e.getY());
			} else if (node.getType() == TreeNodeInitFactory.DATABASE_NODE) {
				refreshMenu.show(this, e.getX(), e.getY());
			}
		} else if (e.getClickCount() == 2) {// 鼠标连击事件
			if (node.getType() == TreeNodeInitFactory.DATABASE_NODE) {
				initTables(node);
			}
		}
	}

	/**
	 * 添加服务器节点
	 * 
	 * @param ctn
	 *            void
	 * @throws
	 * @author YXY
	 */
	public void addServerNode(CommonTreeNode ctn) {
		((DefaultTreeModel) this.getModel()).insertNodeInto(ctn,
				ServerTree.father, ServerTree.father.getChildCount());
		this.expandPath(this.getSelectionPath());
		// 更新服务器选择区域
		jframe.reloadOtherData(DataUtil.loadServers());
	}

	public void deleteNode(CommonTreeNode node) {
		if (node.isRoot()) {
			return;
		}
		((DefaultTreeModel) this.getModel()).removeNodeFromParent(node);
		// 更新服务器选择区域
		jframe.reloadOtherData(DataUtil.loadServers());
	}

	private void initTables(CommonTreeNode node) {
		List<String> tables = DataUtil.getTables(node.getServer());
		node.removeAllChildren();
		for (String name : tables) {
			TreeNodeInitFactory.createTableTreeNode(node, name);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	public MainFrame getJframe() {
		return jframe;
	}

	public void setJframe(MainFrame jframe) {
		this.jframe = jframe;
	}

	public JPopupMenu getAddServerMenu() {
		return addServerMenu;
	}

	public void setAddServerMenu(JPopupMenu addServerMenu) {
		this.addServerMenu = addServerMenu;
	}

	public JPopupMenu getEditServerMenu() {
		return editServerMenu;
	}

	public void setEditServerMenu(JPopupMenu editServerMenu) {
		this.editServerMenu = editServerMenu;
	}

	public JMenuItem getAddServer() {
		return addServer;
	}

	public void setAddServer(JMenuItem addServer) {
		this.addServer = addServer;
	}

	public JMenuItem getEditServer() {
		return editServer;
	}

	public void setEditServer(JMenuItem editServer) {
		this.editServer = editServer;
	}

	public JMenuItem getDeleteServer() {
		return deleteServer;
	}

	public void setDeleteServer(JMenuItem deleteServer) {
		this.deleteServer = deleteServer;
	}

	@Override
	public void testConnectionCallback(boolean isConnected, String failDesc) {
		log.debug("testConnectionCallback: isConnected : " + isConnected
				+ ", failDesc = " + failDesc);
		if (isConnected) {
			CommonDialog.showErrorDialog("连接正常");
		} else {
			CommonDialog.showErrorDialog("连接 失败");
		}
	}

}
