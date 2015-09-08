package com.wangboo.batchSql.comp;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SqlResultView extends JPanel {
	private static final long serialVersionUID = 1L;
	private ExecSqlView esv;
	private DataTransferView dtv;

	private JTextArea sqlArea;
	private JScrollPane sqlScroll;

	// private final Color backColor = new Color(180, 205, 230);

	public SqlResultView(ExecSqlView esv) {
		this.esv = esv;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("执行结果"));
		sqlArea = new JTextArea(ExecSqlView.textAreaY / 2,
				ExecSqlView.textAreaX);
		// sqlArea.setBackground(backColor);
		sqlArea.setFont(new Font("Default", Font.PLAIN, 15));
		// 自动换行,换行不断字
		sqlArea.setLineWrap(true);
		sqlArea.setWrapStyleWord(true);
		sqlArea.setEditable(false);
		sqlScroll = new JScrollPane(sqlArea);
		sqlArea.setCaretPosition(sqlArea.getText().length());
		add(sqlScroll);
	}

	public SqlResultView(DataTransferView dtv) {
		this.dtv = dtv;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("执行结果"));
		sqlArea = new JTextArea(8, ExecSqlView.textAreaX * 8 / 9);
		// sqlArea.setBackground(backColor);
		sqlArea.setFont(new Font("Default", Font.PLAIN, 15));
		sqlArea.setLineWrap(true);
		sqlArea.setEditable(false);
		sqlScroll = new JScrollPane(sqlArea);
		// sqlArea.setCaretPosition(sqlArea.getText().length());
		add(sqlScroll);
	}

	public ExecSqlView getEsv() {
		return esv;
	}

	public JTextArea getSqlArea() {
		return sqlArea;
	}

	public JScrollPane getSqlScroll() {
		return sqlScroll;
	}

	public DataTransferView getDtv() {
		return dtv;
	}

}
