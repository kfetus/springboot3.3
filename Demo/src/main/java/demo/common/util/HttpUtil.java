package demo.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {

	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	
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
}
