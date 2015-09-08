package com.wangboo.batchSql.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class TableSelectView extends JPanel implements
		ListCellRenderer<TableCheckItem>, ActionListener {
	private static final long serialVersionUID = 1L;

	private JList<TableCheckItem> list;
	private List<String> tableNames;
	private String url;

	private JScrollPane jsPane;
	private TableCheckItem[] tci;

	private JPanel bpane;
	private JButton all;
	private JButton cancel;

	public TableSelectView() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder("选择表"));
		setPreferredSize(new Dimension(200, 500));
	}

	public void init(List<String> tableNames, String url) {
		this.removeAll();
		this.tableNames = tableNames;
		this.url = url;

		int size = tableNames.size();
		tci = new TableCheckItem[size];
		int i = 0;
		for (String name : tableNames) {
			tci[i] = new TableCheckItem(name);
			i++;
		}
		list = new JList<TableCheckItem>(tci);
		list.setCellRenderer(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CheckListener cls = new CheckListener(this);
		list.addMouseListener(cls);
		jsPane = new JScrollPane();
		jsPane.getViewport().add(list);
		jsPane.setPreferredSize(new Dimension(400, 600));
		add(jsPane);

		bpane = new JPanel();
		bpane.setLayout(new BoxLayout(bpane, BoxLayout.X_AXIS));
		all = new JButton("选择全部");
		all.addActionListener(this);
		cancel = new JButton("取消选择");
		cancel.addActionListener(this);

		bpane.add(new Label());
		bpane.add(all);
		bpane.add(new Label());
		bpane.add(cancel);
		bpane.add(new Label());
		add(bpane);
		setVisible(true);
	}

	public void setSelect(boolean cmd) {
		ListModel<TableCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			TableCheckItem data = (TableCheckItem) model.getElementAt(k);
			data.getCbox().setSelected(cmd);
		}
		list.repaint();
	}

	public boolean isSelected() {
		boolean isSlected = false;
		ListModel<TableCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			TableCheckItem data = (TableCheckItem) model.getElementAt(k);
			if (data.isSelected()) {
				isSlected = true;
				break;
			}
		}
		return isSlected;
	}

	public boolean allSelected() {
		boolean all = true;
		ListModel<TableCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			TableCheckItem data = (TableCheckItem) model.getElementAt(k);
			if (!data.isSelected()) {
				all = false;
				break;
			}
		}
		return all;
	}

	public List<String> getSelectedNames() {
		List<String> res = new ArrayList<>();
		ListModel<TableCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			TableCheckItem data = (TableCheckItem) model.getElementAt(k);
			if (data.isSelected()) {
				res.add(data.getCbox().getText());
			}
		}
		return res;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public JList<TableCheckItem> getList() {
		return list;
	}

	public TableCheckItem[] getTci() {
		return tci;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == all) {
			setSelect(true);
		} else if (e.getSource() == cancel) {
			setSelect(false);
		}

	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends TableCheckItem> list, TableCheckItem value,
			int index, boolean isSelected, boolean cellHasFocus) {
		TableCheckItem tci = (TableCheckItem) value;
		Color backColor = isSelected ? list.getSelectionBackground() : list
				.getBackground();
		Color foreColor = isSelected ? list.getSelectionForeground() : list
				.getForeground();
		tci.setBackground(backColor);
		tci.getCbox().setBackground(backColor);
		tci.setForeground(foreColor);
		tci.getCbox().setForeground(foreColor);
		tci.setFont(list.getFont());
		return tci;
	}

	class CheckListener implements MouseListener {
		private TableSelectView parent;
		private JList<TableCheckItem> list;

		public CheckListener(TableSelectView parent) {
			this.parent = parent;
			list = parent.getList();
		}

		public void mouseClicked(MouseEvent e) {
			doCheck();
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public TableSelectView getParent() {
			return parent;
		}

		public JList<TableCheckItem> getList() {
			return list;
		}

		public void doCheck() {
			int index = list.getSelectedIndex();
			if (index < 0)
				return;
			TableCheckItem data = list.getModel().getElementAt(index);
			data.invertSelected();
			list.repaint();
		}
	}

}
