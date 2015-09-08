package test;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class WorkTree {

	JTree newtr = null;

	DefaultTreeModel treemodel = null;

	public String NewNodeName;

	public static void main(String[] args) {
		new WorkTree();
	}

	public WorkTree() {

		// 生成树结构
		// JCheckBox chbox = new JCheckBox("aa");
		DefaultMutableTreeNode treecode = new DefaultMutableTreeNode("d://",
				true);
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Windows");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Drivers");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("System32");
		treecode.add(node1);
		treecode.add(node2);
		node1.add(node3);
		DefaultMutableTreeNode node4 = new DefaultMutableTreeNode("Config.sys");
		DefaultMutableTreeNode node5 = new DefaultMutableTreeNode("Boot.ini");
		DefaultMutableTreeNode node6 = new DefaultMutableTreeNode(
				"Explorer.exe");
		DefaultMutableTreeNode node7 = new DefaultMutableTreeNode("Regedit.exe");
		DefaultMutableTreeNode node8 = new DefaultMutableTreeNode("Sound");
		DefaultMutableTreeNode node9 = new DefaultMutableTreeNode("VGA");
		DefaultMutableTreeNode node10 = new DefaultMutableTreeNode(
				"ipconfig.exe");
		DefaultMutableTreeNode node11 = new DefaultMutableTreeNode("net.exe");

		treecode.add(node4);
		treecode.add(node5);
		node1.add(node6);
		node1.add(node7);
		node2.add(node8);
		node2.add(node9);
		node3.add(node10);
		node3.add(node11);
		newtr = new JTree(treecode);
		CheckboxTreeRender cbtr = new CheckboxTreeRender();
		newtr.setCellRenderer(cbtr);
		newtr.setRowHeight(20);
		// newtr.add(chbox);
		treemodel = (DefaultTreeModel) newtr.getModel();

		// MyTreeCellRenderer newTreeNode = new MyTreeCellRenderer();
		// newTreeNode.

		// 将所有结点展开
		expandAll(newtr, new TreePath(newtr.getModel().getRoot()));

		// 添加树的选择事件
		newtr.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				// try {
				JTree tree = (JTree) e.getSource();
				CheckboxTreeRender ct = (CheckboxTreeRender) tree
						.getCellRenderer();
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				// Data data = (Data) ((DefaultMutableTreeNode)
				// e.getSource()).getUserObject();
				// } catch (Exception E) {
				// return;
				// }
			}
		});

		// 生成面板
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JScrollPane(newtr));
		// panel.add(new JScrollPane(new MyTreeCellRenderer()));

		JFrame frame = new JFrame("TreeWork");
		frame.setSize(300, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		// frame.pack();
		frame.setVisible(true);
	}

	// 展开所有结点
	public static void expandAll(JTree tree, TreePath path) {
		// assert (tree != null) && (path != null);
		tree.expandPath(path);
		TreeNode node = (TreeNode) path.getLastPathComponent();
		for (Enumeration i = node.children(); i.hasMoreElements();) {
			expandAll(tree, path.pathByAddingChild(i.nextElement()));
		}
	}

	class CheckboxTreeRender extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			JCheckBox box = new JCheckBox();
			box.setText(value.toString());
			box.setSelected(sel);
			// box.setBackground(Color.white);
			return box;
		}
	}
}
