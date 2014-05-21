package server;

import java.net.InetSocketAddress;

import server.handler.DefaultHandler;
import server.handler.DiffieHellmanHandler;
import server.handler.TokenHandler;
import server.handler.PollingHandler;

import com.sun.net.httpserver.HttpServer;

public class Server {

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/Init", new DefaultHandler());
		server.createContext("/DifHel", new DiffieHellmanHandler());
		server.createContext("/Token", new TokenHandler());
		server.createContext("/Polling", new PollingHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Started");
	}
}