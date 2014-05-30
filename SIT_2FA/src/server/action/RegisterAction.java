package server.action;

import data.MySqlUserDataConnection;
import data.UserDataInterface;

public class RegisterAction {

	private UserDataInterface userDb;
	
	public RegisterAction() {
		this.userDb = new MySqlUserDataConnection();
	}
	
	// username, passwordHash, secret, salt
	public boolean register(String username, String passwordHash, String secret, String salt) {
		boolean created = false;
		
		if(!userDb.existsUser(username)) {
			created = userDb.createUser(username, passwordHash, secret, salt);
		}
		
		return created;
	}

}
