package server.action;

import server.ClientThread;
import data.MySqlUserDataConnection;
import data.UserDataInterface;

public class LoginAction {
	private ClientThread client;
	private UserDataInterface userDb;
	
	public LoginAction(ClientThread client) {
		this.client = client;
		userDb = new MySqlUserDataConnection();
	}
	
	public void login() {
		
	}
}
