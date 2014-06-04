package server.action;

import server.ClientThread;
import data.MySqlUserDataConnection;
import data.UserDataInterface;

public class LoginAction {
	private ClientThread client;
	private UserDataInterface userDb;
	
	public LoginAction(ClientThread client) {
		this.client = client;
		userDb = new MySqlUserDataConnection();
	}
	
	/**
	 * -> salt
	 * 	<- password
	 * -> loggedIn
	 * @param username 
	 */
	public boolean login(String username) {
		client.sendSignedAndEncryptedText(userDb.getSalt(username));
		
		String passwordHash = client.readMessageAndDecrypt();
		String passwordHashDb = userDb.getPassword(username);
		
		return passwordHash.equals(passwordHashDb);
	}
}
