package demo.common.util.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AesCrypto {

	private static final Logger logger = LoggerFactory.getLogger(AesCrypto.class);
	
	private static final String aesKey = "32BYTESPRING_XML_SETTING_AESCRYP";
	private static final String aesKey32 = "32BYTESPRING_XML_SETTING_AESCRYP32BYTESPRING_XML_SETTING_AESCRYP";

	private String iv = "16INITIALNO_SAMP";//16자리 IV(Initial Vector) SEED 개념. 무작위값을 추천하지만 그냥 써도 무방
	private IvParameterSpec ivParam;
	private GCMParameterSpec gcmSpec;
	
	/*
	  SecretKey는 암호화에 사용되는 고유한 키. 세가지 길이로만 설정할 수 있고, 그 외에는 오류가 발생.
	  AES128 = 16 bytes
	  AES192 = 24 bytes
	  AES256 = 32 bytes
	 */
	private SecretKeySpec sks;
	private SecretKeySpec sks32;
	
	public AesCrypto () throws Exception {
		sks = new SecretKeySpec(aesKey.getBytes("UTF-8"),0,16,"AES");
		sks32 = new SecretKeySpec(aesKey32.getBytes("UTF-8"),0,32,"AES");
		ivParam = new IvParameterSpec(iv.getBytes("UTF-8"));
		gcmSpec = new GCMParameterSpec(128,iv.getBytes("UTF-8"));
	}
	/**
	 * 
	 * @설명 : NoPadding 방식 Encode. 16의 배수로 암복호화 데이터가 들어와야 함
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String aesEncNoPadding(String source) throws Exception {
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			logger.debug("retData length="+source.getBytes().length);
			int dataLength = source.getBytes().length;
			if( dataLength % 16 != 0) {
				for (int i =0 ; i < (16 - (dataLength % 16)); i++) {
					source = source + " ";
				}
			}
			logger.debug("retData length="+source.getBytes().length);
			byte[] enc = cipher.doFinal(source.getBytes("UTF-8"));
			retData = Base64.getEncoder().encodeToString(enc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData enc="+retData);
		return retData;
	}
	
	/**
	 * 
	 * @설명 : NoPadding 방식 Decode.
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String aesDecNoPadding (String source) throws Exception {
		byte[] dec = Base64.getDecoder().decode(source.getBytes());
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, sks);
			retData = new String(cipher.doFinal(dec));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData dec="+retData);
		return retData;
	}

	/**
	 * 
	 * @설명 : padding 방식. 값만 들어오면 자동 처리. padding이나 nopadding이나 암호화한 값 길이는 동일함
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String aesEncPadding(String source) throws Exception {
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			byte[] enc = cipher.doFinal(source.getBytes("UTF-8"));
			retData = Base64.getEncoder().encodeToString(enc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData enc="+retData);
		return retData;
	}
	
	/**
	 * 
	 * @설명 : Padding 방식 Decode.
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String aesDecPadding (String source) throws Exception {
		byte[] dec = Base64.getDecoder().decode(source.getBytes());
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, sks);
			retData = new String(cipher.doFinal(dec));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData dec="+retData);
		return retData;
	}

	/**
	 * 
	 * @설명 : 블록 암호화. 이전 암호화 블록이 다음 암호화 블록에 영향을 줌. 최초 암호화 시작은 IvParameterSpec
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String aesEncCBC(String source) throws Exception {
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, sks, ivParam);
			byte[] enc = cipher.doFinal(source.getBytes("UTF-8"));
			retData = Base64.getEncoder().encodeToString(enc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData enc="+retData);
		return retData;
	}
	
	/**
	 * 
	 * @설명 : 블록 암호화 Decode
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String aesDecCBC (String source) throws Exception {
		byte[] dec = Base64.getDecoder().decode(source.getBytes());
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, sks, ivParam);
			retData = new String(cipher.doFinal(dec));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData dec="+retData);
		return retData;
	}

	public String aesEncGCM(String source) throws Exception {
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, sks32, gcmSpec);
			byte[] enc = cipher.doFinal(source.getBytes("UTF-8"));
			retData = Base64.getEncoder().encodeToString(enc);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData enc="+retData);
		return retData;
	}
	
	public String aesDecGCM (String source) throws Exception {
		byte[] dec = Base64.getDecoder().decode(source.getBytes());
		String retData = "";
		try {
			Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, sks32, gcmSpec);
			retData = new String(cipher.doFinal(dec));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		logger.debug("retData dec="+retData);
		return retData;
	}
	
	
	public static void main(String[] arg) {
		try {
			AesCrypto ac = new AesCrypto();
			String enc = ac.aesEncNoPadding("디지로카 요즘 인기 온라인 전용 프리미엄");
			ac.aesDecNoPadding(enc);
			
			String enc2 = ac.aesEncPadding("디지로카 요즘 인기 온라인 전용 프리미엄");
			ac.aesDecPadding(enc2);
			
			String enc3 = ac.aesEncCBC("디지로카 요즘 인기 온라인 전용 프리미엄");
			ac.aesDecCBC(enc3);
			
			String enc4 = ac.aesEncGCM("디지로카 요즘 인기 온라인 전용 프리미엄");
			ac.aesDecGCM(enc4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
