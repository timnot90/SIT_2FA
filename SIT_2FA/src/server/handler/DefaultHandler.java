package server.handler;

import java.io.IOException;
import java.io.OutputStream;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DefaultHandler implements HttpHandler {
	public void handle(HttpExchange httpExchange) throws IOException {
		System.out.println("Request");
		JSONObject response = new JSONObject();
		
		try {
			response.put("name", "foo");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			httpExchange.getResponseHeaders().set("content-type", "application/json");
			httpExchange.sendResponseHeaders(200, response.toString().length());
			OutputStream os = httpExchange.getResponseBody();
			
			os.write(response.toString().getBytes("utf-8"));
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
