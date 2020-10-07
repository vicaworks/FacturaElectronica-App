/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class EnviarComprobanteSRIException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1241957819420119275L;

	/**
	 * 
	 */
	public EnviarComprobanteSRIException() {
	}

	/**
	 * @param message
	 */
	public EnviarComprobanteSRIException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EnviarComprobanteSRIException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EnviarComprobanteSRIException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EnviarComprobanteSRIException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
