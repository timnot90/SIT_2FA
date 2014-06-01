package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import client.Client;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 8849485016773981384L;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JLabel lblLoginFailure;
	private Client client = new Client();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new MainFrame().start();
	}
	
	public void start() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
			
			initlizeEncryption();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initlizeEncryption() {
		client = new Client();
		System.out.printf("id: %d%n", client.connect());
		client.initializeKeyExchange();
	}
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("SIT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 254, 137);
		getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		JLabel lblUsername = new JLabel("Username");
		getContentPane().add(lblUsername, "cell 0 0,alignx trailing");
		
		this.txtUsername = new JTextField();
		getContentPane().add(this.txtUsername, "cell 1 0,growx");
		this.txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		getContentPane().add(lblPassword, "cell 0 1,alignx trailing");
		
		this.pwdPassword = new JPasswordField();
		getContentPane().add(this.pwdPassword, "cell 1 1,growx");
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean loggedIn = client.login(txtUsername.getText(), new String(pwdPassword.getPassword()));
				if(loggedIn) {
					lblLoginFailure.setText("logged in");
				} else {
					lblLoginFailure.setText("invalid username/password");
				}
			}
		});
		getContentPane().add(btnLogin, "cell 0 2");
		
		lblLoginFailure = new JLabel("");
		getContentPane().add(lblLoginFailure, "cell 1 2");
	}
}
