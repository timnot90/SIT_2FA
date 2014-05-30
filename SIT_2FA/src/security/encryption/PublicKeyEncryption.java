package security.encryption;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface PublicKeyEncryption {
	/**
	 * Encrypt the plain text using public key.
	 * 
	 * @param text
	 *            : original plain text
	 * @param key
	 *            : The public key
	 * @return Encrypted text
	 * @throws InvalidKeyException
	 * @throws java.lang.Exception
	 */
	public String encrypt(byte[] text, PublicKey key)
			throws InvalidKeyException;

	/**
	 * Encrypt the plain text using private key.
	 * 
	 * @param text
	 *            : original plain text
	 * @param key
	 *            : The public key
	 * @return Encrypted text
	 * @throws java.lang.Exception
	 */
	public String encrypt(byte[] text, PrivateKey key);

	/**
	 * Decrypt text using public key.
	 * 
	 * @param text
	 *            : encrypted text
	 * @param key
	 *            : The private key
	 * @return plain text
	 * @throws java.lang.Exception
	 */
	public byte[] decrypt(String text, PublicKey key);

	/**
	 * Decrypt text using private key.
	 * 
	 * @param text
	 *            : encrypted text
	 * @param key
	 *            : The private key
	 * @return plain text
	 * @throws java.lang.Exception
	 */
	public byte[] decrypt(String text, PrivateKey key);
}
