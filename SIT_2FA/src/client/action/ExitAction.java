package client.action;

import javax.crypto.SecretKey;

import security.encryption.AESEncryption;
import client.ClientConnection;

public class ExitAction {
	private ClientConnection connection;
	private SecretKey key;
	private AESEncryption symEncription;

	public ExitAction(ClientConnection connection, SecretKey key) {
		this.connection = connection;
		this.key = key;
		this.symEncription = new AESEncryption();
	}
	
	public void exit() {
		connection.sendMessage(symEncription.encrypt("exit", key));
		connection.sendMessage(symEncription.encrypt("exit", key));
		symEncription.decrypt(connection.readMessage(), key);
	}
}
