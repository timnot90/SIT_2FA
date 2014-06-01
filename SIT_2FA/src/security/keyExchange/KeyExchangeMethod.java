package security.keyExchange;

import javax.crypto.SecretKey;

public interface KeyExchangeMethod {
	public void initializeKeyExchange();
	public SecretKey generateKey(byte[] publicKey);
	public String getPublicKey();
}
