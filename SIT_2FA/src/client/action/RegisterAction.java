package client.action;

import javax.crypto.SecretKey;

import security.encryption.AESEncryption;
import security.encryption.PasswordHash;
import security.encryption.SymmetricEncryption;
import client.ClientConnection;

public class RegisterAction {
	private ClientConnection connection;
	private PasswordHash passwordHash;
	private SymmetricEncryption symEncription;
	private SecretKey key;
	
	public RegisterAction(ClientConnection connection, SecretKey key) {
		this.connection = connection;
		this.key = key;
		this.passwordHash = new PasswordHash();
		this.symEncription = new AESEncryption();
	}
	
	/**
	 * sends user information to the server. (AES encrypted)
	 * -> register 
	 * -> username, passwordHash, secret, salt
	 * 	<- created
	 * @param username
	 * @param password
	 * @param secret
	 * @return
	 */
	public boolean register(String username, String password, String secret) {
		String salt = passwordHash.generateSalt();
		String hash = passwordHash.hashPassword(password, salt);

		String message = new String(symEncription.encrypt("register", key));
		connection.sendMessage(message);
		
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(username);
		messageBuilder.append(",");
		messageBuilder.append(hash);
		messageBuilder.append(",");
		messageBuilder.append(secret);
		messageBuilder.append(",");
		messageBuilder.append(salt);
		message = new String(symEncription.encrypt(messageBuilder.toString(), key));
		connection.sendMessage(message);
		System.out.println("Wait for server");
		String created = symEncription.decrypt(connection.readMessage(), key);
		
		System.out.printf("registered: %s%n", created);
		return "true".equals(created);
	}
}
