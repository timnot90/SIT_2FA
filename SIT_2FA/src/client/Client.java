package client;

import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;

import client.action.KeyExchangeAction;
import client.action.LoginAction;
import client.action.RegisterAction;
import security.encryption.PasswordHash;

public class Client {
	private ClientConnetcion connection;
	private PasswordHash paswordHash;
	private int id;
	
	private SecretKey key;
	
	public Client() {
		try {
			connection = new ClientConnetcion();
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
		this.key = new KeyExchangeAction(connection).doKeyExchange();
	}

	public boolean login(String username, String password) {
		return new LoginAction(connection).login(username, password);
	}

	/**
	 * register new user.
	 * password confirmation check in desktop app or here?
	 * @param username
	 * @param password
	 * @param secret
	 * @return
	 */
	public boolean register(String username, String password, String secret) {
		return new RegisterAction(connection).register(username, password, secret);
	}
	
	/**
	 * wait for the server response.
	 * 
	 * @return
	 * 		true - second authentication successful
	 * 		false - second authentication unsuccessful (timeout, secret did not match)
	 */
	public boolean checkForSecondAuthentication() {
		return false;
	}
}
