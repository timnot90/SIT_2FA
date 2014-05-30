package server.action;

import data.MySqlUserDataConnection;

public class SecondAuthenticationAction {
	private MySqlUserDataConnection userDb;

	public SecondAuthenticationAction() {
		this.userDb = new MySqlUserDataConnection();
	}
	
	public boolean checkSecondAuthentication(String username) {
		boolean secondAuthenticationSuccess = false;
		
		// poll every five seconds until expired or authenticated
		while(userDb.isTokenValid(username)	&& !secondAuthenticationSuccess) {
			secondAuthenticationSuccess = userDb.isAuthenticatedWithToken(username);
			try {
				System.out.println("sleep");
			    Thread.sleep(5000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
		userDb.setTokenUsed(true, username);
		
		return secondAuthenticationSuccess;
	}
}
