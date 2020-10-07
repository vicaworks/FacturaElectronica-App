/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class RideSriException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145587914794059264L;

	/**
	 * 
	 */
	public RideSriException() {
	}

	/**
	 * @param message
	 */
	public RideSriException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RideSriException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RideSriException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RideSriException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
