package client.action;

import client.ClientConnetcion;

public class RegisterAction {
	private ClientConnetcion connection;
	
	public RegisterAction(ClientConnetcion connection) {
		this.connection = connection;
	}
	
	public boolean register(String username, String password, String secret) {
		return false;
	}
}
