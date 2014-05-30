package security.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHash {
	private int iterations = 10000;
	private SecureRandom random;

	public PasswordHash() {
		random = new SecureRandom();
	}

	public byte[] generateSalt() {
		return random.generateSeed(10);
	}
	
	public byte[] hashPassword(String password, byte[] salt) {
		byte[] hash = null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(salt);
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
		return hash;
	}
}
