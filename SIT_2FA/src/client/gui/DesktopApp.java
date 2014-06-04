package client.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
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
	LOGIN, SIGN_UP, TOKEN, MAIN, TOKEN_TIMEOUT, ABOUT
};

public class DesktopApp {
	private Client client;
	private JPanel cardPane;
	private JTextField txtToken;
	private JLabel lblLoginFailed;

	public static void main(String[] args) {
		new DesktopApp().start();
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
		inputField.setColumns(15);
		PromptSupport.setPrompt(label, inputField);
		inputField.setMargin(new Insets(5, 5, 5, 5));
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
		txtField.setInputVerifier(new MyInputVerifier());
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
	 * Adds a fully configured label to a container.
	 * 
	 * @param label
	 *            is the text to be shown on the label.
	 * @param pane
	 *            is the container the label will be added to.
	 * @return a reference to the fully configured label.
	 */
	private JLabel addLabelToPane(String label, Container pane) {
		String htmlString = "<html>" + label + "</html>";
		htmlString = htmlString.replaceAll("(\r\n|\n)", "<br/>");
		JLabel lblText = new JLabel(htmlString, JLabel.CENTER);
		lblText.setBorder(new EmptyBorder(0, 15, 15, 15)); // t, r, b, l)

		return (JLabel) addComponentToPane(lblText, pane);
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
		pane.add(Box.createRigidArea(new Dimension(0,2)));
		pane.add(component);
		pane.add(Box.createRigidArea(new Dimension(0,2)));
		
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
		
		lblLoginFailed = addLabelToPane("Wrong username or password.\nPlease try again.", pane);
		lblLoginFailed.setForeground(Color.RED);
		lblLoginFailed.setVisible(false);

		JTextField txtUsername = addTextFieldToPane("Username", pane);
		JPasswordField txtPassword = addPasswordFieldToPane("Password", pane);

		addButtonToPane("Login", pane).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean loggedIn = client.login(formatInputText(txtUsername), 
						new String(txtPassword.getPassword()));

				if (loggedIn) {
					System.out.println("login from desktop successful");
					showCard(Cards.TOKEN);
				} else {
					System.out
							.println("login from desktop failed: invalid username or password");
					lblLoginFailed.setVisible(true);
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
		
		addButtonToPane("About", pane).addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showCard(Cards.ABOUT);
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
				boolean registered = client.register(formatInputText(txtUsername), 
						new String(txtPassword.getPassword()),
						formatInputText(txtSecret));

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
		addLabelToPane(
				"Copy the token below and paste it\non the website that just opened.",
				pane);

		txtToken = addTextFieldToPane("Error: No Token.", pane);
		
		txtToken.setEditable(false);
		txtToken.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		txtToken.setMargin(new Insets(5, 5, 5, 5));
		txtToken.setHorizontalAlignment(JTextField.CENTER);
		txtToken.setBackground(Color.black);
		txtToken.setForeground(Color.white);
		txtToken.setBorder(null);
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
		addLabelToPane(
				"Second authentication timed out.\nRequest a new token and try again.",
				pane);

		addButtonToPane("Request new token", pane).addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showCard(Cards.TOKEN);
					}
				});
	}
	
	/**
	 * show the credentials of the developer
	 * @param pane
	 */
	private void setupAboutPanelOnPane(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		addHeaderToPane("About", pane);
		
		addLabelToPane("Christian Titze\n 1123377\n 1123377@stud.hs-mannheim.de&emsp;\n", pane);
		addLabelToPane("Rene Schmitt\n 1131971\n reneschmitt@gmx.de&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;\n", pane);
		addLabelToPane("Timo Notheisen\n 1120722\n timo.notheisen@googlemail.com\n", pane);
		addLabelToPane("Martin Taenzer\n 1123643\n 1123643@stud.hs-mannheim.de&emsp;\n", pane);

		addButtonToPane("< back", pane).addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showCard(Cards.LOGIN);
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
		// Special case: Token card needs additional setup.
		if (card.name().equals(Cards.TOKEN.name())) {
			txtToken.setText(client.generateToken());
			openUrlInExternalWebBrowser("http://localhost:8000/token");
			new AsyncSecondAuthenticationChecker().execute();
		}

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
		// CMD+Q, ALT-F4
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
		
		// Create a panel which show the creators of the application.
		JPanel aboutPanel = new JPanel();
		setupAboutPanelOnPane(aboutPanel);

		// Create panel which is shown after successful login.
		JPanel mainPanel = new JPanel();
		setupMainPanelOnPane(mainPanel);

		// Create a card panel and add all panels.
		cardPane = new JPanel(new CardLayout());
		cardPane.add(loginPanel, Cards.LOGIN.name());
		cardPane.add(signUpPanel, Cards.SIGN_UP.name());
		cardPane.add(tokenPanel, Cards.TOKEN.name());
		cardPane.add(tokenTimeoutPanel, Cards.TOKEN_TIMEOUT.name());
		cardPane.add(aboutPanel, Cards.ABOUT.name());
		cardPane.add(mainPanel, Cards.MAIN.name());

		// Show the window.
		frame.add(cardPane);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.requestFocus(); // Needed to remove the focus from the first text
								// input field.
	}

	/**
	 * Opens the given URL in the default web browser.
	 * 
	 * @param url
	 *            the URL to be opened.
	 */
	private void openUrlInExternalWebBrowser(String url) {
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
	
	private String formatInputText(JTextField inputText) {
		String text = inputText.getText().toLowerCase();
		text = text.replaceAll("[^a-zA-Z0-9]", "");
		
		return text;
	}

	/**
	 * Runs a background thread which waits for a server response to see if the
	 * second authentication was successful and updates the GUI accordingly.
	 */
	private class AsyncSecondAuthenticationChecker extends
			SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			if (client.checkForSecondAuthentication()) {
				showCard(Cards.MAIN);
			} else {
				showCard(Cards.TOKEN_TIMEOUT);
			}
			return null;
		}

	}
	
	private class MyInputVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            String s = tf.getText();
            
            return s.matches("[a-zA-Z0-9]+");
        }
    }

}