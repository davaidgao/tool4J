package com.gdwii.util.text;

import java.text.ParseException;

/**
 * 封装ParseRuntime为RuntimeException
 * @author gdwii
 *
 */
public class ParseRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4797758706323921338L;

	public ParseRuntimeException() {
		super();
	}

	public ParseRuntimeException(String message, ParseException exception, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, exception, enableSuppression, writableStackTrace);
	}

	public ParseRuntimeException(String message, ParseException exception) {
		super(message, exception);
	}

	public ParseRuntimeException(String message) {
		super(message);
	}

	public ParseRuntimeException(ParseException exception) {
		super(exception);
	}
}
