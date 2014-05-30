package data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DBTest {

	private UserDataInterface db;
	
	@Before
	public void setUp() {
		db = new MySqlUserDataConnection();
	}
	
	@Test
	public void test() {
		assertTrue(true);
	}

}
