package stuba.fei.tp.cdss.view;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class VyhodnotenieView extends JFrame {
	private static final long serialVersionUID = 3707521517187163206L;

	public static final String TITLE = "Systém podľa zadaných príznakov odporúča možné diagnózy:";
	public static final String COMBO_BOX_BUTTON = "OK";

	private static final String OK_BUTTON = "OK";
	
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel centerPanel;
	private JPanel bottomPanel;
	private JButton okButton;
	
	private double jrip;
	private double dt;
	private double lmt;
	//TODO: diagnoza + percentualna hodnota

	public VyhodnotenieView(double jrip, double dt, double lmt) {
		this.jrip = jrip;
		this.dt = dt;
		this.lmt = lmt;
	}

	public void createAndShowGUI() {
		initialize();
	}

	private void initialize() {
		setTitle("Vyhodnotenie");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(createContent());
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
		JLabel label = new JLabel(TITLE, SwingConstants.CENTER);
		label.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		topPanel.add(label, BorderLayout.CENTER);
		
		return topPanel;
	}

	private JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(0, 2));

		//jrip
		centerPanel.add(createPercentageBarPanel((int) Math.round(jrip*100.0)));
		centerPanel.add(createDiagnoseLabelPanel("Jrip"));
			
		//dt
		centerPanel.add(createPercentageBarPanel((int) Math.round(dt*100.0)));
		centerPanel.add(createDiagnoseLabelPanel("Decision Table"));
			
		//lmt
		centerPanel.add(createPercentageBarPanel((int) Math.round(lmt*100.0)));
		centerPanel.add(createDiagnoseLabelPanel("LMT"));
		
		return centerPanel;
	}

	private JPanel createPercentageBarPanel(int value) {
		JPanel percentageBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JProgressBar percentageBar = new JProgressBar();
		percentageBar.setValue(value);
		percentageBar.setStringPainted(true);
		percentageBarPanel.add(percentageBar);
		
		return percentageBarPanel;
	}

	private JPanel createDiagnoseLabelPanel(String name) {
		JPanel diagnoseLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		JLabel diagnoseLabel = new JLabel(name);
		diagnoseLabel.setSize(200, diagnoseLabel.getHeight());
		diagnoseLabelPanel.add(diagnoseLabel);
		
		return diagnoseLabelPanel;
	}

	private JPanel createBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));
		bottomPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		bottomPanel.add(createOkButton());

		return bottomPanel;
	}
	
	private JButton createOkButton() {
		okButton = new JButton(OK_BUTTON);
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
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					performAction();
				}
			}
		});
		
		this.getRootPane().setDefaultButton(okButton);
		okButton.requestFocusInWindow();
		
		return okButton;
	}

	protected void performAction() {
		dispose();
		System.exit(0);
	}
}
