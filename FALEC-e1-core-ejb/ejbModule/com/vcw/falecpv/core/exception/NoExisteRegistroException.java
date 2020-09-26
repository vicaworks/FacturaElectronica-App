/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class NoExisteRegistroException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3005265685659296018L;

	/**
	 * 
	 */
	public NoExisteRegistroException() {
	}

	/**
	 * @param message
	 */
	public NoExisteRegistroException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NoExisteRegistroException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoExisteRegistroException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoExisteRegistroException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
