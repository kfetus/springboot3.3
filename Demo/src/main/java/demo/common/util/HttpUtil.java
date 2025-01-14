package demo.common.util;

import java.net.URLDecoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {

	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	
	/**
	 * 사용자 IP. 기본적인 차례대로 리턴. Proxy등 인터넷 환경에 따라 달라지므로 단지 참조용
	 * @param req
	 * @return
	 */
	public static String getClientIp(HttpServletRequest req) {
		logger.debug("@@@@@@@@@@ client IP info={} {} {} {} {} {}",req.getHeader("X-Forwarded-For"),req.getHeader("Proxy-Client-IP")
				,req.getHeader("WL-Proxy-Client-IP"),req.getHeader("HTTP_CLIENT_IP"),req.getHeader("HTTP_X_FORWARDED_FOR"),req.getRemoteAddr());
		
		if ( req.getHeader("X-Forwarded-For") != null) {
			return req.getHeader("X-Forwarded-For");
		} else if (req.getHeader("Proxy-Client-IP") != null) {
			return req.getHeader("Proxy-Client-IP");
		} else if (req.getHeader("WL-Proxy-Client-IP") != null) {
			return req.getHeader("WL-Proxy-Client-IP");
		} else if ( req.getHeader("HTTP_CLIENT_IP") != null ) {
			return req.getHeader("HTTP_CLIENT_IP");
		} else if (req.getHeader("HTTP_X_FORWARDED_FOR") != null) {
			return req.getHeader("HTTP_X_FORWARDED_FOR");
		} else {
			return req.getRemoteAddr();
		} 
	}
	
	/**
	 * 사용자 브라우저 정보
	 * @param req
	 * @return
	 */
	public static String getClientBrowser(HttpServletRequest req) {
		String agent = req.getHeader("User-Agent");
		String cBrowser = "not Judge";
		if (agent != null) {
			if (agent.indexOf("Trident") > -1) {
				cBrowser = "MSIE";
			} else if (agent.indexOf("Edg") > -1) {
				cBrowser = "Edge";
			} else if (agent.indexOf("Chrome") > -1) {
				cBrowser = "Chrome";
			} else if (agent.indexOf("Opera") > -1) {
				cBrowser = "Opera";
			} else if (agent.indexOf("iPhone") > -1 && agent.indexOf("Mobile") > -1) {
				cBrowser = "iPhone";
			} else if (agent.indexOf("Android") > -1 && agent.indexOf("Mobile") > -1) {
				cBrowser = "Android";
			}
		}
		return cBrowser;
	}
	
	/**\
	 * 사용자 PC 모바일 구분. Spring의 device가 대체가능
	 * @param req
	 * @return
	 */
	public static String getClientAccessDevice(HttpServletRequest req) {
		String mobileType[] = {"iphone","ipod","android","windows ce","blackberry","symbian","windows phone","webos","opera mini","opera mobi","polaris","iemobile","lgtelecom","nokia","sonyericsson","lg","samsung"};
		String aDevice = "not Judge";
		
		for(String loopData : mobileType){
			if ( req.getHeader("User-Agent").toLowerCase().indexOf(loopData) != -1) {
				aDevice = "MOBILE";
				break;
			} else {
				aDevice = "PC";
			}
		}
		return aDevice;
	}
	
	/**
	 * 쿠키값 리턴. 쿠키에 저장하는 장바구니라던가, 오늘본 상품 등 용
	 * @param req
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getCookies(HttpServletRequest req, String key) throws Exception {
		String value= "";

		Cookie cookie[] = req.getCookies();
		
		if( "".equals(key)) {
			return value;
		}
		
		if( cookie != null && cookie.length > 0) {
			for(int i = 0; i < cookie.length; i++) {
				if(key == cookie[i].getName()) {
					value = URLDecoder.decode(cookie[i].getValue(), "UTF-8");
				}
			}
		}
		return value;
	}
	
}
