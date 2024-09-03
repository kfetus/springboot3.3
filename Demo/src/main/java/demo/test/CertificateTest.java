package demo.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;

public class CertificateTest {

	public static void main(String[] args) throws Exception {
		String msg = "하늘에는 달이 없고, 땅에는 바람이 없습니다.\n사람들은 소리가 없고, 나는 마음이 없습니다.\n\n우주는 죽음인가요.\n인생은 잠인가요.";
		PublicKey publicKey = getPublicKey("C:/signCert.der");
		PrivateKey privateKey = getPrivateKey("C:/signPri.key");

		// 전자서명하기
		Signature signatureA = Signature.getInstance("SHA1withRSA");
		signatureA.initSign(privateKey);
		signatureA.update(msg.getBytes());
		byte[] sign = signatureA.sign();
		System.out.println("signature : " + byteArrayToHexString(sign));

		// 전사서명 검증하기
		String msgB = msg;
		Signature signatureB = Signature.getInstance("SHA1withRSA");
		signatureB.initVerify(publicKey);
		signatureB.update(msgB.getBytes());
		boolean verifty = signatureB.verify(sign);
		System.out.println("검증 결과 : " + verifty);
	}

	public static PublicKey getPublicKey(String file) throws Exception {
		X509Certificate cert = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(file));
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
			cert = (X509Certificate) certificateFactory.generateCertificate(fis);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException ie) {
				}
		}
		System.out.println(cert.getPublicKey());
		return cert.getPublicKey();
	}

	public static PrivateKey getPrivateKey(String file) throws Exception {
		// 1. 개인키 파일 읽어오기
		byte[] encodedKey = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(new File(file));
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, read);
			}
			encodedKey = bos.toByteArray();
		} finally {
			if (bos != null)
				try {
					bos.close();
				} catch (IOException ie) {
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException ie) {
				}
		}

		System.out.println("EncodedKey : " + byteArrayToHexString(encodedKey));

		// 2. 개인카 파일 분석하기
		EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(encodedKey);
		System.out.println(encryptedPrivateKeyInfo);
		System.out.println(encryptedPrivateKeyInfo.getAlgName());

		byte[] salt = new byte[8];
		System.arraycopy(encodedKey, 20, salt, 0, 8);
		System.out.println("salt : " + byteArrayToHexString(salt));
		byte[] cBytes = new byte[4];
		System.arraycopy(encodedKey, 30, cBytes, 2, 2);
		int iterationCount = byteToint(cBytes);
		System.out.println("iterationCount : " + byteArrayToHexString(cBytes));
		System.out.println("iterationCount : " + iterationCount);

		String password = "password";

		// 추출키(DK) 생성
		byte[] dk = pbkdf1(password, salt, iterationCount);
		System.out.println("dk : " + byteArrayToHexString(dk));

		// 생성된 추출키(DK)에서 처음 16바이트를 암호화 키(K)로 정의한다.
		byte[] keyData = new byte[16];
		System.arraycopy(dk, 0, keyData, 0, 16);

		// 추출키(DK)에서 암호화 키(K)를 제외한 나머지 4바이트를 SHA-1
		// 으로 해쉬하여 20바이트의 값(DIV)을 생성하고, 그 중 처음 16바이트를 초기
		// 벡터(IV)로 정의한다.
		byte[] div = new byte[20];
		byte[] tmp4Bytes = new byte[4];
		System.arraycopy(dk, 16, tmp4Bytes, 0, 4);
		
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(tmp4Bytes);
		div = digest.digest();
		System.out.println("div : " + byteArrayToHexString(div));
		byte[] iv = new byte[16];
		System.arraycopy(div, 0, iv, 0, 16);
		System.out.println("iv : " + byteArrayToHexString(iv));

		// 3. SEED로 복호화하기
		BouncyCastleProvider provider = new BouncyCastleProvider();
		Cipher cipher = Cipher.getInstance("SEED/CBC/PKCS5Padding", provider);
		Key key = new SecretKeySpec(keyData, "SEED");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		byte[] output = cipher.doFinal(encryptedPrivateKeyInfo.getEncryptedData());

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(output);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(keySpec);
		System.out.println(privateKey);
		return privateKey;

	}

	public static byte[] pbkdf1(String password, byte[] salt, int iterationCount) throws NoSuchAlgorithmException {
		byte[] dk = new byte[20]; // 생성이 의미가 없지만 한눈에 알아보라고 20바이트로 초기화
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(password.getBytes());
		md.update(salt);
		dk = md.digest();
		for (int i = 1; i < iterationCount; i++) {
			dk = md.digest(dk);
		}
		return dk;
	}

	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b & 0xff));
		}
		return sb.toString();
	}

	public static int byteToint(byte[] arr) {
		return (arr[0] & 0xff) << 24 | (arr[1] & 0xff) << 16 | (arr[2] & 0xff) << 8 | (arr[3] & 0xff);
	}
}
