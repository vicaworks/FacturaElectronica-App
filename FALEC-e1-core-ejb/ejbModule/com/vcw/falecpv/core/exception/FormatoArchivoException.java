/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class FormatoArchivoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 975513778840886502L;

	/**
	 * 
	 */
	public FormatoArchivoException() {
	}

	/**
	 * @param message
	 */
	public FormatoArchivoException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FormatoArchivoException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FormatoArchivoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FormatoArchivoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
