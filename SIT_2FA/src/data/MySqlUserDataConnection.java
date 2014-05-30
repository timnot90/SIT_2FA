package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MySqlUserDataConnection implements UserDataInterface{
	private String username ="sit";
	private String password = "qwer1234";
	private String address = "127.0.0.1";
	private String schema = "sit";
	
	private Connection connection;
		
	public MySqlUserDataConnection() {
	      // this will load the MySQL driver, each DB has its own driver
	      try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(address, username, password);
			connection.setSchema(schema);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean createUser(String username, String password, String secret,
			String salt) {
		try {
			PreparedStatement userCreationStatement = 
					connection.prepareStatement("INSERT INTO "
							+ "users(username, password, secret, salt) "
							+ "VALUES(?,?,?,?)");
			
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

	@Override
	public void setToken(String token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPassword(String username) {
		// TODO Auto-generated method stub
		return null;
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
