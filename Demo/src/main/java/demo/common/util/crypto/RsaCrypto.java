package demo.common.util.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * RSA 암복호화 클래스
 * @FileName : RsaCrypto.java
 * @author : 
 * @설명 :
 */
public class RsaCrypto {

	private static final Logger logger = LoggerFactory.getLogger(RsaCrypto.class);
	
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;
	
	public RsaCrypto() throws Exception {
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			String SEED = "NOW_TEST"+System.currentTimeMillis();
			SecureRandom sr = new SecureRandom(SEED.getBytes());
			keyGen.initialize(2048,sr);
			KeyPair key = keyGen.generateKeyPair();
			privateKey = (RSAPrivateKey) key.getPrivate();
			publicKey = (RSAPublicKey) key.getPublic();
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("privateKey="+privateKey);
		logger.debug("publicKey="+publicKey);
	}
	
	public String rsaEnc(String source) throws Exception {
		byte[] enc;
		String retData = "";
		Cipher cip = null;
		try {
			cip = Cipher.getInstance("RSA");
			cip.init(Cipher.ENCRYPT_MODE, privateKey);
			enc = cip.doFinal(source.getBytes());
			retData = Base64.getEncoder().encodeToString(enc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("enc data:"+retData);
		return retData;
	}
	
	public String rsaDec(String source) throws Exception {
		byte[] dec;
		String retData = "";
		if( !StringUtils.hasText(source)) {
			return source;
		}
		Cipher cip = null;
		try {
			cip = Cipher.getInstance("RSA");
			cip.init(Cipher.DECRYPT_MODE, publicKey);

			dec = cip.doFinal(Base64.getDecoder().decode(source.getBytes()));
			retData = new String(dec);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("enc data:"+retData);
		return retData;
	}
	
	public static void main(String[] args ) {
		try {
			RsaCrypto rc = new RsaCrypto();
			String strDec = rc.rsaEnc("평문으로 전달받은 공개키를 사용하기 위해 공개키 객체 생성");
			rc.rsaDec(strDec);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
