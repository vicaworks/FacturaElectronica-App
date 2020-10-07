/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class FirmarXmlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5535721399788076556L;

	/**
	 * 
	 */
	public FirmarXmlException() {
	}

	/**
	 * @param message
	 */
	public FirmarXmlException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FirmarXmlException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FirmarXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FirmarXmlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
