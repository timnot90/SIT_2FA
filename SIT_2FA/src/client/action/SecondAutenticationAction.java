package client.action;

import javax.crypto.SecretKey;

import security.encryption.AESEncryption;
import security.encryption.SymmetricEncryption;
import client.ClientConnetcion;

public class SecondAutenticationAction {
	private ClientConnetcion connection;
	private SecretKey key;
	private SymmetricEncryption symEncription;
	
	public SecondAutenticationAction(ClientConnetcion connection, SecretKey key) {
		this.connection = connection;
		this.key = key;
		this.symEncription = new AESEncryption();
	}
	
	/**
	 * blocks the thread, until server response
	 * @param username
	 * @return
	 */
	public boolean checkSecondAuthentication(String username) {
		connection.sendMessage(symEncription.encrypt("second", key));
		connection.sendMessage(symEncription.encrypt(username, key));
		String secondAutenticationSuccess = "";
		while(secondAutenticationSuccess.equals("")) {
			secondAutenticationSuccess = symEncription.decrypt(connection.readMessage(), key);
		}
		return secondAutenticationSuccess.equals("true");
	}
}
