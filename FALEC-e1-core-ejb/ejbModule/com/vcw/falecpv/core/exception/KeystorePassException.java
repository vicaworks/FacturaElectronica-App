/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class KeystorePassException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436572893344011067L;

	/**
	 * 
	 */
	public KeystorePassException() {
	}

	/**
	 * @param message
	 */
	public KeystorePassException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public KeystorePassException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KeystorePassException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public KeystorePassException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
