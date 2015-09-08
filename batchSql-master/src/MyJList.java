import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MyJList extends JFrame {
	static final Icon icon = new ImageIcon("imags/server.jpg");
	protected JList<InstallData> m_list;
	protected JLabel m_total;

	public MyJList() {
		super("Swing List [Check boxes]");
		setSize(260, 240);
		getContentPane().setLayout(new FlowLayout());

		InstallData[] options = { new InstallData("Program executable", 118),
				new InstallData("Help files", 52),
				new InstallData("Tools and converters", 83),
				new InstallData("Source code", 133) };

		m_list = new JList(options);
		CheckListCellRenderer renderer = new CheckListCellRenderer();
		m_list.setCellRenderer(renderer);
		m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		CheckListener lst = new CheckListener(this);
		m_list.addMouseListener(lst);
		m_list.addKeyListener(lst);

		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(m_list);

		m_total = new JLabel("Space required: 0K");

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(ps, BorderLayout.CENTER);
		p.add(m_total, BorderLayout.SOUTH);
		p.setBorder(new TitledBorder(new EtchedBorder(),
				"Please select options:"));
		// ************************************************************
		// System.err.println(icon == null);
		// JCheckBox box = new JCheckBox("test", icon);
		//
		// p.add(box);

		// ************************************************************

		getContentPane().add(p);

		WindowListener wndCloser = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		addWindowListener(wndCloser);

		setVisible(true);

		recalcTotal();
	}

	public void recalcTotal() {
		ListModel model = m_list.getModel();
		int total = 0;
		for (int k = 0; k < model.getSize(); k++) {
			InstallData data = (InstallData) model.getElementAt(k);
			if (data.isSelected()) {
				total += data.getMsize();
				System.err.println(data.getName());
			}

		}
		m_total.setText("Space required: " + total + "K");
	}

	public static void main(String argv[]) {
		new MyJList();
	}
}

class CheckListCellRenderer implements ListCellRenderer {
	// protected static Border m_noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	// JCheckBox jcb = new JCheckBox("test");

	public CheckListCellRenderer() {
		super();
		// add(jcb);
		// JLabel label = new JLabel("test");
		// add(label);
		// setOpaque(true);
		// setBorder(m_noFocusBorder);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		InstallData iddl = (InstallData) value;

		// setText(value.toString());

		iddl.setBackground(isSelected ? list.getSelectionBackground() : list
				.getBackground());
		iddl.setForeground(isSelected ? list.getSelectionForeground() : list
				.getForeground());

		// InstallData data = (InstallData) value;
		// setSelected(data.isSelected());

		iddl.setFont(list.getFont());
		// iddl.setBorder((cellHasFocus) ? UIManager
		// .getBorder("List.focusCellHighlightBorder") : m_noFocusBorder);
		return iddl;
	}
}

class CheckListener implements MouseListener, KeyListener {
	protected MyJList m_parent;
	protected JList<InstallData> m_list;

	public CheckListener(MyJList parent) {
		m_parent = parent;
		m_list = parent.m_list;
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

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ')
			doCheck();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	protected void doCheck() {
		int index = m_list.getSelectedIndex();
		if (index < 0)
			return;
		InstallData data = (InstallData) m_list.getModel().getElementAt(index);
		data.invertSelected();
		m_list.repaint();
		m_parent.recalcTotal();
	}

}