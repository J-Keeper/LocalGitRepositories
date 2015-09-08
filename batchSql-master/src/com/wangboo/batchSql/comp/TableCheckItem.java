package com.wangboo.batchSql.comp;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 数据表选项
 * 
 * @Description: TODO
 * @author YongXinYu
 * @date 2015年9月8日 下午11:01:25
 */
public class TableCheckItem extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Icon icon = new ImageIcon("imgs/server.jpg");

	// UI
	private JLabel label;
	private JCheckBox cbox;

	// DATA
	private String tableName;

	public TableCheckItem(String tableName) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.tableName = tableName;
		cbox = new JCheckBox(tableName);
		label = new JLabel(icon);
		add(label);
		add(cbox);
	}

	public JLabel getLabel() {
		return label;
	}

	public JCheckBox getCbox() {
		return cbox;
	}

	public String getTableName() {
		return tableName;
	}

	public void setSelected(boolean selected) {
		cbox.setSelected(selected);
	}

	public boolean isSelected() {
		return cbox.isSelected();
	}

	public void invertSelected() {
		this.cbox.setSelected(!this.cbox.isSelected());
	}

	public String toString() {
		return tableName;
	}
}
