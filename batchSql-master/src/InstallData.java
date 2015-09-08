import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstallData extends JPanel {

	protected String m_name;
	protected int m_size;

	private JCheckBox cbox;
	private JLabel label;

	public InstallData(String name, int size) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		cbox = new JCheckBox(name);

		label = new JLabel(new ImageIcon("imgs/server.jpg"));
		add(label);
		add(cbox);

		m_name = name;
		m_size = size;
	}

	public String getName() {
		return m_name;
	}

	public int getMsize() {
		return m_size;
	}

	public void setSelected(boolean selected) {
		cbox.setSelected(selected);
	}

	public boolean isSelected() {
		return cbox.isSelected();
	}

	public String toString() {
		return m_name + " (" + m_size + " K)";
	}

	public void invertSelected() {
		this.cbox.setSelected(!this.cbox.isSelected());

	}
}