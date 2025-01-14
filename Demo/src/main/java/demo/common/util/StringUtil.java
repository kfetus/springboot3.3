package demo.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import demo.framework.exception.BaseException;

public class StringUtil {

	private static final Logger logger = LogManager.getLogger(StringUtil.class);
	
	/**
	 * html escape. Xss 대응 치환된 문자열을 다시 태그로 변환
	 * @param unEscapedStr
	 * @return
	 */
	public static String unEscapeXSS(String unEscapedStr) {
//		logger.debug("========== StringUtil.unEscapeXSS String =>" + unEscapedStr);

		if (unEscapedStr == null) {
			return null;
		}
		String escapedStr = unEscapedStr.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#40;", "(").replaceAll("&#41;", ")").replaceAll("&apos;", "\'").replaceAll("&amp;", "&");
		
		logger.debug("========== StringUtil.unEscapeXSS String End =>" + escapedStr);
		return escapedStr;
	}
	
	/**
	 * 문자 타입 오른쪽 공백 padding
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	public static String rPadSpace(String data, int length) {
		if (data != null && data.length() <= length) {
			data = String.format("%1$-" + length + "s", data);
		} else {
			logger.error("@@@@@@ Input Data is Fault @@@@@@");
			data = String.format("%1$-" + length + "s", "");
		}
		return data;
	}

	/**
	 * 문자 타입 왼쪽 공백 padding
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	public static String lPadSpace(String data, int length) {
		if (data != null && data.length() <= length) {
			data = String.format("%1$" + length + "s", data);
		} else {
			logger.error("@@@@@@ Input Data is Fault @@@@@@");
			data = String.format("%1$" + length + "s", "");
		}
		return data;
	}

	/**
	 * 숫자 타입 왼쪽 0 padding
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	public static String lPadZero(String data, int length) {
		if (data != null && data.length() <= length) {
			data = String.format("%0" + length + "d", Long.parseLong(data));
		} else {
			logger.error("@@@@@@ Input Data is Fault @@@@@@");
			data = String.format("%0" + length + "d", 0);
		}
		return data;
	}
	
	/**
	 * 이름 마스킹
	 * @param name
	 * @return
	 */
	public static String asteriskName(String name) {
		String chgStr = "";
		if(name == null || name.length() < 2) {
			return name;
		} else if (name.length() == 2) {
			chgStr = name.substring(0, 1) + "*";
		} else {
			chgStr = name.substring(0, 1); 
			for ( int i = 0 ; i < name.length() -2; i++) {
				chgStr += "*";
			}
			chgStr += name.substring(name.length()-1);
		}
		
		return chgStr;
	}

	/**
	 * 핸드폰 번호 마스킹
	 * @param hpNo
	 * @return
	 */
	public static String asteriskHP(String hpNo) {
		String mask = "";
		
		if( hpNo.length() == 10 || hpNo.length() == 11) {
			if( hpNo.length() == 10 ) {
				mask = hpNo.substring(0,3) + "-" + hpNo.substring(3,4) + "**-" + hpNo.substring(6,8)+"**";
			} else {
				mask = hpNo.substring(0,3) + "-" + hpNo.substring(3,5) + "**-" + hpNo.substring(7,9)+"**";
			}
		} else {
			return hpNo;
		}
		
		return mask;
	}
	
	/**
	 * 주민등록번호 마스킹
	 * @param ssn
	 * @return
	 * @throws Exception
	 */
	public static String asteriskSsn(String ssn) throws Exception{
		String maskSsn = "";
		
		if(ssn.length() != 13) {
			throw new BaseException("9999","형식이 잘못되었습니다.");
		}
//		for ( int i = 0; i < ssn.length(); i++) {
//			System.out.println(ssn.charAt(i));
//		}
		maskSsn = ssn.substring(0, 6) + "-" + ssn.substring(6,7) + "******";
		
		return maskSsn;
	}

	/**
	 * 카드번호 마스킹
	 * @param cardNo
	 * @return
	 * @throws Exception
	 */
	public static String getCardNo(String cardNo) throws Exception {
		String maskStr = "";
		if(cardNo.length() != 16) {
			throw new BaseException("9999","형식이 잘못되었습니다.");
		}
		maskStr = cardNo.substring(0, 4) + "-" + cardNo.substring(4,6) + "**-**" + cardNo.substring(10,12) + "-" + cardNo.substring(12,16);
		return maskStr;
	}
	
	/**
	 * 숫자유틸을 넣어야 하나?
	 * 숫자 포맷팅(콤마)
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String getMoneyFormat(long value) throws Exception {
		String formatedStr = "";
		formatedStr = String.format("%,d", value);
		return formatedStr;
	}
	
}