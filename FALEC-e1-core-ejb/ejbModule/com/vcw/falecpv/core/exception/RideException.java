/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class RideException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1113696800737972336L;

	/**
	 * 
	 */
	public RideException() {
	}

	/**
	 * @param message
	 */
	public RideException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RideException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RideException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RideException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
