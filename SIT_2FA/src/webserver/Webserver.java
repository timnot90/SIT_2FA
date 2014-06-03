package webserver;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Webserver {

	public static final int WEBSERVER_PORT = 8000;

	private static Encoder b64Encoder = Base64.getEncoder();
	private static Decoder b64Decoder = Base64.getDecoder();

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(
				WEBSERVER_PORT), 0);

		ParameterFilter parameterFilter = new ParameterFilter();
		TokenHandler tokenHandler = new TokenHandler();

		server.createContext("/Token", tokenHandler).getFilters()
				.add(parameterFilter);
		server.createContext("/token", tokenHandler).getFilters()
				.add(parameterFilter);
		
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Webserver listening on port " + WEBSERVER_PORT);
	}

	public static String hashToken(String token) {
		byte[] hash = null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			hash = digest.digest(token.getBytes("UTF-8"));
			// for (int i = 0; i < iterations; i++) {
			// digest.reset();
			// hash = digest.digest(hash);
			// }
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b64Encoder.encodeToString(hash);
	}
}