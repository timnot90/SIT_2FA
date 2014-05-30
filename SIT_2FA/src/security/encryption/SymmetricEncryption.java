package security.encryption;

import javax.crypto.SecretKey;

public interface SymmetricEncryption {
	public byte[] encrypt(String plainText, SecretKey key);
	public String decrypt(byte[] cipherText, SecretKey key);
}
