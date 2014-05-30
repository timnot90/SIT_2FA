package server;

import data.MySqlUserDataConnection;
import data.UserDataInterface;

public class Login {
	private ClientThread client;
	private UserDataInterface userDb;
	
	public Login(ClientThread client) {
		this.client = client;
		userDb = new MySqlUserDataConnection();
	}
	
	public void login() {
		
	}
}
