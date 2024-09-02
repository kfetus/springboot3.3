package demo.framework.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;


public class HTMLTagFilterRequestWrapper  extends HttpServletRequestWrapper {

	private final Logger logger = LogManager.getLogger(HTMLTagFilterRequestWrapper.class);

	private byte[] reqStreamData;

	public HTMLTagFilterRequestWrapper(HttpServletRequest request) {
		super(request);

		try {
			String reqAccept = request.getHeader("accept");
			//jquery 든 fetch든 ajax 요청시 필수 셋팅 헤더. 셋팅하디 않으면 content-type이 application/x-www-form-urlencoded로 들어와서 @RequestBody가 먹히지 않게 됨
			String reqContentType = request.getHeader("content-type");
//			String ajaxRequest = request.getHeader("x-requested-with"); jquery 용도 
			logger.debug("========== HTMLTagFilterRequestWrapper 생성자 request.getRequestURI()="+request.getRequestURI()+", reqAccept="+reqAccept);
			logger.debug("========== HTMLTagFilterRequestWrapper reqContentType="+reqContentType);
			
			if( (reqAccept != null && reqContentType != null) && 
					(reqAccept.indexOf("application/json") > -1 || reqContentType.indexOf("application/json") > -1 || reqContentType.indexOf("multipart/form-data") > -1 ) ) {
//				logger.debug("========== HTMLTagFilterRequestWrapper InputStream 가로채기");		
				InputStream is = request.getInputStream();
				this.reqStreamData = handleXSS(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("========== HTMLTagFilterRequestWrapper 생성자 종료");
	}

    private byte[] handleXSS(byte[] data) {
        String strData = new String(data);
//        logger.debug("========== HTMLTagFilterRequestWrapper.handleXSS byte=>"+strData);
        strData = handleXSS(strData);
		return strData.getBytes();
    }

	private String handleXSS(String parameter) {
//		logger.debug("========== HTMLTagFilterRequestWrapper.handleXSS String =>" + parameter);

		if (parameter == null) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i < parameter.length(); i++) {
			char c = parameter.charAt(i);
			switch (c) {
			case '<':
				strBuff.append("&lt;");
				break;
			case '>':
				strBuff.append("&gt;");
				break;
			case '&':
				strBuff.append("&amp;");
				break;
//			case '\"':
//				strBuff.append("&quot;");
//				break;
			case '\'':
				strBuff.append("&apos;");
				break;
			case '(':
				strBuff.append("&#40;");
				break;
			case ')':
				strBuff.append("&#41;");
				break;
			default:
				strBuff.append(c);
				break;
			}
		}
		parameter = strBuff.toString();
//		logger.debug("========== HTMLTagFilterRequestWrapper.handleXSS End String =>" + parameter);
		return parameter;

	}

	//request.getInputStream(); 한번 실행되면 해당 InputStream 소실됨. 카피했던 InputStream 리턴
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (this.reqStreamData == null) {
			return super.getInputStream();
		}

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.reqStreamData);

		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public boolean isFinished() {
				return false;
			}
		};
	}

	@Override
	public String getQueryString() {
//		logger.debug("========== HTMLTagFilterRequestWrapper.getQueryString");
		return handleXSS(super.getQueryString());
	}

	@Override
	public String getParameter(String parameter) {
//		logger.debug("========== HTMLTagFilterRequestWrapper.getParameter"+parameter);
		return handleXSS(super.getParameter(parameter));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> params = super.getParameterMap();
//		logger.debug("========== HTMLTagFilterRequestWrapper.getParameterMap"+params);
		if (params != null) {
			params.forEach((key, value) -> {
				for (int i = 0; i < value.length; i++) {
					value[i] = handleXSS(value[i]);
				}
			});
		}
		return params;
	}

	@Override
	public String[] getParameterValues(String parameter) {
//		logger.debug("========== HTMLTagFilterRequestWrapper.getParameterValues=>"+parameter);
		String[] params = super.getParameterValues(parameter);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				params[i] = handleXSS(params[i]);
			}
		}
		return params;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), "UTF_8"));
	}
}