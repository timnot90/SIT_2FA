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
		try {
			PreparedStatement userCreationStatement = connection
					.prepareStatement("INSERT INTO "
							+ "users(username, password, secret, salt) "
							+ "VALUES(?,?,?,?);");

			userCreationStatement.setString(1, username);
			userCreationStatement.setString(2, password);
			userCreationStatement.setString(3, secret);
			userCreationStatement.setString(4, salt);

			return userCreationStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
	public void setToken(String token) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToken(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDateTime getExperationDate(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
