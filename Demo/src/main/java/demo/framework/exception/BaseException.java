package demo.framework.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;

	String message;
	
	String errorCode;
	
	public String getMessage() {
		return message;
	}

	public String setMessage(String defaultMessage) {
		return this.message = defaultMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	
	public BaseException(String defaultMessage) {
		this.message = defaultMessage;
	}

	public BaseException(String code, String message ) {
		this.errorCode = code;
		this.message = message;
	}

	public BaseException(String defaultMessage,Throwable throwable) {
		super(defaultMessage, throwable);
	}

	public BaseException(String defaultMessage, String code ,Throwable throwable) {
		super(defaultMessage, throwable);
		this.errorCode = code;
	}
	
	/**
	 * 에러메세지를 프로퍼티 다국어로 관리할때 쓰는 용도
	 * @param messageSource
	 * @param messageKey
	 * @param messageParameters
	 * @param defaultMessage
	 * @param locale
	 * @param throwable
	 */
	public BaseException(MessageSource messageSource, String messageKey, Object[] messageParameters, String defaultMessage, Locale locale, Throwable throwable) {
		super(throwable);

		if(locale == null) locale = Locale.getDefault();
		this.message = messageSource.getMessage(messageKey, messageParameters, defaultMessage, locale);
	}

	@Override
	public String toString() {
		return "BaseException [message=" + message + ", errorCode=" + errorCode + "]";
	}
	
	
	
}