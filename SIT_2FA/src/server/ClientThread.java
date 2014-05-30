package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.SecretKey;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import security.signature.RSASignature;
import security.encryption.AESEncryption;
import security.encryption.SymmetricEncryption;
import server.action.GenerateTokenAction;
import server.action.KeyExchangeAction;
import server.action.LoginAction;
import server.action.RegisterAction;
import server.action.SecondAuthenticationAction;

public class ClientThread extends Thread {

	private Socket clientSocket;
	private int clientID;

	private RSASignature signature;
	private PrivateKey privK;
	private SymmetricEncryption symEncription;
	private SecretKey key;
	private SecureRandom random;

	private BufferedReader in;
	private PrintWriter out;
	Gson gson = new Gson();

	public ClientThread(Socket clientSocket, PrivateKey pk, int clientID) {
		this.clientSocket = clientSocket;
		this.clientID = clientID;
		this.symEncription = new AESEncryption();
		this.random = new SecureRandom();
		openConnectionStreams();
		try {
			this.signature = new RSASignature();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.privK = pk;
	}

	private void openConnectionStreams() {
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean exit = false;
		
		sendSignedText(clientID+"");
		key = new KeyExchangeAction(privK, this).doKeyExchange();
		
		while(!exit) {
			String input[] = new String[2];
			for(int i = 0; i < input.length; i++) {
				input[i] = readMessageAndDecrypt();
			}
			String message = "";
			System.out.printf("action: %s%n",input[0]);
			switch(input[0])
			{
			case "register":
				String[] registerData = input[1].split(","); // username, passwordHash, secret, salt
				message = new RegisterAction().register(registerData[0],registerData[1],registerData[2],registerData[3]) ? "true" : "false";
				break;
			case "login":
				message = new LoginAction(this).login(input[1]) ? "true" : "false";
				break;
			case "second":
				message = new SecondAuthenticationAction().checkSecondAuthentication(input[1]) ? "true" : "false";
				break;
			case "token":
				message = new GenerateTokenAction(random).generateToken(input[1]);
				break;
			case "exit":
				message = "exit";
				exit = true;
			}
			sendSignedAndEncryptedText(message);
		}
		System.out.printf("Session with %d finished%n", clientID);
	}

	/**
	 * send the given message with a signature as json
	 * @param text
	 */
	public void sendSignedText(String text) {
		String signature = this.signature.sign(text, privK);
		String json = "";

		String[] messages;
		messages = new String[] { signature, text };
		json = gson.toJson(messages);

		out.println(json);
	}
	
	/**
	 * send the given message with a signature and encrypted as json
	 * @param text
	 */
	public void sendSignedAndEncryptedText(String text) {
		sendSignedText(symEncription.encrypt(text, key));
	}

	public String readMessage() {
		String input = "";
		try {
			input = gson.fromJson(in.readLine(), String.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	
	public String readMessageAndDecrypt() {
		return symEncription.decrypt(readMessage(), key);
	}
}
