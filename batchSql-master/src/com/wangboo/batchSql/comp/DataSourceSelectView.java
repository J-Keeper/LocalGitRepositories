package com.wangboo.batchSql.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DataSourceSelectView extends JPanel {
	private static final long serialVersionUID = 1L;

	private DataTransferView dtv;

	private ServerSelectBox ssb;
	private TableSelectView tsb;

	public DataSourceSelectView(DataTransferView dtv) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, 1000));
		this.setBorder(BorderFactory.createTitledBorder("源数据"));
		this.dtv = dtv;
	}

	public void init(List<Map<String, Object>> servers) {
		this.removeAll();
		ssb = new ServerSelectBox(this);
		ssb.init(servers);
		add(ssb, BorderLayout.NORTH);

		tsb = new TableSelectView();
		tsb.init(new ArrayList<String>(), "");
		add(tsb, BorderLayout.CENTER);
	}

	public DataTransferView getDtv() {
		return dtv;
	}

	public ServerSelectBox getSsb() {
		return ssb;
	}

	public TableSelectView getTsb() {
		return tsb;
	}

	public boolean serverIsSelected() {
		return ssb.isSelected();
	}

	public boolean tableIsSelected() {
		return tsb.isSelected();
	}

}
