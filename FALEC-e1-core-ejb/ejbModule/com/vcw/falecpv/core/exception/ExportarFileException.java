/**
 * 
 */
package com.vcw.falecpv.core.exception;

/**
 * @author cristianvillarreal
 *
 */
public class ExportarFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5669104384276772932L;

	/**
	 * 
	 */
	public ExportarFileException() {
	}

	/**
	 * @param message
	 */
	public ExportarFileException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExportarFileException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExportarFileException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ExportarFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
