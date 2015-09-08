package com.wangboo.batchSql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class TreeWin extends JFrame implements MouseListener, ActionListener {
	JTree tree;

	JPopupMenu popMenu;

	JMenuItem addItem;
	JMenuItem delItem;
	JMenuItem editItem;

	public TreeWin() {
		tree = new JTree();
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(this);
		// 设置单元格可编辑
		tree.setCellEditor(new DefaultTreeCellEditor(tree,
				new DefaultTreeCellRenderer()));
		getContentPane().add(tree);
		setSize(200, 200);

		popMenu = new JPopupMenu();
		addItem = new JMenuItem("添加");
		addItem.addActionListener(this);
		delItem = new JMenuItem("删除");
		delItem.addActionListener(this);
		editItem = new JMenuItem("修改");
		editItem.addActionListener(this);

		popMenu.add(addItem);
		popMenu.add(delItem);
		popMenu.add(editItem);

		getContentPane().add(new JScrollPane(tree));
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
		if (path == null) {
			return;
		}
		tree.setSelectionPath(path);

		if (e.getButton() == 3) {
			System.err.println("点击了右键");
			popMenu.show(tree, e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (e.getSource() == addItem) {
			((DefaultTreeModel) tree.getModel()).insertNodeInto(
					new DefaultMutableTreeNode("Test"), node,
					node.getChildCount());
			tree.expandPath(tree.getSelectionPath());
		} else if (e.getSource() == delItem) {
			if (node.isRoot()) {
				return;
			}
			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
		} else if (e.getSource() == editItem) {
			tree.startEditingAtPath(tree.getSelectionPath());
		}
	}

	public static void main(String[] args) {
		TreeWin frame = new TreeWin();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}