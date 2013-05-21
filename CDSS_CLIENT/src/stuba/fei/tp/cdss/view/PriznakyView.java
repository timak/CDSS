package stuba.fei.tp.cdss.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import stuba.fei.tp.cdss.dto.Patient;
import stuba.fei.tp.cdss.parser.RecordsParser;
import stuba.fei.tp.cdss.service.ServiceCallHandler;
import stuba.fei.tp.cdss.weka.WekaDbCoordinator;
import stuba.fei.tp.cdss.weka.WekaManager;
import weka.core.Instance;
import weka.core.Instances;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class PriznakyView extends JFrame {
	private static final long serialVersionUID = -3253473463468950709L;

	public static final String TITLE_LABEL = "Zadajte na pacienta špecifické príznaky, prosím";
	public static final String BUTTON_BACK = "Back";
	public static final String BUTTON_NEXT = "Next";
	public static final String OK_BUTTON = "OK";

	private JProgressBar progressBarInPanel;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;
	private JButton okButton;
	private String disease;
	private String[] symptoms;

	private WekaDbCoordinator wekaCoor;
	private Instances instances;
	private Instance patientData;
	private String symptomValues[][] = {};
	
	private String jrip;
	private String dt;
	private String lmt;

	public PriznakyView(String disease) {
		this.disease = disease;
	}

	public void createAndShowGUI() {
		initialize();
	}

	private void initialize() {
		setTitle("Priznaky");
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
		centerPanel = new JPanel(new GridLayout(0, 2));

		for (int i = 0; i < symptoms.length; i++) {
			centerPanel.add(createSymptomPanel(symptoms[i]));
			centerPanel.add(createSymptomComboBox());
		}

		return centerPanel;
	}

	private JPanel createBottomPanel() {
		bottomPanel = new JPanel(new GridLayout());
		bottomPanel.add(createProgressBarPanel());
		bottomPanel.add(createOkButtonPanel());

		return bottomPanel;
	}

	private JPanel createProgressBarPanel() {
		JPanel progressBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,
				20, 20));

		progressBarInPanel = new JProgressBar();
		progressBarInPanel.setIndeterminate(true);
		progressBarPanel.add(progressBarInPanel);

		return progressBarPanel;
	}

	private JPanel createOkButtonPanel() {
		JPanel okButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20,
				20));
		okButton = new JButton(OK_BUTTON);
		okButton.setEnabled(false);
		okButtonPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performAction();
			}
		});

		okButton.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					performAction();
				}
			}
		});

		this.getRootPane().setDefaultButton(okButton);
		okButton.requestFocusInWindow();

		return okButtonPanel;
	}

	private JPanel createSymptomPanel(String symptomName) {
		JPanel symptomLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 10));
		JLabel symptomLabel = new JLabel(symptomName);
		symptomLabelPanel.add(symptomLabel);

		return symptomLabelPanel;
	}

	private JPanel createSymptomComboBox() {
		JPanel symptomComboBoxPanel = new JPanel(new GridLayout(1, 1, 20, 20));
		JComboBox symptomComboBox = new JComboBox();
		symptomComboBoxPanel.add(symptomComboBox);

		return symptomComboBoxPanel;
	}

	public String[] getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String[] symptoms) {
		this.symptoms = symptoms;
	}
	
	protected void performAction() {
		try {
			setSelectedValues();
			getDiagnoses();
		} catch (Exception e) {
			System.out.println("Error getting diagnoses");
			e.printStackTrace();
		}
		dispose();
		VyhodnotenieView vyhodnotenieView = new VyhodnotenieView(jrip, dt, lmt);
		vyhodnotenieView.createAndShowGUI();
	}

	private void setSelectedValues() {
		int comboBoxCounter = 1;			
		for(Component comp : centerPanel.getComponents()) {
			if(comp instanceof JPanel) {
				for(Component panelComp : ((JPanel) comp).getComponents()) {
					if(panelComp instanceof JComboBox) {
						symptomValues[comboBoxCounter++][0] = (String) ((JComboBox) panelComp).getSelectedItem();
					}
				}
			}
		}
	}

	private void getSymptomsValuesArray() {
		try {
			String endocedData = ServiceCallHandler.getMedicalRecords(disease, getSymptoms());
			
			// decode data
			byte[] decodedByteData = Base64.decode(endocedData);
			String decodedXml = new String(decodedByteData);
			
			// parse xml
			RecordsParser parser = new RecordsParser();
			ArrayList<Patient> recordsData = parser.parseRecords(decodedXml);
			
			wekaCoor = new WekaDbCoordinator();
			
			// create temp table for weka
			wekaCoor.createWekaData(recordsData);
			
			instances = WekaManager.createNewInstance();
			symptomValues = WekaManager.getSymptomValues(instances);
		} catch (Exception e) {
			System.out.println("Error in getSymptomValues()");
			e.printStackTrace();
		}
	}

	protected void getDiagnoses() throws Exception {

		try {
			patientData = WekaManager.createNewPatient(instances, symptomValues);
			// classify data
			jrip = WekaManager.clasifyByJrip(patientData);
			dt = WekaManager.clasifyByDt(patientData);
			lmt = WekaManager.clasifyByLmt(patientData);

			// remove temp table
			wekaCoor.dropTable();
			wekaCoor.closeConnection();

		} catch (Exception e) {
			System.out.println("Unable to create temp table for weka: "
					+ e.toString());
			wekaCoor.closeConnection();
		}
	}

	@SuppressWarnings("rawtypes")
	class Task extends SwingWorker {

		@Override
		protected Object doInBackground() throws Exception {
			getSymptomsValuesArray();
			addValuesToComboBoxes();			
			
			Thread.sleep(5000);
			return null;
		}

		private void addValuesToComboBoxes() {
			int comboBoxCounter = 1;
			for(Component comp : centerPanel.getComponents()) {
				if(comp instanceof JPanel) {
					for(Component panelComp : ((JPanel) comp).getComponents()) {
						if(panelComp instanceof JComboBox) {
							JComboBox comboBox = (JComboBox) panelComp;
							for (int i = 0; i < symptomValues[comboBoxCounter].length; i++) {
								if(symptomValues[comboBoxCounter][i] == null) {
									break;
								}
								comboBox.addItem(symptomValues[comboBoxCounter][i]);
							}
							comboBoxCounter++;
						}
					}
				}
			}			
		}

		@Override
		protected void done() {
			okButton.setEnabled(true);
			progressBarInPanel.setIndeterminate(false);
			progressBarInPanel.setValue(100);
			progressBarInPanel.setStringPainted(true);
		}
	}
}
