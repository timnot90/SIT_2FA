package webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.MySqlUserDataConnection;
import data.UserDataInterface;

public class TokenHandler implements HttpHandler {
	
	// TODO this does not give a valid path if there are spaces in the path! See solution below...
	private final String RELATIVE_PATH_ROOT = TokenHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private final UserDataInterface db = new MySqlUserDataConnection();
	
	public void handle(HttpExchange httpExchange) throws IOException {

		String requestMethod = httpExchange.getRequestMethod().toUpperCase();
		
		if(requestMethod.equals("POST")) {
			
			Map params = (Map)httpExchange.getAttribute("parameters");

			String username  = params.get("username").toString();
			String sentToken = params.get("token").toString();	
			
			String expectedToken = db.getToken(username);
			
			if(sentToken.equals(expectedToken)) {
				sendHtml(httpExchange, "tokenOk.html");
				db.setIsAuthenticatedWithToken(username, true);
			} else {
				sendHtml(httpExchange, "tokenFalse.html");	
			}			
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
		File file = new File(URLDecoder.decode(RELATIVE_PATH_ROOT, "UTF-8") + pathToFile);
		byte[] content = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
			in.read(content);
		return  new String(content, "UTF-8");
	}
}
