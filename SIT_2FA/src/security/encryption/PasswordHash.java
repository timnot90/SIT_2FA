package security.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class PasswordHash {
	private int iterations = 10000;
	private SecureRandom random;

	private Encoder b64Encoder = Base64.getEncoder();
	private Decoder b64Decoder = Base64.getDecoder();
	
	public PasswordHash() {
		random = new SecureRandom();
	}

	public String generateSalt() {
		byte[] salt = new byte[20];
		random.nextBytes(salt);
		return b64Encoder.encodeToString(salt);
	}
	
	public String hashPassword(String password, String salt) {
		byte[] hash = null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(b64Decoder.decode(salt));
			hash = digest.digest(password.getBytes("UTF-8"));
			for (int i = 0; i < iterations; i++) {
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
