package webserver;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Webserver {

	public static final int WEBSERVER_PORT = 8000;
	
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(WEBSERVER_PORT), 0);
		
		HttpContext context = server.createContext("/Token", new TokenHandler());
        context.getFilters().add(new ParameterFilter());
        
        server.setExecutor(null); // creates a default executor
        server.start();
		System.out.println("Webserver started on port " + WEBSERVER_PORT);
	}
}