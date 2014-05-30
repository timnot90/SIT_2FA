package security.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtils {
	public static PrivateKey loadPrivateKey(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {

		// Read Private Key.
		byte[] encodedPrivateKey = readKeyFile(path);

		// Generate Private Key.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return privateKey;
	}

	public static PublicKey loadPublicKey(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {

		// Read Public Key.
		byte[] encodedPublicKey = readKeyFile(path);

		// Generate Private Key.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		return publicKey;
	}

	private static byte[] readKeyFile(String path) throws IOException {
		File fileKey = new File(path);
		FileInputStream fis = new FileInputStream(path);
		byte[] encodedKey = new byte[(int) fileKey.length()];
		fis.read(encodedKey);
		fis.close();
		return encodedKey;
	}

	public static void SaveKeyPair(String pathPub, String pathPriv,  KeyPair keyPair)
			throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(pathPub);
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(pathPriv);
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

	public static KeyPair generateKeyPair(String algotithm, int bit)
			throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algotithm);
		keyGen.initialize(bit);
		return keyGen.genKeyPair();
	}
}
