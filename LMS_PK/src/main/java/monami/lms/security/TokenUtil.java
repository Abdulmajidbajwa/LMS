package monami.lms.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;

public class TokenUtil {

	
	public static final String MAGIC_KEY = "LMSAppliance";
	
	public static String createToken() {
		return computeSignature();
	}
 
	public static String computeSignature() {
		
		long expires = System.currentTimeMillis() + 1000L * 60 * 60;
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(expires);
		signatureBuilder.append(TokenUtil.MAGIC_KEY);
 
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!");
		}
		return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
	}
 
	public static String getUserNameFromToken(String authToken) {
		if (authToken == null) {
			return null;
		}
		String[] parts = authToken.split(":");
		return parts[0];
	}
 
	public static boolean validateToken(String authToken) {
		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[0]);
		return expires >= System.currentTimeMillis();
	}
}
