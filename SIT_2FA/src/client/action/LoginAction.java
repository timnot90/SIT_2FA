package client.action;

import client.ClientConnetcion;
import security.encryption.PasswordHash;

public class LoginAction {
	private PasswordHash passwordHash;
	private ClientConnetcion connection;
	
	public LoginAction(ClientConnetcion connection) {
		this.passwordHash = new PasswordHash();
		this.connection = connection;
	}

	public boolean login(String username, String password) {
		boolean loggedIn = false;

		if (!username.equals("") && !password.equals("") && username != null
				&& password != null) {

			byte[] salt = connection.readMessage().getBytes();
			byte[] hash = passwordHash.hashPassword(password, salt);
		}
		return loggedIn;
	}
}
