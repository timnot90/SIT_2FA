package client.action;

import java.security.InvalidKeyException;

import javax.crypto.SecretKey;

import security.encryption.PublicKeyEncryption;
import security.encryption.RSAEncryption;
import security.keyExchange.DiffieHellmanKeyExchange;
import security.keyExchange.KeyExchangeMethod;
import client.ClientConnection;

public class KeyExchangeAction {
	private KeyExchangeMethod keyExchange;
	private ClientConnection connection;
	private PublicKeyEncryption encryption;
	
	public KeyExchangeAction(ClientConnection connection) {
		this.connection = connection;
		this.keyExchange = new DiffieHellmanKeyExchange();
	}
	
	public SecretKey doKeyExchange() {
		SecretKey key = null;
		
		keyExchange.initializeKeyExchange();
		String prime = ((DiffieHellmanKeyExchange) keyExchange).getPrime();
		String base = ((DiffieHellmanKeyExchange) keyExchange).getBase();
		String publicKey = keyExchange.getPublicKey();
		encryption = new RSAEncryption();

		try {
			connection.sendMessage(encryption.encrypt(prime.getBytes(), connection.getPublicKey()));
			connection.sendMessage(encryption.encrypt(base.getBytes(), connection.getPublicKey()));
			connection.sendMessage(encryption.encrypt(publicKey.getBytes(), connection.getPublicKey()));
			
			String serverPublicKey = connection.readMessage();
			key = keyExchange.generateKey(serverPublicKey.getBytes());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
}
