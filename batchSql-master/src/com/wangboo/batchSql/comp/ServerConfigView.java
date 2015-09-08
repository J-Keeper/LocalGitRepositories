package com.wangboo.batchSql.comp;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.wangboo.batchSql.MainFrame;
import com.wangboo.batchSql.util.TreeNodeInitFactory;

public class ServerConfigView extends JPanel {
	private static final long serialVersionUID = 1L;

	private MainFrame mainFrame;
	private JScrollPane jsPanel;
	private ServerTree tree;

	public ServerConfigView(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());
		tree = new ServerTree(TreeNodeInitFactory.createFatherTreeNode(),
				mainFrame);
		jsPanel = new JScrollPane(tree);
		add(jsPanel, BorderLayout.WEST);
		setVisible(true);
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public ServerTree getTree() {
		return tree;
	}

}
