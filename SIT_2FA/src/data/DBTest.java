package data;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class DBTest {

	private UserDataInterface db;
	
	@Before
	public void setUp() {
		db = new MySqlUserDataConnection();
	}
	
	@Test
	public void createUserTest() {
		db.createUser("martin", "bob", "ja", "definitiv");
		String password = db.getPassword("martin");
		String salt = db.getSalt("martin");
		db.deleteUser("martin");
		
		assertEquals("definitiv", salt);
		assertEquals("bob", password);
	}
	
	@Test
	public void setTokenTest() {
		LocalDateTime experationDate =  LocalDateTime.now().plusSeconds(45);
		db.createUser("martin", "bob", "ja", "definitiv");
		db.setToken("testToken", experationDate, "martin");
		LocalDateTime dbDateTime = db.getExperationDate("martin");
		db.deleteUser("martin");
		assertEquals(experationDate, dbDateTime);
	}

	@Test
	public void existsUserTest() {
		assertFalse(db.existsUser("martin"));
		db.createUser("martin", "bob", "ja", "definitiv");
		assertTrue(db.existsUser("martin"));
		db.deleteUser("martin");
	}
}
