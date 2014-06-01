package server.action;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import data.MySqlUserDataConnection;

public class GenerateTokenAction {
	private int tokenExpireTime = 20;
	private MySqlUserDataConnection userDb;
	private SecureRandom random;

	public GenerateTokenAction(SecureRandom random) {
		this.random = random;
		this.userDb = new MySqlUserDataConnection();
	}
	
	public String generateToken(String username) {
		String token = random.nextInt()+"";
		userDb.setToken(token, LocalDateTime.now().plusSeconds(tokenExpireTime), username);
		return token;
	}
}
