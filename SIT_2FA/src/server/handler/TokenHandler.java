package server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TokenHandler implements HttpHandler {
	public void handle(HttpExchange httpExchange) throws IOException {

		System.out.println(httpExchange.getProtocol()); 
		System.out.println(httpExchange.getRequestMethod());
		System.out.println(httpExchange.getRequestURI()); 
		System.out.println(httpExchange.getRequestHeaders().get(""));
		 if ("post".equalsIgnoreCase(httpExchange.getRequestMethod())) {
	            @SuppressWarnings("unchecked")
	            Map<String, Object> parameters =
	                (Map<String, Object>)httpExchange.getAttribute("parameters");
	            System.out.println(parameters.toString());//get("token"));
//	            InputStreamReader isr =
//	                new InputStreamReader(httpExchange.getRequestBody(),"utf-8");
//	            BufferedReader br = new BufferedReader(isr);
//	            String query = br.readLine();
	            //parseQuery(query, parameters);
	        }
		BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
		System.out.println("Token: " + br.readLine());
		br.close();
	}
}
