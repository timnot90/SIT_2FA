package webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.MySqlUserDataConnection;
import data.UserDataInterface;

public class TokenHandler implements HttpHandler {

	private static Encoder b64Encoder = Base64.getEncoder();

	private static String RELATIVE_PATH_ROOT;
	private final UserDataInterface db = new MySqlUserDataConnection();
	private static final int HASH_ITERATIONS = 100;

	public TokenHandler() {
		super();
		try {
			RELATIVE_PATH_ROOT = URLDecoder.decode(TokenHandler.class
					.getProtectionDomain().getCodeSource().getLocation()
					.getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
	}

	public void handle(HttpExchange httpExchange) throws IOException {

		String requestMethod = httpExchange.getRequestMethod().toUpperCase();

		if (requestMethod.equals("POST")) {
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>) (httpExchange
					.getAttribute("parameters"));

			String username = params.get("username").toString();
			String receivedSum = params.get("sum").toString();

			Integer randomNumber = Integer.parseInt(db.getToken(username));
			Integer secret = Integer.parseInt(db.getSecret(username));
			String expectedSum = hash(((Integer) (randomNumber + secret))
					.toString());

			LocalDateTime expirationDate = db.getExpirationDate(username);

			// check if the token is still valid
			if (expirationDate.isAfter(LocalDateTime.now().plusSeconds(6))) {
				if (receivedSum.equals(expectedSum)) {
					sendHtml(httpExchange, "tokenOk.html");
					db.setIsAuthenticatedWithToken(username, true);
				} else {
				}
			} else {
				sendHtml(httpExchange, "tokenFalse.html");
			}
		} else if (requestMethod.equals("GET")) {
			sendHtml(httpExchange, "token.html");
		}
	}

	private void sendHtml(HttpExchange httpExchange, String fileName) {

		String responseBody;
		try {
			responseBody = getHtmlFileContent("/webserver/html/" + fileName);

			httpExchange.getResponseHeaders().set("content-type", "text/html");
			httpExchange.sendResponseHeaders(200, responseBody.length());

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					httpExchange.getResponseBody()));
			out.write(responseBody);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getHtmlFileContent(String fileName) throws IOException {
		File file = new File(URLDecoder.decode(RELATIVE_PATH_ROOT, "UTF-8")
				+ fileName);
		byte[] content = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
		in.read(content);
		in.close();
		return new String(content, "UTF-8");
	}

	private static String hash(String text) {
		byte[] hash = null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			hash = digest.digest(text.getBytes("UTF-8"));
			for (int i = 0; i < HASH_ITERATIONS; i++) {
				digest.reset();
				hash = digest.digest(hash);
			}
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
