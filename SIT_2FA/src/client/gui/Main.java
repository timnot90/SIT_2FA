package client.gui;

import client.Client;

public class Main {

	private static void initializeClientEncryption(Client client) {
		//client = new Client();
		System.out.printf("id: %d%n", client.connect());
		client.initilizeKeyExchange();
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		DesktopApp desktopApp = new DesktopApp(client);
		
		initializeClientEncryption(client);
		desktopApp.startGui();
	}

}
