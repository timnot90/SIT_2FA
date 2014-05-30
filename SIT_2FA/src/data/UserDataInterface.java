package data;

import java.time.LocalDateTime;

public interface UserDataInterface {
	public boolean createUser(String username, String password, String secret, String salt);
	public void setToken(String token);
	
	public String getPassword(String username);
	public String getSalt(String username);
	public String getToken(String username);
	public LocalDateTime getExperationDate(String username);
}
