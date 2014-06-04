package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import security.encryption.PublicKeyEncryption;
import security.encryption.RSAEncryption;
import security.signature.RSASignature;
import security.signature.SignatureMethod;
import security.util.KeyUtils;

import com.google.gson.Gson;

public class ClientConnection {

	private String serverAddress = "127.0.0.1";
	private int serverPort = 2020;
	private Socket clientSocket;
	private SignatureMethod signature;
	@SuppressWarnings("unused")
	private PublicKeyEncryption encryption;
	private PublicKey pubK;

	private BufferedReader in;
	private PrintWriter out;
	private Gson gson;
	
	private String algorithm = "RSA"; // 4094 bit keys
	private String publicKeyPath = "src" + File.separator + "client"
			+ File.separator + "public.key";
	
	public ClientConnection() throws UnknownHostException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		pubK = KeyUtils.loadPublicKey(publicKeyPath, algorithm);
		clientSocket = new Socket(serverAddress, serverPort);
		signature = new RSASignature();
		encryption = new RSAEncryption();
		gson = new Gson();
		openConnectionStreams();
	}
	
	public ClientConnection(String serverAddress, int serverPort) {
		try {
			clientSocket = new Socket(serverAddress, serverPort);
		} catch (UnknownHostException e) {
			System.err.println("Coul not find Server " + serverAddress);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		openConnectionStreams();
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	public void openConnectionStreams() {
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		closeConnectionStreams();
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnectionStreams() {
		try {
			in.close();
		} catch (IOException e) {
			System.err.println("error while trying to close input stream");
			e.printStackTrace();
		}
		out.close();
	}
	
	/**
	 * reads a json and verifies the signature
	 * splits the json into a String[]
	 * [0] - signature
	 * [1] - message
	 * @return
	 */
	public String readMessage() {
		String message = "";
		try {
			String[] messages = gson.fromJson(in.readLine(), String[].class);
			if(this.signature.validSignature(messages[0], messages[1], pubK)) {
				message = messages[1];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	public void sendMessage(String message) {
		String json = gson.toJson(message);
		out.println(json);
	}
	
	public PublicKey getPublicKey() {
		return pubK;
	}
}
