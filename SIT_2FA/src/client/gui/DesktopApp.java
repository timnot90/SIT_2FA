package client.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import client.Client;

public class DesktopApp implements ActionListener {
	private Client client;
	JPanel cards; // a panel that uses CardLayout
	final static String LOGIN_CREATE_PANEL = "Card login and create user";
	final static String MAIN_PANEL = "Card after succesfull login";
	
	JPasswordField userPwField;
	JTextField userNameField;
	
	public DesktopApp(Client client) {
		this.client = client;
	}

	public void addComponentToPane(Container pane) {
		// Create the "cards".
		// --------------------------------------------
		
		Box login_createCard = new Box(BoxLayout.Y_AXIS);

		Dimension sizeOfContainerPanel = new Dimension(220, 160);

		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
		containerPanel.setPreferredSize(sizeOfContainerPanel);
		containerPanel.setMaximumSize(sizeOfContainerPanel);
		containerPanel.setMinimumSize(sizeOfContainerPanel);

		userNameField = new JTextField(5);
		userNameField.setHorizontalAlignment(JTextField.CENTER);
		JLabel userNameLabel = new JLabel("Benutzername");
		userNameLabel.setLabelFor(userNameField);
		userNameLabel.setFont(userNameLabel.getFont().deriveFont(14.0f));
		userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		userPwField = new JPasswordField(5);
		userPwField.setHorizontalAlignment(JPasswordField.CENTER);
		// userPwField.setActionCommand(OK);
		// userPwField.addActionListener(this);

		JLabel userPwLabel = new JLabel("Password");
		userPwLabel.setLabelFor(userPwField);
		userPwLabel.setFont(userPwLabel.getFont().deriveFont(14.0f));
		userPwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel btnPanel = new JPanel(new BorderLayout());
		
		JButton createUserBtn = new JButton("SignUp");
		createUserBtn.setActionCommand("create");
		
		JButton loginUserBtn = new JButton("SignIn");
		loginUserBtn.setActionCommand("login");
		
		createUserBtn.addActionListener(this);
		loginUserBtn.addActionListener(this);

		btnPanel.add(createUserBtn, BorderLayout.WEST);
		btnPanel.add(loginUserBtn, BorderLayout.EAST);

		containerPanel.add(userNameLabel);
		containerPanel.add(userNameField);
		containerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		containerPanel.add(userPwLabel);
		containerPanel.add(userPwField);
		containerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		containerPanel.add(btnPanel);
		containerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		login_createCard.add(Box.createVerticalStrut(125));
		login_createCard.add(containerPanel);
		login_createCard.add(Box.createVerticalGlue());
		
		// --------------------------------------------
		
		Box mainCard = new Box(BoxLayout.Y_AXIS);
		JPanel containerMainPanel = new JPanel();
		containerMainPanel.setLayout(new BoxLayout(containerMainPanel, BoxLayout.Y_AXIS));
		containerMainPanel.setBackground(Color.black);
		
		JLabel randomNumerLabel = new JLabel("Zufallszahl:");
		randomNumerLabel.setFont(randomNumerLabel.getFont().deriveFont(18.0f));
		randomNumerLabel.setForeground(Color.white);
		randomNumerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		EmptyBorder eBorder = new EmptyBorder(5, 0, 5, 0); // oben, rechts, unten, links
		randomNumerLabel.setBorder(eBorder); 

		JLabel randomNumerOutput = new JLabel("000000");
		randomNumerOutput.setFont(randomNumerOutput.getFont().deriveFont(35.0f));
		randomNumerOutput.setForeground(Color.white);
		randomNumerOutput.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		eBorder = new EmptyBorder(10, 10, 10, 10); // oben, rechts, unten, links
		randomNumerOutput.setBorder(eBorder); 
		
		containerMainPanel.add(randomNumerLabel);
		containerMainPanel.add(randomNumerOutput);
		
		mainCard.add(Box.createVerticalStrut(200));
		mainCard.add(containerMainPanel);
		mainCard.add(Box.createVerticalGlue());

		// --------------------------------------------
		// Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());
		cards.add(login_createCard, LOGIN_CREATE_PANEL);
		cards.add(mainCard, MAIN_PANEL);

		pane.add(cards, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
        if ("login".equals(e.getActionCommand())) {
        	boolean loggedIn = client.login(userNameField.getText(), new String(userPwField.getPassword()));
			if(loggedIn) {
				System.out.println("logged in");
				CardLayout cl = (CardLayout) (cards.getLayout());
	    		cl.show(cards, MAIN_PANEL);
			} else {
				System.out.println("invalid username/password");
			}
        } else if ("create".equals(e.getActionCommand())){
            
        }
    }
	
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Desktop App");
		frame.setPreferredSize(new Dimension(400, 500));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		//DesktopApp demo = new DesktopApp();
		addComponentToPane(frame.getContentPane());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public void startGui() {		
		/* Turn off metal's use of bold fonts */
		//UIManager.put("swing.boldMetal", Boolean.FALSE);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
