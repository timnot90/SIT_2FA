package security.keyExchange;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;

public class DiffieHellmanKeyExchange implements KeyExchangeMethod {

	private PrivateKey privateKey;
	private PublicKey ownPublicKey;
	private PublicKey externalPublicKey;

	private BigInteger p;
	private BigInteger g;
	
	private Encoder b64Encoder = Base64.getEncoder();
	private Decoder b64Decoder = Base64.getDecoder();
	
	public DiffieHellmanKeyExchange() {
		SecureRandom random = new SecureRandom();
		this.p = BigInteger.probablePrime(512, random);
		this.g = BigInteger.probablePrime(512, random);
	}
	
	public DiffieHellmanKeyExchange(BigInteger p, BigInteger g) {
		this.p = p;
		this.g = g;
	}
	
	public DiffieHellmanKeyExchange(String p, String g) {
		this.p = new BigInteger(p);
		this.g = new BigInteger(g);
	}
	
	public void initilizeKeyExchange() {
		DHParameterSpec params = new DHParameterSpec(p, g);
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("DH");
			kpg.initialize(params);
			KeyPair keyPair = kpg.generateKeyPair();

			privateKey = keyPair.getPrivate();
			ownPublicKey = keyPair.getPublic();
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SecretKey generateKey(byte[] publicKey) {
		SecretKey key = null;
		try {// Convert the public key bytes into a PublicKey object
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
					b64Decoder.decode(publicKey));
			KeyFactory keyFact;
			keyFact = KeyFactory.getInstance("DH");
			externalPublicKey = keyFact.generatePublic(x509KeySpec);

			// Prepare to generate the secret key with the private key and
			// public key of the other party
			KeyAgreement ka = KeyAgreement.getInstance("DH");
			ka.init(privateKey);
			ka.doPhase(externalPublicKey, true);
			
			key = ka.generateSecret("AES");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DH finished");
		System.out.printf("Key: %s%n", b64Encoder.encodeToString(key.getEncoded()));
		return key;
	}

	public String getPublicKey() {
		return b64Encoder.encodeToString(ownPublicKey.getEncoded());
	}
	
	public String getPrime() {
		return p.toString();
	}
	
	public String getBase() {
		return g.toString();
	}
}
