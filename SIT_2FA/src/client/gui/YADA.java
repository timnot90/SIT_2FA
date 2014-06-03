package client.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.prompt.PromptSupport;

import client.Client;

enum Cards {
	LOGIN, SIGN_UP, TOKEN, MAIN, TOKEN_TIMEOUT
};

public class YADA {
	Client client;
	JPanel cardPane;
	JTextField txtToken;

	public static void main(String[] args) {
		new YADA().start();
	}

	/**
	 * Initializes the connection to the server and runs the GUI in a separate
	 * thread.
	 */
	public void start() {
		initializeServerConnection();
		runGuiInThread();
	}

	/**
	 * Initializes the connection to the server.
	 */
	private void initializeServerConnection() {
		client = new Client();
		client.connect();
		client.initializeKeyExchange();
	}

	/**
	 * Runs the GUI in its own thread.
	 */
	private void runGuiInThread() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}

	/**
	 * Sets the properties for a textfield so that all textfields in the GUI
	 * look the same.
	 * 
	 * @param label
	 *            is the text to be shown as a placeholder in the textfield.
	 * @param inputField
	 *            is the textfield the properties will be set on.
	 */
	private void setupInputField(String label, JTextField inputField) {
		inputField.setColumns(10);
		PromptSupport.setPrompt(label, inputField);
		inputField.setMaximumSize(inputField.getPreferredSize());
	}

	/**
	 * Adds a fully configured password-textfield to a container.
	 * 
	 * @param label
	 *            is the text to be shown as a placeholder in the
	 *            password-textfield.
	 * @param pane
	 *            is the container the password-textfield will be added to.
	 * @return a reference to the fully configured password-textfield.
	 */
	private JPasswordField addPasswordFieldToPane(String label, Container pane) {
		JPasswordField pwField = new JPasswordField();
		setupInputField(label, pwField);

		return (JPasswordField) addComponentToPane(pwField, pane);
	}

	/**
	 * Adds a fully configured textfield to a container.
	 * 
	 * @param label
	 *            is the text to be shown as a placeholder in the textfield.
	 * @param pane
	 *            is the container the textfield will be added to.
	 * @return a reference to the fully configured textfield.
	 */
	private JTextField addTextFieldToPane(String label, Container pane) {
		JTextField txtField = new JTextField();
		setupInputField(label, txtField);

		return (JTextField) addComponentToPane(txtField, pane);
	}

	/**
	 * Adds a fully configured button to a container.
	 * 
	 * @param label
	 *            is the text to be shown on the button.
	 * @param pane
	 *            is the container the button will be added to.
	 * @return a reference to the fully configured button.
	 */
	private JButton addButtonToPane(String label, Container pane) {
		JButton btn = new JButton(label);

		return (JButton) addComponentToPane(btn, pane);
	}

	/**
	 * Adds a fully configured headline to a container.
	 * 
	 * @param label
	 *            is the text to be shown in the headline.
	 * @param pane
	 *            is the container the headline will be added to.
	 * @return a reference to the fully configured headline.
	 */
	private JLabel addHeaderToPane(String label, Container pane) {
		JLabel lblHeader = new JLabel(label);
		lblHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		lblHeader.setBorder(new EmptyBorder(15, 15, 15, 15)); // t, r, b, l

		return (JLabel) addComponentToPane(lblHeader, pane);
	}

	/**
	 * Adds any component to a container.
	 * 
	 * @param component
	 *            is the component to be added to the container.
	 * @param pane
	 *            is the container the component will be added to.
	 * @return a reference to the component added to the container.
	 */
	private JComponent addComponentToPane(JComponent component, Container pane) {
		component.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(component);

		return component;
	}

	/**
	 * Fully configures the panel where the user can log in and adds it to the
	 * specified container.
	 * 
	 * @param pane
	 *            the container the fully configured panel should be added to.
	 */
	private void setupLoginPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		addHeaderToPane("Login", pane);

		JTextField txtUsername = addTextFieldToPane("Username", pane);
		JPasswordField txtPassword = addPasswordFieldToPane("Password", pane);

		addButtonToPane("Login", pane).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean loggedIn = client.login(txtUsername.getText()
						.toLowerCase(), new String(txtPassword.getPassword()));

				if (loggedIn) {
					System.out.println("login from desktop successful");
					txtToken.setText(client.generateToken());
					showCard(Cards.TOKEN);
					openUrl("http://localhost:8000/token");
				} else {
					System.out
							.println("login from desktop failed: invalid username or password");
				}
			}
		});

		addButtonToPane("Create new account", pane).addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showCard(Cards.SIGN_UP);
					}
				});
	}

	/**
	 * Fully configures the panel where the user can create a new account and
	 * adds it to the specified container.
	 * 
	 * @param pane
	 *            the container the fully configured panel should be added to.
	 */
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
				boolean registered = client.register(txtUsername.getText()
						.toLowerCase(), new String(txtPassword.getPassword()),
						txtSecret.getText().toLowerCase());

				if (registered) {
					System.out.println("registration from desktop successful");
					showCard(Cards.LOGIN);
				} else {
					System.out.println("registration from desktop failed");
				}
			}
		});
	}

	/**
	 * Fully configures the panel where the token is shown to the user and adds
	 * it to the specified container.
	 * 
	 * @param pane
	 *            the container the fully configured panel should be added to.
	 */
	private void setupTokenPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		addHeaderToPane("Token", pane);

		txtToken = addTextFieldToPane("Error: No Token.", pane);
		txtToken.setEditable(false);

		// Wait for second authentication.
		addButtonToPane("Check for second authentication", pane)
				.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						checkForSecondAuthentication();
					}
				});
	}

	/**
	 * Fully configures the panel where the user can request a new token after
	 * the second authentication timed out to the specified container.
	 * 
	 * @param pane
	 *            the container the fully configured panel should be added to.
	 */
	private void setupTokenTimeoutPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		addHeaderToPane("Login Failed", pane);

		// Give the user a description of what went wrong.
		addComponentToPane(
				new JLabel(
						"<html>Second authentication timed out.<br>Request a new token and try again.</html>",
						JLabel.CENTER), pane);

		addButtonToPane("Request new token", pane).addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						txtToken.setText(client.generateToken());
						showCard(Cards.TOKEN);
					}
				});
	}

	/**
	 * Fully configures the panel which is shown to the user after a successful
	 * login and adds it to the specified container.
	 * 
	 * @param pane
	 *            the container the fully configured panel should be added to.
	 */
	private void setupMainPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		addHeaderToPane("Login Successful", pane);

		// Add a fancy clown to the container :o)
		JLabel imageLabel = new JLabel();
		imageLabel.setIcon(new ImageIcon(this.getClass().getResource(
				"clown.gif")));
		imageLabel.setBorder(new EmptyBorder(0, 15, 15, 15)); // t, r, b, l
		addComponentToPane(imageLabel, pane);
	}

	/**
	 * Redraws the GUI to show a specified card.
	 * 
	 * @param card
	 *            is the card to be shown on the GUI.
	 */
	private void showCard(Cards card) {
		CardLayout cl = (CardLayout) (cardPane.getLayout());
		cl.show(cardPane, card.name());
	}

	/**
	 * Creates and shows the GUI.
	 */
	private void createAndShowGui() {
		// Set up the window.
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// TODO exception on server if app is terminated some other way, e.g.
		// CMD+Q
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

		// Create panel which shows the token from the server.
		JPanel tokenPanel = new JPanel();
		setupTokenPanelOnPane(tokenPanel);

		// Create a panel which is shown if the second authentication times out.
		JPanel tokenTimeoutPanel = new JPanel();
		setupTokenTimeoutPanelOnPane(tokenTimeoutPanel);

		// Create panel which is shown after successful login.
		JPanel mainPanel = new JPanel();
		setupMainPanelOnPane(mainPanel);

		// Create a card panel and add all panels.
		cardPane = new JPanel(new CardLayout());
		cardPane.add(loginPanel, Cards.LOGIN.name());
		cardPane.add(signUpPanel, Cards.SIGN_UP.name());
		cardPane.add(tokenPanel, Cards.TOKEN.name());
		cardPane.add(tokenTimeoutPanel, Cards.TOKEN_TIMEOUT.name());
		cardPane.add(mainPanel, Cards.MAIN.name());

		// Show the window.
		frame.add(cardPane);
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus(); // Needed to remove the focus from the first text
								// input field.
	}

	/**
	 * Waits for a server response to see if the second authentication was
	 * successful and updates the GUI accordingly.
	 */
	private void checkForSecondAuthentication() {
		/*
		 * if (client.checkForSecondAuthentication()) { showCard(Cards.MAIN); }
		 * else { showCard(Cards.TOKEN_TIMEOUT); }
		 */
		SecondAuthChecker checker = new SecondAuthChecker();
		checker.execute();
	}

	/**
	 * start the default browser and open the url
	 * @param url
	 */
	private void openUrl(String url) {
		if (java.awt.Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				URI uri;
				try {
					uri = new URI(url);
					desktop.browse(uri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// SwingWorker class is used to simulate a task being performed
	class SecondAuthChecker extends SwingWorker<Void, String> {

		@Override
		public Void doInBackground() throws InterruptedException {
			if (client.checkForSecondAuthentication()) {
				publish("Auth OK");
			} else {
				publish("Auth Fail");
			}

			return null;
		}

		@Override
		protected void process(List<String> result) {
			for (String string : result) {
				if (string.equals("Auth OK")) {
					showCard(Cards.MAIN);
				}
				if (string.equals("Auth Fail")) {
					showCard(Cards.TOKEN_TIMEOUT);
				}
			}
		}

		// when the 'task' has finished
		@Override
		public void done() {

		}
	}

}
