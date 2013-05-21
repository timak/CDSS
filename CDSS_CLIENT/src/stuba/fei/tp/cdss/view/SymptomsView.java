package stuba.fei.tp.cdss.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import stuba.fei.tp.cdss.service.ServiceCallHandler;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.Symptom;
import stuba.fei.tp.cdss.view.PriznakyView.Task;

public class SymptomsView extends JFrame {
	private static final long serialVersionUID = 5464385185828215235L;
	
	public static final String TITLE_LABEL = "Máme k dispozícií tieto údaje, vyberte si prosím, ktoré potrebujete:";
	public static final String BUTTON_BACK = "Back";
	public static final String BUTTON_NEXT = "Next";

	private MainView mainView;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;
	private String disease;
	private Symptom[] availableSymptoms;
	
	public SymptomsView(String disease) {
		this.disease = disease;
	}

	public void createAndShowGUI(MainView mainView) {
		this.mainView = mainView;
		initialize();
	}

	private void initialize() {
		setTitle("Symptoms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(createContent());
		setResizable(false);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	private JPanel createContent() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(createTopPanel(), BorderLayout.NORTH);
		mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
		mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
		
		return mainPanel;
	}
	
	private JPanel createTopPanel() {
		topPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(TITLE_LABEL, SwingConstants.CENTER);
		label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		topPanel.add(label, BorderLayout.CENTER);
		return topPanel;
	}
	
	private JPanel createCenterPanel() {
		try {
			availableSymptoms = ServiceCallHandler.getAvailableSymptoms(disease);
		} catch (RemoteException e) {
			System.out.println("Error calling getAvailableSymptoms");
			e.printStackTrace();
		}
		centerPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		for(int i = 0; i < availableSymptoms.length; i++) {
			c.gridx++;
			centerPanel.add(createSymptomBox(availableSymptoms[i].getName()), c);
			if(c.gridx == 4) {
				//newline
				c.gridx = 0;
				c.gridy++;
			}
		}
		
		return centerPanel;
	}
	
	private JPanel createBottomPanel() {
		bottomPanel = new JPanel(new GridLayout());
		bottomPanel.add(createBackButtonPanel());
		bottomPanel.add(createNextButtonPanel());
		
		return bottomPanel;
	}
	
	private JPanel createBackButtonPanel() {
		JPanel buttonBackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
		JButton buttonBack = new JButton(BUTTON_BACK);
		buttonBackPanel.add(buttonBack);
		buttonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				mainView.setVisible(true);
			}
		});
		
		return buttonBackPanel;
	}

	private JPanel createNextButtonPanel() {
		JPanel buttonNextPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
		JButton buttonNext = new JButton(BUTTON_NEXT);
		buttonNextPanel.add(buttonNext);
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performAction();
			}
		});
		
		buttonNext.addKeyListener(new KeyListener() {
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

		this.getRootPane().setDefaultButton(buttonNext);
		buttonNext.requestFocusInWindow();

		return buttonNextPanel;
	}

	protected void performAction() {
		try {
			dispose();
			mainView.dispose();
			PriznakyView priznakyView = new PriznakyView(disease);
			priznakyView.setSymptoms(getCheckedSymptoms());
			priznakyView.createAndShowGUI();
			Task task = priznakyView.new Task();
			task.execute();
		} catch (Exception e1) {
			System.out.println("Error calling getDiagnoses");
			e1.printStackTrace();
		}
	}

	protected String[] getCheckedSymptoms() {
		List<String> checkedSymptoms = new ArrayList<String>();
		Component[] components = centerPanel.getComponents();
		for(Component comp : components) {
			if (comp instanceof JCheckBox && ((JCheckBox) comp).isSelected()) {
				String s = new String(((JCheckBox)comp).getText());
				checkedSymptoms.add(s);
			}
		}
		String [] symptomsArray = checkedSymptoms.toArray(new String[checkedSymptoms.size()]);
		
		return symptomsArray;
	}

	private JCheckBox createSymptomBox(String symptomName) {
		JCheckBox checkBox = new JCheckBox(symptomName);
		checkBox.setSize(150, checkBox.getHeight());
		checkBox.setSelected(false);
		
		return checkBox;
	}
}
