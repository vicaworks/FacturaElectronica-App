/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class EstadoComprobanteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6607676236573124308L;

	/**
	 * 
	 */
	public EstadoComprobanteException() {
	}

	/**
	 * @param message
	 */
	public EstadoComprobanteException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EstadoComprobanteException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EstadoComprobanteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EstadoComprobanteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
