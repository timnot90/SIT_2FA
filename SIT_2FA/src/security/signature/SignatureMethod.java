package security.signature;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignatureMethod {
	/**
	 * sign a given text with the private key
	 * @param text
	 * 		: text to sign
	 * @param privK
	 * 		: private key to sign text with
	 * @return
	 * 		: signature as String (Base64 encoded)
	 */
	public String sign(String text, PrivateKey privK);

	public String sign(byte[] text, PrivateKey privK);
	
	/**
	 * verifies a given signature with the public key
	 * @param signature
	 * 		: signature to verify (Base64 encoded)
	 * @param pubK
	 * 		: public key for verification
	 * @param text
	 * 		: text, that was signed
	 * @return
	 * 		: true - valid signature, false - invalid signature
	 */
	boolean validSignature(String signature, String text, PublicKey pubK);
}
