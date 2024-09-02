package demo.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringUtil {

	private static final Logger logger = LogManager.getLogger(StringUtil.class);
	
	public static String unEscapeXSS(String unEscapedStr) {
//		logger.debug("========== StringUtil.unEscapeXSS String =>" + unEscapedStr);

		if (unEscapedStr == null) {
			return null;
		}
		String escapedStr = unEscapedStr.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#40;", "(").replaceAll("&#41;", ")").replaceAll("&apos;", "\'").replaceAll("&amp;", "&");
		
		logger.debug("========== StringUtil.unEscapeXSS String End =>" + escapedStr);
		return escapedStr;

	}
}
