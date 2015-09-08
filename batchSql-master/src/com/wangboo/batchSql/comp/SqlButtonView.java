package com.wangboo.batchSql.comp;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.wangboo.batchSql.util.SqlExecRunnable;
import com.wangboo.batchSql.util.StringUtils;

/**
 * sql语句执行按钮组
 * 
 * @author YongXinYu
 * @date 2015年9月7日 下午8:41:41
 */
public class SqlButtonView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private ExecSqlView esv;

	private JButton submit;
	private JButton clear;

	public SqlButtonView(ExecSqlView esv) {
		this.esv = esv;
		this.setBorder(BorderFactory.createTitledBorder("功能选择"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.submit = new JButton("立即执行");
		this.clear = new JButton("清除输入");
		submit.addActionListener(this);
		clear.addActionListener(this);

		add(new Label());
		add(submit);
		add(new Label());
		add(clear);
		add(new Label());
	}

	public ExecSqlView getEsv() {
		return esv;
	}

	public JButton getSubmit() {
		return submit;
	}

	public JButton getClear() {
		return clear;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			String sql = esv.getSqlArea().getText();
			if (StringUtils.isBlank(sql)) {
				CommonDialog.showErrorDialog("请输入要执行的sql");
				return;
			}
			if (noSelected()) {
				CommonDialog.showErrorDialog("选择要执行的服务器");
				return;
			}
			submit.setEnabled(false);
			execSql(sql);
			submit.setEnabled(true);
		} else if (e.getSource() == clear) {
			esv.getSqlArea().setText("");
		}
	}

	private void execSql(String sql) {
		ServerCheckItem[] jbs = esv.getSsv().getJb();
		for (ServerCheckItem jrb : jbs) {
			if (jrb.isSelected()) {
				SqlExecRunnable ser = new SqlExecRunnable(esv,
						jrb.getServerName(), sql, jrb.getMysqlUrl(),
						jrb.getUser(), jrb.getPwd());
				new Thread(ser).run();
			}
		}
	}

	private boolean noSelected() {
		boolean res = true;
		ServerCheckItem[] jbs = esv.getSsv().getJb();
		for (ServerCheckItem jrb : jbs) {
			if (jrb.isSelected()) {
				res = false;
				break;
			}
		}
		return res;
	}
}
