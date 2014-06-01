package security.encryption;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;

public class RSAEncryption implements PublicKeyEncryption {
	private Encoder b64Encoder = Base64.getEncoder();
	private Decoder b64Decoder = Base64.getDecoder();
	
	  public String encrypt(byte[] text, PublicKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return b64Encoder.encodeToString(cipherText);
	  }
	  
	  public String encrypt(byte[] text, PrivateKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return b64Encoder.encodeToString(cipherText);
	  }
	  
	  public byte[] decrypt(String text, PrivateKey key) {
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      dectyptedText = cipher.doFinal(b64Decoder.decode(text));

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }

	    return dectyptedText;
	  }

	  public byte[] decrypt(String text, PublicKey key) {
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      dectyptedText = cipher.doFinal(b64Decoder.decode(text));

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }

	    return dectyptedText;
	  }
}
