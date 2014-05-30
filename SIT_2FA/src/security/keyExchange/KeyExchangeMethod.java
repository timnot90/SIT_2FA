package security.keyExchange;

import javax.crypto.SecretKey;

public interface KeyExchangeMethod {
	public void initilizeKeyExchange();
	public SecretKey generateKey(byte[] publicKey);
	public String getPublicKey();
}
