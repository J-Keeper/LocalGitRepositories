package com.wangboo.batchSql.comp;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.wangboo.batchSql.MainFrame;

/**
 * 数据传输页面
 * 
 * @author YXY
 * @date 2015年9月7日 下午3:59:55
 */
public class DataTransferView extends JPanel {
	private static final long serialVersionUID = 1L;

	private MainFrame mainFrame;

	private DataSourceSelectView dsv;
	private TransferButtons tbs;
	private ServerSlectView ssv;
	private SqlResultView srv;

	private JPanel right;
	private JPanel right_up;
	private JPanel right_botm;

	public DataTransferView(MainFrame mainFrame,
			List<Map<String, Object>> servers) {
		this.mainFrame = mainFrame;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.init(servers);
	}

	public void init(List<Map<String, Object>> servers) {
		this.removeAll();
		dsv = new DataSourceSelectView(this);
		dsv.init(servers);
		add(dsv);

		right_up = new JPanel();
		right_up.setLayout(new BoxLayout(right_up, BoxLayout.X_AXIS));
		tbs = new TransferButtons(this);
		right_up.add(tbs);

		ssv = new ServerSlectView(servers);
		right_up.add(ssv);
		right_up.setBorder(new EmptyBorder(5, 5, 5, 5));

		right_botm = new JPanel();
		srv = new SqlResultView(this);
		setPreferredSize(new Dimension(right_up.getSize().width, 500));
		right_botm.add(srv);

		right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.add(right_up);
		right.add(right_botm);

		add(right);
		setVisible(true);
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public SqlResultView getSrv() {
		return srv;
	}

	public DataSourceSelectView getDsv() {
		return dsv;
	}

	public TransferButtons getTbs() {
		return tbs;
	}

	public ServerSlectView getSsv() {
		return ssv;
	}

	public void setSrv(SqlResultView srv) {
		this.srv = srv;
	}

}
