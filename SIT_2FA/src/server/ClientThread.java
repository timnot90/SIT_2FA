package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import security.signature.RSASignature;
import security.encryption.RSAEncryption;

public class ClientThread extends Thread {

	private Socket clientSocket;
	private int clientID;

	private RSASignature signature;
	private PrivateKey privK;
	private SecretKey key;

	private BufferedReader in;
	private PrintWriter out;
	Gson gson = new Gson();

	public ClientThread(Socket clientSocket, PrivateKey pk, int clientID) {
		this.clientSocket = clientSocket;
		this.clientID = clientID;
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
		sendSignedText(clientID+"");
		
		KeyExchange exchange = new KeyExchange(privK, this);
		key = exchange.doKeyExchange();
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
		System.out.printf("json: %s%n", json);

		out.println(json);
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
}
