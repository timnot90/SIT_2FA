package client.action;

import javax.crypto.SecretKey;

import security.encryption.AESEncryption;
import security.encryption.SymmetricEncryption;
import client.ClientConnection;

public class GenerateTokenAction {
	private ClientConnection connection;
	private SecretKey key;
	private SymmetricEncryption symEncription;
	
	public GenerateTokenAction(ClientConnection connection, SecretKey key) {
		this.connection = connection;
		this.key = key;
		this.symEncription = new AESEncryption();
	}
	
	public String generateToken(String username) {
		connection.sendMessage(symEncription.encrypt("token", key));
		connection.sendMessage(symEncription.encrypt(username, key));
		
		String token = symEncription.decrypt(connection.readMessage(), key);
		return token;
	}
}
