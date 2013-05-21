/**
 * 1. treba najprv okno kde si lekar moze vybrat, ci chce diabetes alebo srdcovu chorobu a gombik next, 
 * 2. potom budu vymenovane atributy danej choroby - ako checkboxy a gombik next, 
 * 3. potom choicelisty s mnoznymi hodnotami vybranych atributov + next, 
 * 4. potom vyhodnotenie meno klasifikatora + progressbary a na konci text area s odporucanim.
 */

package stuba.fei.tp.cdss.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

public class MainView extends JFrame {
	private static final long serialVersionUID = 3707521517187163206L;
	
	public static final String TITLE = " CDSS ";
	public static final String COMBO_1 = "Diabetes";
	public static final String COMBO_2 = "Srdcove choroby";
	public static final String COMBO_BOX_LABEL = "Vyberte si odbor, prosím:";
	public static final String COMBO_BOX_BUTTON = "OK";
	
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;
	private JComboBox comboBox;
	
	public MainView() {

	}

	public void createAndShowGUI() {
		initialize();
	}

	private void initialize() {
		setLookAndFeel();
		setTitle("Main window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(createContent());
		setResizable(false);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	private void setLookAndFeel() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
	}

	private JPanel createContent() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
		mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
		
		return mainPanel;
	}

	private JPanel createCenterPanel() {
		centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
		JLabel label = new JLabel(TITLE, SwingConstants.CENTER);
		Border redBorder = BorderFactory.createLineBorder(Color.RED);
		label.setFont(new Font(Font.SERIF, Font.BOLD, 60));
		label.setForeground(Color.RED);
		label.setBorder(redBorder);
		centerPanel.add(label);
		
		return centerPanel;
	}
	
	private JPanel createBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		bottomPanel.add(createComboBoxLabel());
		bottomPanel.add(createComboBox());
		bottomPanel.add(createButton());
		
		bottomPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		return bottomPanel;
	}
	
	private JButton createButton() {
		JButton button = new JButton(COMBO_BOX_BUTTON);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performAction();
			}
		});
		
		button.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					performAction();
				}
			}
		});
		
		this.getRootPane().setDefaultButton(button);
		button.requestFocusInWindow();
		
		return button;
	}

	protected void performAction() {
		String disease = (String) comboBox.getSelectedItem();
		setVisible(false);
		SymptomsView symptomsView = new SymptomsView(disease);
		symptomsView.createAndShowGUI(getInstance());
	}

	private JComboBox createComboBox() {
		comboBox = new JComboBox();
		comboBox.addItem(COMBO_1);
		comboBox.addItem(COMBO_2);
		comboBox.setSelectedIndex(0);
		
		return comboBox;
	}

	private JLabel createComboBoxLabel() {
		JLabel label = new JLabel(COMBO_BOX_LABEL);
		
		return label;
	}

	private MainView getInstance() {
		return this;
	}
}
