package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MySqlUserDataConnection implements UserDataInterface {
	private String username = "root";
	private String password = "";
	private String address = "127.0.0.1";
	private String schema = "sit";

	private Connection connection;

	public MySqlUserDataConnection() {
		// this will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://" 
					+ address + "/" + schema + "?user=" + username 
					+ "&password=" + password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean createUser(String username, String password, String secret,
			String salt) {
		boolean created = false;
		String statement = "INSERT INTO "
				+ "users(username, password, secret, salt) "
				+ "VALUES(?,?,?,?);";
		try {
			PreparedStatement userCreationStatement = connection
					.prepareStatement(statement);

			userCreationStatement.setString(1, username);
			userCreationStatement.setString(2, password);
			userCreationStatement.setString(3, secret);
			userCreationStatement.setString(4, salt);

			userCreationStatement.execute();
			created = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return created;
	}
	
	public boolean deleteUser(String username) {
		String statement = "DELETE FROM users WHERE username=?;";
		try {
			PreparedStatement userdeleteStatement = connection
					.prepareStatement(statement);
			userdeleteStatement.setString(1, username);
			
			userdeleteStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void setToken(String token, LocalDateTime experationDate, String username) {
		String statement = "UPDATE users SET token = ?, experationDate = ?, tokenUsed = ? WHERE username = ?";
		try {
			PreparedStatement setTokenStatement = connection.prepareStatement(statement);
			setTokenStatement.setString(1, token);
			setTokenStatement.setString(2, experationDate.toString());
			setTokenStatement.setBoolean(3, false);
			setTokenStatement.setString(4, username);
			
			setTokenStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getPassword(String username) {
		String password = "";
		String statement = "SELECT password FROM users WHERE username= ? ;";
		try {
			PreparedStatement userSelectionStatement =
					connection.prepareStatement(statement);
			userSelectionStatement.setString(1, username);
			ResultSet rs = userSelectionStatement.executeQuery();
			rs.next();
			password = rs.getString("password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password;
	}

	@Override
	public String getSalt(String username) {
		String salt = "";
		String statement = "SELECT salt FROM users WHERE username= ? ;";
		try {
			PreparedStatement userSelectionStatement =
					connection.prepareStatement(statement);
			userSelectionStatement.setString(1, username);
			ResultSet rs = userSelectionStatement.executeQuery();
			rs.next();
			salt = rs.getString("salt");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salt;
	}

	@Override
	public String getToken(String username) {
		String token = "";
		String statement = "SELECT token FROM users WHERE username= ? ;";
		try {
			PreparedStatement userSelectionStatement =
					connection.prepareStatement(statement);
			userSelectionStatement.setString(1, username);
			ResultSet rs = userSelectionStatement.executeQuery();
			rs.next();
			token = rs.getString("token");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}

	@Override
	public LocalDateTime getExperationDate(String username) {
		LocalDateTime experationDate = null;
		String statement = "SELECT experationDate FROM users WHERE username= ? ;";
		try {
			PreparedStatement userSelectionStatement =
					connection.prepareStatement(statement);
			userSelectionStatement.setString(1, username);
			ResultSet rs = userSelectionStatement.executeQuery();
			rs.next();
			experationDate = LocalDateTime.parse(rs.getString("experationDate"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return experationDate;
	}

	public boolean isAuthenticatedWithToken(String username) {
		boolean isAuthenticated = false;
		String statement = "SELECT secondAuthentication FROM users WHERE username = ? ;";
		try {
			PreparedStatement autenticationStatement = connection.prepareStatement(statement);
			autenticationStatement.setString(1, username);
			
			ResultSet rs = autenticationStatement.executeQuery();
			rs.next();
			isAuthenticated = rs.getBoolean("secondAuthentication");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return isAuthenticated;
	}

	@Override
	public boolean existsUser(String username) {
		boolean exists = false;
		String statement = "SELECT username FROM users WHERE username= ? ;";
		try {
			PreparedStatement userSelectionStatement =
					connection.prepareStatement(statement);
			userSelectionStatement.setString(1, username);
			ResultSet rs = userSelectionStatement.executeQuery();
			if(rs.next())
				exists = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
	}

	@Override
	public void setTokenUsed(boolean used, String username) {
		String statement = "UPDATE users SET tokenUsed = ? WHERE username = ?";
		try {
			PreparedStatement setTokenStatement = connection.prepareStatement(statement);
			setTokenStatement.setBoolean(1, used);
			setTokenStatement.setString(2, username);
			
			setTokenStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	@Override
	public void setIsAuthenticatedWithToken(String username, boolean state) {
		String statement = "UPDATE users SET secondAuthentication = ? WHERE username = ?";
		try {
			PreparedStatement setIsAuthenticatedStatement = connection.prepareStatement(statement);
			setIsAuthenticatedStatement.setBoolean(1, state);
			setIsAuthenticatedStatement.setString(2, username);

			setIsAuthenticatedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isTokenValid(String username) {
		boolean valid = false;
		String statement = "SELECT experationDate, tokenUsed FROM users WHERE username= ? ;";
		try {
			PreparedStatement userSelectionStatement =
					connection.prepareStatement(statement);
			userSelectionStatement.setString(1, username);
			ResultSet rs = userSelectionStatement.executeQuery();
			rs.next();
			valid = (getExperationDate(username).isAfter(LocalDateTime.now()) && !rs.getBoolean("tokenUsed"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
}
