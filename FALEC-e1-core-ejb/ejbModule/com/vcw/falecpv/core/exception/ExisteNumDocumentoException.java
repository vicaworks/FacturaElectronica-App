/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class ExisteNumDocumentoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6060403948379568589L;

	/**
	 * 
	 */
	public ExisteNumDocumentoException() {
	}

	/**
	 * @param message
	 */
	public ExisteNumDocumentoException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExisteNumDocumentoException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExisteNumDocumentoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ExisteNumDocumentoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
