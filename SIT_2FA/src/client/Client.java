package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;

import security.encryption.PasswordHash;
import client.action.ExitAction;
import client.action.GenerateTokenAction;
import client.action.KeyExchangeAction;
import client.action.LoginAction;
import client.action.RegisterAction;
import client.action.SecondAuthenticationAction;

public class Client {
	private ClientConnection connection;
	@SuppressWarnings("unused")
	private PasswordHash paswordHash;
	private int id;
	private String username;
	
	private SecretKey key;
	
	public Client() {
		try {
			connection = new ClientConnection();
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
	 * receive the client id from the server
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
	public void initializeKeyExchange() {
		this.key = new KeyExchangeAction(connection).doKeyExchange();
	}

	public boolean login(String username, String password) {
		boolean loggedIn =  new LoginAction(connection, key).login(username, password);
		
		if(loggedIn) {
			this.username = username;
		}
		
		return loggedIn;
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
		return new RegisterAction(connection, key).register(username, password, secret);
	}
	
	public String generateToken() {
		String token = new GenerateTokenAction(connection, key).generateToken(username);
		System.out.println(token);
		return token;
	}
	
	/**
	 * wait for the server response.
	 * 
	 * @return
	 * 		true - second authentication successful
	 * 		false - second authentication unsuccessful (timeout, secret did not match)
	 */
	public boolean checkForSecondAuthentication() {
		boolean success = new SecondAuthenticationAction(connection, key).checkSecondAuthentication(username);
		System.out.printf("2. autentication %s%n", success);
		return success;
	}
	
	public void exit() {
		new ExitAction(connection, key).exit();
	}
}
