package client;

public class TestMain {
	public static void main (String[] args) {
		Client c = new Client();
		c.connect();
		c.initializeKeyExchange();
		c.register("rene", "hallo", "12345");
		c.login("rene", "hallo");
		c.generateToken();
		c.checkForSecondAuthentication();
		c.exit();
		System.out.println("finished");
	}
}
