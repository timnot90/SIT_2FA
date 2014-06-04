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
}