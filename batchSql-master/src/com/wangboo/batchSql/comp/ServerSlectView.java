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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * 服务器选择菜单
 * 
 * @author YXY
 * @date 2015年9月6日 下午5:52:52
 */
public class ServerSlectView extends JPanel implements
		ListCellRenderer<ServerCheckItem>, ActionListener {
	private static final long serialVersionUID = 1L;

	private JList<ServerCheckItem> list;
	private JScrollPane jsPane;

	private JPanel bpane;
	private JButton all;
	private JButton cancel;

	private ServerCheckItem[] jb;
	private List<Map<String, Object>> servers;

	public ServerSlectView(List<Map<String, Object>> servers) {
		this.servers = servers;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("选择要执行的服务器"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		refreshServers();
	}

	public void refreshServers() {
		this.removeAll();
		ServerCheckItem[] options = new ServerCheckItem[servers.size()];
		int i = 0;
		for (Map<String, Object> map : servers) {
			System.err.println(map);
			options[i] = new ServerCheckItem(map);
			i++;
		}
		list = new JList<ServerCheckItem>(options);
		list.setCellRenderer(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CheckListener cls = new CheckListener(this);
		list.addMouseListener(cls);

		jsPane = new JScrollPane();
		jsPane.getViewport().add(list);
		jsPane.setPreferredSize(new Dimension(200, 1000));
		add(jsPane);

		all = new JButton("选择全部");
		all.addActionListener(this);
		cancel = new JButton("取消选择");
		cancel.addActionListener(this);
		bpane = new JPanel();
		bpane.setLayout(new BoxLayout(bpane, BoxLayout.X_AXIS));
		bpane.add(new Label());
		bpane.add(all);
		bpane.add(new Label());
		bpane.add(cancel);
		bpane.add(new Label());
		add(bpane);

		setVisible(true);
	}

	public List<Map<String, Object>> getSelectedServers() {
		List<Map<String, Object>> res = new ArrayList<>();
		ListModel<ServerCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			ServerCheckItem data = (ServerCheckItem) model.getElementAt(k);
			if (data.isSelected()) {
				System.err.println(data.getData());
				res.add(data.getData());
				break;
			}
		}
		return res;
	}

	public List<Map<String, Object>> getServers() {
		return servers;
	}

	public void setServers(List<Map<String, Object>> servers) {
		this.servers = servers;
	}

	public ServerCheckItem[] getJb() {
		return jb;
	}

	public JList<ServerCheckItem> getList() {
		return list;
	}

	public boolean isSelected() {
		boolean isSlected = false;
		ListModel<ServerCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			ServerCheckItem data = (ServerCheckItem) model.getElementAt(k);
			if (data.isSelected()) {
				isSlected = true;
				break;
			}
		}
		return isSlected;
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends ServerCheckItem> list, ServerCheckItem value,
			int index, boolean isSelected, boolean cellHasFocus) {
		ServerCheckItem sci = (ServerCheckItem) value;
		Color backColor = isSelected ? list.getSelectionBackground() : list
				.getBackground();
		Color foreColor = isSelected ? list.getSelectionForeground() : list
				.getForeground();
		sci.setBackground(backColor);
		sci.getCbox().setBackground(backColor);
		sci.setForeground(foreColor);
		sci.getCbox().setForeground(foreColor);
		sci.setFont(list.getFont());
		return sci;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == all) {
			setSelected(true);
		} else if (e.getSource() == cancel) {
			setSelected(false);
		}
	}

	private void setSelected(boolean cmd) {
		ListModel<ServerCheckItem> model = list.getModel();
		for (int k = 0; k < model.getSize(); k++) {
			ServerCheckItem data = (ServerCheckItem) model.getElementAt(k);
			data.getCbox().setSelected(cmd);
		}
		list.repaint();
	}

	class CheckListener implements MouseListener {
		private ServerSlectView parent;
		private JList<ServerCheckItem> list;

		public CheckListener(ServerSlectView parent) {
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

		public ServerSlectView getParent() {
			return parent;
		}

		public JList<ServerCheckItem> getList() {
			return list;
		}

		public void doCheck() {
			int index = list.getSelectedIndex();
			if (index < 0)
				return;
			ServerCheckItem data = list.getModel().getElementAt(index);
			data.invertSelected();
			list.repaint();
		}
	}

}
