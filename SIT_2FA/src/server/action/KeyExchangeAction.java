package server.action;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.SecretKey;

import security.encryption.PublicKeyEncryption;
import security.encryption.RSAEncryption;
import security.keyExchange.DiffieHellmanKeyExchange;
import security.keyExchange.KeyExchangeMethod;
import server.ClientThread;

public class KeyExchangeAction {
	private PrivateKey privateKey;

	private PublicKeyEncryption encryption;

	private BigInteger p;
	private BigInteger g;
	private KeyExchangeMethod keyExchange;
	private String publicKey;

	private ClientThread client;
	
	private Encoder b64Encoder = Base64.getEncoder();
	private Decoder b64Decoder = Base64.getDecoder();

	public KeyExchangeAction(PrivateKey pk, ClientThread client) {
		this.privateKey = pk;
		this.encryption = new RSAEncryption();
		this.client = client;
	}

	public SecretKey doKeyExchange() {
		SecretKey key = null;
		readParameterForKeyExchange();

		keyExchange = new DiffieHellmanKeyExchange(p, g);
		keyExchange.initializeKeyExchange();
		
		client.sendSignedText(keyExchange.getPublicKey());
		key = keyExchange.generateKey(publicKey.getBytes());		
		return key;
	}

	private void readParameterForKeyExchange() {
		byte[][] input = new byte[3][];
		for (int i = 0; i < 3; i++) {
			input[i] = encryption.decrypt(client.readMessage(), privateKey);
		}
		p = new BigInteger(new String(input[0]));
		g = new BigInteger(new String(input[1]));
		publicKey = new String(input[2]);
	}
}
