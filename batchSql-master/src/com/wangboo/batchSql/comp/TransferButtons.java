package com.wangboo.batchSql.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.wangboo.batchSql.util.DataTransferRunnable;
import com.wangboo.batchSql.util.DataUtil;

/**
 * 数据同步按钮组
 * 
 * @Description: TODO
 * @author YongXinYu
 * @date 2015年9月7日 下午8:42:31
 */
public class TransferButtons extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private DataTransferView dtv;

	// 执行
	private JButton submit;

	public TransferButtons(DataTransferView dtv) {
		this.dtv = dtv;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		submit = new JButton("开始传输>>>");
		submit.addActionListener(this);
		add(submit);
		setVisible(true);

	}

	public DataTransferView getDtv() {
		return dtv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			if (!dtv.getDsv().serverIsSelected()) {
				CommonDialog.showErrorDialog("请选择源服务器");
			} else if (!dtv.getDsv().tableIsSelected()) {
				CommonDialog.showErrorDialog("请选择要同步的表");
			} else if (!dtv.getSsv().isSelected()) {
				CommonDialog.showErrorDialog("请选择要同步的服务器");
			} else {
				// 导出数据
				exportData();
				// 开始同步
				importData();
			}
		}
	}

	private void exportData() {
		Map<String, Object> config = dtv.getDsv().getSsb().getServerConf();
		List<String> tables = dtv.getDsv().getTsb().getSelectedNames();
		boolean isAll = dtv.getDsv().getTsb().allSelected();
		DataUtil.exportSqlData(dtv.getSrv(), config, tables, isAll);
	}

	private void importData() {
		List<Map<String, Object>> list = dtv.getSsv().getSelectedServers();
		for (Map<String, Object> config : list) {
			new Thread(new DataTransferRunnable(config, dtv.getSrv())).start();
		}
	}

}
