package client.action;

import javax.crypto.SecretKey;

import client.ClientConnetcion;
import security.encryption.AESEncryption;
import security.encryption.PasswordHash;
import security.encryption.SymmetricEncryption;

public class LoginAction {
	private PasswordHash passwordHash;
	private ClientConnetcion connection;
	private SecretKey key;
	private SymmetricEncryption symEncription;
	
	public LoginAction(ClientConnetcion connection, SecretKey key) {
		this.connection = connection;
		this.key = key;
		this.passwordHash = new PasswordHash();
		this.symEncription = new AESEncryption();
	}
	
	/**
	 * -> login
	 * -> <username>
	 * 	<- <salt>
	 * -> <passwordHash>
	 * 	<- loggedIn
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean login(String username, String password) {		
		connection.sendMessage(symEncription.encrypt("login", key));
		connection.sendMessage(symEncription.encrypt(username, key));
		
		if (!username.equals("") && !password.equals("") && username != null
				&& password != null) {

			String salt = symEncription.decrypt(connection.readMessage(), key);
			String hash = passwordHash.hashPassword(password, salt);
			
			connection.sendMessage(new String(symEncription.encrypt(hash, key)));
		}
		String loggedIn = symEncription.decrypt(connection.readMessage(), key);
		
		System.out.printf("logged in: %s%n", loggedIn);
		return "true".equals(loggedIn);
	}
}
