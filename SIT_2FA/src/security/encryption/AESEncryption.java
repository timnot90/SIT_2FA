package security.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESEncryption implements SymmetricEncryption {
	private String settings = "AES/CBC/PKCS5Padding";
	private IvParameterSpec parameterSpec = new IvParameterSpec("g7k30os4g7k30os4".getBytes());
	
	private Encoder b64Encoder = Base64.getEncoder();
	private Decoder b64Decoder = Base64.getDecoder();
	
	@Override
	public String encrypt(String plainText, SecretKey key) {
		byte[] encrypted = null;
		try {
			Cipher cipher = Cipher.getInstance(settings);
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
		    cipherOutputStream.write(plainText.getBytes("UTF-8"));
		    cipherOutputStream.flush();
		    cipherOutputStream.close();
		    encrypted = outputStream.toByteArray();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b64Encoder.encodeToString(encrypted);
	}

	@Override
	public String decrypt(String cipherText, SecretKey key) {
		String decrypted = "";
		CipherInputStream cipherInputStream = null;
		try {
			Cipher cipher = Cipher.getInstance(settings);
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    ByteArrayInputStream inStream = new ByteArrayInputStream(b64Decoder.decode(cipherText));
		    cipherInputStream = new CipherInputStream(inStream, cipher);
		    byte[] buf = new byte[1024];
		    int bytesRead;
		    while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
		        outputStream.write(buf, 0, bytesRead);
		    }

		    decrypted = new String(outputStream.toByteArray());

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cipherInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return decrypted;
	}

}
