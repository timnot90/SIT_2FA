package server;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import security.util.KeyUtils;

public class Server {
	private int port = 2020;
	private ServerSocket socketService;
	private int clientID = 0;

	private String algorithm = "RSA";
	private int bit = 4096;
	private String privateKeyPath = "src" + File.separator + "server"
			+ File.separator + "private.key";
	private String publicKeyPath = "src" + File.separator + "server"
			+ File.separator + "public.key";

	private PrivateKey pk;

	public Server() {
		try {
			socketService = new ServerSocket(port);
			pk = KeyUtils.loadPrivateKey(privateKeyPath, algorithm);
		} catch (IOException e) {
			System.err.println("Could not start socket service");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startServer() {
		System.out.printf("Server started at port %d%n", port);
		try {
			while (true) {
				new Thread(new ClientThread(socketService.accept(), pk,
						++clientID)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server shut down");
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.startServer();
	}
}
