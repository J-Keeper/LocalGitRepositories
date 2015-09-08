package test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class JCheckBoxTree extends JTree {

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		CheckNode root = new CheckNode("Root");
		CheckNode child1 = new CheckNode("child1");
		CheckNode child2 = new CheckNode("child2");
		child2.add(new CheckNode("child3"));
		child1.add(new CheckNode("child4"));
		root.add(child1);
		root.add(child2);
		JCheckBoxTree boxTree = new JCheckBoxTree(root);
		JFrame frame = new JFrame();
		frame.add(boxTree);
		frame.setVisible(true);
		frame.setSize(300, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static final long serialVersionUID = 1L;

	public JCheckBoxTree(CheckNode checkNode) {
		super(checkNode);
		this.setCellRenderer(new CheckRenderer());
		this.setShowsRootHandles(true);
		this.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.putClientProperty("JTree.lineStyle", "Angled");
		this.addLister(this);
	}

	/***
	 * 添加点击事件使其选中父节点时 子节点也选中
	 * 
	 * @param tree
	 */
	private void addLister(final JTree tree) {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int row = tree.getRowForLocation(e.getX(), e.getY());
				TreePath path = tree.getPathForRow(row);
				if (path != null) {
					CheckNode node = (CheckNode) path.getLastPathComponent();
					node.setSelected(!node.isSelected);
					if (node.getSelectionMode() == CheckNode.DIG_IN_SELECTION) {
						if (node.isSelected) {
							tree.expandPath(path);
						} else {
							if (!node.isRoot()) {
								tree.collapsePath(path);
							}
						}
					}
					// 响应事件更新树
					((DefaultTreeModel) tree.getModel()).nodeChanged(node);
					tree.revalidate();
					tree.repaint();
				}
			}

		});
	}

	private class CheckRenderer extends JPanel implements TreeCellRenderer {
		private static final long serialVersionUID = 1L;

		protected JCheckBox check;

		protected TreeLabel label;

		public CheckRenderer() {
			setLayout(null);
			add(check = new JCheckBox());
			add(label = new TreeLabel());
			check.setBackground(UIManager.getColor("Tree.textBackground"));
			label.setForeground(UIManager.getColor("Tree.textForeground"));
		}

		/**
		 * 改变的节点的为JLabel和JChekBox的组合
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean isSelected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			String stringValue = tree.convertValueToText(value, isSelected,
					expanded, leaf, row, hasFocus);
			setEnabled(tree.isEnabled());
			check.setSelected(((CheckNode) value).isSelected());
			label.setFont(tree.getFont());
			label.setText(stringValue);
			label.setSelected(isSelected);
			label.setFocus(hasFocus);
			if (leaf) {
				// label.setIcon(UIManager.getIcon("Tree.leafIcon"));
				label.setIcon(null);// 把leaf前的图片去掉
			} else if (expanded) {
				label.setIcon(UIManager.getIcon("Tree.openIcon"));
			} else {
				label.setIcon(UIManager.getIcon("Tree.closedIcon"));
			}
			return this;
		}

		public Dimension getPreferredSize() {
			Dimension d_check = check.getPreferredSize();
			Dimension d_label = label.getPreferredSize();
			return new Dimension(d_check.width + d_label.width,
					(d_check.height < d_label.height ? d_label.height
							: d_check.height));
		}

		public void doLayout() {
			Dimension d_check = check.getPreferredSize();
			Dimension d_label = label.getPreferredSize();
			int y_check = 0;
			int y_label = 0;
			if (d_check.height < d_label.height) {
				y_check = (d_label.height - d_check.height) / 2;
			} else {
				y_label = (d_check.height - d_label.height) / 2;
			}
			check.setLocation(0, y_check);
			check.setBounds(0, y_check, d_check.width, d_check.height);
			label.setLocation(d_check.width, y_label);
			label.setBounds(d_check.width, y_label, d_label.width,
					d_label.height);
		}

		public void setBackground(Color color) {
			if (color instanceof ColorUIResource)
				color = null;
			super.setBackground(color);
		}

		private class TreeLabel extends JLabel {
			private static final long serialVersionUID = 1L;

			private boolean isSelected;

			private boolean hasFocus;

			public TreeLabel() {
			}

			public void setBackground(Color color) {
				if (color instanceof ColorUIResource)
					color = null;
				super.setBackground(color);
			}

			public void paint(Graphics g) {
				String str;
				if ((str = getText()) != null) {
					if (0 < str.length()) {
						if (isSelected) {
							g.setColor(UIManager
									.getColor("Tree.selectionBackground"));
						} else {
							g.setColor(UIManager
									.getColor("Tree.textBackground"));
						}
						Dimension d = getPreferredSize();
						int imageOffset = 0;
						Icon currentI = getIcon();
						if (currentI != null) {
							imageOffset = currentI.getIconWidth()
									+ Math.max(0, getIconTextGap() - 1);
						}
						g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
								d.height);
						if (hasFocus) {
							g.setColor(UIManager
									.getColor("Tree.selectionBorderColor"));
							g.drawRect(imageOffset, 0, d.width - 1
									- imageOffset, d.height - 1);
						}
					}
				}
				super.paint(g);
			}

			public Dimension getPreferredSize() {
				Dimension retDimension = super.getPreferredSize();
				if (retDimension != null) {
					retDimension = new Dimension(retDimension.width + 3,
							retDimension.height);
				}
				return retDimension;
			}

			public void setSelected(boolean isSelected) {
				this.isSelected = isSelected;
			}

			public void setFocus(boolean hasFocus) {
				this.hasFocus = hasFocus;
			}
		}
	}

	public static class CheckNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 1L;

		public final static int SINGLE_SELECTION = 0;

		public final static int DIG_IN_SELECTION = 4;

		protected int selectionMode;

		protected boolean isSelected;

		public CheckNode() {
			this(null);
		}

		public CheckNode(Object userObject) {
			this(userObject, true, false);
		}

		public CheckNode(Object userObject, boolean allowsChildren,
				boolean isSelected) {
			super(userObject, allowsChildren);
			this.isSelected = isSelected;
			setSelectionMode(DIG_IN_SELECTION);
		}

		public void setSelectionMode(int mode) {
			selectionMode = mode;
		}

		public int getSelectionMode() {
			return selectionMode;
		}

		/**
		 * 选中父节点时也级联选中子节点
		 * 
		 * @param isSelected
		 */
		@SuppressWarnings("unchecked")
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
			if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
				Enumeration e = children.elements();
				while (e.hasMoreElements()) {
					CheckNode node = (CheckNode) e.nextElement();
					node.setSelected(isSelected);
				}
			}
		}

		public boolean isSelected() {
			return isSelected;
		}
	}
}