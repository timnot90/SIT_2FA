package data;

import java.time.LocalDateTime;

public interface UserDataInterface {
	public boolean createUser(String username, String password, String secret, String salt);
	public boolean deleteUser(String username);
	public boolean existsUser(String username);
	public void setToken(String token, LocalDateTime experationDate, String username);
	public void setTokenUsed(boolean used, String username);
	public boolean isTokenValid(String username);
	
	public String getPassword(String username);
	public String getSalt(String username);
	public String getToken(String username);
	public LocalDateTime getExperationDate(String username);
	public boolean isAuthenticatedWithToken(String username);
}
