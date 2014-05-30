package client;

import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;

import security.keyExchange.DiffieHellmanKeyExchange;
import security.keyExchange.KeyExchangeMethod;
import security.encryption.PasswordHash;
import security.encryption.PublicKeyEncryption;
import security.encryption.RSAEncryption;

public class Client {
	private ClientConnetcion connection;
	private KeyExchangeMethod keyExchange;
	private PasswordHash paswordHash;
	private int id;
	
	private PublicKeyEncryption encryption;
	private SecretKey key;
	
	public Client() {
		try {
			connection = new ClientConnetcion();
			keyExchange = new DiffieHellmanKeyExchange();
			paswordHash = new PasswordHash();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * recieve the client id from the server
	 * 
	 * @return
	 * 		: the client id
	 */
	public int connect() {
		id = Integer.parseInt(connection.readMessage());
		return id;
	}
	
	/**
	 * sends the parameter for the key exchange in three steps
	 * 1. message: prime
	 * 2. message: base
	 * 3. message: public key
	 */
	public void initilizeKeyExchange() {
		keyExchange.initilizeKeyExchange();
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
	}

	public boolean login(String username, String password) {
		byte[] salt = connection.readMessage().getBytes();
		byte[] hash = paswordHash.hashPassword(password, salt);
		
		return false;
	}
}
