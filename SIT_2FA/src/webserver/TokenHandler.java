package webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.MySqlUserDataConnection;

public class TokenHandler implements HttpHandler {
	
	private final String RELATIVE_PATH_ROOT = TokenHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private final MySqlUserDataConnection db = new MySqlUserDataConnection();
	
	public void handle(HttpExchange httpExchange) throws IOException {

		String requestMethod = httpExchange.getRequestMethod().toUpperCase();
		
		if(requestMethod.equals("POST")) {
			
			Map params = (Map)httpExchange.getAttribute("parameters");
			
			String token = params.get("token").toString();	
			String username  = params.get("username").toString();
			
			db.setToken(token, LocalDateTime.now().plusMinutes(1), username);
			System.out.println(db.getToken(username));
			
			sendHtml(httpExchange, "tokenOk.html");
			
		} else if(requestMethod.equals("GET")) {	
			sendHtml(httpExchange, "token.html");
		}
	}
	
	private void sendHtml(HttpExchange httpExchange, String fileName) {

		String responseBody;
		try {
			responseBody = getHtmlFileContent("/webserver/html/" + fileName);
			
			httpExchange.getResponseHeaders().set("content-type", "text/html");
			httpExchange.sendResponseHeaders(200, responseBody.length());
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()));
			out.write(responseBody);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getHtmlFileContent(String pathToFile) throws IOException {
		File file = new File(RELATIVE_PATH_ROOT + pathToFile);
		byte[] content = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
			in.read(content);
		return  new String(content, "UTF-8");
	}
}
