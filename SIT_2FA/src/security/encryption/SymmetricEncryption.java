package security.encryption;

import javax.crypto.SecretKey;

public interface SymmetricEncryption {
	public String encrypt(String plainText, SecretKey key);
	public String decrypt(String cipherText, SecretKey key);
}
