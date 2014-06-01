package client.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.prompt.PromptSupport;

import client.Client;

enum Cards {LOGIN, SIGN_UP, TOKEN};

public class YADA {
	Client client;
	JPanel cardPane;
	JTextField txtToken;

	public static void main(String[] args) {
		new YADA().start();
	}
	
	public void start() {
		initializeServerConnection();
		runGuiInThread();
	}
	
	private void initializeServerConnection() {
		client = new Client();
		client.connect();
		client.initilizeKeyExchange();
	}
	
	private void runGuiInThread() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
	}
	
	private void setupInputField(String label, JTextField inputField) {
		inputField.setColumns(10);
		PromptSupport.setPrompt(label, inputField);
		inputField.setMaximumSize(inputField.getPreferredSize());
	}
	
	private JPasswordField addPasswordFieldToPane(String label, Container pane) {
		JPasswordField pwField = new JPasswordField();
		setupInputField(label, pwField);
		
		return (JPasswordField)addComponentToPane(pwField, pane);
	}
	
	private JTextField addTextFieldToPane(String label, Container pane) {		
		JTextField txtField = new JTextField();
		setupInputField(label, txtField);
		
		return (JTextField)addComponentToPane(txtField, pane);
	}
	
	private JButton addButtonToPane(String label, Container pane) {
		JButton btn = new JButton(label);
		
		return (JButton)addComponentToPane(btn, pane);
	}
	
	private JLabel addHeaderToPane(String label, Container pane) {
		JLabel lblHeader = new JLabel(label);
		lblHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		lblHeader.setBorder(new EmptyBorder(15, 0, 15, 0)); // t, r, b, l
		
		return (JLabel) addComponentToPane(lblHeader, pane);
	}
	
	private JComponent addComponentToPane(JComponent component, Container pane) {
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(component);
		
		return component;
	}
	
	private void setupLoginPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		addHeaderToPane("Login", pane);
		
		JTextField txtUsername = addTextFieldToPane("Username", pane);
		JPasswordField txtPassword = addPasswordFieldToPane("Password", pane);
		
		addButtonToPane("Login", pane).addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean loggedIn = client.login(txtUsername.getText().toLowerCase(), new String(txtPassword.getPassword()));
				
				if (loggedIn) {
					System.out.println("logged in");
					txtToken.setText(client.generateToken());
					showCard(Cards.TOKEN);
				} else {
					System.out.println("invalid username/password");
				}
			}
		});
		
		addButtonToPane("Create new account", pane).addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard(Cards.SIGN_UP);
			}
		});
	}

	private void setupTokenPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		addHeaderToPane("Token", pane);
		
		txtToken = addTextFieldToPane("Error: No Token.", pane);
		txtToken.setEditable(false);
	}
	
	private void setupSignUpPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		addHeaderToPane("Sign Up", pane);
		
		JTextField txtUsername = addTextFieldToPane("Username", pane);
		JPasswordField txtPassword = addPasswordFieldToPane("Password", pane);
		JTextField txtSecret = addTextFieldToPane("Secret", pane);
		
		// Create panel with two buttons: "Cancel" and "Sign Up".
		JButton btnCancel = new JButton("Cancel");
		JButton btnSignUp = new JButton("Sign Up");
		
		JPanel btnPanel = new JPanel();
		btnPanel.add(btnCancel);
		btnPanel.add(btnSignUp);
		
		addComponentToPane(btnPanel, pane);
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard(Cards.LOGIN);
			}
		});
		
		btnSignUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean registered = client.register(txtUsername.getText().toLowerCase(), new String(txtPassword.getPassword()), txtSecret.getText().toLowerCase());
				
				if (registered) {
					System.out.println("registered");
					showCard(Cards.LOGIN);
				} else {
					System.out.println("registration failed");
				}
			}
		});
	}
	
	private void showCard(Cards card) {
		CardLayout cl = (CardLayout) (cardPane.getLayout());
		cl.show(cardPane, card.name());
	}
	
	private void createAndShowGui() {
		// Set up the window.
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO exception on server if app is terminated some other way, e.g. CMD+Q
		// Close the connection to the server when the window is closing.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				client.exit();
			}
		});
		
		// Create panel with login functionality.
		JPanel loginPanel = new JPanel();
		setupLoginPanelOnPane(loginPanel);
		
		// Create panel with sign-up functionality.
		JPanel signUpPanel = new JPanel();
		setupSignUpPanelOnPane(signUpPanel);
		
		// Create panel which shows the random number.
		JPanel tokenPanel = new JPanel();
		setupTokenPanelOnPane(tokenPanel);

		// Create a card panel and add all panels.
		cardPane = new JPanel(new CardLayout());
		cardPane.add(loginPanel, Cards.LOGIN.name());
		cardPane.add(signUpPanel, Cards.SIGN_UP.name());
		cardPane.add(tokenPanel, Cards.TOKEN.name());
		
		// Show the window.
		frame.add(cardPane);
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus();
	}

}
