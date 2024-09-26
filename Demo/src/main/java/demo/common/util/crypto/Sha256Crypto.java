package demo.common.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sha256Crypto {

	private static final Logger logger = LoggerFactory.getLogger(Sha256Crypto.class);
	
	public static String getSalt() throws Exception {
		String salt = "";
		
		SecureRandom sr;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
		byte[] saltByte = new byte[16];
		sr.nextBytes(saltByte);
		
		salt = Base64.getEncoder().encodeToString(saltByte);
		
		return salt;
	}
	
    public static String encSah256(String source, String salt) throws Exception {
        String retData = "";
		
    	MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
	        md.update((source+salt).getBytes());
//	        md.update(salt.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
		byte[] bytes = md.digest();
		StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        retData = builder.toString();
		
		logger.debug("encSah256="+retData);
        return retData;
    }

    public static void main(String [] args) {
    	String sample = "SYSTEM";
    	try {
			String salt = "";
			for ( int i = 0 ; i < 10; i++) {
				salt = getSalt();
				encSah256(sample,salt);
			}
			logger.debug(salt);
			encSah256(sample,salt);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
