package com.wangboo.batchSql.comp;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 左边标题，右边输入框组件 左边占用X像素,右边占用剩余宽度
 * 
 * @author wangboo
 * 
 */
public class LabelAndInputFieldView extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JTextField inputField;

	private static final int LABEL_WIDTH = 100;
	
	
	private static final int SPACE_X = 5, SPACE_Y = 5;

	public LabelAndInputFieldView(int width, int height, String name) {
		setLayout(new FlowLayout(FlowLayout.LEFT, SPACE_X, SPACE_Y));
		nameLabel = new JLabel(name);
		inputField = new JTextField(25);
		int compHeight = height - 2 * SPACE_Y;
		nameLabel.setSize(LABEL_WIDTH, compHeight);
		add(nameLabel);
		add(inputField);
	}

	public String getIputString() {
		return inputField.getText();
	}

	public JTextField getInputField() {
		return inputField;
	}

}
