package security.signature;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class RSASignature implements SignatureMethod{
	
	private Signature signature;
	private Encoder b64Encoder = Base64.getEncoder();
	private Decoder b64Decoder = Base64.getDecoder();
	
	
	public RSASignature() throws NoSuchAlgorithmException {
		 signature = Signature.getInstance("SHA1withRSA");
	}
	
	@Override
	public String sign(String text, PrivateKey privK) {
		byte[] signature = null;
		try {
			this.signature.initSign(privK);
			this.signature.update(text.getBytes("UTF8"));
			signature = this.signature.sign();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b64Encoder.encodeToString(signature);
	}
	
	@Override
	public String sign(byte[] text, PrivateKey privK) {
		byte[] signature = null;
		try {
			this.signature.initSign(privK);
			this.signature.update(text);
			signature = this.signature.sign();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b64Encoder.encodeToString(signature);
	}

	@Override
	public boolean validSignature(String signatureString, String text, PublicKey pubK) {
		boolean valid = false;
		byte[] signature = b64Decoder.decode(signatureString);
		try {
			this.signature.initVerify(pubK);
			this.signature.update(text.getBytes());
			valid = this.signature.verify(signature);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}

}
